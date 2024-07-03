package com.example.narumanam.accounts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultButton
import com.example.narumanam.DefaultTextField
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProfileData
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.SplashActivity
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.ProfileInfo
import com.example.narumanam.sellersidepages.products.ProductData
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyDetailsScreen(
    navController : NavHostController
) {

    val context = LocalContext.current as Activity
    val isConnection = networkConnection(context = context)
    val focusManager = LocalFocusManager.current
    val emailRegex = "^[a-zA-z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{3}\$".toRegex()

    val names = ProfileInfo.userName.split(" ")
    var profileUri : Any? by remember { mutableStateOf(ProfileInfo.profilePhoto) }
    var profileUploadedUrl : Any? by remember { mutableStateOf("")}
    var processState by remember { mutableStateOf(true) }

    val profilePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()){uri->
        processState = false
        if (uri != null)
            profileUri = uri
    }

    val myProducts =  remember { mutableStateListOf<ProductData>() }

    var firstName by rememberSaveable { mutableStateOf(names[0]) }
    var firstNameError by remember { mutableStateOf(false)}

    var lastName by rememberSaveable { mutableStateOf(names[1]) }
    var lastNameError by remember { mutableStateOf(false)}

    var mobileNumber by rememberSaveable { mutableStateOf(ProfileInfo.phoneNumber) }
    var mobileNumberError by remember { mutableStateOf(false)}

    var emailId by rememberSaveable { mutableStateOf(ProfileInfo.email) }
    var emailIdError by remember { mutableStateOf(false)}

    var showDialog by remember { mutableStateOf(false) }

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")

    val userSigned = sharedPreference.getBoolean("USERSIGNED",false)
    val sharedEdit = sharedPreference.edit()

    val auth = FirebaseAuth.getInstance()
    val userId = ProfileInfo.userId
    val database = FirebaseDatabase.getInstance().getReference("Sellers")
    val globProductDatabase = FirebaseDatabase.getInstance().getReference("GlobalProducts")
    val sellerProfileDatabase = Firebase.storage.getReference("SellerProfileImages")

    database
        .child(phoneNumber?:"")
        .child("MyInventory")
        .addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myProducts.clear()
                snapshot.children.forEach { item->

                    item.getValue(ProductData::class.java)?.let {
                        myProducts.add(it)
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
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
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

                LazyColumn(

                ){
                    item {
                        DividerHorizontal()
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.White)
                                .padding(start = 12.dp, end = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            if (processState) {
                                ProgressIndicator()
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(15.dp)
                                ) {

                                    Image(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .border(
                                                width = if (profileUri == "") 2.dp else 0.dp,
                                                color = Color(0xFF7C7C7C),
                                                shape = RoundedCornerShape(18.dp)
                                            )
                                            .clip(RoundedCornerShape(18.dp))

                                            .clickable {
                                                processState = true
                                                profilePhotoPicker.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            }
                                            .padding(if (profileUri == "") 10.dp else 0.dp),
                                        painter = rememberAsyncImagePainter(
                                            model = if (profileUri == "") R.drawable.user_add else profileUri,
                                            contentScale = ContentScale.FillBounds
                                        ),
                                        contentDescription = "profile",
                                        contentScale = ContentScale.FillBounds
                                    )

                                    TextButton(
                                        onClick = {

                                            if (isConnection) {
                                                processState = true

                                                if (ProfileInfo.profilePhoto != "") {

                                                    val globImageDatabase =
                                                        Firebase.storage.getReferenceFromUrl(
                                                            ProfileInfo.profilePhoto.trim()
                                                        )

                                                    globImageDatabase.delete()
                                                        .addOnSuccessListener {

                                                            val userProfile = ProfileData(
                                                                phoneNumber = mobileNumber,
                                                                userId = userId,
                                                                userName = "$firstName $lastName",
                                                                email = emailId,

                                                                password = ProfileInfo.password
                                                            )

                                                            database
                                                                .child(phoneNumber ?: "")
                                                                .child("BasicInfo")
                                                                .setValue(userProfile)
                                                                .addOnCompleteListener { task ->

                                                                    processState = false

                                                                    if (task.isSuccessful) {

                                                                        Toast.makeText(
                                                                            context,
                                                                            "Profile photo successfully removed",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()

                                                                        profileUri = ""

                                                                    } else {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "You haven't any Profile photo to remove",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()
                                                                    }

                                                                }

                                                        }
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "You haven't any Profile photo to remove",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    processState = false
                                                }
                                            }
                                        }
                                    ){
                                        Text(
                                            text = "Remove Photo",
                                            fontSize = 15.sp,
                                            fontFamily = Poppins,
                                            fontStyle = FontStyle.Italic,
                                            color = Red01
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "First Name",
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            DefaultTextField(
                                text = firstName,
                                onValueChange = {firstNameChar->
                                    firstNameError = false
                                    if (firstNameChar.length <= 20) {
                                        firstName = firstNameChar
                                    }
                                },
                                textError = firstNameError,
                                placeholderText = "eg. steven",
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    imeAction = ImeAction.Next,
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        firstNameError = TextUtils.isEmpty(firstName)
                                        if (!firstNameError) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    }
                                )

                            )

                            Text(
                                text = "Last Name",
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            DefaultTextField(
                                text = lastName,
                                onValueChange = {char->
                                    lastNameError = false
                                    if (char.length <= 20) {
                                        lastName = char
                                    }
                                },
                                textError = lastNameError,
                                placeholderText = "eg. smith",
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    imeAction = ImeAction.Next,
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        firstNameError = TextUtils.isEmpty(firstName)
                                        lastNameError = TextUtils.isEmpty(lastName)
                                        if (!firstNameError && !lastNameError) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    }
                                )

                            )

                            Text(
                                text = "Mobile Number",
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            DefaultTextField(
                                text = mobileNumber,
                                onValueChange = {char->
                                    mobileNumberError = false
                                    if (char.length <= 20) {
                                        mobileNumber = char
                                    }
                                },
                                textError = mobileNumberError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    imeAction = ImeAction.Next,
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        firstNameError = TextUtils.isEmpty(firstName)
                                        lastNameError = TextUtils.isEmpty(lastName)
                                        mobileNumberError = TextUtils.isEmpty(mobileNumber)
                                        if (!firstNameError && !lastNameError && !mobileNumberError) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    }
                                ),
                                readOnly = true

                            )

                            Text(
                                text = "Email Id",
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            DefaultTextField(
                                text = emailId,
                                onValueChange = {
                                    emailId = it.replace("\\p{Zs}+".toRegex(), "")
                                    emailIdError = !emailId.matches(emailRegex)
                                },
                                textError = emailIdError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone  = {
                                        firstNameError = TextUtils.isEmpty(firstName)
                                        lastNameError = TextUtils.isEmpty(lastName)
                                        mobileNumberError = TextUtils.isEmpty(mobileNumber)
                                        emailIdError = TextUtils.isEmpty(emailId)
                                        if (!firstNameError && !lastNameError
                                            && !mobileNumberError && !emailIdError) {
                                            focusManager.clearFocus()
                                        }
                                    }
                                ),
                            )

                            TextButton(
                                onClick = {

                                    showDialog = true

                                }
                            ) {
                                Text(
                                    text = "Deactivate Account",
                                    fontSize = 15.sp,
                                    fontFamily = Poppins,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    color = Green01
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            DefaultButton(
                                modifier = Modifier
                                    .height(55.dp)
                                    .align(Alignment.End),
                                onclick = {

                                    firstNameError = TextUtils.isEmpty(firstName)
                                    lastNameError = TextUtils.isEmpty(lastName)
                                    mobileNumberError = TextUtils.isEmpty(mobileNumber)
                                    emailIdError = TextUtils.isEmpty(emailId)


                                    if (!firstNameError && !lastNameError
                                        && !mobileNumberError && !emailIdError && isConnection) {

                                        processState = true

                                        if (profileUri.toString() != ProfileInfo.profilePhoto && ProfileInfo.profilePhoto != "") {

                                                val globImageDatabase =
                                                    Firebase.storage.getReferenceFromUrl(ProfileInfo.profilePhoto)
                                                globImageDatabase.delete().addOnSuccessListener {

                                                    database
                                                        .child(phoneNumber ?: "")
                                                        .child("BasicInfo")
                                                        .setValue(
                                                            ProfileData(
                                                                phoneNumber = mobileNumber,
                                                                userId = userId,
                                                                profilePhoto = "",
                                                                userName = "$firstName $lastName",
                                                                email = emailId,
                                                                password = ProfileInfo.password
                                                            )
                                                        )
                                                        .addOnSuccessListener {

                                                            sharedEdit.putString("EMAIL",emailId)
                                                            sharedEdit.putString("USERNAME", "$firstName $lastName")
                                                            sharedEdit.apply()

                                                        val imageUri = Uri.parse(profileUri.toString())
                                                        sellerProfileDatabase
                                                        .child(phoneNumber ?: "")
                                                        .child(getFilename(context, imageUri).toString())
                                                        .putFile(imageUri)
                                                        .addOnSuccessListener {
                                                            sellerProfileDatabase
                                                                .child(phoneNumber ?: "")
                                                                .child(
                                                                    getFilename(
                                                                        context,
                                                                        imageUri
                                                                    ).toString()
                                                                )
                                                                .downloadUrl.addOnSuccessListener { uploadedUrl ->

                                                                    profileUploadedUrl = uploadedUrl


                                                                    if (profileUploadedUrl != "") {

                                                                        val userProfile =
                                                                            ProfileData(
                                                                                phoneNumber = mobileNumber,
                                                                                userId = userId,
                                                                                profilePhoto = profileUploadedUrl.toString(),
                                                                                userName = "$firstName $lastName",
                                                                                email = emailId,
                                                                                password = ProfileInfo.password
                                                                            )

                                                                        database
                                                                            .child(
                                                                                phoneNumber ?: ""
                                                                            )
                                                                            .child("BasicInfo")
                                                                            .setValue(userProfile)
                                                                            .addOnCompleteListener { task ->

                                                                                sharedEdit.putString("EMAIL",emailId)
                                                                                sharedEdit.putString("USERNAME", "$firstName $lastName")
                                                                                sharedEdit.apply()

                                                                                processState = false

                                                                                if (task.isSuccessful) {

                                                                                    Toast.makeText(
                                                                                        context,
                                                                                        "Changes Successfully Modified",
                                                                                        Toast.LENGTH_LONG
                                                                                    ).show()


                                                                                } else {
                                                                                    Toast.makeText(
                                                                                        context,
                                                                                        "Failed to Change",
                                                                                        Toast.LENGTH_LONG
                                                                                    )
                                                                                        .show()
                                                                                }

                                                                            }

                                                                    }
                                                                }
                                                        }
                                                    }
                                                }



                                        } else if (ProfileInfo.profilePhoto == "" && profileUri.toString() != ""){

                                            val imageUri = Uri.parse(profileUri.toString())
                                            sellerProfileDatabase
                                                .child(phoneNumber ?: "")
                                                .child(getFilename(context, imageUri).toString())
                                                .putFile(imageUri)
                                                .addOnSuccessListener {
                                                    sellerProfileDatabase
                                                        .child(phoneNumber ?: "")
                                                        .child(getFilename(context, imageUri).toString())
                                                        .downloadUrl.addOnSuccessListener { uploadedUrl ->

                                                            profileUploadedUrl = uploadedUrl


                                                            if (profileUploadedUrl != "") {

                                                                val userProfile =
                                                                    ProfileData(
                                                                        phoneNumber = mobileNumber,
                                                                        userId = userId,
                                                                        profilePhoto = profileUploadedUrl.toString(),
                                                                        userName = "$firstName $lastName",
                                                                        email = emailId,
                                                                        password = ProfileInfo.password
                                                                    )

                                                                database
                                                                    .child(phoneNumber ?: "")
                                                                    .child("BasicInfo")
                                                                    .setValue(userProfile)
                                                                    .addOnCompleteListener { task ->

                                                                        sharedEdit.putString("EMAIL",emailId)
                                                                        sharedEdit.putString("USERNAME", "$firstName $lastName")
                                                                        sharedEdit.apply()

                                                                        processState = false

                                                                        if (task.isSuccessful) {

                                                                            Toast.makeText(
                                                                                context,
                                                                                "Changes Successfully Modified",
                                                                                Toast.LENGTH_LONG
                                                                            ).show()


                                                                        } else {
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Failed to Change",
                                                                                Toast.LENGTH_LONG
                                                                            )
                                                                                .show()
                                                                        }

                                                                    }

                                                            }
                                                        }
                                                }


                                        } else {

                                                val userProfile =
                                                ProfileData(
                                                    phoneNumber = mobileNumber,
                                                    userId = userId,
                                                    profilePhoto = ProfileInfo.profilePhoto,
                                                    userName = "$firstName $lastName",
                                                    email = emailId,
                                                    password = ProfileInfo.password
                                                )

                                                database
                                                .child(phoneNumber ?: "")
                                                .child("BasicInfo")
                                                .setValue(userProfile)
                                                .addOnCompleteListener { task ->

                                                    sharedEdit.putString("EMAIL",emailId)
                                                    sharedEdit.putString("USERNAME", "$firstName $lastName")
                                                    sharedEdit.apply()

                                                    processState = false

                                                    if (task.isSuccessful) {

                                                        Toast.makeText(
                                                            context,
                                                            "Changes Successfully Modified",
                                                            Toast.LENGTH_LONG
                                                        ).show()


                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to Change",
                                                            Toast.LENGTH_LONG
                                                        )
                                                            .show()
                                                    }
                                                }

                                        }

                                    }

                                },
                                shape = RoundedCornerShape(5.dp),
                                text = "Submit"
                            )

                            if (showDialog){

                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    icon = {
                                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
                                    },
                                    title = {
                                        Text(
                                            text = "Are you sure want to Deactivate?",
                                            fontSize = 13.sp,
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "Your data will be removed permanently ",
                                            fontSize = 13.sp,
                                            fontFamily = Poppins,
                                            textAlign = TextAlign.Justify,
                                        )

                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                if (isConnection) {
                                                    showDialog = false
                                                    processState = true

                                                    myProducts.forEach { item ->

                                                        val images = item.productImages
                                                        val imagesUrl = images.split(",")

                                                        imagesUrl.forEachIndexed { index, url ->

                                                            Firebase.storage.getReferenceFromUrl(url)
                                                                .delete().addOnSuccessListener {

                                                                    if (index + 1 == imagesUrl.size) {
                                                                        globProductDatabase
                                                                            .child(item.globProductId)
                                                                            .removeValue()
                                                                            .addOnSuccessListener {
                                                                                database
                                                                                    .child(
                                                                                        phoneNumber
                                                                                            ?: ""
                                                                                    )
                                                                                    .child("MyInventory")
                                                                                    .child(item.productId)
                                                                                    .removeValue()
                                                                                    .addOnSuccessListener {
                                                                                        myProducts.remove(
                                                                                            item
                                                                                        )

                                                                                        if (myProducts.size == 0) {

                                                                                            if (ProfileInfo.profilePhoto != "") {

                                                                                                Firebase.storage.getReferenceFromUrl(
                                                                                                    ProfileInfo.profilePhoto
                                                                                                )
                                                                                                    .delete()
                                                                                                    .addOnSuccessListener {
                                                                                                        database
                                                                                                            .child(
                                                                                                                phoneNumber
                                                                                                                    ?: ""
                                                                                                            )
                                                                                                            .removeValue()
                                                                                                            .addOnSuccessListener {

                                                                                                                if (userSigned) {
                                                                                                                    processState =
                                                                                                                        false
                                                                                                                    auth.signOut()
                                                                                                                    sharedEdit.clear()
                                                                                                                        .apply()
                                                                                                                    navController.popBackStack()
                                                                                                                    context.finish()
                                                                                                                    val intent =
                                                                                                                        Intent(
                                                                                                                            context,
                                                                                                                            SplashActivity::class.java
                                                                                                                        )
                                                                                                                    context.startActivity(
                                                                                                                        intent
                                                                                                                    )
                                                                                                                } else {
                                                                                                                    Toast.makeText(
                                                                                                                        context,
                                                                                                                        "User not signed",
                                                                                                                        Toast.LENGTH_SHORT
                                                                                                                    )
                                                                                                                        .show()
                                                                                                                }


                                                                                                            }
                                                                                                    }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                            }
                                                                    }

                                                                }
                                                        }

                                                    }

                                                }

                                            })
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
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = Color.White),
        title = {
            Text(
                text = "My Details",
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

@SuppressLint("Range")
private fun getFilename(context : Context, uri: Uri): String? {
    if (uri.scheme == "content") {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
    }
    return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
}
