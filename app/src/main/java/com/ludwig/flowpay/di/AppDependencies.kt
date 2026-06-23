package com.ludwig.flowpay.di

import android.content.Context
import com.ludwig.flowpay.utils.NetworkConnectivityObserver
import com.ludwig.flowpay.data.remote.CustomWorkerApiService
import com.ludwig.flowpay.data.remote.NetworkModule
import com.ludwig.flowpay.data.repository.CoinRepository
import com.ludwig.flowpay.data.repository.RevolutRepository

class AppDependencies(context: Context) {
    private val customWorkerApiService: CustomWorkerApiService = NetworkModule.customWorkerApiService
    val revolutRepository = RevolutRepository(customWorkerApiService)


    private val coinApiService = NetworkModule.coinApiServer
    val coinRepository = CoinRepository(coinApiService)


    val networkConnectivityObserver = NetworkConnectivityObserver(context)
}