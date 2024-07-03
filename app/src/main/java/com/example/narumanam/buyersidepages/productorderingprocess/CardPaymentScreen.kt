package com.example.narumanam.buyersidepages.productorderingprocess

import android.text.TextUtils
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultDropDown
import com.example.narumanam.DefaultOutLinedTextField
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.R
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle

@Composable
fun CardPaymentScreen(
    navController: NavHostController
) {
    val months = listOf("01","02","03", "04","05","06", "07","08", "09","10","11","12")
    val years = listOf("24","25","26", "27","28","29", "30","31", "32","33", "34","35")

    var cardNumber by remember { mutableStateOf("") }
    var cardNumberError by remember { mutableStateOf(false) }
    var cvv by remember { mutableStateOf("") }
    var cvvError by remember { mutableStateOf(false) }

    var monthExpanded by remember { mutableStateOf(false)}
    var monthFieldSize by remember { mutableStateOf(Size.Zero)}
    var monthSelected by rememberSaveable { mutableStateOf("")}
    var monthError by remember { mutableStateOf(false)}
    val monthIcon = if (monthExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown


    var yearExpanded by remember { mutableStateOf(false)}
    var yearFieldSize by remember { mutableStateOf(Size.Zero)}
    var yearSelected by rememberSaveable { mutableStateOf("")}
    var yearError by remember { mutableStateOf(false)}
    val yearIcon = if (monthExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
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
                                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {


                            Row(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                PaymentProgressStatus()
                            }

                            Text(
                                text = "Select any one Option",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                            )

                            DefaultOutLinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = cardNumber,
                                textError = cardNumberError,
                                onValueChange = {cardChar->
                                    cardNumberError = false
                                    if (cardChar.length <= 24){
                                        cardNumber = cardChar
                                    }
                                },
                                labelText = "Card Number",
                                placeholderText = "Enter your card number",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {

                                        cardNumberError = TextUtils.isEmpty(cardNumber)
                                    }
                                )

                            )
                            Text(
                                text = "Expiry",
                                fontSize = 15.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                DefaultDropDown(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(100.dp)
                                        .onGloballyPositioned { layoutCoordinates ->
                                            monthFieldSize =
                                                layoutCoordinates.size.toSize()
                                        },
                                    dropDownModifier = Modifier
                                        .width(with(LocalDensity.current) { monthFieldSize.width.toDp() }),
                                    selectedText = monthSelected,
                                    onValueChange = {mth->
                                        monthError = false
                                        monthSelected = mth
                                    },
                                    expanded = monthExpanded,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = monthIcon,
                                            contentDescription = "DropdownIcon",
                                            modifier = Modifier
                                                .clickable {
                                                    monthExpanded =
                                                        !monthExpanded
                                                },
                                            tint = Color(0xFF7C7C7C)
                                        )
                                    },
                                    placeholderText = "MM",
                                    textError = monthError,
                                    onDismiss = { monthExpanded = false },
                                    dropDownItems = months,
                                    dropDownOnclick = {mth->
                                        monthError = false
                                        monthSelected = mth
                                        monthExpanded = false
                                    }

                                )
                                DefaultDropDown(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(90.dp)
                                        .onGloballyPositioned { layoutCoordinates ->
                                            yearFieldSize =
                                                layoutCoordinates.size.toSize()
                                        },
                                    dropDownModifier = Modifier
                                        .width(with(LocalDensity.current) { yearFieldSize.width.toDp() }),
                                    selectedText = yearSelected,
                                    onValueChange = {yr->
                                        yearError = false
                                        yearSelected = yr
                                    },
                                    expanded = yearExpanded,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = yearIcon,
                                            contentDescription = "DropdownIcon",
                                            modifier = Modifier
                                                .clickable {
                                                    yearExpanded =
                                                        !yearExpanded
                                                },
                                            tint = Color(0xFF7C7C7C)
                                        )
                                    },
                                    placeholderText = "YY",
                                    textError = yearError,
                                    onDismiss = { yearExpanded = false },
                                    dropDownItems = years,
                                    dropDownOnclick = {yr->
                                        yearError = false
                                        yearSelected = yr
                                        yearExpanded = false
                                    }

                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                DefaultOutLinedTextField(
                                    modifier = Modifier
                                        .width(100.dp),
                                    text = cvv,
                                    textError = cvvError,
                                    onValueChange = {cvvChar->
                                        cvvError = false
                                        if (cvvChar.length <= 3){
                                            cvv = cvvChar
                                        }
                                    },
                                    labelText = "CVV",
                                    placeholderText = "CVV",
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {

                                            cardNumberError = TextUtils.isEmpty(cardNumber)
                                            cvvError = TextUtils.isEmpty(cvv)

                                        }
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = {  }) {
                                           Icon(imageVector = Icons.Filled.Help, contentDescription = "help",tint = Color(0xFFB3B3B3))
                                        }
                                    }

                                )


                            }


                            DividerHorizontal()

                            Text(
                                text = "Price Details",
                                fontSize = 17.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,

                                )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        modifier = Modifier
                                            .weight(1f),
                                        text = "Item(1)",
                                        fontSize = 14.sp,
                                        fontFamily = Poppins,
                                    )
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
                                            text = "20.0",
                                            fontFamily = Poppins,
                                            fontSize = 12.sp,
                                            color = Color(0xFF344356)
                                        )
                                    }

                                }

                                SummaryItem(text = "Delivery Charges", price = "100")
                                DividerHorizontal()

                            }
                            SummaryItem(text = "Amount Payable", price = "1000")

                            Spacer(modifier = Modifier.height(40.dp))

                            DefaultButton(
                                modifier = Modifier
                                    .height(55.dp)
                                    .align(Alignment.End),
                                onclick = {

                                },
                                shape = RoundedCornerShape(5.dp),
                                text = "Pay Now"
                            )


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
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Text(
                text = "Payments",
                style = mobileTitle
            )
        },
        navigationIcon = {
            DefaultBackArrow {
                navController.popBackStack()
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )

}
@Composable
private fun PaymentProgressStatus() {


    Column {


        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = Green01, shape = CircleShape)
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
                    tint = Green01
                )
            }

            DividerHorizontal(
                modifier = Modifier
                    .width(75.dp),
                thickness = 2.dp,
                color = Green01
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .border(width = 1.5.dp, color = Green01, shape = CircleShape)
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
                    tint = Green01
                )
            }
            DividerHorizontal(
                modifier = Modifier
                    .width(75.dp),
                thickness = 2.dp,
                color = Green01
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(color = Green01, shape = CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        Row {
            Text(
                text = "Address",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = "Order Summary",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                text = "Payment",
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}