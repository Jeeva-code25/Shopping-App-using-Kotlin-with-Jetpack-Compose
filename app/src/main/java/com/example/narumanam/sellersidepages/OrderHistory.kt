package com.example.narumanam.sellersidepages

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultSearchBar
import com.example.narumanam.DefaultTextField
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.OrderManageProduct
import com.example.narumanam.ProfileData
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.IdClass
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.orderhistroy.BarGraph
import com.example.narumanam.sellersidepages.orderhistroy.BarType
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate

import java.util.Calendar
import java.util.Date
import java.util.Locale


var MyHistoryItem = OrderManageProduct()
var MyHistoryItemImage = ""
var MyHistoryItemCustName = ""

@SuppressLint("NewApi", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistory(
    navController : NavHostController
){
    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val formatter = SimpleDateFormat("dd-MM-yyyy")

    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var totalEarnings by remember { mutableDoubleStateOf(0.0) }
    var maxPrice by remember { mutableFloatStateOf(0f) }

    val month1 = remember { mutableFloatStateOf(0f) }
    val month2 = remember { mutableFloatStateOf(0f) }
    val month3 = remember { mutableFloatStateOf(0f) }
    val dataList =  mutableListOf(0,0,0)
    val floatValue = mutableListOf<Float>()
    val datesList = mutableListOf(getMonth(month = -2), getMonth(month = -1), getMonth(month = 0))

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")

    val myOrderHistory =  remember { mutableStateListOf<OrderManageProduct>() }
    var processState by remember { mutableStateOf(true) }

    val scope  = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var sliderPosition by remember { mutableStateOf(0f..maxPrice) }

    var isVegetables by remember { mutableStateOf(false) }
    var isFruits by remember { mutableStateOf(false) }
    var isSpices by remember { mutableStateOf(false) }
    var isMillets by remember { mutableStateOf(false) }
    var isHerbs by remember { mutableStateOf(false) }
    var isOils by remember { mutableStateOf(false) }
    var isNuts by remember { mutableStateOf(false) }
    var isNonTimberForest by remember { mutableStateOf(false) }

    var vegetables by remember { mutableStateOf("") }
    var fruits by remember { mutableStateOf("") }
    var spices by remember { mutableStateOf("") }
    var millets by remember { mutableStateOf("") }
    var herbs by remember { mutableStateOf("") }
    var oils by remember { mutableStateOf("") }
    var nuts by remember { mutableStateOf("") }
    var nonTimberForest by remember { mutableStateOf("") }

    val startCalendarState = rememberDatePickerState()
    startCalendarState.setSelection(dateStringToMilliseconds("14-03-2024"))
    var startCalendarDialog by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(if (startCalendarState.selectedDateMillis != null) getFormattedDate(startCalendarState.selectedDateMillis!!) else  "") }

    val endCalendarState = rememberDatePickerState()
    endCalendarState.setSelection(Date().time+86400000)
    var endCalendarDialog by remember { mutableStateOf(false) }
    var endDate by remember { mutableStateOf(if (endCalendarState.selectedDateMillis != null) getFormattedDate(endCalendarState.selectedDateMillis!!) else "") }


    val sellerDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Sellers")

    val productManageDatabase = FirebaseDatabase
        .getInstance()
        .getReference("ProductOrderManagement")


    sellerDatabase
        .child(phoneNumber ?: "")
        .child("OrderRequest")
        .addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                totalEarnings = 0.0
                myOrderHistory.clear()

                snapshot.children.forEach {item->

                    item.getValue(IdClass :: class.java)?.let { id->

                        productManageDatabase
                            .child(id.orderId)
                            .get()
                            .addOnSuccessListener {snap->
                                snap.getValue(OrderManageProduct::class.java)
                                    ?.let {

                                        if (!myOrderHistory.contains(it) && (it.orderStatus == "ORDER_SHIPPED" || it.orderProgressing == "NO") ){
                                            myOrderHistory.add(it)

                                            if (it.orderStatus == "ORDER_SHIPPED" && it.orderProgressing == "YES"){

                                                totalEarnings += it.price.toDouble() * it.orderedQuantity.toDouble()

                                                val dateStr = it.orderedDate.substring(startIndex = 3, endIndex = 5)
                                                val date = if(dateStr[0] == '0') dateStr[1].digitToInt() else dateStr.toInt()
                                                when(getMonthName(date).uppercase(Locale.getDefault())){
                                                     datesList[0] -> month1.floatValue += it.price.toFloat()
                                                     datesList[1] -> month2.floatValue += it.price.toFloat()
                                                     datesList[2] -> month3.floatValue += it.price.toFloat()
                                                }


                                            }

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
        topBar = {
            if (!isActive) {
                TopBar()
            }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp),
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
                                query = it
                            },
                            active = isActive,
                            onActiveChange = {
                                isActive = it
                            },
                            placeholder = {
                                Text(
                                    text = "Search a Customer", fontFamily = Poppins,
                                    fontSize = 12.sp, color = Color(0xFF7C7C7C),
                                    textAlign = TextAlign.Justify
                                )
                            },
                            colors = SearchBarDefaults.colors(
                                containerColor = Color(0xFFF2F3F2),
                            ),
                            shape = RoundedCornerShape(15.dp),
                        ) {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Color.White)
                                    .padding(top = 15.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                val searchProducts = myOrderHistory.filter {
                                    it.customerName.lowercase()
                                        .contains(Regex(query.lowercase())) || query == ""
                                }

                                items(items = searchProducts) { product ->

                                    HistoryItems(
                                        product = product,
                                        navController = navController,
                                    )

                                }

                            }
                        }

                        if (!isActive) {
                            IconButton(
                                modifier = Modifier
                                    .size(40.dp),
                                onClick = { if (isConnection){showBottomSheet = true} }) {

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
                                        valueRange = 0f..maxPrice,
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
                                            text = "0.0",
                                            fontSize = 14.sp,
                                            fontFamily = Poppins,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = maxPrice.toString(),
                                            fontSize = 14.sp,
                                            fontFamily = Poppins,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Text(
                                    text = "Date",
                                    fontSize = 16.sp,
                                    fontFamily = Poppins,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Text(
                                            text = "From",
                                            fontSize = 13.sp,
                                            fontFamily = Poppins,
                                            color = Color.Black
                                        )

                                        DefaultTextField(
                                            text = startDate,
                                            onValueChange = {

                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number,
                                                imeAction = ImeAction.Done
                                            ),
                                            colors = TextFieldDefaults.colors(
                                                unfocusedContainerColor = Color(0xFFF2F3F2),
                                                focusedContainerColor = Color(0xFFF2F3F2),
                                                unfocusedIndicatorColor = Color.Black,
                                                errorContainerColor = Color(0xFFF2F3F2),
                                                errorTextColor = Color.Red,
                                                errorIndicatorColor = Color.Red,
                                                focusedPlaceholderColor = Color(0xFF7C7C7C),
                                                unfocusedPlaceholderColor = Color(0xFF7C7C7C),
                                                errorPlaceholderColor = Color.Red,
                                                focusedIndicatorColor = Green01
                                            ),
                                            trailingIcon = {
                                                IconButton(
                                                    onClick = { startCalendarDialog = true }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.CalendarMonth,
                                                        contentDescription = "calendar",
                                                        tint = Color.Black
                                                    )
                                                }
                                            },
                                            readOnly = true

                                        )

                                        if (startCalendarDialog) {
                                            DatePickerDialog(
                                                onDismissRequest = { startCalendarDialog = false },
                                                confirmButton = {
                                                    TextButton(onClick = {
                                                        startDate =
                                                            if (startCalendarState.selectedDateMillis != null) getFormattedDate(
                                                                startCalendarState.selectedDateMillis!!
                                                            ) else ""
                                                        startCalendarDialog = false
                                                    }) {
                                                        Text(
                                                            text = "Ok",
                                                            fontFamily = Poppins,
                                                        )
                                                    }
                                                },
                                                dismissButton = {
                                                    TextButton(onClick = {

                                                        startCalendarDialog = false
                                                    }) {
                                                        Text(
                                                            text = "Cancel",
                                                            fontFamily = Poppins,
                                                        )
                                                    }
                                                }
                                            ) {
                                                DatePicker(
                                                    dateFormatter = DatePickerFormatter("yy-MM-dd"),
                                                    state = startCalendarState,
                                                )
                                            }
                                        }
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Text(
                                            text = "To",
                                            fontSize = 13.sp,
                                            fontFamily = Poppins,
                                            color = Color.Black
                                        )
                                        DefaultTextField(
                                            text = endDate,
                                            onValueChange = {

                                            },
                                            colors = TextFieldDefaults.colors(
                                                unfocusedContainerColor = Color(0xFFF2F3F2),
                                                focusedContainerColor = Color(0xFFF2F3F2),
                                                unfocusedIndicatorColor = Color.Black,
                                                errorContainerColor = Color(0xFFF2F3F2),
                                                errorTextColor = Color.Red,
                                                errorIndicatorColor = Color.Red,
                                                focusedPlaceholderColor = Color(0xFF7C7C7C),
                                                unfocusedPlaceholderColor = Color(0xFF7C7C7C),
                                                errorPlaceholderColor = Color.Red,
                                                focusedIndicatorColor = Green01
                                            ),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number,
                                                imeAction = ImeAction.Done
                                            ),
                                            trailingIcon = {
                                                IconButton(
                                                    onClick = { endCalendarDialog = true }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.CalendarMonth,
                                                        contentDescription = "calendar",
                                                        tint = Color.Black
                                                    )
                                                }
                                            },
                                            readOnly = true

                                        )

                                        if (endCalendarDialog) {
                                            DatePickerDialog(
                                                onDismissRequest = { endCalendarDialog = false },
                                                confirmButton = {
                                                    TextButton(onClick = {
                                                        endDate =
                                                            if (endCalendarState.selectedDateMillis != null) getFormattedDate(
                                                                endCalendarState.selectedDateMillis!!
                                                            ) else ""
                                                        endCalendarDialog = false
                                                    }) {
                                                        Text(
                                                            text = "Ok",
                                                            fontFamily = Poppins,
                                                        )
                                                    }
                                                },
                                                dismissButton = {
                                                    TextButton(onClick = {
                                                        endCalendarDialog = false
                                                    }) {
                                                        Text(
                                                            text = "Cancel",
                                                            fontFamily = Poppins,
                                                        )
                                                    }
                                                }
                                            ) {
                                                DatePicker(
                                                    dateFormatter = DatePickerFormatter("yy-MM-dd"),
                                                    state = endCalendarState,
                                                )
                                            }
                                        }
                                    }

                                }
                                Text(
                                    text = "Categories",
                                    fontSize = 16.sp,
                                    fontFamily = Poppins,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isVegetables,
                                                onCheckedChange = {
                                                    isVegetables = it
                                                    vegetables = if (isVegetables) "Vegetables" else ""
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Vegetables",
                                                fontFamily = Poppins
                                            )

                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isFruits,
                                                onCheckedChange = {
                                                    isFruits = it
                                                    fruits = if (isFruits) "Fruits" else ""
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Fruits",
                                                fontFamily = Poppins
                                            )

                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isSpices,
                                                onCheckedChange = {
                                                    isSpices = it
                                                    spices = if (isSpices) "Spices" else ""
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Spices",
                                                fontFamily = Poppins
                                            )

                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isMillets,
                                                onCheckedChange = {
                                                    isMillets = it
                                                    millets = if (isMillets) "Crops & Millets" else ""

                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Millets",
                                                fontFamily = Poppins
                                            )

                                        }

                                    }
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ){

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isHerbs,
                                                onCheckedChange = {
                                                    isHerbs = it
                                                    herbs = if (isHerbs) "Herbs & Medical Plants" else ""
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Herbs & Medical Plants",
                                                fontFamily = Poppins
                                            )

                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isOils,
                                                onCheckedChange = {
                                                    isOils = it
                                                    oils = if (isOils) "Honey & Oil" else ""

                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Oils & Honey",
                                                fontFamily = Poppins
                                            )

                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isNuts,
                                                onCheckedChange = {
                                                    isNuts = it
                                                    nuts = if (isNuts) "Nuts" else ""
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Nuts",
                                                fontFamily = Poppins
                                            )

                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            Checkbox(
                                                checked = isNonTimberForest,
                                                onCheckedChange = {
                                                    isNonTimberForest = it
                                                    nonTimberForest = if (isNonTimberForest) "Non Timber Forest Product" else ""
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Green01,
                                                    checkmarkColor = Color.White
                                                )
                                            )

                                            Text(
                                                text = "Non Timber Forest Products",
                                                fontFamily = Poppins
                                            )

                                        }

                                    }

                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                DefaultButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    onclick = {
                                        if (isConnection) {

                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }

                                        }
                                    },
                                    text = "Apply Filters"
                                )
                            }

                        }

                    }

                    if(processState){
                        ProgressIndicator()
                    }
                    if (myOrderHistory.size>0 && maxPrice == 0f) {

                        maxPrice = myOrderHistory.maxBy { it.price.toDouble() }.price.toFloat()
                        sliderPosition = 0f..maxPrice
                        dataList[0] = month1.floatValue.toInt()
                        dataList[1] = month2.floatValue.toInt()
                        dataList[2] = month3.floatValue.toInt()

                        dataList.forEachIndexed {index, value ->

                            floatValue.add(index = index, element = value.toFloat()/dataList.max().toFloat())

                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                    ) {


                        item {
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 12.dp,),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(top = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Total Earnings ",
                                        fontSize = 18.sp,
                                        fontFamily = Poppins,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black

                                    )

                                    Icon(
                                        modifier = Modifier
                                            .size(17.dp),
                                        imageVector = Icons.Outlined.CurrencyRupee, contentDescription = "")
                                    Text(
                                        text = totalEarnings.toString(),
                                        fontSize = 16.sp,
                                        fontFamily = Poppins,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black

                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 20.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ){


                                    if ((month1.floatValue+month2.floatValue+month3.floatValue).toInt() > 0) {
                                        BarGraph(
                                            graphBarData = floatValue,
                                            xAxisScaleData = datesList,
                                            barDataFunc = listOf(
                                                month1.floatValue.toInt(),
                                                month2.floatValue.toInt(),
                                                month3.floatValue.toInt()
                                            ),
                                            height = 200.dp,
                                            roundType = BarType.TOP_CURVED,
                                            barWidth = 30.dp,
                                            barColor = Green01,
                                            barArrangement = Arrangement.SpaceEvenly
                                        )
                                    } else {
                                        BarGraph(
                                            graphBarData = listOf(0f,0f,0f),
                                            xAxisScaleData = datesList,
                                            barDataFunc = listOf(
                                                month1.floatValue.toInt(),
                                                month2.floatValue.toInt(),
                                                month3.floatValue.toInt()
                                            ),
                                            height = 200.dp,
                                            roundType = BarType.TOP_CURVED,
                                            barWidth = 30.dp,
                                            barColor = Green01,
                                            barArrangement = Arrangement.SpaceEvenly
                                        )
                                    }

                                }



                            }
                        }

                        if(myOrderHistory.size > 0) {


                            item {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 12.dp, end = 12.dp,),
                                    verticalArrangement = Arrangement.spacedBy(15.dp)
                                ) {

                                    Text(
                                        text = "My Sales ",
                                        fontSize = 18.sp,
                                        fontFamily = Poppins,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black

                                    )

                                    if (
                                        isVegetables || isFruits || isHerbs || isNuts ||
                                        isMillets || isOils || isNonTimberForest || isSpices
                                    ){


                                        myOrderHistory.forEach { product ->

                                            val start = formatter.parse(startDate)
                                            val end = formatter.parse(endDate)
                                            val productDate = formatter.parse(product.orderedDate)!!

                                            val startCompare = productDate.compareTo(start)
                                            val endCompare = productDate.compareTo(end)

                                            if (
                                                product.price.toFloat() in sliderPosition
                                                && (startCompare >= 0  && endCompare <= 0)
                                            ) {
                                                if (
                                                    product.productCategory == vegetables ||
                                                    product.productCategory == fruits ||
                                                    product.productCategory == herbs ||
                                                    product.productCategory == nuts ||
                                                    product.productCategory == millets ||
                                                    product.productCategory == oils ||
                                                    product.productCategory == nonTimberForest ||
                                                    product.productCategory == spices
                                                ) {
                                                    HistoryItems(
                                                        navController = navController,
                                                        product = product,
                                                    )
                                                }

                                            }

                                        }

                                    }  else{


                                        myOrderHistory.forEach { product ->

                                            val start = formatter.parse(startDate)
                                            val end = formatter.parse(endDate)
                                            val productDate = formatter.parse(product.orderedDate)!!

                                            val startCompare = productDate.compareTo(start)
                                            val endCompare = productDate.compareTo(end)

                                            if (
                                                product.price.toFloat() in sliderPosition
                                                && (startCompare >= 0  && endCompare <= 0)
                                                ) {

                                                    HistoryItems(
                                                        navController = navController,
                                                        product = product,
                                                    )
                                                }

                                        }
                                    }


                                }

                            }
                        } else {

                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    if (!processState) {
                                        Text(
                                            modifier = Modifier.align(alignment = Alignment.Center),
                                            text = "No Sales!",
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
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = Color.White),
        title = {
            Text(
                text = "History",
                style = mobileTitle
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )

}



@Composable
private fun HistoryItems(
    navController: NavHostController,
    product: OrderManageProduct,
) {

    val isConnection = networkConnection(context = LocalContext.current)
    val imageList = product.productImage.split(",")
    val image = imageList[0].replace("[","")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (isConnection) {
                    MyHistoryItem = product
                    MyHistoryItemImage = image
                    MyHistoryItemCustName = product.customerName
                    navController.navigate(SellerScreens.ItemHistoryScreen.route)
                }
            },
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(image),
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

            Text(
                text = "${product.productName} ordered by ${product.customerName}",
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${product.orderedQuantity}${product.quantityCategory}, Price",
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
                    text = product.price,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFF344356)
                )
            }

        }
        Text(modifier = Modifier
            .weight(1.2f)
            .width(125.dp)
            .height(45.dp)
            .background(
                color = if (product.orderStatus == "ORDER_SHIPPED" && product.orderProgressing == "YES") Green01 else Red01,
                shape = RoundedCornerShape(13.dp)
            )
            .wrapContentSize(align = Alignment.Center)
            .padding(4.dp),
            fontFamily = Poppins,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            text =  if (product.orderStatus == "ORDER_SHIPPED" && product.orderProgressing == "YES") "Shipped" else "Cancelled",
            color = Color.White
        )
    }
    DividerHorizontal()
}

@SuppressLint("SimpleDateFormat")
private fun getMonth(month : Int) : String{

    val monthFormat = SimpleDateFormat("MMMM")
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, month)
    return monthFormat.format(cal.time).toString().substring(startIndex = 0, endIndex = 3).uppercase()
}

private fun getMonthName(monthNumber: Int): String {
    val dfs = DateFormatSymbols()
    val months = dfs.shortMonths
    return months[monthNumber-1]
}

@SuppressLint("SimpleDateFormat")
private fun getFormattedDate(timeInMillis : Long): String {

    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    return dateFormat.format(calender.timeInMillis)

}

fun dateValidator(): (Long) -> Boolean {
    return {
            timeInMillis ->
        val endCalenderDate = Calendar.getInstance()
        endCalenderDate.timeInMillis = timeInMillis
        endCalenderDate.set(Calendar.DATE, Calendar.DATE + 20)
        timeInMillis > Calendar.getInstance().timeInMillis && timeInMillis < endCalenderDate.timeInMillis
    }
}

@SuppressLint("SimpleDateFormat")
private fun dateStringToMilliseconds(dateString: String): Long {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val date = sdf.parse(dateString)
    return date?.time ?: -1 // return -1 if parsing failed
}