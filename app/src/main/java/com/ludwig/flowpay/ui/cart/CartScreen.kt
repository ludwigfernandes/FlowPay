package com.ludwig.flowpay.ui.cart

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ludwig.flowpay.ui.navigation.Screens

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navToScreen: (Screens) -> Unit
) {

    val cartItemsState = cartViewModel.cartItems.collectAsStateWithLifecycle()
    val cartItemsData = cartItemsState.value

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
                    items = cartItemsData ?: emptyList(), key = { it.id }) { coin ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable { }
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
            visible = cartItemsData?.isEmpty() == true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Such an empty!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}