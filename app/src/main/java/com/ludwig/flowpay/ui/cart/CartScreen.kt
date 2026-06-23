package com.ludwig.flowpay.ui.cart

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.StrokeCap
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
import com.ludwig.flowpay.data.model.OrderDetailsRequest
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.ludwig.flowpay.data.model.PaymentOutcome
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.ludwig.flowpay.ui.navigation.Screens
import com.ludwig.flowpay.utils.FieldFormating.toCleanString
import com.ludwig.flowpay.utils.NetworkResult
import com.revolut.cardpayments.api.CardPaymentLauncher
import com.revolut.cardpayments.api.CardPaymentParams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    revolutViewModel: RevolutViewModel,
    revCardPaymentLauncher: CardPaymentLauncher,
    navToScreen: (Screens) -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            revolutViewModel.destroyRecords()
        }
    }


    val orderDetailsState by revolutViewModel.orderDetails.collectAsStateWithLifecycle()
    val orderDetailsData = (orderDetailsState as? NetworkResult.Success<OrderDetailsResponse>)?.data
    val orderDetailsError = (orderDetailsState as? NetworkResult.Error)?.message
    val orderDetailsLoading = orderDetailsState is NetworkResult.Loading
    LaunchedEffect(orderDetailsData?.token) {
        orderDetailsData?.token?.let { token ->
            revCardPaymentLauncher.launch(
                CardPaymentParams(
                    orderId = token,
                    email = null,
                    billingAddress = null,
                    shippingAddress = null,
                    savePaymentMethodFor = null
                )
            )
        }
    }
    LaunchedEffect(orderDetailsError) {
        orderDetailsError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    val paymentResultState by revolutViewModel.paymentResult.collectAsStateWithLifecycle()
    val paymentResultData = (paymentResultState as? NetworkResult.Success<String>)?.data
    val paymentResultError = (paymentResultState as? NetworkResult.Error)?.message
    val paymentResultIsLoading = paymentResultState is NetworkResult.Loading
    LaunchedEffect(paymentResultError, paymentResultData) {
        paymentResultError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            navToScreen(
                Screens.TransactionOutcome(
                    paymentOutcome = PaymentOutcome(
                        successMessage = null,
                        failureMessage = it,
                        isError = true
                    )
                )
            )
        }
        paymentResultData?.let {
            navToScreen(
                Screens.TransactionOutcome(
                    paymentOutcome = PaymentOutcome(
                        successMessage = it,
                        failureMessage = null,
                        isError = false
                    )
                )
            )
        }
    }


    val cartItemsData = cartViewModel.cartItems.collectAsStateWithLifecycle().value
    val orderRequestData = revolutViewModel.orderRequest.collectAsStateWithLifecycle().value
    val currencySymbol by remember { mutableStateOf("GBP") }
    val cartAmount by remember(cartItemsData) { mutableDoubleStateOf(cartItemsData.sumOf { it.quantity }) }


    val orderDetailsSheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.Hidden }
    )


    Box(
        modifier = Modifier.fillMaxSize()
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
                revolutViewModel.createOrder(
                    orderDetailsRequest = OrderDetailsRequest(
                        amount = cartAmount,
                        currency = currencySymbol
                    )
                )
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
        if (cartItemsData.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
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
        if (orderDetailsLoading) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                sheetState = orderDetailsSheetState,
                onDismissRequest = {},
                dragHandle = null,
                properties = ModalBottomSheetProperties(
                    shouldDismissOnBackPress = false
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 12.dp,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }
    }
}