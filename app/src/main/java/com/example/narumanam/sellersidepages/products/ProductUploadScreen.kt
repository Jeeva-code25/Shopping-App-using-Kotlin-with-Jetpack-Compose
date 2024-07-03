package com.example.narumanam.sellersidepages.products

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultDropDown
import com.example.narumanam.DefaultNextButton
import com.example.narumanam.DefaultTextField
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlin.math.round

var AddressExist = true
var AddressId = ""

@Composable
fun ProductUploadScreen(
    navController : NavHostController
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val database = FirebaseDatabase.getInstance().getReference("Sellers")

    var processState by remember {
        mutableStateOf(false)
    }
    var initialProcessState by remember {
        mutableStateOf(true)
    }


    val isConnection = networkConnection(context = context)


    val priceRegex = "^[0-9]+\\.[0-9]+".toRegex()
    var productId by rememberSaveable { mutableStateOf("") }
    var productIdError by remember { mutableStateOf(false)}





    if (productId.isNotEmpty()) {
        database.child(phoneNumber ?: "").child("MyInventory").child(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productIdError = snapshot.exists()
                    processState = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed " + error.message, Toast.LENGTH_SHORT).show()
                    processState = false
                }
            })

    } else {
        processState = false
    }


    var categoryExpanded by remember { mutableStateOf(false)}
    var categorySelected by rememberSaveable { mutableStateOf("")}
    var categoryError by remember { mutableStateOf(false)}
    var categoryFieldSize by remember{mutableStateOf(Size.Zero)}
    val categoryIcon = if (categoryExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val productCategories = listOf(
        "Vegetables","Fruits","Honey & Oil",
        "Spices","Nuts","Crops & Millets",
        "Ready to cook","Non Timber Forest Product",
        "Herbs & Medical Plants","Chocolates",
        "Soaps","Others")

    var itemName by rememberSaveable { mutableStateOf("") }
    var itemError by remember { mutableStateOf(false)}

    var brandName by rememberSaveable { mutableStateOf("") }
    var brandError by remember { mutableStateOf(false)}

    var quantity by rememberSaveable { mutableStateOf("0") }
    var quantityError by remember { mutableStateOf(false)}
    var quantityIsSelected by remember { mutableStateOf(false)}

    var quantityCategoryExpanded by remember { mutableStateOf(false)}
    var quantityCategoryFieldSize by remember { mutableStateOf(Size.Zero)}
    var quantityCategorySelected by rememberSaveable { mutableStateOf("")}
    var quantityCategoryError by remember { mutableStateOf(false)}
    val quantityCategories = listOf("kgs","gms","pcs","ltr","ml")
    val quantityIcon = if (quantityCategoryExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    var actualPrice by rememberSaveable { mutableStateOf("0.0") }
    var constPrice by rememberSaveable { mutableStateOf("0.0") }
    var priceError by remember { mutableStateOf(false)}
    var priceIsSelected by remember { mutableStateOf(false)}

    var inHand by rememberSaveable { mutableStateOf("") }
    var inHandError by remember { mutableStateOf(false)}

    var offer by rememberSaveable { mutableStateOf("0") }
    var offerError by remember { mutableStateOf(false)}

    database
        .child(phoneNumber?:"")
        .child("SelectedAddress")
        .get()
        .addOnSuccessListener {
            initialProcessState= false
            if (it.exists()) {
                AddressId = it.getValue(String::class.java)!!
            }  else {
                AddressExist = false
                navController.navigate(Screens.AddressScreen.route+ "/${"PRODUCTUPLOAD"}")
            }
        }

        Column(modifier =
            Modifier
            .fillMaxSize()
            .background(color = Color.White)
            ) {

                if (processState||initialProcessState) {
                    ProgressIndicator()
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        DefaultBackArrow(
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            onclick = {
                                navController.popBackStack()
                            }
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "Product Upload",
                            style = mobileTitle
                        )

                    }


                }

                LazyColumn(
                    modifier= Modifier
                        .fillMaxWidth()

                ){

                    item {

                        Column (
                            modifier = Modifier
                                .padding(12.dp)
                        ){

                            Text(
                                text = "Product Id:",
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            DefaultTextField(
                                text = productId,
                                onValueChange = {
                                    productIdError = false
                                    if (it.isDigitsOnly() && it.length <= 10) {
                                        productId = it
                                    }
                                    processState = true
                                },
                                textError = productIdError,
                                placeholderText = "eg. 01",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (!TextUtils.isEmpty(productId)) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        } else {
                                            productIdError = true
                                        }
                                    }
                                ),
                                trailingIcon = {

                                    if (productIdError) {
                                        Icon(
                                            imageVector = Icons.Outlined.ErrorOutline,
                                            contentDescription = "error",
                                            tint = Color.Red
                                        )
                                    } else if (productId.isNotEmpty()) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = "check",
                                            tint = Green01
                                        )
                                    }
                                }


                            )
                            if (productIdError) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "already exists",
                                    fontFamily = Poppins,
                                    color = Color.Red
                                )
                            }


                            Text(
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                text = "Product Category:",
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            DefaultDropDown(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .onGloballyPositioned { layoutCoordinates ->
                                        categoryFieldSize = layoutCoordinates.size.toSize()
                                    },
                                selectedText = categorySelected,
                                onValueChange = {
                                    categorySelected = it
                                },
                                placeholderText = "Select Category",
                                expanded = categoryExpanded,
                                trailingIcon = {
                                    Icon(
                                        imageVector = categoryIcon,
                                        contentDescription = "DropdownIcon",
                                        modifier = Modifier
                                            .clickable {
                                                categoryExpanded = !categoryExpanded
                                            },
                                        tint = Color(0xFF7C7C7C)
                                    )
                                },
                                textError = categoryError,
                                onDismiss = {
                                    categoryExpanded = false
                                },
                                dropDownItems = productCategories,
                                dropDownModifier = Modifier
                                    .width(with(LocalDensity.current) { categoryFieldSize.width.toDp() }),
                                dropDownOnclick = {
                                    categoryError = false
                                    categorySelected = it
                                    categoryExpanded = false
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            )


                            Text(
                                text = "Item Name:",
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            DefaultTextField(
                                text = itemName,
                                onValueChange = {
                                    itemError = false
                                    if (it.length <= 20) {
                                        itemName = it
                                    }
                                },
                                textError = itemError,
                                placeholderText = "eg. apple",
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    imeAction = ImeAction.Next,
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (
                                            !TextUtils.isEmpty(productId) &&
                                            !TextUtils.isEmpty(categorySelected) &&
                                            !TextUtils.isEmpty(itemName)
                                        ) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        } else {
                                            productIdError = TextUtils.isEmpty(productId)
                                            categoryError = TextUtils.isEmpty(categorySelected)
                                            itemError = TextUtils.isEmpty(itemName)
                                        }
                                    }
                                )

                            )

                            Text(
                                text = "Brand Name:",
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            DefaultTextField(
                                text = brandName,
                                onValueChange = {
                                    brandError = false
                                    if (it.length <= 20) {
                                        brandName = it
                                    }
                                },
                                textError = brandError,
                                placeholderText = "eg. super apple company",
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (
                                            !TextUtils.isEmpty(productId) &&
                                            !TextUtils.isEmpty(categorySelected) &&
                                            !TextUtils.isEmpty(itemName) &&
                                            !TextUtils.isEmpty(brandName)
                                        ) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        } else {
                                            productIdError = TextUtils.isEmpty(productId)
                                            categoryError = TextUtils.isEmpty(categorySelected)
                                            itemError = TextUtils.isEmpty(itemName)
                                            brandError = TextUtils.isEmpty(brandName)
                                        }
                                    }
                                )

                            )

                            Text(
                                text = "Quantity:",
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Row(
                                modifier = Modifier.padding(top = 15.dp),
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
                                            if (
                                                !TextUtils.isEmpty(productId) &&
                                                !TextUtils.isEmpty(categorySelected) &&
                                                !TextUtils.isEmpty(itemName) &&
                                                !TextUtils.isEmpty(brandName) &&
                                                !TextUtils.isEmpty(quantity)
                                            ) {
                                                focusManager.moveFocus(FocusDirection.Right)
                                            } else {
                                                productIdError = TextUtils.isEmpty(productId)
                                                categoryError = TextUtils.isEmpty(categorySelected)
                                                itemError = TextUtils.isEmpty(itemName)
                                                brandError = TextUtils.isEmpty(brandName)
                                                quantityError = TextUtils.isEmpty(quantity)
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
                            Text(
                                text = "Actual Price:",
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 15.dp)
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
                                            if (
                                                !TextUtils.isEmpty(productId) &&
                                                !TextUtils.isEmpty(categorySelected) &&
                                                !TextUtils.isEmpty(itemName) &&
                                                !TextUtils.isEmpty(brandName) &&
                                                !TextUtils.isEmpty(quantity) &&
                                                !TextUtils.isEmpty(quantityCategorySelected) &&
                                                !TextUtils.isEmpty(actualPrice)
                                            ) {
                                                focusManager.moveFocus(FocusDirection.Down)
                                            } else {
                                                productIdError = TextUtils.isEmpty(productId)
                                                categoryError = TextUtils.isEmpty(categorySelected)
                                                itemError = TextUtils.isEmpty(itemName)
                                                brandError = TextUtils.isEmpty(brandName)
                                                quantityError = TextUtils.isEmpty(quantity)
                                                quantityCategoryError =
                                                    TextUtils.isEmpty(quantityCategorySelected)
                                                priceError = TextUtils.isEmpty(actualPrice)
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

                            Text(
                                text = "In Hand:",
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            DefaultTextField(
                                text = inHand,
                                onValueChange = { it ->
                                    inHandError = false
                                    inHand = it.filter { it.isLetterOrDigit() }

                                },
                                textError = inHandError,
                                placeholderText = "eg. 20 kgs",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (
                                            !TextUtils.isEmpty(productId) &&
                                            !TextUtils.isEmpty(categorySelected) &&
                                            !TextUtils.isEmpty(itemName) &&
                                            !TextUtils.isEmpty(brandName) &&
                                            !TextUtils.isEmpty(quantity) &&
                                            !TextUtils.isEmpty(quantityCategorySelected) &&
                                            !TextUtils.isEmpty(actualPrice) &&
                                            !TextUtils.isEmpty(inHand)
                                        ) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        } else {
                                            productIdError = TextUtils.isEmpty(productId)
                                            categoryError = TextUtils.isEmpty(categorySelected)
                                            itemError = TextUtils.isEmpty(itemName)
                                            brandError = TextUtils.isEmpty(brandName)
                                            quantityError = TextUtils.isEmpty(quantity)
                                            quantityCategoryError =
                                                TextUtils.isEmpty(quantityCategorySelected)
                                            priceError = TextUtils.isEmpty(actualPrice)
                                            inHandError = TextUtils.isEmpty(inHand)
                                        }
                                    }
                                )

                            )

                            Text(
                                text = "Offer:",
                                modifier = Modifier
                                    .padding(top = 25.dp),
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

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
                                        productIdError = TextUtils.isEmpty(productId)
                                        categoryError  = TextUtils.isEmpty(categorySelected)
                                        itemError = TextUtils.isEmpty(itemName)
                                        brandError = TextUtils.isEmpty(brandName)
                                        quantityError = TextUtils.isEmpty(quantity)
                                        quantityCategoryError = TextUtils.isEmpty(quantityCategorySelected)
                                        priceError = TextUtils.isEmpty(actualPrice)
                                        inHandError = TextUtils.isEmpty(inHand)
                                        offerError = TextUtils.isEmpty(offer)

                                        if (!productIdError && !categoryError && !itemError && !brandError
                                                && !quantityError && !quantityCategoryError && !priceError
                                                && !inHandError && !offerError
                                            ) {

                                                focusManager.clearFocus()
                                        }

                                    }
                                )

                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                DefaultNextButton(
                                    modifier = Modifier
                                        .size(67.dp),
                                    onclick = {

                                        productIdError = TextUtils.isEmpty(productId)
                                        categoryError  = TextUtils.isEmpty(categorySelected)
                                        itemError = TextUtils.isEmpty(itemName)
                                        brandError = TextUtils.isEmpty(brandName)
                                        quantityError = TextUtils.isEmpty(quantity)
                                        quantityCategoryError = TextUtils.isEmpty(quantityCategorySelected)
                                        priceError = TextUtils.isEmpty(actualPrice)
                                        inHandError = TextUtils.isEmpty(inHand)
                                        offerError = TextUtils.isEmpty(offer)


                                        if (!productIdError && !categoryError && !itemError && !brandError
                                                && !quantityError && !quantityCategoryError && !priceError
                                                && !inHandError && !offerError) {

                                                itemName = itemName.trim()
                                                brandName = brandName.trim()

                                            if (isConnection) {
                                                navController.navigate(
                                                    SellerScreens.productScreen2.route + "/${productId}/${categorySelected}/${itemName}" +
                                                            "/${brandName}/${quantity}/${quantityCategorySelected}/${actualPrice}/${inHand}/${offer}"
                                                )
                                            }
                                        }

                                    }
                                )
                            }
                        }
                    }
                }


            }







}


fun offerPriceCalc(
    offer: String,
    offerError: Boolean,
    constPrice: String): String {

        if (!offerError){
            val offerPercentage = Integer.parseInt(offer)
            val originalPrice = constPrice.toDouble()
            val offerPrice = (originalPrice * offerPercentage / 100)
            return (originalPrice - offerPrice).toString()
        }
    return "0.0"
    }



