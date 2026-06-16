package com.ludwig.flowpay

import com.ludwig.flowpay.data.model.OrderDetailsRequest
import com.ludwig.flowpay.data.model.OrderDetailsResponse
import com.ludwig.flowpay.data.repository.RevolutRepository
import com.ludwig.flowpay.ui.home.RevolutViewModel
import com.ludwig.flowpay.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RevoluteViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var mockRepository: RevolutRepository
    private lateinit var viewModel: RevolutViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mock()
        viewModel = RevolutViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `createOrder emits Loading then Success`() = runTest {
        val orderRequest = OrderDetailsRequest(
            amount = 400,
            currency = "GBP"
        )
        val orderResponse = OrderDetailsResponse(
            token = "554dca6f-169a-46eb-a5b0-808e8baf54e8",
            amount = 400,
            currency = "GBP",
            id = "6a3273ee-248a-a79c-adf9-4d8a45afe868",
            state = "pending"
        )
        whenever(mockRepository.createOrder(orderRequest))
            .thenReturn(NetworkResult.Success(orderResponse))

        viewModel.createOrder(orderRequest)

        assertEquals(NetworkResult.Success(orderResponse), viewModel.orderDetails.value)
    }

    @Test
    fun `createOrder emits Error when repo returns error`() = runTest {
        val orderRequest = OrderDetailsRequest(
            amount = 400,
            currency = "GBP"
        )
        whenever {  mockRepository.createOrder(orderRequest)}
            .thenReturn(NetworkResult.Error("Something went wrong!"))

        viewModel.createOrder(orderRequest)

        assertEquals(NetworkResult.Error("Something went wrong!"), viewModel.orderDetails.value)
    }

}