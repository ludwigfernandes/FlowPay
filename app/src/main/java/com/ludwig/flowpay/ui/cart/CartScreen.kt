package com.ludwig.flowpay.ui.cart

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ludwig.flowpay.data.model.CoinData
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.ludwig.flowpay.ui.navigation.Screens
import com.ludwig.flowpay.utils.FieldFormating.toCleanString
import com.ludwig.flowpay.utils.NetworkResult
import com.revolut.cardpayments.api.CardPaymentLauncher
import com.revolut.cardpayments.api.CardPaymentParams
import com.revolut.cardpayments.core.api.AddressParams

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    revolutViewModel: RevolutViewModel,
    revCardPaymentLauncher: CardPaymentLauncher,
    navToScreen: (Screens) -> Unit
) {
    val context = LocalContext.current

    val orderDetailsState by revolutViewModel.orderDetails.collectAsStateWithLifecycle()
    val orderDetailsData = (orderDetailsState as? NetworkResult.Success<OrderDetailsResponse>)?.data
    val orderDetailsError = (orderDetailsState as? NetworkResult.Error)?.message
    val orderDetailsLoading = orderDetailsState is NetworkResult.Loading
    LaunchedEffect(orderDetailsData?.token) {
        // TODO: clearn token after payment or if failed
        orderDetailsData?.token?.let { token ->
            revCardPaymentLauncher.launch(
                CardPaymentParams(
                    orderId = token,
                    email = "itsludwigferns@gmail.com",
                    billingAddress = AddressParams(
                        streetLine1 = "1 Android Square",
                        streetLine2 = "Kotlin street",
                        city = "London",
                        region = "Greater London",
                        country = "GB",
                        postcode = "54321"
                    ),
                    shippingAddress = null,
                    savePaymentMethodFor = null
                )
            )
        }
    }

    val paymentResultState by revolutViewModel.paymentResult.collectAsStateWithLifecycle()
    val paymentResultData = (paymentResultState as? NetworkResult.Success<String>)?.data
    val paymentResultError = (paymentResultState as? NetworkResult.Error)?.message
    val paymentResultIsLoading = paymentResultState is NetworkResult.Loading
    LaunchedEffect(orderDetailsError, paymentResultError, paymentResultData) {
        val message = paymentResultData ?: paymentResultError ?: orderDetailsError
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    val cartItemsState = cartViewModel.cartItems.collectAsStateWithLifecycle()
    val cartItemsData = cartItemsState.value

    val orderRequest = revolutViewModel.orderRequest.collectAsStateWithLifecycle().value
    val currencySymbol by remember { mutableStateOf("GBP") }
    val cartAmount by remember(cartItemsData) { mutableDoubleStateOf(cartItemsData.sumOf { it.quantity }) }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = cartItemsData, key = { it.id }
                ) { coin ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            when (value) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    cartViewModel.removeCartItem(coin.id)
                                    true
                                }
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    cartViewModel.removeCartItem(coin.id)
                                    true
                                }
                                SwipeToDismissBoxValue.Settled -> false
                            }
                        }
                    )

                    var tfQuantity by rememberSaveable { mutableStateOf(coin.quantity.toCleanString()) }
                    var quantity by rememberSaveable { mutableDoubleStateOf(coin.quantity) }

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Red.copy(0.4f)),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 14.dp, vertical = 2.dp)
                                .animateItem()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = coin.image,
                                    contentDescription = coin.name,
                                    modifier = Modifier.size(40.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(Modifier.width(10.dp))
                                Column(
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Text(
                                        text = "${coin.name}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(Modifier.height(1.dp))
                                    Text(
                                        text = "${coin.symbol}", fontSize = 12.sp, color = Color.Gray
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = "${coin.currentPrice}", fontSize = 12.sp
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            BasicTextField(
                                value = tfQuantity,
                                onValueChange = { input ->
                                    if (input.matches(Regex("""^\d*\.?\d*$"""))) {
                                        tfQuantity = input

                                        val parsed = input.toDoubleOrNull() ?: 0.0
                                        val isIntermediate = input.isEmpty() || input.endsWith(".")
                                        if (!isIntermediate && parsed != quantity) {
                                            quantity = parsed
                                            cartViewModel.updateCart(
                                                coin = CoinData(
                                                    id = coin.id,
                                                    name = coin.name,
                                                    image = coin.image,
                                                    symbol = coin.symbol,
                                                    currentPrice = coin.currentPrice
                                                ),
                                                quantity = parsed
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.End,
                                    color = Color.Black
                                ),
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .background(
                                        color = Color(0xFFF0F0F0),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .widthIn(min = 60.dp)
                            )
                        }
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomCenter),
            enabled = cartItemsData.isNotEmpty(),
            onClick = {
                revolutViewModel.updateOrderRequest(
                    amount = cartAmount,
                    currency = currencySymbol
                )
                revolutViewModel.createOrder(orderRequest)
            }
        ) {
            Text(
                if (cartAmount > 0) {
                    "Proceed To Pay ${cartAmount.toCleanString()} $currencySymbol"
                } else {
                    "Add something to your cart"
                }
            )
        }
        AnimatedVisibility(
            visible = cartItemsData.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Such an empty!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}