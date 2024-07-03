package com.example.narumanam.sellersidepages.navigation

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.narumanam.Screens
import com.example.narumanam.accounts.accountsNavGraph
import com.example.narumanam.sellersidepages.SELLER_BOTTOM
import com.example.narumanam.sellersidepages.SELLER_ROOT
import com.example.narumanam.sellersidepages.orderhistroy.orderHistoryNavGraph
import com.example.narumanam.sellersidepages.orderrequest.orderRequestNavGraph
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SellerNavGraph(navController : NavHostController) {
    val context = LocalContext.current as Activity
    val sharedPreference = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val userRole = sharedPreference.getString("USERROLE", "")

    NavHost(navController = navController,
        startDestination = SELLER_BOTTOM,
        route = SELLER_ROOT){

        bottomNavGraph(navController)
        productNavGraph(navController = navController)
        productEditNavGraph(navController = navController)
        orderRequestNavGraph(navController = navController)
        orderHistoryNavGraph(navController = navController)
        accountsNavGraph(navController = navController, userRole = userRole?:"")

    }

}