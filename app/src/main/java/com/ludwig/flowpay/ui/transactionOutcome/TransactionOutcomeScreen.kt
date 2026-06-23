package com.ludwig.flowpay.ui.transactionOutcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ludwig.flowpay.data.model.PaymentOutcome
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.ludwig.flowpay.utils.NetworkResult

@Composable
fun TransactionOutcomeScreen(
    paymentOutcome: PaymentOutcome
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        paymentOutcome.successMessage?.let { message ->
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "payment outcome",
                tint = Color.Green,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Payment Successful",
                fontSize = 22.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
        paymentOutcome.failureMessage?.let { message ->
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "payment outcome",
                tint = Color.Red,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Payment Failed",
                fontSize = 22.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}