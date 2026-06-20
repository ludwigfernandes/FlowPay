package com.ludwig.flowpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.ludwig.flowpay.di.RevolutViewModelFactory
import com.ludwig.flowpay.ui.coins.CoinViewModel
import com.ludwig.flowpay.di.CoinViewModelFactory
import com.ludwig.flowpay.ui.navigation.BottomNavBar
import com.ludwig.flowpay.ui.navigation.FlowPayNavDisplay
import com.ludwig.flowpay.ui.navigation.Screens
import com.ludwig.flowpay.ui.theme.FlowPayTheme
import com.revolut.cardpayments.api.CardPaymentLauncher

const val TAG = "TAG"

class MainActivity : ComponentActivity() {

    private val appDependencies by lazy { (application as FlowPayApplication).appDependencies }



    private val revolutViewModelFactory by lazy {
        RevolutViewModelFactory(appDependencies.revolutRepository)
    }
    private val revolutViewModel by viewModels<RevolutViewModel> { revolutViewModelFactory }

    private val coinViewModelFactory by lazy {
        CoinViewModelFactory(appDependencies.coinRepository)
    }
    private val coinViewModel by viewModels<CoinViewModel> { coinViewModelFactory }



    private val revCardPaymentLauncher = CardPaymentLauncher(this) { result ->
        revolutViewModel.onPaymentResult(result)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FlowPayTheme {

                val backStack = remember { mutableStateListOf<Any>(Screens.HomeScreen) }
                fun showNavBar(key: Screens?): Boolean {
                    return when (key) {
                        Screens.HomeScreen, Screens.CartScreen -> true
                        else -> false

                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentKey = backStack.lastOrNull() as? Screens
                        val showNavBar = showNavBar(currentKey)
                        if (showNavBar) {
                            BottomNavBar(
                                currentKey = currentKey,
                                updateBackStack = { bottomNavItem ->
                                    backStack.clear()
                                    backStack.add(bottomNavItem.key)
                                }
                            )
                        }
                    }
                ) { innerPadding ->

                    FlowPayNavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        revolutViewModel = revolutViewModel,
                        coinViewModel = coinViewModel,
                        revCardPaymentLauncher = revCardPaymentLauncher
                    )
                }
            }
        }
    }
}