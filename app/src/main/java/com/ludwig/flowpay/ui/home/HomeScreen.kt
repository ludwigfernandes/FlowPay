package com.ludwig.flowpay.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ludwig.flowpay.ui.loading.LoadingScreen
import com.ludwig.flowpay.utils.NetworkResult
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.revolut.cardpayments.api.CardPaymentLauncher
import com.revolut.cardpayments.api.CardPaymentParams
import com.revolut.cardpayments.core.api.AddressParams


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    revolutViewModel: RevolutViewModel,
    revCardPaymentLauncher: CardPaymentLauncher
) {

    val context = LocalContext.current

    val orderDetailsState by revolutViewModel.orderDetails.collectAsStateWithLifecycle()
    val orderDetailsData = (orderDetailsState as? NetworkResult.Success<OrderDetailsResponse>)?.data
    val orderDetailsError = (orderDetailsState as? NetworkResult.Error)?.message
    val orderDetailsLoading = orderDetailsState is NetworkResult.Loading

    val paymentResultState by revolutViewModel.paymentResult.collectAsStateWithLifecycle()
    val paymentResultData = (paymentResultState as? NetworkResult.Success<String>)?.data
    val paymentResultError = (paymentResultState as? NetworkResult.Error)?.message
    val paymentResultIsLoading = paymentResultState is NetworkResult.Loading

    val orderRequest = revolutViewModel.orderRequest.collectAsStateWithLifecycle().value


    LaunchedEffect(orderDetailsError, paymentResultError, paymentResultData) {
        val message = paymentResultData ?: paymentResultError ?: orderDetailsError
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(orderDetailsData?.token) {
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



    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Coin x28474 Added to Cart worth ${orderRequest.amount} ${orderRequest.currency}!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    revolutViewModel.createOrder(orderRequest)
                }
            ) {
                Text("Proceed to Pay")
            }
        }
        AnimatedVisibility(
            visible = orderDetailsLoading || paymentResultIsLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingScreen()
        }
    }
}