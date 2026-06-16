package com.ludwig.flowpay.di

import com.ludwig.flowpay.data.remote.CustomWorkerApiService
import com.ludwig.flowpay.data.remote.NetworkModule
import com.ludwig.flowpay.data.repository.RevolutRepository

class AppDependencies {
    private val customWorkerApiService: CustomWorkerApiService = NetworkModule.customWorkerApiService
    val revolutRepository = RevolutRepository(customWorkerApiService)

}