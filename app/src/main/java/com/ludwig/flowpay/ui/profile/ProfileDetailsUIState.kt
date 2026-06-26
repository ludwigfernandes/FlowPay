package com.ludwig.flowpay.ui.profile

data class ProfileDetailsUIState(
    val name: String = "",
    val email: String = "",
    val streetLine1: String = "",
    val streetLine2: String = "",
    val city: String = "",
    val region: String = "",
    val country: String = "",
    val postcode: String = "",
)