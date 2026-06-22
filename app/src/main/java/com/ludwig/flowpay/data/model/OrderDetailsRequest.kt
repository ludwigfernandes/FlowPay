package com.ludwig.flowpay.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderDetailsRequest(
    val amount: Double,
    val currency: String
)