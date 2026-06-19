package com.ludwig.flowpay.data.repository

import com.ludwig.flowpay.TAG
import com.ludwig.flowpay.data.model.CoinListResponse
import com.ludwig.flowpay.data.remote.CoinGeckoApiService
import com.ludwig.flowpay.utils.CustomLogger.logDebugLogs
import com.ludwig.flowpay.utils.ErrorHandling.unwrapError
import com.ludwig.flowpay.utils.NetworkResult
import okhttp3.ResponseBody

class CoinRepository(
    private val coinGeckoApiService: CoinGeckoApiService
) {

    suspend fun getCoinList(): NetworkResult<CoinListResponse> {
        return try {
            val response = coinGeckoApiService.getCoinList()
            if (response.isSuccessful) {
                val resBody = response.body()
                resBody?.let { body ->
                    NetworkResult.Success(body)
                } ?: run {
                    NetworkResult.Error("Empty response from server")
                }
            } else {
                val errorMessage = unwrapError("getCoinList()", response.errorBody())
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            logDebugLogs(TAG, "getCoinList()", e.toString())
            NetworkResult.Error("Something went wrong!")
        }
    }
}