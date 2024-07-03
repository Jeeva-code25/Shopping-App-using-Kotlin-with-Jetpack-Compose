package com.example.narumanam.accounts

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.OrderManageProduct
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.buyersidepages.IdClass
import com.example.narumanam.networkConnection
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var MyOrderedItem = OrderManageProduct()
@Composable
fun MyOrdersScreen(
    navController : NavHostController
) {

    val context = LocalContext.current
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val buyerDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Buyers")

    val productManageDatabase = FirebaseDatabase
        .getInstance()
        .getReference("ProductOrderManagement")

    val myOrders =  remember { mutableStateListOf<OrderManageProduct>() }

    var processState by remember { mutableStateOf(true) }

    buyerDatabase
        .child(phoneNumber ?: "")
        .child("MyOrders")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myOrders.clear()

                snapshot.children.forEach {item->

                    item.getValue(IdClass :: class.java)?.let { id->

                        productManageDatabase
                            .child(id.orderId)
                            .get()
                            .addOnSuccessListener {snap->
                                snap.getValue(OrderManageProduct::class.java)
                                    ?.let {
                                        if (!myOrders.contains(it)){
                                            myOrders.add(it)
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
        topBar = {
            TopBar(navController = navController)
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                ) {
                    item {
                        DividerHorizontal()
                    }


                    if(myOrders.size>0) {
                        items(count = myOrders.size) {
                            OrderPlacedItems(
                                navController = navController,
                                myProduct = myOrders[it]
                            )
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!processState) {
                                    Text(
                                        modifier = Modifier.align(alignment = Alignment.Center),
                                        text = "Go to Purchase!",
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

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavHostController
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = Color.White),
        title = {
            Text(
                text = "My Orders",
                style = mobileTitle
            )
        },
        navigationIcon = {
            DefaultBackArrow {
                navController.popBackStack()
            }
        }
    )

}

@Composable
private fun OrderPlacedItems(
    navController: NavHostController,
    myProduct : OrderManageProduct
) {

    val isConnection = networkConnection(context = LocalContext.current)
    val imageList = myProduct.productImage.split(",")
    val image = imageList[0].replace("[","")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {

                if (isConnection) {
                    MyOrderedItem = myProduct
                    navController.navigate(BuyerScreens.TrackOrderScreen.route)
                }

            },
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(image),
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
                text = myProduct.productName,
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${myProduct.orderedQuantity}${myProduct.quantityCategory}, price",
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
                    text = myProduct.price,
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
                if (isConnection) {
                    MyOrderedItem = myProduct
                    navController.navigate(BuyerScreens.TrackOrderScreen.route)
                }
            }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Right",
            )
        }
    }

    DividerHorizontal()

}