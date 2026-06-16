package com.ludwig.flowpay.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ludwig.flowpay.data.repository.RevolutRepository
import com.ludwig.flowpay.ui.home.RevolutViewModel

class RevolutViewModelFactory(
    private val repository: RevolutRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RevolutViewModel(repository) as T
    }
}