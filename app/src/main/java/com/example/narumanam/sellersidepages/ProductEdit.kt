package com.example.narumanam.sellersidepages

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultDropDown
import com.example.narumanam.DefaultTextField
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.HeadingText
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.SplashActivity
import com.example.narumanam.SubTitleText
import com.example.narumanam.accounts.Address
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.AddressExist
import com.example.narumanam.sellersidepages.products.AddressId
import com.example.narumanam.sellersidepages.products.offerPriceCalc
import com.example.narumanam.sellersidepages.products.productObject
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlin.math.round

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductEdit(
    navController : NavHostController,
) {

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val focusManager = LocalFocusManager.current
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val database = FirebaseDatabase.getInstance().getReference("Sellers")
    val globalDatabase = FirebaseDatabase.getInstance().getReference("GlobalProducts")
    
    val  productEdit = SELECTED_PRODUCT
    val imageList = PRODUCT_IMAGES
    val priceRegex = "^[0-9]+\\.[0-9]+".toRegex()


    val state = rememberPagerState() {
        imageList.size
    }

    var showDialog by remember { mutableStateOf(false) }
    var processState by remember { mutableStateOf(false) }
    var initialProcessState by remember { mutableStateOf(true) }

    var descExpanded by remember { mutableStateOf(false) }

    var quantity by rememberSaveable { mutableStateOf(productEdit.quantity) }
    var quantityError by remember { mutableStateOf(false)}
    var quantityIsSelected by remember { mutableStateOf(false)}

    var quantityCategoryExpanded by remember { mutableStateOf(false)}
    var quantityCategoryFieldSize by remember { mutableStateOf(Size.Zero)}
    var quantityCategorySelected by rememberSaveable { mutableStateOf(productEdit.quantityCategory)}
    var quantityCategoryError by remember { mutableStateOf(false)}
    val quantityCategories = listOf("kgs","gms","pcs","ltr","ml")
    val quantityIcon = if (quantityCategoryExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    var actualPrice by rememberSaveable { mutableStateOf(productEdit.actualPrice) }
    var constPrice by rememberSaveable { mutableStateOf(productEdit.actualPrice) }
    var priceError by remember { mutableStateOf(false)}
    var priceIsSelected by remember { mutableStateOf(false)}

    var inHand by rememberSaveable { mutableStateOf(productEdit.inHand) }
    var inHandError by remember { mutableStateOf(false)}

    var offer by rememberSaveable { mutableStateOf(productEdit.offer) }
    var offerError by remember { mutableStateOf(false)}

    var sellerAddress = Address()

    database
        .child(phoneNumber?:"")
        .child("SelectedAddress")
        .get()
        .addOnSuccessListener {
            initialProcessState= false
            if (it.exists()) {
                AddressId = it.getValue(String::class.java)!!
                database
                    .child(phoneNumber?:"")
                    .child("MyAddress")
                    .child(AddressId)
                    .get()
                    .addOnSuccessListener {snapshot->
                        sellerAddress = snapshot.getValue(Address::class.java)?: Address()
                    }
            }  else {
                AddressExist = false
                navController.navigate(Screens.AddressScreen.route+ "/${"PRODUCTEDIT"}")
            }
        }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ){


        item {

            if(processState || initialProcessState){
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
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {

                SubTitleText(
                    modifier = Modifier,
                    text = productEdit.itemName,
                    fontSize = 20.sp
                )
                Text(
                    text = "ID: "+productEdit.productId,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7C7C7C)
                    )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
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
                            imageVector = if(descExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "arrow"
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(200)),
                    text = productEdit.description,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFF7C7C7C),
                    maxLines = if (descExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )
                DividerHorizontal()
                
                HeadingText(text = "Quantity:")

                Row(
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
                            .height(50.dp)
                            .width(60.dp),
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
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                quantityError = TextUtils.isEmpty(quantity)
                                if (!quantityError) {
                                    focusManager.moveFocus(FocusDirection.Right)
                                }
                            }
                        )

                    )
                    IconButton(onClick = {
                        if (isConnection) {
                            quantityIsSelected = false
                        }
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


                    Spacer(modifier = Modifier.width(70.dp))


                    DefaultDropDown(
                        modifier = Modifier
                            .height(50.dp)
                            .width(110.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                quantityCategoryFieldSize =
                                    layoutCoordinates.size.toSize()
                            },
                        dropDownModifier = Modifier
                            .width(with(LocalDensity.current) { quantityCategoryFieldSize.width.toDp() }),
                        selectedText = quantityCategorySelected,
                        onValueChange = {
                            quantityCategoryError = false
                            quantityCategorySelected = it
                        },
                        expanded = quantityCategoryExpanded,
                        trailingIcon = {
                            Icon(
                                imageVector = quantityIcon,
                                contentDescription = "DropdownIcon",
                                modifier = Modifier
                                    .clickable {
                                        quantityCategoryExpanded =
                                            !quantityCategoryExpanded
                                    },
                                tint = Color(0xFF7C7C7C)
                            )
                        },
                        textError = quantityCategoryError,
                        onDismiss = { quantityCategoryExpanded = false },
                        dropDownItems = quantityCategories,
                        dropDownOnclick = {
                            quantityCategoryError = false
                            quantityCategorySelected = it
                            quantityCategoryExpanded = false
                            focusManager.moveFocus(FocusDirection.Down)
                        }

                    )


                }

                DividerHorizontal()

                HeadingText(text = "Price:")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    IconButton(onClick = {
                        priceIsSelected = true
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.subtract_icon),
                            contentDescription = "lessIcon",
                            tint = if (!priceIsSelected) Color(0XFFB3B3B3) else Green01,
                            modifier = Modifier.clickable {
                                var lessPrice = actualPrice.toDoubleOrNull()

                                if (lessPrice != null) {
                                    lessPrice -= 1
                                    lessPrice = round(lessPrice * 100) / 100
                                    actualPrice =
                                        if (lessPrice > 0) lessPrice.toString() else "0.0"
                                    constPrice = actualPrice

                                    if (offer.isNotEmpty()) {
                                        offerError = Integer.parseInt(offer) > 100
                                        actualPrice = offerPriceCalc(
                                            offer,
                                            offerError,
                                            constPrice
                                        )
                                    } else {
                                        actualPrice = constPrice
                                    }

                                } else {
                                    actualPrice = "0.0"
                                }
                            }
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .height(50.dp)
                            .width(100.dp),
                        value = actualPrice,
                        onValueChange = { it ->

                            actualPrice = it.filter { it == '.' || it.isDigit() }
                            constPrice = it.filter { it == '.' || it.isDigit() }
                            priceError =
                                !priceRegex.matches(actualPrice) && !priceRegex.matches(
                                    constPrice
                                )

                            if (offer.isNotEmpty() && !priceError) {
                                offerError = Integer.parseInt(offer) > 100
                                actualPrice =
                                    offerPriceCalc(offer, offerError, constPrice)
                            } else {
                                actualPrice = constPrice
                            }
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
                            textAlign = TextAlign.Start
                        ),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.CurrencyRupee,
                                contentDescription = "Currency Icon"
                            )
                        },
                        isError = priceError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                quantityError = TextUtils.isEmpty(quantity)
                                quantityCategoryError = TextUtils.isEmpty(quantityCategorySelected)
                                priceError = TextUtils.isEmpty(actualPrice)
                                if (
                                    !quantityError &&
                                    !quantityCategoryError &&
                                    !priceError
                                ) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            }
                        )
                    )
                    IconButton(onClick = {
                        priceIsSelected = false
                    }) {

                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "addIcon",
                            tint = if (priceIsSelected) Color(0XFFB3B3B3) else Green01,
                            modifier = Modifier.clickable {
                                var addPrice = actualPrice.toDoubleOrNull()

                                if (addPrice != null) {
                                    addPrice += 1
                                    addPrice = round(addPrice * 100) / 100
                                    actualPrice = addPrice.toString()
                                    constPrice = actualPrice

                                    if (offer.isNotEmpty()) {
                                        offerError = Integer.parseInt(offer) > 100
                                        actualPrice = offerPriceCalc(
                                            offer,
                                            offerError,
                                            constPrice
                                        )
                                    } else {
                                        actualPrice = constPrice
                                    }

                                } else {
                                    actualPrice = "0.0"
                                }
                            }
                        )

                    }
                }

                DividerHorizontal()

                HeadingText(text = "Stock In Hand:")
                DefaultTextField(
                    text = inHand,
                    onValueChange = { it ->
                        inHandError = false
                        inHand = it.filter { it.isDigit() }

                    },
                    textError = inHandError,
                    placeholderText = "eg. 20 kgs",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            quantityError = TextUtils.isEmpty(quantity)
                            quantityCategoryError = TextUtils.isEmpty(quantityCategorySelected)
                            priceError = TextUtils.isEmpty(actualPrice)
                            inHandError = TextUtils.isEmpty(inHand)
                            if (
                                !quantityError &&
                                !quantityCategoryError &&
                                !priceError &&
                                !inHandError
                            ) {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                    )

                )

                HeadingText(text = "Offer:")
                DefaultTextField(
                    text = offer,
                    onValueChange = { it ->
                        offerError = false
                        offer = it.filter { it.isDigit() }
                        if (offer.isNotEmpty()) {
                            offerError = Integer.parseInt(offer) > 100
                            actualPrice = offerPriceCalc(offer, offerError, constPrice)
                        } else {
                            actualPrice = constPrice
                        }

                    },
                    textError = offerError,
                    placeholderText = "eg. 20 %",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                            quantityError = TextUtils.isEmpty(quantity)
                            quantityCategoryError = TextUtils.isEmpty(quantityCategorySelected)
                            priceError = TextUtils.isEmpty(actualPrice)
                            inHandError = TextUtils.isEmpty(inHand)
                            offerError = TextUtils.isEmpty(offer)

                            if (
                                !quantityError &&
                                !quantityCategoryError &&
                                !priceError &&
                                !inHandError &&
                                !offerError
                            ) {
                                focusManager.clearFocus()
                            }
                        }
                    )

                )

                Spacer(modifier = Modifier.height(30.dp))
                    
                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        onclick = {

                            if (isConnection) {
                                processState = true
                                quantityError = TextUtils.isEmpty(quantity)
                                quantityCategoryError = TextUtils.isEmpty(quantityCategorySelected)
                                priceError = TextUtils.isEmpty(actualPrice)
                                inHandError = TextUtils.isEmpty(inHand)
                                offerError = TextUtils.isEmpty(offer)
                                if (
                                    !quantityError &&
                                    !quantityCategoryError &&
                                    !priceError &&
                                    !inHandError &&
                                    !offerError
                                ) {

                                    inHand += quantityCategorySelected

                                    val product = productObject(
                                        globProductId = productEdit.globProductId,
                                        phoneNumber = phoneNumber ?: "",
                                        productId = productEdit.productId,
                                        productCategory = productEdit.productCategory,
                                        itemName = productEdit.itemName,
                                        brandName = productEdit.brandName,
                                        quantity = quantity,
                                        quantityCategory = productEdit.quantityCategory,
                                        actualPrice = actualPrice,
                                        inHand = inHand,
                                        offer = offer,
                                        imageUrl = productEdit.productImages,
                                        description = productEdit.description,
                                        addressDetail = sellerAddress
                                    )


                                    globalDatabase
                                        .child(productEdit.globProductId)
                                        .setValue(product[0])
                                        .addOnCompleteListener { globTask ->

                                            if (globTask.isSuccessful) {
                                                database
                                                    .child(phoneNumber ?: "")
                                                    .child("MyInventory")
                                                    .child(productEdit.productId)
                                                    .setValue(product[1])
                                                    .addOnCompleteListener { localTask ->
                                                        processState = false
                                                        if (localTask.isSuccessful) {
                                                            Toast.makeText(
                                                                context,
                                                                "Changes successfully Modified",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                                .show()


                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Product failed to Modifiy",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                                .show()

                                                        }

                                                    }
                                            } else {
                                                processState = false
                                                Toast.makeText(
                                                    context,
                                                    "Product failed to upload",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }

                                } else {
                                    processState = false
                                }
                            }

                                  },
                        text = "Submit",
                        color = ButtonDefaults.buttonColors(Green01)
                    )

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        onclick = {

                            if (isConnection) {
                                showDialog = true
                            }
                        },
                        text = "Delete",
                        color = ButtonDefaults.buttonColors(Red01)
                    )

            }

            if (showDialog){
                AlertDialog(
                    onDismissRequest = {  },
                    icon = {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
                    },
                    title = {
                        Text(
                            text = "Are you sure want to delete?",
                            fontSize = 13.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    text = {
                        Text(
                            text = "Product will be removed permanently ",
                            fontSize = 13.sp,
                            fontFamily = Poppins,
                            textAlign = TextAlign.Justify,
                        )

                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog = false
                                processState = true

                                if (isConnection) {
                                    var count = 0

                                    for (url in imageList) {

                                        count += 1
                                        val image = url.filterNot { it == '[' || it == ']' }

                                        val globImageDatabase =
                                            Firebase.storage.getReferenceFromUrl(image.trim())
                                        globImageDatabase.delete().addOnSuccessListener {

                                            if (count == imageList.size) {

                                                globalDatabase
                                                    .child(productEdit.globProductId)
                                                    .removeValue()
                                                    .addOnSuccessListener {

                                                        database
                                                            .child(phoneNumber ?: "")
                                                            .child("MyInventory")
                                                            .child(productEdit.productId)
                                                            .removeValue()
                                                            .addOnCompleteListener { task ->

                                                                if (task.isSuccessful && processState) {
                                                                    processState = false
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Successfully product removed",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    navController.popBackStack()
                                                                } else if (processState) {
                                                                    processState = false
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Failed to  remove a product",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                            }
                                                    }
                                                    .addOnFailureListener {
                                                        processState = false
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to  remove a product",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        }


                                    }

                                }
                            }
                        )
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
                            showDialog = false
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
        }
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

