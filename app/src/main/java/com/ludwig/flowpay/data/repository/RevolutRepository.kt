package com.ludwig.flowpay.data.repository

import com.ludwig.flowpay.TAG
import com.ludwig.flowpay.data.model.CoinListResponse
import com.ludwig.flowpay.data.model.OrderDetailsRequest
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.ludwig.flowpay.data.model.OrderListResponse
import com.ludwig.flowpay.data.remote.CustomWorkerApiService
import com.ludwig.flowpay.utils.CustomLogger.logDebugLogs
import com.ludwig.flowpay.utils.ErrorHandling.unwrapError
import com.ludwig.flowpay.utils.NetworkResult

class RevolutRepository(
    private val customWorkerApiService: CustomWorkerApiService
) {

    suspend fun createOrder(orderDetailsRequest: OrderDetailsRequest): NetworkResult<OrderDetailsResponse>  {
        return try {
            val response = customWorkerApiService.createOrder(orderDetailsRequest)
            if (response.isSuccessful) {
                val resBody = response.body()
                resBody?.let { body ->
                    NetworkResult.Success(body)
                } ?: run {
                    NetworkResult.Error("Empty response from server")
                }
            } else {
                val errorMessage = unwrapError("createOrder()", response.errorBody())
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            logDebugLogs(TAG, "getCoinList()", e.toString())
            NetworkResult.Error("Something went wrong!")
        }
    }

    suspend fun getPreviousOrders(): NetworkResult<OrderListResponse>  {
        return try {
            val response = customWorkerApiService.getPreviousOrders()
            if (response.isSuccessful) {
                val resBody = response.body()
                resBody?.let { body ->
                    NetworkResult.Success(body)
                } ?: run {
                    NetworkResult.Error("Empty response from server")
                }
            } else {
                val errorMessage = unwrapError("getPreviousOrders()", response.errorBody())
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            logDebugLogs(TAG, "getPreviousOrders()", e.toString())
            NetworkResult.Error("Something went wrong!")
        }
    }
}