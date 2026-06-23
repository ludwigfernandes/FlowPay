package com.ludwig.flowpay.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludwig.flowpay.utils.NetworkConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable = _isNetworkAvailable.asStateFlow()

    init {
        viewModelScope.launch {
            networkConnectivityObserver.isConnected.collect { isConnected ->
                _isNetworkAvailable.value = isConnected
            }
        }
    }


}