package com.example.narumanam.buyersidepages.productorderingprocess

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultSearchBar
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.GlobProduct
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

var PRODUCTDATA : GlobProduct = GlobProduct()
var BUYERPRODUCTIMAGES : List<String> = emptyList()

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProductDisplayScreen(
    navController: NavHostController,
    title : String
) {
    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val allProducts =  remember { mutableStateListOf<GlobProduct>() }
    var processState by remember { mutableStateOf(true) }
    val searchHistory = remember { mutableStateListOf("Apple", "Honey", "Onion") }
    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    val scope  = rememberCoroutineScope()
    val snackBarHostState  =   remember{SnackbarHostState()}

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var sliderPosition by remember { mutableStateOf(0f..10000f) }

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")

    val globalProductDatabase = FirebaseDatabase.getInstance().getReference("GlobalProducts")

    globalProductDatabase.addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProducts.clear()
                snapshot.children.forEach { item->

                    item.getValue(GlobProduct::class.java)?.let {
                        if (title == it.productCategory) {
                            allProducts.add(it)
                        }

                    }
                }
                processState = false

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error", Toast.LENGTH_SHORT).show()
            }

        }
    )

    Scaffold(
        snackbarHost = {
                       SnackbarHost(hostState = snackBarHostState)
        },

        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                navController = navController,
                title = title
            )
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                ) {

                    DividerHorizontal()

                    if (processState) {
                        ProgressIndicator()
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = if (!isActive) {
                                    12.dp
                                } else 0.dp, top = 8.dp, end = if (!isActive) {
                                    12.dp
                                } else 0.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .weight(1f),
                            query = query,
                            onQueryChange = {
                                query = it
                            },
                            onSearch = {
                                isActive = false
                                searchHistory.add(query)
                            },
                            active = isActive,
                            onActiveChange = {
                                isActive = it
                            },
                            placeholder = {
                                Text(
                                    text = "Search Product", fontFamily = Poppins,
                                    fontSize = 12.sp, color = Color(0xFF7C7C7C),
                                    textAlign = TextAlign.Justify
                                )
                            },
                            colors = SearchBarDefaults.colors(
                                containerColor = Color(0xFFF2F3F2),
                            ),
                            shape = RoundedCornerShape(15.dp),
                        ) {

                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Color.White),
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),

                                ) {

                                val searchProducts = allProducts.filter { it.itemName.lowercase()
                                    .contains(Regex(query.lowercase())) || query == "" }

                                items(count = searchProducts.size) {

                                        ProductItems(
                                            navController,
                                            product = searchProducts[it],
                                            phoneNumber = phoneNumber ?: "",
                                            snackBarHostState = snackBarHostState,
                                            scope = scope
                                        )

                                }

                            }

                        }

                        if (!isActive) {
                            IconButton(
                                modifier = Modifier
                                    .size(40.dp),
                                onClick = {
                                    showBottomSheet = true
                                }) {

                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.filter_icon),
                                    contentDescription = "filter Icon"
                                )
                            }
                        }
                    }

                    if (showBottomSheet) {

                        ModalBottomSheet(
                            modifier = Modifier,
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState,
                            containerColor = Color(0xFFF2F3F2)
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .background(color = Color(0xFFF2F3F2)),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {

                                Text(
                                    text = "Price",
                                    fontSize = 16.sp,
                                    fontFamily = Poppins,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Column {
                                    RangeSlider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(15.dp),
                                        value = sliderPosition,
                                        steps = 5,
                                        onValueChange = { value ->
                                            sliderPosition = value
                                        },
                                        valueRange = 0f..10000f,
                                        colors = SliderDefaults.colors(
                                            activeTrackColor = Green01,
                                            inactiveTrackColor = Color(0xFFB3B3B3),
                                            thumbColor = Green01,
                                            activeTickColor = Green01,
                                            inactiveTickColor = Green01
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = "0",
                                            fontSize = 14.sp,
                                            fontFamily = Poppins,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "10000",
                                            fontSize = 14.sp,
                                            fontFamily = Poppins,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Black
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                DefaultButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    onclick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                    },
                                    text = "Apply Filters"
                                )
                            }

                        }

                    }
                    ShowProducts(
                        productList = allProducts,
                        navController = navController,
                        snackBarHostState = snackBarHostState,
                        scope = scope,
                        phoneNumber = phoneNumber ?: "",
                        priceRange = sliderPosition
                    )
                }





            }
        }
    )
}



@Composable
private fun ShowProducts(
    productList : List<GlobProduct>,
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    phoneNumber: String,
    priceRange : ClosedFloatingPointRange<Float>
) {


    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ){

        items(count = productList.size) {

            if (productList[it].actualPrice.toFloat() in priceRange) {
                ProductItems(
                    navController,
                    product = productList[it],
                    phoneNumber = phoneNumber,
                    snackBarHostState = snackBarHostState,
                    scope = scope
                )
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavHostController,
    title: String
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
        ),
        title = {
            Text(
                text = title,
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
private fun ProductItems(
    navController: NavHostController,
    product : GlobProduct,
    phoneNumber : String,
    snackBarHostState : SnackbarHostState,
    scope: CoroutineScope
) {

    val myCartDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Buyers")

    val isConnection = networkConnection(context = LocalContext.current)

    val imageList = product.productImages.split(",")
    val image = imageList[0].replace("[", "")

    val inHandQty = product.inHand.filter { it.isDigit() }


    Box(

        modifier = Modifier
            .width(175.dp)
            .height(210.dp)
            .border(width = 1.dp, color = Color(0xFFE2E2E2), shape = RoundedCornerShape(18.dp))
            .padding(10.dp)
            .clickable {
                if (isConnection) {
                    PRODUCTDATA = product
                    BUYERPRODUCTIMAGES = imageList
                    navController.navigate(BuyerScreens.ProductInDetailScreen.route)
                }
            },
        contentAlignment = Alignment.Center
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = "Category Image",
                modifier = Modifier
                    .size(95.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.CenterStart),
                text = product.itemName,
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.CenterStart),
                text = "Price ${product.quantity}${product.quantityCategory}",
                fontFamily = Poppins,
                fontSize = 10.sp,
                color = Color(0xFF7C7C7C)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
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

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Green01, shape = RoundedCornerShape(14.dp))
                        .clickable {
                            if (isConnection) {
                                myCartDatabase
                                    .child(phoneNumber)
                                    .child("MyCart")
                                    .child(product.globProductId)
                                    .child("globalProductId")
                                    .setValue(product.globProductId)
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
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }

            }
        }
        if (inHandQty.toInt() <= 0) {

            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.White)
                    .align(Alignment.Center),
                fontFamily = Poppins,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                text = "Out of Stock",
                color = Red01
            )

        }
    }
}

