package com.ludwig.flowpay.ui.cart

import androidx.lifecycle.ViewModel
import com.ludwig.flowpay.TAG
import com.ludwig.flowpay.data.model.CartItem
import com.ludwig.flowpay.data.model.CoinData
import com.ludwig.flowpay.utils.CustomLogger.logDebugLogs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.indexOfFirst

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()
    fun updateCart(coin: CoinData, quantity: Double) {
        _cartItems.update { currentItems ->
            val existing = currentItems.indexOfFirst { it.id == coin.id }
            if (existing != -1) {
                currentItems.toMutableList().apply {
                    this[existing] = this[existing].copy(quantity = quantity)
                }
            } else {
                if (!coin.id.isNullOrBlank() && coin.currentPrice != null) {
                    currentItems + CartItem(
                        id = coin.id,
                        name = coin.name.orEmpty(),
                        symbol = coin.symbol.orEmpty(),
                        image = coin.image.orEmpty(),
                        currentPrice = coin.currentPrice,
                        quantity = quantity
                    )
                } else {
                    currentItems
                }
            }
        }
        logDebugLogs(TAG, "updateCart()", "Items in cart ${_cartItems.value.size}")
    }

    fun removeCartItem(itemId: String) {
        _cartItems.update { items ->
            items.filterNot { it.id == itemId }
        }
    }

}