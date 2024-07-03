package com.example.narumanam.buyersidepages.navigation

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.narumanam.Screens
import com.example.narumanam.accounts.accountsNavGraph
import com.example.narumanam.buyersidepages.BUYER_BOTTOM
import com.example.narumanam.buyersidepages.BUYER_ROOT


@Composable
fun BuyerNavGraph(navController : NavHostController) {

    val context = LocalContext.current as Activity
    val sharedPreference = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val userRole = sharedPreference.getString("USERROLE", "")

    NavHost(navController = navController,
        startDestination = BUYER_BOTTOM,
        route = BUYER_ROOT){

        buyerBottomNavGraph(navController)
        orderPlacingNavGraph(navController)
        accountsNavGraph(navController, userRole = userRole?:"")

    }

}