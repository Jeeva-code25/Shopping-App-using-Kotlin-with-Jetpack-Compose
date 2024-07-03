package com.example.narumanam.accounts


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.BUYER_ACCOUNT
import com.example.narumanam.SELLER_ACCOUNT
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.navigation.BuyerBottomNavScreens
import com.example.narumanam.sellersidepages.Account
import com.example.narumanam.sellersidepages.navigation.BottomNavScreens


fun NavGraphBuilder.accountsNavGraph(
    navController : NavHostController,
    userRole : String
) {

    navigation(
        startDestination =  if (userRole == "Sellers"){BottomNavScreens.Account.route} else{BuyerBottomNavScreens.Account.route},
        route = if (userRole == "Sellers"){SELLER_ACCOUNT} else { BUYER_ACCOUNT}
    ) {
        composable(
            route = BottomNavScreens.Account.route
        ) {
            Account(navController = navController)
        }
        composable(
            route = BuyerBottomNavScreens.Account.route
        ) {
            com.example.narumanam.buyersidepages.Account(navController = navController)
        }
        composable(
            route = Screens.PostDiscountOfferScreen.route
        ) {
            PostDiscountOfferScreen(navController = navController)
        }
        composable(
            route = Screens.MyDetailsScreen.route
        ){
            MyDetailsScreen(navController = navController)
        }
        composable(
            route = Screens.BuyerMyDetailsScreen.route
        ){
            BuyerMyDetailsScreen(navController = navController)
        }
        composable(
            route = Screens.AddressScreen.route+"/{FROMPAGE}"
        ) {navBackStack->
            val fromPage = navBackStack.arguments?.getString("FROMPAGE")
            MyAddressScreen(navController = navController, fromPage = fromPage?:"")
        }
        composable(
            route = Screens.NewAddressScreen.route+"/{FROMPAGE}"
        ) {navBackStack->
            val fromPage = navBackStack.arguments?.getString("FROMPAGE")
            AddNewAddressScreen(navController = navController, fromPage = fromPage?:"")
        }
        composable(
            route = Screens.NotificationScreen.route
        ){
            NotificationScreen(navController = navController)
        }
        composable(
            route = Screens.Help.route
        ){
            HelpScreen(navController = navController)
        }
        composable(
            route = Screens.About.route
        ){
            AboutScreen(navController = navController)
        }

        composable(
            route = Screens.MyOrdersScreen.route
        ){
            MyOrdersScreen(navController = navController)
        }
    }
}