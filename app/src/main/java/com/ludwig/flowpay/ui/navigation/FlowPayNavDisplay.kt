package com.ludwig.flowpay.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ludwig.flowpay.ui.cart.CartScreen
import com.ludwig.flowpay.ui.cart.CartViewModel
import com.ludwig.flowpay.ui.coins.CoinScreen
import com.ludwig.flowpay.ui.coins.CoinViewModel
import com.ludwig.flowpay.ui.home.HomeScreen
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.revolut.cardpayments.api.CardPaymentLauncher

@Composable
fun FlowPayNavDisplay(
    modifier: Modifier,
    backStack: SnapshotStateList<Any>,
    revolutViewModel: RevolutViewModel,
    coinViewModel: CoinViewModel,
    cartViewModel: CartViewModel,
    revCardPaymentLauncher: CardPaymentLauncher
) {
    fun navToScreen(key: Screens) {
        backStack.add(key)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = entryProvider {
            entry<Screens.HomeScreen> {
                HomeScreen(
                    revolutViewModel = revolutViewModel,
                    navToScreen = ::navToScreen
                )
            }
            entry<Screens.CoinScreen> {
                CoinScreen(
                    coinViewModel = coinViewModel,
                    cartViewModel = cartViewModel,
                    navToScreen = ::navToScreen
                )
            }
            entry<Screens.CartScreen> {
                CartScreen(
                    cartViewModel = cartViewModel,
                    revolutViewModel = revolutViewModel,
                    revCardPaymentLauncher = revCardPaymentLauncher,
                    navToScreen = ::navToScreen
                )
            }
        }
    )
}