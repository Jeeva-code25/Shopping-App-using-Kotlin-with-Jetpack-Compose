package com.example.narumanam.sellersidepages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.narumanam.sellersidepages.SELLER_PRODUCT_UPLOAD
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.sellersidepages.products.ProductUploadScreen
import com.example.narumanam.sellersidepages.products.ProductUploadScreen2


fun NavGraphBuilder.productNavGraph(navController : NavHostController)
{
    navigation(
        startDestination = SellerScreens.productScreen.route,
        route = SELLER_PRODUCT_UPLOAD
    ){

        composable(
            route = SellerScreens.productScreen.route
        ){
            ProductUploadScreen(navController = navController)
        }
        composable(
            route = SellerScreens.productScreen2.route+"/{PRODUCTID}/{PRODUCTCATEGORY}/{ITEMNAME}"+
                    "/{BRANDNAME}/{QUANTITY}/{QUANTITYCATEGORY}/{ACTUALPRICE}/{INHAND}/{OFFER}"
        ){navBackStack ->

            val productId = navBackStack.arguments?.getString("PRODUCTID")
            val productCategory = navBackStack.arguments?.getString("PRODUCTCATEGORY")
            val itemName = navBackStack.arguments?.getString("ITEMNAME")
            val brandName = navBackStack.arguments?.getString("BRANDNAME")
            val quantity = navBackStack.arguments?.getString("QUANTITY")
            val quantityCategory = navBackStack.arguments?.getString("QUANTITYCATEGORY")
            val actualPrice = navBackStack.arguments?.getString("ACTUALPRICE")
            val inHand = navBackStack.arguments?.getString("INHAND")
            val offer = navBackStack.arguments?.getString("OFFER")

            ProductUploadScreen2(
                navController = navController,
                productId = productId.toString(),
                productCategory = productCategory.toString(),
                itemName = itemName.toString(),
                brandName = brandName.toString(),
                quantity = quantity.toString(),
                quantityCategory = quantityCategory.toString(),
                actualPrice = actualPrice.toString(),
                inHand = inHand.toString(),
                offer = offer.toString()
            )
        }

    }

}