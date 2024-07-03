package com.example.narumanam.sellersidepages.orderrequest

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.sellersidepages.OrderHistory
import com.example.narumanam.sellersidepages.OrderRequest
import com.example.narumanam.sellersidepages.SELLER_ORDER_HISTORY
import com.example.narumanam.sellersidepages.SELLER_ORDER_REQUEST
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.sellersidepages.navigation.BottomNavScreens

fun NavGraphBuilder.orderRequestNavGraph(navController : NavHostController) {
    navigation(
        startDestination = BottomNavScreens.Request.route,
        route = SELLER_ORDER_REQUEST
    ) {

    }
        composable(
            route = BottomNavScreens.Request.route
        ) {
            OrderRequest(navController = navController)
        }
    composable(
        route = SellerScreens.OrderConfirmationScreen.route
    ) {
        OrderConfirmationScreen(navController = navController)
    }
    composable(
        route = SellerScreens.OrderShippingScreen.route
    ) {
        OrderShippingScreen(navController = navController)
    }

    }