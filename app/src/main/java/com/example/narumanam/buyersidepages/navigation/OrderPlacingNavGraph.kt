package com.example.narumanam.buyersidepages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.buyersidepages.ORDER_PLACING_ROOT
import com.example.narumanam.buyersidepages.TrackOrderScreen
import com.example.narumanam.buyersidepages.productorderingprocess.CardPaymentScreen
import com.example.narumanam.buyersidepages.productorderingprocess.OrderConfirmationScreen
import com.example.narumanam.buyersidepages.productorderingprocess.OrderSummaryScreen
import com.example.narumanam.buyersidepages.productorderingprocess.PaymentScreen
import com.example.narumanam.buyersidepages.productorderingprocess.ProductDisplayScreen
import com.example.narumanam.buyersidepages.productorderingprocess.ProductInDetailScreen
import com.example.narumanam.buyersidepages.productorderingprocess.UPIPaymentScreen


fun NavGraphBuilder.orderPlacingNavGraph(navController : NavHostController)
{
    navigation(
        startDestination = BuyerScreens.ProductDisplayScreen.route,
        route = ORDER_PLACING_ROOT
    ) {

         composable(
            route = BuyerScreens.ProductDisplayScreen.route+"/{PRODUCTCATEGORY}"
        ) { navBackStack ->

            val productCategory = navBackStack.arguments?.getString("PRODUCTCATEGORY")
             ProductDisplayScreen(navController = navController, title = productCategory?:"")
         }

        composable(
            route = BuyerScreens.ProductInDetailScreen.route
        ) {

            ProductInDetailScreen(navController = navController)

        }

        composable(
            route = BuyerScreens.OrderSummaryScreen.route
        ) {

            OrderSummaryScreen(navController = navController)
        }

        composable(
            route = BuyerScreens.PaymentScreen.route
        ) {

            PaymentScreen(navController = navController)
        }
        composable(
            route = BuyerScreens.UPIPaymentScreen.route
        ) {

            UPIPaymentScreen(navController = navController)
        }
        composable(
            route = BuyerScreens.CardPaymentScreen.route
        ) {

            CardPaymentScreen(navController = navController)
        }

        composable(
            route = BuyerScreens.OrderConfirmationScreen.route
        ) {

            OrderConfirmationScreen(navController = navController)
        }
        composable(
            route = BuyerScreens.TrackOrderScreen.route
        ) {

            TrackOrderScreen(navController = navController)
        }
    }
}