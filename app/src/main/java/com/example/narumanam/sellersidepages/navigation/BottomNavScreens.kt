package com.example.narumanam.sellersidepages.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavScreens(
    val route: String,
    val title: String,
    val icon: ImageVector) {

    object Home: BottomNavScreens(
        route = "home_screen",
        title = "Store",
        icon =  Icons.Outlined.Storefront
    )
    object Request: BottomNavScreens(
        route = "order_request",
        title = "Request",
        icon =  Icons.Outlined.Notifications
    )
    object History: BottomNavScreens(
        route = "order_History",
        title = "History",
        icon =  Icons.Outlined.History
    )
    object Account: BottomNavScreens(
        route = "account",
        title = "Account",
        icon =  Icons.Outlined.Person
    )

}