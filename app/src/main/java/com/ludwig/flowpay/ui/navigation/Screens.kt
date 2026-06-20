package com.ludwig.flowpay.ui.navigation

sealed interface Screens {
    data object CoinScreen: Screens
    data object HomeScreen: Screens
    data object CartScreen: Screens
}