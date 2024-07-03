package com.example.narumanam.sellersidepages.orderrequest

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultNextButton
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.OrderManageProduct
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.sellersidepages.MyRequestCustName
import com.example.narumanam.sellersidepages.MyRequestItem
import com.example.narumanam.sellersidepages.MyRequestItemImage
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

@Composable
fun OrderConfirmationScreen(
    navController: NavHostController
) {

    val context = LocalContext.current
    val productManageDatabase = FirebaseDatabase
        .getInstance()
        .getReference("ProductOrderManagement")
    var processState by remember { mutableStateOf(false) }

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
                    item {
                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp,bottom=12.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                when (MyRequestItem.orderStatus) {
                                    "ORDER_PLACED" -> ConfirmationProgressStatus(isProgress = MyRequestItem.orderProgressing)
                                    "ORDER_SHIPPED" -> ShippingProgressStatus(isProgress = MyRequestItem.orderProgressing)
                                }

                            }

                            Text(
                                modifier = Modifier
                                    .padding(top = 15.dp),
                                text = "Ordered Product",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Green01
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFE2E2E2),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .padding(8.dp)
                            ){

                                Column(
                                    modifier = Modifier,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ){

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(MyRequestItemImage),
                                            contentDescription = "Product Image",
                                            modifier = Modifier
                                                .weight(1f)
                                                .size(125.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                        Column(
                                            modifier = Modifier
                                                .padding(start = 20.dp)
                                                .weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = MyRequestItem.productName,
                                                fontFamily = Poppins,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${MyRequestItem.orderedQuantity}${MyRequestItem.quantityCategory}, Price",
                                                fontFamily = Poppins,
                                                fontSize = 13.sp,
                                                color = Color(0xFF344356)
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                Icon(
                                                    modifier = Modifier
                                                        .size(15.dp),
                                                    imageVector = Icons.Outlined.CurrencyRupee,
                                                    contentDescription = "CurrencyRupee",
                                                    tint =  Color(0xFF344356)
                                                )
                                                Text(
                                                    text = MyRequestItem.price,
                                                    fontFamily = Poppins,
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF344356)
                                                )
                                            }



                                        }
                                    }

                                    Text(
                                        text =
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Bold,
                                                    color =  Color(0xFF344356) ,
                                                )
                                            ){
                                                append("ORDERED ID: ")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    color =  Color(0xFF344356) ,
                                                )
                                            ){
                                                append(MyRequestItem.orderId)
                                            }
                                        }
                                    )

                                    Text(
                                        text =
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Bold,
                                                    color =  Color(0xFF344356) ,
                                                )
                                            ){
                                                append("DATE & TIME: ")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    color =  Color(0xFF344356) ,
                                                )
                                            ){
                                                append("${MyRequestItem.orderedDate} & ${MyRequestItem.orderedTime}")
                                            }
                                        }
                                    )
                                }

                            }
                            DividerHorizontal()
                            Text(
                                text = "Contact Detail",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Green01
                            )

                            Column {

                                Text(
                                    text = MyRequestCustName,
                                    fontFamily = Poppins,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${MyRequestItem.houseNo}, ${MyRequestItem.streetName}," +
                                            " ${MyRequestItem.district} -${MyRequestItem.pinCode}," +
                                            " State: ${MyRequestItem.state}, Landmark: ${MyRequestItem.nearestLandMark}," +
                                            " Phone: ${MyRequestItem.phoneNumber}",
                                    fontFamily = Poppins,
                                    fontSize = 15.sp,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            DividerHorizontal()


                            Spacer(modifier = Modifier.height(50.dp))
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
                                            processState = true
                                            val cancelledProduct = OrderManageProduct(
                                                orderId = MyRequestItem.orderId,
                                                globProductId = MyRequestItem.globProductId,
                                                buyerPhoneNumber = MyRequestItem.buyerPhoneNumber,
                                                sellerPhoneNumber = MyRequestItem.sellerPhoneNumber,
                                                orderedQuantity = MyRequestItem.orderedQuantity,
                                                quantityCategory = MyRequestItem.quantityCategory,
                                                productName = MyRequestItem.productName,
                                                productImage = MyRequestItem.productImage,
                                                price = MyRequestItem.price,
                                                orderedDate = MyRequestItem.orderedDate,
                                                orderedTime = MyRequestItem.orderedTime,
                                                addressId = MyRequestItem.addressId,
                                                phoneNumber = MyRequestItem.phoneNumber,
                                                customerName = MyRequestItem.customerName,
                                                userName = MyRequestItem.userName,
                                                houseNo = MyRequestItem.houseNo,
                                                streetName = MyRequestItem.streetName,
                                                nearestLandMark = MyRequestItem.nearestLandMark,
                                                pinCode = MyRequestItem.pinCode,
                                                district = MyRequestItem.district,
                                                state = MyRequestItem.state,
                                                orderStatus = "ORDER_CONFIRMED",
                                                orderProgressing = "NO"
                                            )
                                            productManageDatabase
                                                .child(MyRequestItem.orderId)
                                                .setValue(cancelledProduct)
                                                .addOnSuccessListener {
                                                    processState = false
                                                    Toast.makeText(context,"Order has been Cancelled",Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()
                                                }

                                        },
                                        color = ButtonDefaults.buttonColors(Color(0xFFF3603F)),
                                        icon = ImageVector.vectorResource(id = R.drawable.cancel)
                                    )

                                }

                                    DefaultNextButton(
                                        modifier = Modifier
                                            .size(67.dp),
                                        onclick = {
                                            processState = true
                                            val confirmProduct = OrderManageProduct(
                                                orderId = MyRequestItem.orderId,
                                                globProductId = MyRequestItem.globProductId,
                                                buyerPhoneNumber = MyRequestItem.buyerPhoneNumber,
                                                sellerPhoneNumber = MyRequestItem.sellerPhoneNumber,
                                                orderedQuantity = MyRequestItem.orderedQuantity,
                                                quantityCategory = MyRequestItem.quantityCategory,
                                                productName = MyRequestItem.productName,
                                                productImage = MyRequestItem.productImage,
                                                price = MyRequestItem.price,
                                                orderedDate = MyRequestItem.orderedDate,
                                                orderedTime = MyRequestItem.orderedTime,
                                                addressId = MyRequestItem.addressId,
                                                phoneNumber = MyRequestItem.phoneNumber,
                                                customerName = MyRequestItem.customerName,
                                                userName = MyRequestItem.userName,
                                                houseNo = MyRequestItem.houseNo,
                                                streetName = MyRequestItem.streetName,
                                                nearestLandMark = MyRequestItem.nearestLandMark,
                                                pinCode = MyRequestItem.pinCode,
                                                district = MyRequestItem.district,
                                                state = MyRequestItem.state,
                                                orderStatus = "ORDER_CONFIRMED",
                                                orderProgressing = "YES"
                                            )

                                            productManageDatabase
                                                .child(MyRequestItem.orderId)
                                                .setValue(confirmProduct)
                                                .addOnSuccessListener {
                                                    processState = false
                                                    Toast.makeText(context,"Order has been Confirmed",Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()
                                                }

                                        },
                                    )


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
                text = "Order Confirmation Process",
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
private fun ConfirmationProgressStatus(
    isProgress : String
) {

    val color = if(isProgress == "YES") Green01 else Red01

    Column {


        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    .width(75.dp),
                thickness = 2.dp,
                color = color
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(color = color, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if(isProgress == "YES") {
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
                text = "Received",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(45.dp))
            Text(
                text = if (isProgress == "YES") "Confirmed" else "Cancelled",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(40.dp))
            Text(
                text = "Shipped",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = if (isProgress == "YES") Color(0xFFB3B3B3) else Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ShippingProgressStatus(
    isProgress: String
) {

    val color = if (isProgress == "YES") Green01 else Red01

    Column {


        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    .width(75.dp),
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
                    .width(75.dp),
                thickness = 2.dp,
                color = color
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(color = color, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isProgress == "YES") {
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
        }

        Row {
            Text(
                text =  "Received",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(45.dp))
            Text(
                text = "Confirmed",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(40.dp))
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

