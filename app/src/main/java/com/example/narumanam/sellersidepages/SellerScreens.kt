package com.example.narumanam.sellersidepages

const val SELLER_ROOT = "seller_root"
const val SELLER_BOTTOM = "seller_bottom"
const val SELLER_PRODUCT_UPLOAD = "seller_product_upload"
const val SELLER_PRODUCT_EDIT = "seller_product_edit"
const val SELLER_ORDER_REQUEST = "seller_order_request"
const val SELLER_ORDER_HISTORY = "seller_order_history"

sealed class SellerScreens(
    val route : String
) {
    object productScreen : SellerScreens(route = "product_upload_screen")
    object productScreen2 : SellerScreens(route = "product_upload_screen_2")
    object productEditScreen : SellerScreens(route = "product_edit_screen")
    object ItemHistoryScreen : SellerScreens(route = "item_history_screen")
    object OrderConfirmationScreen : SellerScreens(route = "order_confirmation_screen")
    object OrderShippingScreen : SellerScreens(route = "order_shipping_screen")


}