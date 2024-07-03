package com.example.narumanam

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.commonScreenNavGraph(

    navController : NavHostController
){
    navigation(
        startDestination = Screens.splashScreen.route,
        route = COMMONSCREEN
    ){

        // Navigation match the start destination parameter to
        // below composable function route parameter. which one is matched that page will be displayed

        composable(
            route = Screens.splashScreen.route
        ){
            // Start Splash Screen
            AnimatedSplashScreen(navController)
        }

        composable(
            route = Screens.decisionScreen.route
        ){
            // User Choose their role buyer or seller Screen
            DecisionScreen(navController)
        }

        composable(
            route = Screens.mobileScreen.route
        ){
            //Get Mobile number from user Screen
            MobileNumberScreen(navController)
        }

        composable(
            route = Screens.OtpVerificationScreen.route +"/{VERIFICATIONID}/{PHONENUMBER}/{FROMPAGE}"
        ){ navBackStack ->

            // OTP Verification Screen
            val verfiyId = navBackStack.arguments?.getString("VERIFICATIONID")
            val phoneNo = navBackStack.arguments?.getString("PHONENUMBER")
            val fromPage = navBackStack.arguments?.getString("FROMPAGE")

            VerificationScreen(
                navController,
                verificationId = verfiyId.toString(),
                phoneNumber = phoneNo.toString(),
                fromPage = fromPage.toString()
            )
        }

        composable(
            route = Screens.UpdatePasswordScreen.route + "/{PHONENUMBER}"
        ){navBackStack->

            //Forgot Password Screen
            val phoneNo = navBackStack.arguments?.getString("PHONENUMBER")
            UpdatePasswordScreen(navController = navController,phone = phoneNo.toString())
        }

        composable(
            route = Screens.signupScreen.route+"/{PHONENUMBER}"
        ){navBackStack ->

            //Signup Screen for Collect information from First time user
            val phoneNo = navBackStack.arguments?.getString("PHONENUMBER")
            SignupScreen(navController, phoneNumber = phoneNo?:"")
        }

        composable(
            route = Screens.loginScreen.route+"/{PHONENUMBER}/{PASSWORD}/{EMAIL}/{USERNAME}"
        ){navBackStack ->

            //Login Screen for already signed user
            val phoneNumber = navBackStack.arguments?.getString("PHONENUMBER")
            val password = navBackStack.arguments?.getString("PASSWORD")
            val email = navBackStack.arguments?.getString("EMAIL")
            val userName = navBackStack.arguments?.getString("USERNAME")

            LoginScreen(
                navController,
                phoneNumber = phoneNumber.toString(),
                signupPassword =  password.toString(),
                email = email.toString(),
                userName = userName.toString()
            )

        }

    }

}