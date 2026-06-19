package com.ludwig.flowpay.utils

import com.ludwig.flowpay.TAG
import com.ludwig.flowpay.utils.CustomLogger.logDebugLogs
import okhttp3.ResponseBody

object ErrorHandling {

    fun unwrapError(source: String, errorBody: ResponseBody?): String {
        return try {
            val errorMessage = errorBody?.string()?.takeIf { it.isNotBlank() } ?: "Unknown error occurred"
            logDebugLogs(TAG, source, errorMessage)
            errorMessage
        } catch (e: Exception) {
            "Something went wrong!"
        }
    }
}