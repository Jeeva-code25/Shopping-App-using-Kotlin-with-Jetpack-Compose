package com.example.narumanam.sellersidepages.orderhistroy

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.R
import com.example.narumanam.accounts.MyOrderedItem
import com.example.narumanam.sellersidepages.MyHistoryItem
import com.example.narumanam.sellersidepages.MyHistoryItemCustName
import com.example.narumanam.sellersidepages.MyHistoryItemImage
import com.example.narumanam.sellersidepages.MyRequestItem
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle

@Composable
fun HistoryProductDetailedScreen(
    navController: NavHostController
) {


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
                                .padding(start = 12.dp, end = 12.dp,bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                when(MyHistoryItem.orderStatus) {
                                    "ORDER_CONFIRMED"-> ConfirmationProgressStatus(isProgress = MyHistoryItem.orderProgressing)
                                    "ORDER_SHIPPED"->  ProductHistoryProgressStatus(isProgress = MyHistoryItem.orderProgressing)
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
                                            painter = rememberAsyncImagePainter(MyHistoryItemImage),
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
                                                text = MyHistoryItem.productName,
                                                fontFamily = Poppins,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${ MyHistoryItem.orderedQuantity}${MyHistoryItem.quantityCategory}, Price",
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
                                                    text = MyHistoryItem.price,
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
                                                append(MyHistoryItem.orderId)
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
                                                append("${MyHistoryItem.orderedDate} & ${MyHistoryItem.orderedTime}")
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
                                    text = MyHistoryItemCustName,
                                    fontFamily = Poppins,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${MyHistoryItem.houseNo}, ${MyHistoryItem.streetName}," +
                                            " ${MyHistoryItem.district} -${MyHistoryItem.pinCode}," +
                                            " State: ${MyHistoryItem.state}, Landmark: ${MyHistoryItem.nearestLandMark}," +
                                            " Phone: ${MyHistoryItem.phoneNumber}",
                                    fontFamily = Poppins,
                                    fontSize = 15.sp,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            DividerHorizontal()


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
                text = "Item History",
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
private fun ProductHistoryProgressStatus(
    isProgress : String
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
                    .border(width = 1.5.dp, color = color, shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isProgress == "YES") {
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

