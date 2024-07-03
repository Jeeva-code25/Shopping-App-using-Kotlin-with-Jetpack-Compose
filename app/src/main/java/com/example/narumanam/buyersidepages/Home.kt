package com.example.narumanam.buyersidepages

import android.content.Context
import android.provider.CalendarContract.Colors
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultSearchBar
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.SubTitleText
import com.example.narumanam.buyersidepages.productorderingprocess.BUYERPRODUCTIMAGES
import com.example.narumanam.buyersidepages.productorderingprocess.PRODUCTDATA
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.ItemTemplate
import com.example.narumanam.sellersidepages.products.GlobProduct
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavHostController){

    val context = LocalContext.current
    val allProducts =  remember { mutableStateListOf<GlobProduct>() }

    var processState by remember { mutableStateOf(true) }
    val searchHistory = remember { mutableStateListOf("Apple", "Honey", "Onion") }
    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    val scope  = rememberCoroutineScope()
    val snackBarHostState  =   remember{SnackbarHostState()}

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")


    val globalProductDatabase = FirebaseDatabase
        .getInstance()
        .getReference("GlobalProducts")

    globalProductDatabase.addListenerForSingleValueEvent(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProducts.clear()
                snapshot.children.forEach { item->

                    item.getValue(GlobProduct::class.java)?.let {

                            allProducts.add(it)

                    }
                }
                processState = false

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error", Toast.LENGTH_SHORT).show()
            }

        }
    )

    val categoryImages : List<Painter> = listOf(
        painterResource(id = R.drawable.vegetables),
        painterResource(id = R.drawable.honey_oil),
        painterResource(id = R.drawable.spices),
        painterResource(id = R.drawable.fruits),
        painterResource(id = R.drawable.nuts),
        painterResource(id = R.drawable.crops),
        painterResource(id = R.drawable.ready_to_cook),
        painterResource(id = R.drawable.non_timber_forest),
        painterResource(id = R.drawable.herbs),
        painterResource(id = R.drawable.chocolates),
        painterResource(id = R.drawable.soaps),
        painterResource(id = R.drawable.others),
    )
    val categoryColors : List<Color> = listOf(
        Color(0xFF53B175),
        Color(0xFFF8A44C),
        Color(0xFFE32227),
        Color(0xFFD3B0E0),
        Color(0xFF836AF6),
        Color(0xFFFDE598),
        Color(0xFFF7A593),
        Color(0xFFD73B77),
        Color(0xFFB7DFF5),
        Color(0xFF7B3F00),
        Color(0xFF800000),
        Color(0xFF00452A)
    )
    val categories : List<String> = listOf(
        "Vegetables",
        "Honey & Oil",
        "Spices",
        "Fruits",
        "Nuts",
        "Crops & Millets",
        "Ready to cook",
        "Non Timber Forest Product",
        "Herbs & Medical Plants",
        "Chocolates",
        "Soaps",
        "Others"
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),

        ) {

        if (processState){
            ProgressIndicator()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(end=10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(25.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.app_logo_color),
                tint = Color.Unspecified,
                contentDescription = "app_logo"
            )
        }


            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 12.dp, end = 12.dp),
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

           LazyVerticalGrid(
               modifier = Modifier
                   .fillMaxSize(),
               columns = GridCells.Fixed(2),
               contentPadding = PaddingValues(12.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp),
           ){

               items(count = 12){

                   CategoryItems(
                       navController= navController,
                       categoryColor =  categoryColors[it],
                       image = categoryImages[it],
                       text = categories[it]
                   )

               }


           }



    }
}

@Composable
fun CategoryItems(
    navController: NavHostController,
    categoryColor : Color,
    image : Painter,
    text : String
) {

    val isConnection = networkConnection(context = LocalContext.current)

    Box(

        modifier = Modifier
            .width(175.dp)
            .height(190.dp)
            .background(
                color = categoryColor.copy(alpha = 0.6f),
                shape = RoundedCornerShape(18.dp)
            )
            .border(width = 1.dp, color = categoryColor, shape = RoundedCornerShape(18.dp))
            .padding(10.dp)
            .clickable {
                if (isConnection) {
                    navController.navigate(BuyerScreens.ProductDisplayScreen.route + "/${text}")
                }
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = image,
                contentDescription = "Category Image",
                modifier = Modifier
                    .size(95.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center),
                    text = text,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ProductItems(
    navController: NavHostController,
    product : GlobProduct,
    phoneNumber : String,
    snackBarHostState : SnackbarHostState,
    scope: CoroutineScope
) {

    val isConnection = networkConnection(context = LocalContext.current)

    val myCartDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Buyers")


    val imageList = product.productImages.split(",")
    val image = imageList[0].replace("[","")

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
                ){
                    Icon(
                        modifier = Modifier
                            .size(15.dp),
                        imageVector = Icons.Outlined.CurrencyRupee,
                        contentDescription = "CurrencyRupee",
                        tint =  Color(0xFF344356)
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
                ){
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
    }
}