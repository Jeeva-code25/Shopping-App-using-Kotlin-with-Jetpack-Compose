package com.example.narumanam.buyersidepages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.accounts.accountsNavGraph
import com.example.narumanam.buyersidepages.BUYER_BOTTOM
import com.example.narumanam.buyersidepages.Cart
import com.example.narumanam.buyersidepages.Favourite
import com.example.narumanam.buyersidepages.Account
import com.example.narumanam.buyersidepages.Home


fun NavGraphBuilder.buyerBottomNavGraph(navController : NavHostController){

    navigation(
        startDestination = BuyerBottomNavScreens.Home.route,
        route = BUYER_BOTTOM
    ){
        composable(
            route = BuyerBottomNavScreens.Home.route
        ){
            Home(navController = navController)
        }
        composable(
            route = BuyerBottomNavScreens.Cart.route
        ){
            Cart(navController = navController)
        }
        composable(
            route = BuyerBottomNavScreens.Favourite.route
        ){
            Favourite(navController = navController)
        }
        composable(
            route = BuyerBottomNavScreens.Account.route
        ){
            Account(navController = navController)
        }
    }
}