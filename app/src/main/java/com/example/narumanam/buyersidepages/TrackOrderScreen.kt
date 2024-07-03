package com.example.narumanam.buyersidepages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.PermDeviceInformation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultNextButton
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.OrderManageProduct
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.accounts.MyOrderedItem
import com.example.narumanam.accounts.MyOrdersScreen
import com.example.narumanam.networkConnection
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun TrackOrderScreen(
    navController : NavHostController
) {

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)

    var shippedExpanded by remember { mutableStateOf(false) }
    var processState by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    val productManageDatabase = FirebaseDatabase
        .getInstance()
        .getReference("ProductOrderManagement")

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

                if(processState){
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

                    item{


                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)

                        ) {

                            if (showCancelDialog) {
                                AlertDialog(
                                    onDismissRequest = { showCancelDialog = false },
                                    icon = {
                                        Icon(imageVector = Icons.Outlined.PermDeviceInformation, contentDescription = "alert")
                                    },
                                    title = {
                                        Text(
                                            text = "Are you sure? to cancel Order",
                                            fontSize = 13.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "10% of amount will be deducted from product price ",
                                            fontSize = 13.sp,
                                            fontFamily = Poppins,
                                            textAlign = TextAlign.Justify,
                                        )

                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                if (isConnection) {
                                                    processState = true
                                                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")

                                                    productManageDatabase
                                                        .child(MyOrderedItem.orderId)
                                                        .child("orderProgressing")
                                                        .setValue("NO")
                                                        .addOnSuccessListener {
                                                            productManageDatabase
                                                                .child(MyOrderedItem.orderId)
                                                                .child("productCategory")
                                                                .setValue(MyOrderedItem.productCategory)
                                                                .addOnSuccessListener {
                                                                    productManageDatabase
                                                                        .child(MyOrderedItem.orderId)
                                                                        .child("cancelDate")
                                                                        .setValue(
                                                                            dateFormat.format(
                                                                                Date()
                                                                            )
                                                                        )
                                                                        .addOnSuccessListener {
                                                                            processState = false
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Order has been Cancelled",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                            navController.popBackStack()
                                                                        }
                                                                }


                                                        }
                                                    showCancelDialog = false
                                                }
                                            })
                                        {

                                            Text(
                                                text = "Confirm",
                                                color = Color.Red,
                                                fontSize = 14.sp,
                                                fontFamily = Poppins,
                                            )
                                        }
                                    },
                                    dismissButton = {

                                        TextButton(onClick = {
                                            showCancelDialog = false
                                        }) {
                                            Text(
                                                text = "Cancel",
                                                color =  Color(0xFF7C7C7C),
                                                fontSize = 14.sp,
                                                fontFamily = Poppins,
                                            )

                                        }
                                    }
                                )
                            }
                            Text(
                                text = "Delivery Address",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )


                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = MyOrderedItem.userName,
                                    fontFamily = Poppins,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${MyOrderedItem.houseNo}, ${MyOrderedItem.streetName}," +
                                            " ${MyOrderedItem.district} -${MyOrderedItem.pinCode}," +
                                            " State: ${MyOrderedItem.state}, Landmark: ${MyOrderedItem.nearestLandMark}," +
                                            " Phone: ${MyOrderedItem.phoneNumber}",
                                    fontFamily = Poppins,
                                    fontSize = 15.sp,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            DividerHorizontal()

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Surface(
                                   modifier = Modifier
                                       .weight(1f)
                                       .background(color = Color.White)
                                ) {

                                    when (MyOrderedItem.orderStatus) {
                                        "ORDER_CONFIRMED" -> MyOrderShippedStatus(isProgress = MyOrderedItem.orderProgressing,status = MyOrderedItem.orderStatus)
                                        "ORDER_SHIPPED" -> MyOrderShippedStatus(isProgress = MyOrderedItem.orderProgressing,status = MyOrderedItem.orderStatus)
                                    }

                                }
                                IconButton(
                                    modifier = Modifier.align(alignment = Alignment.Bottom),
                                    onClick = {
                                        shippedExpanded = !shippedExpanded
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (shippedExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                        contentDescription = "arrow"
                                    )
                                }




                            }

                            if (shippedExpanded) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize(animationSpec = tween(200)),
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 15.sp,
                                                fontFamily = Poppins,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF7C7C7C)
                                            )
                                        ) {
                                            append("Courier Name: ")
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 12.sp,
                                                fontFamily = Poppins,
                                                color = Color(0xFF7C7C7C)
                                            )
                                        ) {
                                            append(MyOrderedItem.courierPartnerName)
                                        }
                                    },
                                )

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize(animationSpec = tween(200)),
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 15.sp,
                                                fontFamily = Poppins,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF7C7C7C)
                                            )
                                        ) {
                                            append("Track Id: ")
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 12.sp,
                                                fontFamily = Poppins,
                                                color = Color(0xFF7C7C7C)
                                            )
                                        ) {
                                            append(MyOrderedItem.courierTrackId)
                                        }
                                    },
                                )
                            }

                            DividerHorizontal()

                            Text(
                                text = "Order Details",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("Order Id: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            color = Color(0xFF7C7C7C)
                                        )
                                    ) {
                                        append(MyOrderedItem.orderId)
                                    }
                                },
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("Item Name: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            color = Color(0xFF7C7C7C)
                                        )
                                    ) {
                                        append(MyOrderedItem.productName)
                                    }
                                },
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("Quantity: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            color = Color(0xFF7C7C7C)
                                        )
                                    ) {
                                        append(MyOrderedItem.orderedQuantity+ MyOrderedItem.quantityCategory)
                                    }
                                },
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("Date & Time: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            color = Color(0xFF7C7C7C)
                                        )
                                    ) {
                                        append("${MyOrderedItem.orderedDate} & ${MyOrderedItem.orderedTime}")
                                    }
                                },
                            )

                            DividerHorizontal()

                            Text(
                                text = "Contact Seller",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("Name: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            color = Color(0xFF7C7C7C)
                                        )
                                    ) {
                                        append(MyOrderedItem.sellerName)
                                    }
                                },
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("Phone: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            color = Color(0xFF7C7C7C)
                                        )
                                    ) {
                                        append(MyOrderedItem.sellerPhoneNumber)
                                    }
                                },
                            )

                            Spacer(modifier = Modifier.height(50.dp))
                            if (MyOrderedItem.orderProgressing != "NO") {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .weight(1f),
                                    ) {
                                        DefaultNextButton(
                                            modifier = Modifier
                                                .size(67.dp),
                                            onclick = {
                                                showCancelDialog = true

                                            },
                                            color = ButtonDefaults.buttonColors(Color(0xFFF3603F)),
                                            icon = ImageVector.vectorResource(id = R.drawable.cancel)
                                        )

                                    }
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
private fun MyOrderConfirmedStatus(
    isProgress : String,
    status : String
) {

    val color = if(isProgress == "YES") Green01 else Red01

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {


        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = color, shape = CircleShape)
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
                    tint = color
                )
            }

            DividerHorizontal(
                modifier = Modifier
                    .height(75.dp)
                    .width(2.dp),
                thickness = 2.dp,
                color = color
            )

            if(isProgress == "YES" && status == "ORDER_CONFIRMED"){
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .border(width = 1.5.dp, color = color, shape = CircleShape)
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
                        tint = color
                    )
                }

            } else {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(color = color, shape = CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isProgress == "YES" && status == "ORDER_PLACED") {
                        Text(
                            text = "2",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .width(14.dp)
                                .height(10.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.card_cancel),
                            contentDescription = "done",
                            tint = Color.White
                        )
                    }
                }
            }

            DividerHorizontal(
                 modifier = Modifier
                     .height(75.dp)
                     .width(2.dp),
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

        Column(
        ){
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = "Order Placed",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(83.dp))
            Text(
                text = if (isProgress == "YES") "Confirmed" else "Cancelled",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(85.dp))
            Text(
                text = "Shipped",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFB3B3B3),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun MyOrderShippedStatus(
    isProgress: String,
    status : String
) {

    val color = if(isProgress == "YES") Green01 else Red01

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {


        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = color, shape = CircleShape)
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
                    tint = color
                )
            }

            DividerHorizontal(
                modifier = Modifier
                    .height(75.dp)
                    .width(2.dp),
                thickness = 2.dp,
                color = color
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = color, shape = CircleShape)
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
                    tint = color
                )
            }

            DividerHorizontal(
                modifier = Modifier
                    .height(75.dp)
                    .width(2.dp),
                thickness = 2.dp,
                color = color
            )

            if(status == "ORDER_CONFIRMED"){
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(color = color, shape = CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if(isProgress == "YES"){
                        Text(
                            text = "3",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .width(14.dp)
                                .height(10.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.card_cancel),
                            contentDescription = "done",
                            tint = Color.White
                        )
                    }

                }
            } else {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .border(width = 1.5.dp, color = color, shape = CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if(isProgress == "YES"){
                        Icon(
                            modifier = Modifier
                                .width(14.dp)
                                .height(10.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.done_progress_status),
                            contentDescription = "done",
                            tint = color
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .width(14.dp)
                                .height(10.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.card_cancel),
                            contentDescription = "done",
                            tint = color
                        )
                    }

                }
            }

        }

        Column(
        ){
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = "Order Placed",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(83.dp))
            Text(
                text =  "Confirmation",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(85.dp))
            Text(
                text = if (isProgress == "YES") "Shipped" else "Cancelled",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}



