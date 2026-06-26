package com.ludwig.flowpay.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ludwig.flowpay.data.repository.RevolutRepository
import com.ludwig.flowpay.data.repository.UserProfileRepository
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.ludwig.flowpay.ui.profile.ProfileViewModel

class ProfileViewModelFactory(
    private val repository: UserProfileRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(repository) as T
    }
}