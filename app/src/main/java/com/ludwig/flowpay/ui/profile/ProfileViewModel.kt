package com.ludwig.flowpay.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludwig.flowpay.data.repository.UserProfileRepository
import com.ludwig.flowpay.utils.NetworkConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: UserProfileRepository
) : ViewModel() {



}