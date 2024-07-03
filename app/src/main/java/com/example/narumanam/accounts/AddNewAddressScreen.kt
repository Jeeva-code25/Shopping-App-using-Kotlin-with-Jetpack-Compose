package com.example.narumanam.accounts

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultOutLinedTextField
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.Screens
import com.example.narumanam.SubTitleText
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.sellersidepages.products.AddressExist
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.values

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddNewAddressScreen(
    navController: NavHostController,
    fromPage: String = ""
) {

    val focusManager = LocalFocusManager.current

    var processState = false
    var name by remember{ mutableStateOf(AddressEdit.userName) }
    var nameError by remember{ mutableStateOf(false) }

    var phoneNumber by remember{ mutableStateOf(AddressEdit.phoneNumber) }
    var phoneNumberError by remember{ mutableStateOf(false) }

    var houseNo by remember{ mutableStateOf(AddressEdit.houseNo) }
    var houseNoError by remember{ mutableStateOf(false) }

    var streetName by remember{ mutableStateOf(AddressEdit.streetName) }
    var streetNameError by remember{ mutableStateOf(false) }

    var landMark by remember{ mutableStateOf(AddressEdit.nearestLandMark) }
    var landMarkError by remember{ mutableStateOf(false) }

    var pincode by remember{ mutableStateOf(AddressEdit.pinCode) }
    var pincodeError by remember{ mutableStateOf(false) }

    var district by remember{ mutableStateOf(AddressEdit.district) }
    var districtError by remember{ mutableStateOf(false) }

    var state by remember{ mutableStateOf(AddressEdit.state) }
    var stateError by remember{ mutableStateOf(false) }

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val currentPhone = sharedPreference.getString("PHONENUMBER","")
    val userRole = sharedPreference.getString("USERROLE", "")

    val database = FirebaseDatabase.getInstance().getReference(userRole?:"")
    val addressId = if(AddressEdit.addressId == "") database.push().key?:"" else AddressEdit.addressId


    Scaffold (
        topBar = {
            TopBar(
                navController
            )
        },
        content = { it ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp)
                        .background(color = Color.White)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {

                    if (processState){
                        ProgressIndicator()
                    }

                    SubTitleText(
                         text = "Contact Detail",
                        fontSize = 17.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)

                    ) {

                        DefaultOutLinedTextField(
                            modifier = Modifier
                                .weight(1f),
                            text = name,
                            textError = nameError,
                            onValueChange = {nameChar->
                                nameError = false
                                if (nameChar.length <= 20){
                                    name = nameChar
                                }
                            },
                            labelText = "Name",
                            placeholderText = "name",
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    nameError = TextUtils.isEmpty(name)
                                    if (!nameError){
                                        focusManager.moveFocus(FocusDirection.Right)
                                    }
                                }
                            )

                        )
                        DefaultOutLinedTextField(
                            modifier = Modifier
                                .weight(1f),
                            text = phoneNumber,
                            textError = phoneNumberError,
                            onValueChange = {phoneChar->
                                phoneNumberError = false
                                if (phoneChar.length <= 12){
                                    phoneNumber = phoneChar
                                }
                            },
                            labelText = "Phone Number",
                            placeholderText = "phone ",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    nameError = TextUtils.isEmpty(name)
                                    phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                    if (!nameError && !phoneNumberError){
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                }
                            )
                        )
                    }
                    DividerHorizontal()

                    DefaultOutLinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = houseNo,
                        textError = houseNoError,
                        onValueChange = {houseChar->
                            houseNoError = false
                            if (houseChar.length <= 20){
                                houseNo = houseChar
                            }
                        },
                        labelText = "House No/Flat No",
                        placeholderText = "Enter your house number",
                        keyboardActions = KeyboardActions(
                            onNext = {

                                nameError = TextUtils.isEmpty(name)
                                phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                houseNoError = TextUtils.isEmpty(houseNo)

                                if (!nameError && !phoneNumberError && !houseNoError){
                                    focusManager.moveFocus(FocusDirection.Down)
                                }

                            }
                        )

                    )

                    DefaultOutLinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = streetName,
                        textError = streetNameError,
                        onValueChange = {streetChar->
                            streetNameError = false
                            if (streetChar.length <= 30){
                                streetName = streetChar
                            }
                        },
                        labelText = "Area/Colony/Street Name",
                        placeholderText = "Enter your street name",
                        keyboardActions = KeyboardActions(
                            onNext = {
                                nameError = TextUtils.isEmpty(name)
                                phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                houseNoError = TextUtils.isEmpty(houseNo)
                                streetNameError = TextUtils.isEmpty(streetName)

                                if (!nameError && !phoneNumberError
                                    && !houseNoError && !streetNameError){
                                    focusManager.moveFocus(FocusDirection.Down)
                                }

                            }
                        )

                    )

                    DefaultOutLinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = landMark,
                        textError = landMarkError,
                        onValueChange = {landChar->
                            landMarkError = false
                            if (landMark.length < 30){
                                landMark = landChar
                            }
                        },
                        labelText = "Nearest LandMark",
                        placeholderText = "Enter your nearest landMark",
                        keyboardActions = KeyboardActions(
                            onNext = {
                                nameError = TextUtils.isEmpty(name)
                                phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                houseNoError = TextUtils.isEmpty(houseNo)
                                streetNameError = TextUtils.isEmpty(streetName)
                                landMarkError = TextUtils.isEmpty(landMark)

                                if (!nameError && !phoneNumberError
                                    && !houseNoError && !streetNameError
                                    && !landMarkError){
                                    focusManager.moveFocus(FocusDirection.Down)
                                }

                            }
                        )
                    )
                    DefaultOutLinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = pincode,
                        textError = pincodeError,
                        onValueChange = {pinChar->
                            pincodeError = false
                            if (pinChar.length <= 6){
                                pincode = pinChar.filter { it.isDigit() }
                            }
                        },
                        labelText = "PinCode",
                        placeholderText = "Enter your pinCode",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {

                                nameError = TextUtils.isEmpty(name)
                                phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                houseNoError = TextUtils.isEmpty(houseNo)
                                streetNameError = TextUtils.isEmpty(streetName)
                                landMarkError = TextUtils.isEmpty(landMark)
                                pincodeError = TextUtils.isEmpty(pincode)

                                if (!nameError && !phoneNumberError
                                    && !houseNoError && !streetNameError
                                    && !landMarkError && !pincodeError){
                                    focusManager.moveFocus(FocusDirection.Down)
                                }

                            }
                        )
                    )

                    DividerHorizontal()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)

                    ) {

                        DefaultOutLinedTextField(
                            modifier = Modifier
                                .weight(1f),
                            text = district,
                            textError = districtError,
                            onValueChange = {districtChar->
                                districtError = false
                                if (districtChar.length <= 25){
                                    district = districtChar
                                }
                            },
                            labelText = "City/District",
                            placeholderText = "city",
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    nameError = TextUtils.isEmpty(name)
                                    phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                    houseNoError = TextUtils.isEmpty(houseNo)
                                    streetNameError = TextUtils.isEmpty(streetName)
                                    landMarkError = TextUtils.isEmpty(landMark)
                                    pincodeError= TextUtils.isEmpty(pincode)
                                    districtError= TextUtils.isEmpty(district)

                                    if (!nameError && !phoneNumberError
                                        && !houseNoError && !streetNameError
                                        && !landMarkError && !pincodeError
                                        && !districtError){
                                        focusManager.moveFocus(FocusDirection.Right)
                                    }

                                }
                            )
                        )

                        DefaultOutLinedTextField(
                            modifier = Modifier
                                .weight(1f),
                            text = state,
                            textError = stateError,
                            onValueChange = {stateChar->
                                stateError = false
                                if (stateChar.length < 25){
                                    state = stateChar
                                }
                            },
                            labelText = "State",
                            placeholderText = "state",
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    nameError = TextUtils.isEmpty(name)
                                    phoneNumberError = TextUtils.isEmpty(phoneNumber)
                                    houseNoError = TextUtils.isEmpty(houseNo)
                                    streetNameError = TextUtils.isEmpty(streetName)
                                    landMarkError = TextUtils.isEmpty(landMark)
                                    pincodeError= TextUtils.isEmpty(pincode)
                                    districtError= TextUtils.isEmpty(district)
                                    stateError= TextUtils.isEmpty(state)

                                    if (!nameError && !phoneNumberError
                                        && !houseNoError && !streetNameError
                                        && !landMarkError && !pincodeError
                                        && !districtError && !stateError){
                                        focusManager.clearFocus()
                                    }

                                }
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    DefaultButton(
                        modifier = Modifier
                            .height(55.dp)
                            .width(120.dp)
                            .align(Alignment.End),
                        onclick = {

                            processState = true

                            nameError = TextUtils.isEmpty(name)
                            phoneNumberError = TextUtils.isEmpty(phoneNumber)
                            houseNoError = TextUtils.isEmpty(houseNo)
                            streetNameError = TextUtils.isEmpty(streetName)
                            landMarkError = TextUtils.isEmpty(landMark)
                            pincodeError= TextUtils.isEmpty(pincode)
                            districtError= TextUtils.isEmpty(district)
                            stateError= TextUtils.isEmpty(state)

                            if (!nameError && !phoneNumberError
                                && !houseNoError && !streetNameError
                                && !landMarkError && !pincodeError
                                && !districtError && !stateError && isConnection){



                                val newAddress = Address(
                                    addressId = addressId,
                                    phoneNumber = phoneNumber.trim(),
                                    userName = name.trim(),
                                    houseNo = houseNo.trim(),
                                    streetName = streetName.trim(),
                                    nearestLandMark = landMark.trim(),
                                    pinCode = pincode.trim(),
                                    district = district.trim(),
                                    state = state.trim()
                                )

                                database
                                    .child(currentPhone?:"")
                                    .child("MyAddress")
                                    .child(addressId)
                                    .setValue(newAddress)
                                    .addOnSuccessListener {

                                        database
                                            .child(currentPhone?:"")
                                            .child("SelectedAddress")
                                            .get()
                                            .addOnSuccessListener {

                                                if (it.exists()){
                                                    processState = false
                                                    Toast.makeText(context,"Successfully Added", Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()

                                                } else{
                                                    database
                                                        .child(currentPhone?:"")
                                                        .child("SelectedAddress")
                                                        .setValue(addressId)
                                                        .addOnSuccessListener {
                                                            processState = false
                                                            Toast.makeText(context,"Successfully Added", Toast.LENGTH_SHORT).show()

                                                            when (fromPage) {
                                                                "PRODUCTEDIT" -> {
                                                                    navController.popBackStack(
                                                                        route = SellerScreens.productEditScreen.route,
                                                                        inclusive = false
                                                                    )
                                                                }
                                                                "PRODUCTUPLOAD" -> {
                                                                    navController.popBackStack(
                                                                        route = SellerScreens.productScreen.route,
                                                                        inclusive = false
                                                                    )
                                                                }
                                                                "MYADDRESS" -> {
                                                                    navController.popBackStack()
                                                                }
                                                                else -> {
                                                                    navController.popBackStack(
                                                                        route = BuyerScreens.OrderSummaryScreen.route,
                                                                        inclusive = false
                                                                    )
                                                                }
                                                            }

                                                        }
                                                }
                                            }
                                    }
                                    .addOnFailureListener{
                                        processState = false
                                        Toast.makeText(context,"Failed to  Add", Toast.LENGTH_SHORT).show()
                                    }
                            }

                        },
                        shape = RoundedCornerShape(5.dp),
                        text = "Save"
                        )
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
        modifier =  Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Text(
                text = "Add new address",
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
