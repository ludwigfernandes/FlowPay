package com.ludwig.flowpay.data.repository

import com.ludwig.flowpay.TAG
import com.ludwig.flowpay.data.model.OrderDetailsRequest
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.ludwig.flowpay.data.remote.CustomWorkerApiService
import com.ludwig.flowpay.utils.CustomLogger.logDebugLogs
import com.ludwig.flowpay.utils.NetworkResult

class RevolutRepository(
    private val customWorkerApiService: CustomWorkerApiService
) {

    suspend fun createOrder(orderDetailsRequest: OrderDetailsRequest): NetworkResult<OrderDetailsResponse> {
        return try {
            val response = customWorkerApiService.createOrder(orderDetailsRequest)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Something went wrong!"
                NetworkResult.Error(errorBody)
            }
        } catch (e: Exception) {
            logDebugLogs(TAG, "createOrder()", e.toString())
            NetworkResult.Error("Something went wrong!")
        }
    }
}