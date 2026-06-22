package com.ludwig.flowpay.ui.coins

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ludwig.flowpay.data.model.CartItem
import com.ludwig.flowpay.data.model.CoinData
import com.ludwig.flowpay.data.model.CoinListResponse
import com.ludwig.flowpay.ui.cart.CartViewModel
import com.ludwig.flowpay.ui.loading.LoadingScreen
import com.ludwig.flowpay.ui.navigation.Screens
import com.ludwig.flowpay.utils.NetworkResult

@Composable
fun CoinScreen(
    coinViewModel: CoinViewModel,
    cartViewModel: CartViewModel,
    navToScreen: (Screens) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()


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


    var showCoinDataSheet by remember { mutableStateOf(false) }
    var selectedCoin by remember { mutableStateOf<CoinData?>(null) }
    val cartItems = remember { mutableStateListOf<CartItem>() }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                selectedCoin = coin
                                showCoinDataSheet = true
                            }
                            .animateItem(),
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
        AnimatedVisibility(
            visible = coinListIsLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingScreen()
        }
    }

    if (showCoinDataSheet) {
        selectedCoin?.let { coin ->
            CoinDetailsSheet(
                coinData = coin,
                onDismiss = { showCoinDataSheet = false },
                onAddToCartClicked = { coin, quantity ->
                    cartViewModel.updateCart(
                        coin = coin,
                        quantity = quantity
                    )
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailsSheet(
    coinData: CoinData,
    onDismiss: () -> Unit,
    onAddToCartClicked: (CoinData, Double) -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = coinData.image,
                    contentDescription = coinData.name,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(10.dp))
                Column(
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "${coinData.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = "${coinData.symbol}", fontSize = 12.sp, color = Color.Gray
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${coinData.currentPrice}",
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    onAddToCartClicked(coinData, 80.2)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Add to cart")
            }
        }
    }
}