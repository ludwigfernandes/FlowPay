package com.ludwig.flowpay.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ludwig.flowpay.data.repository.CoinRepository
import com.ludwig.flowpay.ui.coins.CoinViewModel

class CoinViewModelFactory(
    private val repository: CoinRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoinViewModel(repository) as T
    }
}