package com.ludwig.flowpay.data.remote

import com.ludwig.flowpay.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private const val CUSTOM_WORKER_BASE_URL = BuildConfig.CUSTOM_WORKER_BASE_URL
    private val moshi = Moshi.Builder().build()
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()
    private val customWorkerRetrofit = Retrofit.Builder()
        .baseUrl(CUSTOM_WORKER_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

    val customWorkerApiService = customWorkerRetrofit.create(CustomWorkerApiService::class.java)
}