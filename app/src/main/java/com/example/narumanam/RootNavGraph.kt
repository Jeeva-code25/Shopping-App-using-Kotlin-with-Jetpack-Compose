package com.example.narumanam

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun InitialNavGraph(navController : NavHostController) {

    NavHost(
        navController = navController,
        startDestination = COMMONSCREEN,
        route = ROOT
    ){

        // This function control common screens for this app like login,signup...

        commonScreenNavGraph(navController)

    }

}