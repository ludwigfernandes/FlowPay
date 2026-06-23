package com.ludwig.flowpay.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ludwig.flowpay.utils.NetworkConnectivityObserver
import com.ludwig.flowpay.ui.home.HomeViewModel

class HomeViewModelFactory(
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(networkConnectivityObserver) as T
    }
}