package com.example.narumanam.buyersidepages


const val BUYER_ROOT = "buyer_root"
const val BUYER_BOTTOM = "buyer_bottom"
const val ORDER_PLACING_ROOT = "order_placing_root"
val ORDERED_ITEMS = mutableListOf<OrderPlacingProductModel>()
sealed class BuyerScreens(
    val route : String
) {

    object ProductDisplayScreen : BuyerScreens(route = "product_display_screen")
    object ProductInDetailScreen : BuyerScreens(route = "product_in_detail_screen")
    object OrderSummaryScreen : BuyerScreens(route = "order_summary_screen")
    object PaymentScreen : BuyerScreens(route = "payment_screen")
    object UPIPaymentScreen : BuyerScreens(route = "upi_payment_screen")
    object CardPaymentScreen : BuyerScreens(route = "card_payment_screen")
    object OrderConfirmationScreen : BuyerScreens(route = "order_confirmation_screen")
    object TrackOrderScreen : BuyerScreens(route = "track_order_screen")

}