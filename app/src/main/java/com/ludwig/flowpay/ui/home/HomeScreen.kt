package com.ludwig.flowpay.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ludwig.flowpay.data.model.OrderListResponse
import com.ludwig.flowpay.ui.loading.LoadingScreen
import com.ludwig.flowpay.ui.navigation.Screens
import com.ludwig.flowpay.utils.NetworkResult
import kotlin.collections.emptyList


@Composable
fun HomeScreen(
    revolutViewModel: RevolutViewModel, navToScreen: (Screens) -> Unit
) {
    val context = LocalContext.current


    val previousOrdersState by revolutViewModel.previousOrders.collectAsStateWithLifecycle()
    val previousOrdersData = (previousOrdersState as? NetworkResult.Success<OrderListResponse>)?.data
    val previousOrdersError = (previousOrdersState as? NetworkResult.Error)?.message
    val previousOrdersIsLoading = previousOrdersState is NetworkResult.Loading
    LaunchedEffect(Unit) {
        revolutViewModel.getPreviousOrders()
    }
    LaunchedEffect(previousOrdersError) {
        previousOrdersError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = previousOrdersData?.orders ?: emptyList(),
                    key = { it?.id!! }) { order ->
                    Row(modifier = Modifier
                        .clickable {}
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 2.dp)
                        .animateItem(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "${order?.amount} ${order?.currency}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(Modifier.height(1.dp))
                            Text(
                                text = "Created at: ${order?.createdAt}", fontSize = 12.sp, color = Color.Gray
                            )
                            Text(
                                text = "Updated at: ${order?.updatedAt}", fontSize = 12.sp, color = Color.Gray
                            )
                            Text(
                                text = "${order?.id}", fontSize = 8.sp, color = Color.Gray
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "${order?.state}", fontSize = 12.sp
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = previousOrdersIsLoading, enter = fadeIn(), exit = fadeOut()
        ) {
            LoadingScreen()
        }
    }

}