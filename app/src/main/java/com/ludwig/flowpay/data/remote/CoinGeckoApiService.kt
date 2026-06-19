package com.ludwig.flowpay.data.remote

import com.ludwig.flowpay.data.model.CoinListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {

    @GET("coins/markets")
    suspend fun getCoinList(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
    ): Response<CoinListResponse>

}