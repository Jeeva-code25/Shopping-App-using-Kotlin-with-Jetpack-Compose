package com.example.narumanam.buyersidepages

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.productorderingprocess.BUYERPRODUCTIMAGES
import com.example.narumanam.buyersidepages.productorderingprocess.PRODUCTDATA
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.GlobProduct
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Cart(
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

    val cartProducts =  remember { mutableStateListOf<GlobProduct>() }
    val cartProductId =  remember { mutableStateListOf<String>() }

    val scope  = rememberCoroutineScope()
    val snackBarHostState  =   remember{ SnackbarHostState() }

    var processState by remember { mutableStateOf(true) }
    var checkoutClicked by remember { mutableStateOf(false) }


    buyerDatabase
        .child(phoneNumber ?: "")
        .child("MyCart")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartProducts.clear()
                cartProductId.clear()

                snapshot.children.forEach {item->

                    item.getValue(IdClass :: class.java)?.let {id->

                        globalProductDatabase
                            .child(id.globalProductId)
                            .get()
                            .addOnSuccessListener {snap->
                                snap.getValue(GlobProduct::class.java)
                                    ?.let {
                                        if (!cartProducts.contains(it)){
                                            cartProducts.add(it)
                                            cartProductId.add(id.globalProductId)
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

                if (processState ){
                    ProgressIndicator()
                }

                if (cartProducts.size > 0) {
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

                                    cartProducts.forEach { item ->
                                    CartItems(
                                        navController = navController,
                                        product = item,
                                        buyerDatabase = buyerDatabase,
                                        phoneNumber = phoneNumber ?: "",
                                        scope = scope,
                                        snackBarHostState = snackBarHostState,
                                        clicked = checkoutClicked
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
                                    text = "Empty Cart!",
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
                        if (isConnection) {
                            checkoutClicked = true
                            navController.navigate(BuyerScreens.OrderSummaryScreen.route)
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
                        imageVector = ImageVector.vectorResource(id = R.drawable.orders_icon),
                        contentDescription = "Cart",
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        modifier = Modifier,
                        text = "Go to Checkout",
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
                text = "My Cart",
                style = mobileTitle
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )

}

@Composable
private fun CartItems(
    navController: NavHostController,
    product: GlobProduct,
    buyerDatabase: DatabaseReference,
    phoneNumber: String,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    clicked: Boolean
) {

    val focusManager = LocalFocusManager.current
    val isConnection = networkConnection(context = LocalContext.current)
    var quantity by remember { mutableStateOf("1") }
    var quantityError by remember { mutableStateOf(false)}
    var quantityIsSelected by remember { mutableStateOf(false)}

    val images = product.productImages.split(",")
    val inHandQty = product.inHand.filter { it.isDigit() }

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

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                text = product.itemName,
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
                )
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = {
                        if (isConnection) {
                            buyerDatabase
                                .child(phoneNumber)
                                .child("MyCart")
                                .child(product.globProductId)
                                .removeValue()
                                .addOnSuccessListener {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "Item removed to cart",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                        }
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.card_cancel),
                        contentDescription = "Remove",
                        tint = Color(0xFFB3B3B3)
                    )
                }
            }
            if (inHandQty.toInt() <= 0) {

                Text(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.White),
//                        .align(Alignment.Center),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    text = "Out of Stock",
                    color = Red01
                )

            }
            Text(
                text = "${product.quantity}${product.quantityCategory}, price",
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Color(0xFF344356)
            )
            Row(
                modifier = Modifier. fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        quantityIsSelected = true
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.subtract_icon),
                            contentDescription = "lessIcon",
                            tint = if (!quantityIsSelected) Color(0XFFB3B3B3) else Green01,
                            modifier = Modifier.clickable {
                                var lessQuantity = quantity.toIntOrNull()
                                if (lessQuantity != null) {
                                    lessQuantity -= 1
                                    quantity =
                                        if (lessQuantity > 0) lessQuantity.toString() else "0"
                                } else {
                                    quantity = "0"
                                }
                            }
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .size(50.dp),
                        value = quantity,
                        onValueChange = { it ->
                            quantityError = false
                            quantity = it.filter { it.isDigit() }
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
                        isError = quantityError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                quantityError = TextUtils.isEmpty(quantity)
                                if (!quantityError) {
                                    focusManager.clearFocus()
                                }

                            }
                        )

                    )
                    IconButton(onClick = {
                        quantityIsSelected = false
                    }) {

                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "addIcon",
                            tint = if (quantityIsSelected) Color(0XFFB3B3B3) else Green01,
                            modifier = Modifier.clickable {
                                var addQuantity = quantity.toIntOrNull()

                                if (addQuantity != null) {
                                    addQuantity += 1
                                    quantity = addQuantity.toString()
                                } else {
                                    quantity = "0"
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

        }
    }


    if (!clicked && (inHandQty.toInt() > 0 && quantity.toInt() <= inHandQty.toInt())) {

        val item = OrderPlacingProductModel(
            globProductId = product.globProductId,
            sellerProductId = product.productId,
            itemName = product.itemName,
            sellerName = product.userName,
            itemCategory = product.productCategory,
            sellerPhoneNumber = product.sellerPhoneNumber,
            orderedQuantity = quantity,
            inHand = product.inHand,
            quantityCategory = product.quantityCategory,
            productImage = product.productImages,
            price = product.actualPrice
        )

        var itemExist = false
        if (!ORDERED_ITEMS.contains(item)) {

            ORDERED_ITEMS.forEachIndexed { index, productModel ->
                if (productModel.globProductId == item.globProductId) {
                    ORDERED_ITEMS[index] = item
                    itemExist = true
                }
            }

            if (!itemExist) {
                ORDERED_ITEMS.add(item)
            }

        }
    }

    DividerHorizontal()


}

