package com.example.narumanam.buyersidepages.productorderingprocess

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.narumanam.DefaultButton
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.HeadingText
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.SubTitleText
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.buyersidepages.IdClass
import com.example.narumanam.buyersidepages.ORDERED_ITEMS
import com.example.narumanam.buyersidepages.OrderPlacingProductModel
import com.example.narumanam.networkConnection
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductInDetailScreen(
    navController: NavHostController
) {
    val  productData = PRODUCTDATA
    val imageList = BUYERPRODUCTIMAGES

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)

    val scope  = rememberCoroutineScope()
    val snackBarHostState  =   remember{ SnackbarHostState() }

    val inHandQty = productData.inHand.filter { it.isDigit() }
    
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")

    val buyerDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Buyers")


    val state = rememberPagerState() {
        imageList.size
    }

    var processState by remember { mutableStateOf(true) }
    var descExpanded by remember { mutableStateOf(false) }
    var isFavourite by remember { mutableStateOf(false) }

    var productQuantity by remember { mutableStateOf("1") }
    var productQuantityError by remember { mutableStateOf(false) }
    var productQuantityIsSelected by remember { mutableStateOf(false) }


    buyerDatabase
        .child(phoneNumber ?: "")
        .child("MyFavourites")
        .child(productData.globProductId)
        .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isFavourite = snapshot.exists()
                processState = false
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    if (PRODUCTDATA.globProductId != "" && BUYERPRODUCTIMAGES.isNotEmpty()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {


                item {

                    if (processState) {
                        ProgressIndicator()
                    }

                    Box {

                        ProductEditImage(state = state, imageList = imageList)
                        DotsIndicator(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .wrapContentHeight()
                                .align(Alignment.BottomCenter),
                            totalDots = imageList.size,
                            selectedIndex = state.currentPage
                        )

                        DefaultBackArrow(
                            modifier = Modifier
                                .padding(start = 4.dp, top = 4.dp)
                                .align(Alignment.TopStart),
                            onclick = { navController.popBackStack() }
                        )

                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                            .background(color = Color.White),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubTitleText(
                                modifier = Modifier
                                    .weight(1f),
                                text = productData.itemName,
                                fontSize = 20.sp
                            )
                            IconButton(
                                onClick = {

                                    if (isConnection) {
                                        if (isFavourite) {
                                            buyerDatabase
                                                .child(phoneNumber ?: "")
                                                .child("MyFavourites")
                                                .child(productData.globProductId)
                                                .removeValue()
                                                .addOnSuccessListener {
                                                    scope.launch {
                                                        snackBarHostState.showSnackbar(
                                                            message = "Item removed from favourites",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
                                                }
                                        } else {

                                            buyerDatabase
                                                .child(phoneNumber ?: "")
                                                .child("MyFavourites")
                                                .child(productData.globProductId)
                                                .setValue(IdClass(globalProductId = productData.globProductId))
                                                .addOnSuccessListener {
                                                    scope.launch {
                                                        snackBarHostState.showSnackbar(
                                                            message = "Item added to favourites",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
                                                }
                                        }
                                    }
                                }
                            ) {

                                if (isFavourite) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "favourite",
                                        tint = Color(0xFFFF4C4C)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "favourite",
                                        tint = Color(0xFF7C7C7C)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Price ${productData.quantity}${productData.quantityCategory}",
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7C7C7C)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconButton(onClick = {
                                    productQuantityIsSelected = true
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.subtract_icon),
                                        contentDescription = "lessIcon",
                                        tint = if (!productQuantityIsSelected) Color(0XFFB3B3B3) else Green01,
                                        modifier = Modifier.clickable {
                                            var lessQuantity = productQuantity.toIntOrNull()
                                            if (lessQuantity != null) {
                                                lessQuantity -= 1
                                                productQuantity =
                                                    if (lessQuantity > 0) lessQuantity.toString() else "0"
                                            } else {
                                                productQuantity = "0"
                                            }
                                        }
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .size(50.dp),
                                    value = productQuantity,
                                    onValueChange = { it ->
                                        productQuantityError = false
                                        productQuantity = it.filter { it.isDigit() }
                                    },
                                    shape = RoundedCornerShape(17.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        unfocusedIndicatorColor = Color(0xFFB3B3B3),
                                        focusedIndicatorColor = Green01,
                                        errorContainerColor = Color.White,
                                        errorIndicatorColor = Color.Red
                                    ),
                                    textStyle = LocalTextStyle.current.copy(
                                        fontFamily = Poppins,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    singleLine = true,
                                    isError = productQuantityError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            productQuantityError =
                                                TextUtils.isEmpty(productQuantity)

                                        }
                                    )

                                )
                                IconButton(onClick = {
                                    productQuantityIsSelected = false
                                }) {

                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "addIcon",
                                        tint = if (productQuantityIsSelected) Color(0XFFB3B3B3) else Green01,
                                        modifier = Modifier.clickable {
                                            var addQuantity = productQuantity.toIntOrNull()

                                            if (addQuantity != null) {
                                                addQuantity += 1
                                                productQuantity = addQuantity.toString()
                                            } else {
                                                productQuantity = "0"
                                            }

                                        }
                                    )

                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(20.dp),
                                    imageVector = Icons.Outlined.CurrencyRupee,
                                    contentDescription = "CurrencyRupee",
                                    tint = Color.Black
                                )
                                Text(
                                    text = productData.actualPrice,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.Black
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HeadingText(
                                modifier = Modifier
                                    .weight(1f),
                                text = "Description",
                            )
                            IconButton(
                                onClick = {
                                    descExpanded = !descExpanded
                                }
                            ) {
                                Icon(
                                    imageVector = if (descExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = "arrow"
                                )
                            }
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(animationSpec = tween(200)),
                            text = productData.description,
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            color = Color(0xFF7C7C7C),
                            maxLines = if (descExpanded) Int.MAX_VALUE else 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        DividerHorizontal()

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontFamily = Poppins,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    ){
                                        append("Available Stock: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontFamily = Poppins,
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                    ){
                                        append(productData.inHand)
                                    }

                                },
                            )


                        DefaultButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),

                            onclick = {

                                if(isConnection) {
                                    buyerDatabase
                                        .child(phoneNumber ?: "")
                                        .child("MyCart")
                                        .child(productData.globProductId)
                                        .child("globalProductId")
                                        .setValue(productData.globProductId)
                                        .addOnSuccessListener {

                                            scope.launch {
                                                snackBarHostState.showSnackbar(
                                                    message = "Item added to cart",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }

                                        }
                                }

                            },
                            text = "Add to Cart",
                            color = ButtonDefaults.buttonColors(Color(0xFF344356))
                        )

                        DefaultButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            onclick = {

                                if (inHandQty.toInt() > 0 && productQuantity.toInt() <= inHandQty.toInt() && isConnection) {
                                    ORDERED_ITEMS.clear()
                                    val item = OrderPlacingProductModel(
                                        globProductId = productData.globProductId,
                                        sellerProductId = productData.productId,
                                        sellerName = productData.userName,
                                        itemName = productData.itemName,
                                        itemCategory = productData.productCategory,
                                        sellerPhoneNumber = productData.sellerPhoneNumber,
                                        orderedQuantity = productQuantity,
                                        inHand = productData.inHand,
                                        quantityCategory = productData.quantityCategory,
                                        productImage = productData.productImages,
                                        price = productData.actualPrice
                                    )
                                    ORDERED_ITEMS.add(item)
                                    navController.navigate(BuyerScreens.OrderSummaryScreen.route)
                                } else {
                                    Toast.makeText(context,"Out of Stock",Toast.LENGTH_SHORT).show()
                                }

                            },
                            text = if (inHandQty.toInt() > 0 && productQuantity.toInt() <= inHandQty.toInt())
                                "Buy Now" else "Out of Stock",
                            color = ButtonDefaults.buttonColors(Green01)
                        )


                    }
                }
            }
        }
    } else {
        navController.popBackStack()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductEditImage(
    state: PagerState,
    imageList: List<String>
) {
    HorizontalPager(
        state = state
    ) {pageIndex->

        val image = imageList[pageIndex].filterNot { it == '[' || it == ']' }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            painter = rememberAsyncImagePainter(model = image.trim()),
            contentDescription = "ProductImage",
            contentScale = ContentScale.FillBounds
        )


    }
}

@Composable
private fun DotsIndicator(
    modifier: Modifier,
    totalDots : Int,
    selectedIndex : Int
) {

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .height(5.dp)
                        .width(10.dp)
                        .clip(RectangleShape)
                        .background(color = Green01)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color = Color(0xFFB3B3B3))
                )
            }

        }
    }

}

