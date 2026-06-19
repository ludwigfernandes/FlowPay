package com.ludwig.flowpay.data.model

data class CartItem(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Double,
    val quantity: Double
)