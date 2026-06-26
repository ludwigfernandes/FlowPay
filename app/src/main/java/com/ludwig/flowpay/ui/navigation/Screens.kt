package com.ludwig.flowpay.ui.navigation

import com.ludwig.flowpay.data.model.PaymentOutcome

sealed interface Screens {
    data object Coin: Screens
    data object Home: Screens
    data object Cart: Screens
    data class TransactionOutcome(val paymentOutcome: PaymentOutcome): Screens
    data object Profile: Screens
}