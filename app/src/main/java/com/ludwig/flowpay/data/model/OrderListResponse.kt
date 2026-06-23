package com.ludwig.flowpay.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderListResponse(
    val orders: List<Orders?>
)

@JsonClass(generateAdapter = true)
data class Orders(
    val id: String?,
    val token: String?,
    val type: String?,
    val state: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?,
    val amount: Int?,
    val currency: String?,
    @Json(name = "outstanding_amount") val outstandingAmount: Int?,
    @Json(name = "capture_mode") val captureMode: String?,
    @Json(name = "enforce_challenge") val enforceChallenge: String?,
    @Json(name = "authorisation_type") val authorisationType: String?
)
