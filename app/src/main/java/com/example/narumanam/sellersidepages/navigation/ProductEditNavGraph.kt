package com.example.narumanam.sellersidepages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.sellersidepages.ProductEdit
import com.example.narumanam.sellersidepages.SELLER_PRODUCT_EDIT
import com.example.narumanam.sellersidepages.SellerScreens

fun NavGraphBuilder.productEditNavGraph(navController : NavHostController)
{
    navigation(
        startDestination = SellerScreens.productEditScreen.route,
        route = SELLER_PRODUCT_EDIT
    ){

        composable(
            route = SellerScreens.productEditScreen.route
        ){
            ProductEdit(navController = navController)
        }
    }
}