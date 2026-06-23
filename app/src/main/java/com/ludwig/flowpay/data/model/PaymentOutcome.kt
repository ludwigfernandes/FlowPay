package com.ludwig.flowpay.data.model

data class PaymentOutcome(
    val successMessage: String?,
    val failureMessage: String?,
    val isError: Boolean
)
