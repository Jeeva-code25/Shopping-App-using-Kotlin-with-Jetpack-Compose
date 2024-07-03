package com.example.narumanam.sellersidepages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.accounts.accountsNavGraph
import com.example.narumanam.sellersidepages.Account
import com.example.narumanam.sellersidepages.Home
import com.example.narumanam.sellersidepages.OrderHistory
import com.example.narumanam.sellersidepages.OrderRequest
import com.example.narumanam.sellersidepages.SELLER_BOTTOM

fun NavGraphBuilder.bottomNavGraph(navController : NavHostController){

    navigation(
        startDestination = BottomNavScreens.Home.route,
        route = SELLER_BOTTOM
    ){
        composable(
            route = BottomNavScreens.Home.route
        ){
            Home(navController = navController)
        }
        composable(
            route = BottomNavScreens.Request.route
        ){
            OrderRequest(navController = navController)
        }
        composable(
            route = BottomNavScreens.History.route
        ){
            OrderHistory(navController = navController)
        }
        composable(
            route = BottomNavScreens.Account.route
        ){
            Account(navController = navController)
        }
    }
}