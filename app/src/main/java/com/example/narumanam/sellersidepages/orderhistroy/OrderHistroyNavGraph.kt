package com.example.narumanam.sellersidepages.orderhistroy

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.sellersidepages.OrderHistory
import com.example.narumanam.sellersidepages.SELLER_ORDER_HISTORY
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.sellersidepages.navigation.BottomNavScreens

fun NavGraphBuilder.orderHistoryNavGraph(navController : NavHostController) {
    navigation(
        startDestination = BottomNavScreens.History.route,
        route = SELLER_ORDER_HISTORY
    ) {

    }
        composable(
            route = BottomNavScreens.History.route
        ) {
            OrderHistory(navController = navController)
        }

    composable(
        route = SellerScreens.ItemHistoryScreen.route
    ) {
        HistoryProductDetailedScreen(navController = navController)
    }

    }