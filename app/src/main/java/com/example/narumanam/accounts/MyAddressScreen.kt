package com.example.narumanam.accounts

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.sellersidepages.products.AddressExist
import com.example.narumanam.sellersidepages.products.ProductData
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

var AddressEdit = Address()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyAddressScreen(
    navController: NavHostController,
    fromPage: String = ""
) {

    val myAddress =  remember { mutableStateListOf<Address>() }

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val currentPhone = sharedPreference.getString("PHONENUMBER","")
    val userRole = sharedPreference.getString("USERROLE", "")
    val database = FirebaseDatabase.getInstance().getReference(userRole?:"")

    database
        .child(currentPhone?:"")
        .child("MyAddress")
        .addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myAddress.clear()
                snapshot.children.forEach { item->
                    item.getValue(Address :: class.java)?.let {
                        myAddress.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Database Error: $error",Toast.LENGTH_SHORT).show()
            }

        })


    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController, userRole = userRole?:"", fromPage = fromPage)
        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding()
                    )
            ) {

                BackHandler(enabled = true){
                    if (AddressExist || fromPage == "Account"){
                        navController.popBackStack()
                    } else if( fromPage == "PRODUCTEDIT"){
                        navController.popBackStack(
                            route = SellerScreens.productEditScreen.route,
                            inclusive = true
                        )
                    }
//                    else if (fromPage == "ORDERSUMMARY"){
//                        navController.popBackStack(
//                            route = BuyerScreens.OrderSummaryScreen.route,
//                            inclusive = true
//                        )
//                    }
                    else {
                        navController.popBackStack(
                            route =if(userRole == "Sellers") SellerScreens.productScreen.route else BuyerScreens.OrderSummaryScreen.route,
                            inclusive = true
                        )
                    }
                }

                if (myAddress.size > 0) {


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    ) {
                        item {
                            DividerHorizontal()
                            myAddress.forEach{item->

                                AddressList(
                                    navController = navController,
                                    address = item,
                                    database = database,
                                    currentPhone = currentPhone?:"",
                                    context = context,
                                    userRole = userRole?:""
                                )
                                DividerHorizontal()
                            }

                        }

                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Empty Address!!",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

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
                        .height(65.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF7C7C7C),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .background(color = Color.White),
                    onClick = {
                        if (isConnection) {
                            navController.navigate(Screens.NewAddressScreen.route + "/${fromPage}")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Green01
                    )
                ) {

                    Icon(
                        modifier = Modifier
                            .size(22.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Green01
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        modifier = Modifier,
                        text = "Add New Address",
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
private fun TopBar(
    navController: NavHostController,
    userRole : String,
    fromPage : String
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier =  Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Text(
                text = "My Address",
                style = mobileTitle
            )
        },
        navigationIcon = {
            DefaultBackArrow {
                if (AddressExist || fromPage == "Account"){
                    navController.popBackStack()
                }else if( fromPage == "PRODUCTEDIT"){
                    navController.popBackStack(
                        route = SellerScreens.productEditScreen.route,
                        inclusive = true
                    )
                }
//                else if (fromPage == "ORDERSUMMARY") {
//                    navController.popBackStack(
//                        route = BuyerScreens.OrderSummaryScreen.route,
//                        inclusive = true
//                    )
//                }
                else {
                    navController.popBackStack(
                        route = if(userRole == "Sellers") SellerScreens.productScreen.route else BuyerScreens.OrderSummaryScreen.route,
                        inclusive = true
                    )
                }
            }
        }
    )

}

@Composable
fun AddressList(
navController: NavHostController,
address: Address,
database: DatabaseReference,
currentPhone: String,
context: Context,
userRole: String
) {

    val isConnection = networkConnection(context = context)
    val addressId by remember { mutableStateOf(address.addressId) }
    var selectedAddress by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }


    database
        .child(currentPhone ?: "")
        .child("SelectedAddress")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                selectedAddress = snapshot.getValue(String::class.java) ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error: $error", Toast.LENGTH_SHORT).show()
            }

        })

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .selectable(
                selected = addressId == selectedAddress,
                onClick = {
                    database
                        .child(currentPhone ?: "")
                        .child("SelectedAddress")
                        .setValue(addressId)
                        .addOnSuccessListener {
                            selectedAddress = addressId
                            Toast
                                .makeText(context, "Address Selected", Toast.LENGTH_SHORT)
                                .show()
                            if (!AddressExist) {
                                navController.popBackStack(
                                    route = if (userRole == "Sellers") SellerScreens.productScreen.route else BuyerScreens.OrderSummaryScreen.route,
                                    inclusive = false
                                )
                            }
                        }

                },
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier.weight(1f)
        ) {

            RadioButton(
                selected = addressId == selectedAddress,
                onClick = { },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Green01,
                    unselectedColor = Color.LightGray
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = address.userName,
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${address.houseNo}, ${address.streetName}," +
                            " ${address.district} -${address.pinCode}," +
                            " State: ${address.state}, Landmark: ${address.nearestLandMark}," +
                            " Phone: ${address.phoneNumber}",
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }


        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top
        ) {

            Icon(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        menuExpanded = !menuExpanded
                    },
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More"
            )
            DropdownMenu(
                modifier = Modifier,
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
                }
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = ""
                        )
                    },
                    text = {
                        Text(text = "Edit", fontFamily = Poppins)
                    },
                    onClick = {
                        if (isConnection) {
                            menuExpanded = false
                            AddressEdit = address
                            navController.navigate(Screens.NewAddressScreen.route+ "/${"MYADDRESS"}")
                        }
                    }
                )
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "",
                            tint = Color.Red
                        )
                    },
                    text = {
                        Text(text = "Delete", fontFamily = Poppins)
                    },
                    onClick = {

                        if (isConnection) {
                            menuExpanded = false
                            if (selectedAddress == addressId) {
                                database
                                    .child(currentPhone)
                                    .child("SelectedAddress")
                                    .removeValue()
                                    .addOnSuccessListener {
                                        database
                                            .child(currentPhone)
                                            .child("MyAddress")
                                            .child(addressId)
                                            .removeValue()
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "Successfully Removed",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }

                            } else {
                                database
                                    .child(currentPhone)
                                    .child("MyAddress")
                                    .child(addressId)
                                    .removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Successfully Removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                        }


                    }
                )
            }
        }
    }


}

