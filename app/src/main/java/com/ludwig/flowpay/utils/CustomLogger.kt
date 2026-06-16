package com.ludwig.flowpay.utils

import android.util.Log
import com.ludwig.flowpay.BuildConfig

object CustomLogger {

    fun logDebugLogs(tag: String, source: String, log: String){
        if (BuildConfig.DEBUG) Log.d(tag, "$source: $log")
    }
}