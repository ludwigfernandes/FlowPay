package com.ludwig.flowpay.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludwig.flowpay.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: UserProfileRepository
) : ViewModel() {


    val profileDetails = profileRepository.profileDetails
        .map { profile ->
            ProfileDetailsUIState(
                name = profile.name,
                email = profile.email,
                streetLine1 = profile.streetLine1,
                streetLine2 = profile.streetLine2,
                city = profile.city,
                region = profile.region,
                country = profile.country,
                postcode = profile.postcode,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileDetailsUIState(),
        )
    fun saveProfileDetails(
        name: String?,
        email: String?,
        streetLine1: String?,
        streetLine2: String?,
        city: String?,
        region: String?,
        country: String?,
        postcode: String?,
    ) {
        viewModelScope.launch {
            profileRepository.saveProfileDetails(
                name = name,
                email = email,
                streetLine1 = streetLine1,
                streetLine2 = streetLine2,
                city = city,
                region = region,
                country = country,
                postcode = postcode,
            )
        }
    }
}