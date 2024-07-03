package com.example.narumanam

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

const val ROOT = "root"
const val COMMONSCREEN = "common_screen"
const val SELLER_ACCOUNT = "seller_account"
const val BUYER_ACCOUNT = "buyer_account"

sealed class Screens (
   val route : String
){

    object splashScreen : Screens(route = "splash_screen")
    object decisionScreen : Screens(route = "decision_screen")
    object mobileScreen : Screens(route = "mobile_number_screen")
    object OtpVerificationScreen : Screens(route = "Otp_Verification_screen")
    object signupScreen : Screens(route = "signup")
    object loginScreen : Screens(route = "login")
    object AddressScreen : Screens(route = "address_screen")
    object NewAddressScreen : Screens(route = "add_new_address_screen")
    object MyDetailsScreen : Screens(route = "my_details_screen")
    object PostDiscountOfferScreen : Screens(route = "post_discount_offer_screen")
    object NotificationScreen : Screens(route = "notification_screen")
    object Help : Screens(route = "help_screen")
    object About : Screens(route = "about_screen")
    object MyOrdersScreen : Screens(route = "my_orders_screen")
    object BuyerMyDetailsScreen : Screens(route = "buyer_my_details_screen")
    object UpdatePasswordScreen : Screens(route = "update_password_screen")


    companion object {

        val APPNAME :String = "NARUMANAM"
          lateinit var callBacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    }

}