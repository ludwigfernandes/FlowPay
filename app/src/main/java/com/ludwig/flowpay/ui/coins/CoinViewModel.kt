package com.ludwig.flowpay.ui.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludwig.flowpay.data.model.CoinListResponse
import com.ludwig.flowpay.data.repository.CoinRepository
import com.ludwig.flowpay.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinViewModel(
    private val coinRepository: CoinRepository
): ViewModel() {

    private val _coinList = MutableStateFlow<NetworkResult<CoinListResponse>?>(null)
    val coinList = _coinList.asStateFlow()
    fun getCoinList(){
        viewModelScope.launch {
            _coinList.value = NetworkResult.Loading
            _coinList.value = coinRepository.getCoinList()
        }
    }

}