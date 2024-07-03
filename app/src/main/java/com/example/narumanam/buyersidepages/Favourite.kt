package com.example.narumanam.buyersidepages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.productorderingprocess.BUYERPRODUCTIMAGES
import com.example.narumanam.buyersidepages.productorderingprocess.PRODUCTDATA
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.GlobProduct
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

@Composable
fun Favourite(
    navController: NavHostController){
    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val buyerDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Buyers")

    val globalProductDatabase = FirebaseDatabase
        .getInstance()
        .getReference("GlobalProducts")

    val favouriteProducts =  remember { mutableStateListOf<GlobProduct>() }
    val favouriteProductId =  remember { mutableStateListOf<String>() }

    val scope  = rememberCoroutineScope()
    val snackBarHostState  =   remember{ SnackbarHostState() }

    var processState by remember { mutableStateOf(true) }



    buyerDatabase
        .child(phoneNumber ?: "")
        .child("MyFavourites")
        .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                favouriteProducts.clear()
                favouriteProductId.clear()

                snapshot.children.forEach {item->

                    item.getValue(IdClass :: class.java)?.let {id->


                        globalProductDatabase
                            .child(id.globalProductId)
                            .get()
                            .addOnSuccessListener {snap->
                                snap.getValue(GlobProduct::class.java)
                                    ?.let {
                                        if (!favouriteProducts.contains(it)){
                                            favouriteProducts.add(it)
                                            favouriteProductId.add(id.globalProductId)
                                        }

                                    }

                            }

                    }
                }
                processState = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error", Toast.LENGTH_SHORT).show()
            }

        })



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
                 TopBar()
        },
        content = { it ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding()
                    )
                    .background(color = Color.White),
            ) {

                if (processState ){
                    ProgressIndicator()
                }

                if (favouriteProducts.size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                ) {
                    item {
                        DividerHorizontal()
                    }
                    item {

                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 12.dp,),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {

                                    favouriteProducts.forEach { item ->
                                        FavouriteItems(
                                            navController = navController,
                                            product = item
                                        )
                                    }

                            }

                    }

                }

                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White),
                    ) {
                        DividerHorizontal()
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            if (!processState) {
                                Text(
                                    text = "Add products to favourite",
                                    fontFamily = Poppins,
                                    color = Color(0xFF7c7c7c),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                        }

                    }
                }

            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    onClick = {

                        if(isConnection) {
                            processState = favouriteProducts.size > 0

                            var cartMovedCount = 0

                            favouriteProductId.forEach {
                                buyerDatabase
                                    .child(phoneNumber ?: "")
                                    .child("MyCart")
                                    .child(it)
                                    .setValue(IdClass(globalProductId = it))
                                    .addOnSuccessListener {
                                        cartMovedCount += 1
                                        if (cartMovedCount == favouriteProductId.size) {
                                            processState = false
                                            scope.launch {
                                                snackBarHostState.showSnackbar(
                                                    message = "Items are moved to cart",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                            }
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green01,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(19.dp)
                ) {

                    Icon(
                        modifier = Modifier
                            .size(22.dp),
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Cart",
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        modifier = Modifier,
                        text = "Add all to cart",
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,

                    )
                }
            }


        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Text(
                text = "Favourites",
                style = mobileTitle
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )

}

@Composable
private fun FavouriteItems(
    navController: NavHostController,
    product : GlobProduct
) {
    val isConnection = networkConnection(context = LocalContext.current)
    val images = product.productImages.split(",")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (isConnection) {
                    PRODUCTDATA = product
                    BUYERPRODUCTIMAGES = images
                    navController.navigate(BuyerScreens.ProductInDetailScreen.route)
                }
            },
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(images[0]),
            contentDescription = "Product Image",
            modifier = Modifier
                .weight(1f)
                .size(55.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = product.itemName,
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${product.quantity}${product.quantityCategory}, price",
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Color(0xFF344356)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(15.dp),
                    imageVector = Icons.Outlined.CurrencyRupee,
                    contentDescription = "CurrencyRupee",
                    tint = Color(0xFF344356)
                )
                Text(
                    text = product.actualPrice,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFF344356)
                )
            }


        }

        IconButton(
            modifier = Modifier
                .size(24.dp)
                .weight(0.5f),
            onClick = {

            }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Right",
            )
        }
    }

    DividerHorizontal()

}

