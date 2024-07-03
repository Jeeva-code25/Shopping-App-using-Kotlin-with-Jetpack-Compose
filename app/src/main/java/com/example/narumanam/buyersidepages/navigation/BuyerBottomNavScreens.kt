package com.example.narumanam.buyersidepages.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.narumanam.R


sealed class BuyerBottomNavScreens(
    val route: String,
    val title: String,
    val icon: ImageVector) {

    object Home: BuyerBottomNavScreens(
        route = "home_screen",
        title = "Home",
        icon =  Icons.Outlined.Storefront
    )
    object Cart: BuyerBottomNavScreens(
        route = "cart_screen",
        title = "Cart",
        icon =  Icons.Outlined.ShoppingCart
    )
    object Favourite: BuyerBottomNavScreens(
        route = "favourite_screen",
        title = "Favourite",
        icon =  Icons.Outlined.FavoriteBorder
    )
    object Account: BuyerBottomNavScreens(
        route = "account",
        title = "Account",
        icon =  Icons.Outlined.Person
    )

}