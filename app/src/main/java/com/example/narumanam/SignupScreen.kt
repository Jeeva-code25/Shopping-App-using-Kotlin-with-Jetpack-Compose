package com.example.narumanam

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.Surface
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.narumanam.buyersidepages.BuyersActivity
import com.example.narumanam.sellersidepages.MainActivity
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignupScreen(
    navController : NavHostController,
    phoneNumber : String
) {
    val context = LocalContext.current as Activity
    val isConnection = networkConnection(context = context)

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val sharedPreference = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val sharedEdit = sharedPreference.edit()
    val userRole = sharedPreference.getString("USERROLE", "")
    val database = FirebaseDatabase.getInstance().getReference(userRole.toString())
    val sellerProfileDatabase = Firebase.storage.getReference("SellerProfileImages")

    val focusManager = LocalFocusManager.current

    val phone by remember { mutableStateOf(phoneNumber) }

    var profileUri : Any? by remember { mutableStateOf("") }
    var profileUploadedUrl : Any? by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var firstNameError by remember { mutableStateOf(false) }

    var lastName by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf(false) }


    val emailRegex = "^[a-zA-z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{3}\$".toRegex()
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var processState by remember { mutableStateOf(false) }

    val profilePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()){uri->
        processState = false
        if (uri != null)
            profileUri = uri
    }


    val termsConditions = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontFamily = Poppins,
                color = Color(0xFF7C7C7C),
            )
        ) {
            append("By continuing you agree to our ")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontFamily = Poppins,
                color = Green01,
            )
        ) {
            append("Terms of Service and Privacy Policy")
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        },
        content = { it ->

            Surface(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {

                LazyColumn() {

                    item {


                        Column(
                            modifier = Modifier
                                .padding(top = 20.dp, start = 12.dp, end = 12.dp)
                                .background(color = Color.White)
                        ) {


                            AsyncImage(
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (profileUri == "") 3.dp else 0.dp,
                                        color = Color.Gray,
                                        shape = CircleShape
                                    )
                                    .padding(if (profileUri == "") 30.dp else 0.dp)
                                    .clickable {
                                        processState = true
                                        profilePhotoPicker.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(if (profileUri == "") R.drawable.profile else profileUri)
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "Profile",
                                contentScale = if (profileUri == "") ContentScale.Crop else ContentScale.FillBounds

                            )


                            if (processState) {
                                ProgressIndicator()
                            }

                            Text(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                text = "First Name",
                                fontSize = 16.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C7C7C)
                            )

                            DefaultTextField(
                                text = firstName,
                                onValueChange = { it ->
                                    firstNameError = false
                                    if (it.length <= 20) {
                                        firstName = it.filterNot { it.isWhitespace() }
                                    }
                                },
                                textError = firstNameError,
                                keyboardActions = KeyboardActions(
                                    onNext = {

                                        firstNameError = TextUtils.isEmpty(firstName)

                                        if (!firstNameError) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }

                                    }
                                ),
                                trailingIcon = {
                                    if (firstNameError) {
                                        Icon(
                                            imageVector = Icons.Outlined.ErrorOutline,
                                            contentDescription = "error",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            )

                            if (firstNameError) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "Enter a valid name",
                                    fontFamily = Poppins,
                                    color = Color.Red
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                text = "Last Name",
                                fontSize = 16.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C7C7C)
                            )

                            DefaultTextField(
                                text = lastName,
                                onValueChange = { it ->
                                    lastNameError = false
                                    if (it.length <= 20) {
                                        lastName = it.filterNot { it.isWhitespace() }
                                    }
                                },
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        firstNameError = TextUtils.isEmpty(firstName)

                                        if (!firstNameError) {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }

                                    }
                                ),
                            )

                            Text(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                text = "Phone Number",
                                fontSize = 16.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C7C7C)
                            )

                            DefaultTextField(
                                text = phone,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "checked",
                                        tint = Green01
                                    )
                                }
                            )

                            Text(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                text = "Email",
                                fontSize = 16.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C7C7C)
                            )

                            DefaultTextField(
                                text = email,
                                onValueChange = {
                                    email = it.replace("\\p{Zs}+".toRegex(), "")
                                    emailError = !email.matches(emailRegex)
                                },
                                textError = emailError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        firstNameError = TextUtils.isEmpty(firstName)
                                        if (email.matches(emailRegex)) {
                                            emailError = false
                                            focusManager.moveFocus(FocusDirection.Down)
                                        } else {
                                            emailError = true
                                        }
                                    }
                                ),
                                trailingIcon = {
                                    if (emailError) {
                                        Icon(
                                            imageVector = Icons.Outlined.ErrorOutline,
                                            contentDescription = "error",
                                            tint = Color.Red
                                        )
                                    } else if (email.matches(emailRegex)) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = "verified",
                                            tint = Green01
                                        )
                                    }
                                }
                            )

                            if (emailError) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "Enter a valid email",
                                    fontFamily = Poppins,
                                    color = Color.Red
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                text = "Password",
                                fontSize = 16.sp,
                                fontFamily = Poppins,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C7C7C)
                            )

                            DefaultTextField(
                                text = password,
                                onValueChange = {
                                    passwordError = false
                                    if (it.length <= 16) {
                                        password = it
                                    }
                                },
                                textError = passwordError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                trailingIcon = {
                                    IconButton(onClick = { showPassword = !showPassword }) {
                                        Icon(
                                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                            contentDescription = if (showPassword) "showPassword" else "hidePassword",
                                            tint = Color(0xFF7C7C7C)
                                        )
                                    }
                                },
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                            )
                            if (passwordError) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "Password must be 8-16 characters",
                                    fontFamily = Poppins,
                                    color = Color.Red
                                )
                            }

                            ClickableText(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = termsConditions,
                                onClick = {
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {

                                DefaultButton(
                                    modifier = Modifier
                                        .padding(top = 15.dp, bottom = 15.dp)
                                        .height(60.dp)
                                        .fillMaxWidth()
                                        .align(alignment = Alignment.BottomCenter),
                                    text = "Sign Up",
                                    onclick = {
                                        processState = true
                                        firstNameError = TextUtils.isEmpty(firstName)
                                        emailError = !email.matches(emailRegex)
                                        passwordError = password.length < 8

                                        if (!firstNameError && !emailError && !passwordError && isConnection) {

                                            if(profileUri.toString() != "") {
                                                val imageUri = Uri.parse(profileUri.toString())
                                                sellerProfileDatabase
                                                    .child(phoneNumber)
                                                    .child(
                                                        getFilename(
                                                            context,
                                                            imageUri
                                                        ).toString()
                                                    )
                                                    .putFile(imageUri)
                                                    .addOnSuccessListener {
                                                        sellerProfileDatabase
                                                            .child(phoneNumber)
                                                            .child(
                                                                getFilename(
                                                                    context,
                                                                    imageUri
                                                                ).toString()
                                                            )
                                                            .downloadUrl.addOnSuccessListener { uploadedUrl ->

                                                                profileUploadedUrl = uploadedUrl


                                                                if (profileUploadedUrl != "") {

                                                                    val userProfile = ProfileData(
                                                                        phoneNumber = phoneNumber,
                                                                        userId = userId ?: "",
                                                                        profilePhoto = profileUploadedUrl.toString(),
                                                                        userName = "$firstName $lastName",
                                                                        email = email,
                                                                        password = password
                                                                    )

                                                                    database
                                                                        .child(phoneNumber)
                                                                        .child("BasicInfo")
                                                                        .setValue(userProfile)
                                                                        .addOnCompleteListener { task ->

                                                                            processState = false

                                                                            if (task.isSuccessful) {


                                                                                sharedEdit.putBoolean(
                                                                                    "USERSIGNED",
                                                                                    true
                                                                                )
                                                                                sharedEdit.putString(
                                                                                    "EMAIL",
                                                                                    email
                                                                                )
                                                                                sharedEdit.putString(
                                                                                    "PHONENUMBER",
                                                                                    phoneNumber
                                                                                )
                                                                                sharedEdit.putString(
                                                                                    "USERNAME",
                                                                                    "$firstName $lastName"
                                                                                )
                                                                                sharedEdit.apply()

                                                                                Toast.makeText(
                                                                                    context,
                                                                                    "Successfully account created",
                                                                                    Toast.LENGTH_LONG
                                                                                ).show()

                                                                                if (userRole == "Sellers") {

                                                                                    val intent =
                                                                                        Intent(
                                                                                            context,
                                                                                            MainActivity::class.java
                                                                                        )
                                                                                    context.startActivity(
                                                                                        intent
                                                                                    )
                                                                                    context.finish()
                                                                                } else {

                                                                                    val intent =
                                                                                        Intent(
                                                                                            context,
                                                                                            BuyersActivity::class.java
                                                                                        )
                                                                                    context.startActivity(
                                                                                        intent
                                                                                    )
                                                                                    context.finish()
                                                                                }

                                                                            } else {
                                                                                Toast.makeText(
                                                                                    context,
                                                                                    "Failed to create",
                                                                                    Toast.LENGTH_LONG
                                                                                )
                                                                                    .show()
                                                                            }

                                                                        }

                                                                }
                                                            }
                                                    }
                                            } else {

                                                val userProfile = ProfileData(
                                                    phoneNumber = phoneNumber,
                                                    userId = userId ?: "",
                                                    profilePhoto = profileUploadedUrl.toString(),
                                                    userName = "$firstName $lastName",
                                                    email = email,
                                                    password = password
                                                )

                                                database
                                                    .child(phoneNumber)
                                                    .child("BasicInfo")
                                                    .setValue(userProfile)
                                                    .addOnCompleteListener { task ->

                                                        processState = false

                                                        if (task.isSuccessful) {

                                                            sharedEdit.putBoolean(
                                                                "USERSIGNED",
                                                                true
                                                            )
                                                            sharedEdit.putString(
                                                                "EMAIL",
                                                                email
                                                            )
                                                            sharedEdit.putString(
                                                                "PHONENUMBER",
                                                                phoneNumber
                                                            )
                                                            sharedEdit.putString(
                                                                "USERNAME",
                                                                "$firstName $lastName"
                                                            )
                                                            sharedEdit.apply()

                                                            Toast.makeText(
                                                                context,
                                                                "Successfully account created",
                                                                Toast.LENGTH_LONG
                                                            ).show()

                                                            if (userRole == "Sellers") {

                                                                val intent =
                                                                    Intent(
                                                                        context,
                                                                        MainActivity::class.java
                                                                    )
                                                                context.startActivity(
                                                                    intent
                                                                )
                                                                context.finish()
                                                            } else {

                                                                val intent =
                                                                    Intent(
                                                                        context,
                                                                        BuyersActivity::class.java
                                                                    )
                                                                context.startActivity(
                                                                    intent
                                                                )
                                                                context.finish()
                                                            }

                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Failed to create",
                                                                Toast.LENGTH_LONG
                                                            )
                                                                .show()
                                                        }

                                                    }

                                            }

                                        } else {
                                            processState = false
                                            Toast.makeText(
                                                context,
                                                "Check your Connection",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }


                                    })
                            }


                        }
                    }

                }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavHostController
) {
    TopAppBar(
        title = {
            Text(
                text = "Finish your Profile",
                style = mobileTitle
            )
    },
        navigationIcon = {
            DefaultBackArrow(
                onclick = {

                    navController.popBackStack()
                }
            )
        }
    )


}

