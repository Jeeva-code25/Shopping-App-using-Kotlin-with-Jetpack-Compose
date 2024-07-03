package com.example.narumanam.buyersidepages.productorderingprocess

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultButton
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.accounts.Address
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.buyersidepages.ORDERED_ITEMS
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.AddressExist
import com.example.narumanam.sellersidepages.products.AddressId
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderSummaryScreen(
    navController : NavHostController
) {

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val database = FirebaseDatabase.getInstance().getReference("Buyers")

    var processState by remember { mutableStateOf(true) }
    var address by remember { mutableStateOf(Address()) }

    val deliveryCharge = 100.0

    database
        .child(phoneNumber?:"")
        .child("SelectedAddress")
        .get()
        .addOnSuccessListener {
            processState= false
            if (it.exists()) {
                AddressExist = true
                AddressId = it.getValue(String::class.java)!!
                database
                    .child(phoneNumber ?: "")
                    .child("MyAddress")
                    .child(AddressId)
                    .get()
                    .addOnSuccessListener { snapShot ->
                        address = snapShot.getValue(Address::class.java)!!
                    }
            }  else {
                AddressExist = false
                navController.navigate(Screens.AddressScreen.route+ "/${"ORDERSUMMARY"}")
            }
        }


    if (processState) {
        ProgressIndicator()
    }

    if (ORDERED_ITEMS.size > 0 ) {
        if (!processState) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    TopBar(navController = navController)
                },
                content = {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = it.calculateTopPadding(),
                                bottom = it.calculateBottomPadding()
                            )
                            .background(color = Color.White),
                    ) {
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
                                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(15.dp)
                                ) {


                                    Row(
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        OrderSummaryProgressStatus()
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .weight(1f),
                                            text = "Delivery to this address",
                                            fontSize = 17.sp,
                                            fontFamily = Poppins,
                                            fontStyle = FontStyle.Italic,
                                            fontWeight = FontWeight.Bold,
                                        )

                                        OutlinedButton(
                                            onClick = {
                                                navController.navigate(Screens.AddressScreen.route)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = Green01,
                                                containerColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(5.dp),
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = Color(0xFFB3B3B3)
                                            )
                                        ) {
                                            Text(
                                                text = "Change",
                                                fontSize = 14.sp,
                                                fontFamily = Poppins,
                                                fontStyle = FontStyle.Italic,
                                                color = Green01
                                            )
                                        }
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = address.userName,
                                            fontFamily = Poppins,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${address.houseNo}, ${address.streetName}," +
                                                    " ${address.district} -${address.pinCode}," +
                                                    " State: ${address.state}, Landmark: ${address.nearestLandMark}," +
                                                    " Phone: ${address.phoneNumber}",
                                            fontFamily = Poppins,
                                            fontSize = 15.sp,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    DividerHorizontal()

                                    Text(
                                        text = "Order Summary",
                                        fontSize = 17.sp,
                                        fontFamily = Poppins,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Bold,
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {

                                        var totalCost = 0.0

                                        ORDERED_ITEMS.forEach { item ->

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {


                                                Text(
                                                    modifier = Modifier
                                                        .weight(1f),
                                                    text = item.itemName,
                                                    fontSize = 14.sp,
                                                    fontFamily = Poppins,
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
                                                        text = item.orderedQuantity+"x"+item.price,
                                                        fontFamily = Poppins,
                                                        fontSize = 12.sp,
                                                        color = Color(0xFF344356)
                                                    )
                                                }


                                            }

                                            totalCost += item.orderedQuantity.toDouble()*item.price.toDouble()

                                        }

                                        SummaryItem(
                                            text = "Delivery Charges",
                                            price = deliveryCharge.toString()
                                        )
                                        SummaryItem(
                                            text = "Total Cost",
                                            price = (totalCost + deliveryCharge).toString()
                                        )

                                    }
                                    DividerHorizontal()
                                    Spacer(modifier = Modifier.height(40.dp))
                                    DefaultButton(
                                        modifier = Modifier
                                            .height(55.dp)
                                            .align(Alignment.End),
                                        onclick = {

                                            if (isConnection) {
                                                navController.navigate(BuyerScreens.PaymentScreen.route)
                                            }
                                        },
                                        shape = RoundedCornerShape(5.dp),
                                        text = "Continue"
                                    )


                                }
                            }

                        }

                    }
                }
            )
        }
    } else {
        navController.popBackStack()
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavHostController
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Text(
                text = "Order Summary",
                style = mobileTitle
            )
        },
        navigationIcon = {
                         DefaultBackArrow {
                             navController.popBackStack()
                         }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )

}

@Composable
private fun OrderSummaryProgressStatus() {


    Column {


        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = Green01, shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .width(14.dp)
                        .height(10.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.done_progress_status),
                    contentDescription = "done",
                    tint = Green01
                )
            }

            DividerHorizontal(
                modifier = Modifier
                    .width(75.dp),
                thickness = 2.dp,
                color = Green01
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(color = Green01, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "2",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            DividerHorizontal(
                modifier = Modifier
                    .width(75.dp),
                thickness = 2.dp,
                color = Color(0xFFB3B3B3)
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = Color(0xFFB3B3B3), shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFB3B3B3),
                    textAlign = TextAlign.Center
                )
            }
        }

        Row {
            Text(
                text = "Address",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = "Order Summary",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                text = "Payment",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFB3B3B3),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SummaryItem(
    text : String,
    price : String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .weight(1f),
            text = text,
            fontSize = 14.sp,
            fontWeight = if (text == "Amount Payable") FontWeight.Bold else FontWeight.Normal,
            fontFamily = Poppins,
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
                text = price,
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = if (text == "Amount Payable") FontWeight.Bold else FontWeight.Normal,
                color = Color(0xFF344356)
            )
        }

    }

}