package com.ludwig.flowpay.data.remote

import com.ludwig.flowpay.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private val moshi = Moshi.Builder().build()
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    private const val CUSTOM_WORKER_BASE_URL = BuildConfig.CUSTOM_WORKER_BASE_URL
    private val customWorkerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }
    private val customWorkerHttpClient = OkHttpClient.Builder()
        .addInterceptor(customWorkerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    private val customWorkerRetrofit = Retrofit.Builder()
        .baseUrl(CUSTOM_WORKER_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(customWorkerHttpClient)
        .build()
    val customWorkerApiService = customWorkerRetrofit.create(CustomWorkerApiService::class.java)



    private const val COIN_BASE_URL = BuildConfig.COIN_BASE_URL
    private val coinInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-cg-demo-api-key", BuildConfig.COIN_API_KEY)
            .build()
        chain.proceed(request)
    }
    private val coinHttpClient = OkHttpClient.Builder()
        .addInterceptor(coinInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    private val coinRetrofit = Retrofit.Builder()
        .baseUrl(COIN_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(coinHttpClient)
        .build()
    val coinApiServer = coinRetrofit.create(CoinGeckoApiService::class.java)

}