package com.ludwig.flowpay.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val key: Screens,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screens.Home, "Home", Icons.Default.Home),
    BottomNavItem(Screens.Coin, "Coin", Icons.Default.Person),
    BottomNavItem(Screens.Cart, "Cart", Icons.Default.Settings),
)

@Composable
fun BottomNavBar(
    currentKey: Screens?,
    updateBackStack: (BottomNavItem) -> Unit
) {
    NavigationBar() {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, item.title) },
                label = { Text(item.title) },
                alwaysShowLabel = true,
                selected = currentKey == item.key,
                onClick = {
                    if (currentKey != item.key) {
                        updateBackStack(item)
                    }
                }
            )
        }
    }
}