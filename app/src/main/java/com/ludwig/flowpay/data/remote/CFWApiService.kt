package com.ludwig.flowpay.data.remote

import com.ludwig.flowpay.data.model.OrderDetailsRequest
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomWorkerApiService {

    @POST("create-order")
    suspend fun createOrder(@Body requestBody: OrderDetailsRequest): Response<OrderDetailsResponse>
}