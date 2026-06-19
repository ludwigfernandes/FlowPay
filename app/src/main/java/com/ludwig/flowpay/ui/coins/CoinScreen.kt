package com.ludwig.flowpay.ui.coins

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ludwig.flowpay.data.model.CoinListResponse
import com.ludwig.flowpay.ui.loading.LoadingScreen
import com.ludwig.flowpay.utils.NetworkResult

@Composable
fun CoinScreen(
    modifier: Modifier, coinViewModel: CoinViewModel
) {

    val context = LocalContext.current


    val coinListState by coinViewModel.coinList.collectAsStateWithLifecycle()
    val coinListData = (coinListState as? NetworkResult.Success<CoinListResponse>)?.data
    val coinListError = (coinListState as? NetworkResult.Error)?.message
    val coinListIsLoading = coinListState is NetworkResult.Loading


    LaunchedEffect(Unit) {
        coinViewModel.getCoinList()
    }
    LaunchedEffect(coinListError) {
        coinListError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }



    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = coinListData ?: emptyList(), key = { it.id ?: it.hashCode() }) { coin ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .animateItem()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = coin.image,
                                contentDescription = coin.name,
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = "${coin.name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(Modifier.height(1.dp))
                                Text(
                                    text = "${coin.symbol}", fontSize = 12.sp, color = Color.Gray
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${coin.currentPrice}", fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = coinListIsLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingScreen()
        }
    }


}