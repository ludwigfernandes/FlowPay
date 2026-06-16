package com.ludwig.flowpay.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderDetailsResponse(
    val token: String? = null,
    val amount: Int? = null,
    val currency: String? = null,
    val id: String? = null,
    val state: String? = null
)