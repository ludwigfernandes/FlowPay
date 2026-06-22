package com.ludwig.flowpay.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludwig.flowpay.utils.NetworkResult
import com.ludwig.flowpay.data.model.OrderDetailsRequest
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.ludwig.flowpay.utils.PaymentFlowHelper
import com.ludwig.flowpay.TAG
import com.ludwig.flowpay.data.repository.RevolutRepository
import com.ludwig.flowpay.utils.CustomLogger.logDebugLogs
import com.revolut.cardpayments.api.CardPaymentResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RevolutViewModel(
    private val revolutRepository: RevolutRepository
) : ViewModel() {

    private val _orderDetails = MutableStateFlow<NetworkResult<OrderDetailsResponse>?>(null)
    val orderDetails = _orderDetails.asStateFlow()
    fun createOrder(orderDetailsRequest: OrderDetailsRequest) {
        viewModelScope.launch {
            _orderDetails.value = NetworkResult.Loading
            _orderDetails.value = revolutRepository.createOrder(orderDetailsRequest)
        }
    }

    private val _paymentResult = MutableStateFlow<NetworkResult<String>?>(null)
    val paymentResult = _paymentResult.asStateFlow()
    fun onPaymentResult(result: CardPaymentResult) {
        when (result) {
            is CardPaymentResult.Authorised -> {
                logDebugLogs(TAG, "CardPaymentResult.Authorised", result.toString())
                val outcome = PaymentFlowHelper.mapPaymentErrorToReasons(result.toString())
                _paymentResult.value = NetworkResult.Success("Payment successful")
            }

            is CardPaymentResult.Declined -> {
                logDebugLogs(TAG, "CardPaymentResult.Declined", result.failureReason.toString())
                val error = PaymentFlowHelper.mapPaymentErrorToReasons(result.failureReason.toString())
                _paymentResult.value = NetworkResult.Error(error)
            }

            is CardPaymentResult.Failed -> {
                logDebugLogs(TAG, "CardPaymentResult.Failed", result.failureReason.toString())
                val error = PaymentFlowHelper.mapPaymentErrorToReasons(result.failureReason.toString())
                _paymentResult.value = NetworkResult.Error(error)
            }

            is CardPaymentResult.Error -> {
                logDebugLogs(TAG, "CardPaymentResult.Error", result.toString())
                val error = PaymentFlowHelper.mapPaymentErrorToReasons(result.toString())
                _paymentResult.value = NetworkResult.Error(error)
            }

            is CardPaymentResult.UserAbandonedPayment -> {
                logDebugLogs(TAG, "CardPaymentResult.UserAbandonedPayment", result.toString())
                val error = PaymentFlowHelper.mapPaymentErrorToReasons(result.toString())
                _paymentResult.value = NetworkResult.Error("Payment abandoned")
            }
        }
    }


    private val _orderRequest = MutableStateFlow<OrderDetailsRequest?>(OrderDetailsRequest(amount = 0.0, currency = "GBP"))
    val orderRequest = _orderRequest.asStateFlow()
    fun updateOrderRequest(amount: Double, currency: String) {
        _orderRequest.value = _orderRequest.value?.copy(
            amount = amount,
            currency = currency
        )
    }


    fun destroyRecords(){
        _orderDetails.update { null }
        _paymentResult.update { null }
        _orderRequest.update { null }
    }

}