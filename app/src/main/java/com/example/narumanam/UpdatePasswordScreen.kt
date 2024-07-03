package com.example.narumanam

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.database.FirebaseDatabase

@Composable
fun UpdatePasswordScreen(
   navController : NavHostController,
   phone : String
) {

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val userRole = sharedPreference.getString("USERROLE","")
    val database = FirebaseDatabase.getInstance().getReference(userRole.toString())

    var newPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }

    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var processState by remember{ mutableStateOf(false) }




    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
    ) {

        Image(
            modifier = Modifier
                .height(220.dp)
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.verification_bg),
            contentDescription = "Screen bg",
            contentScale = ContentScale.FillBounds
        )

        DefaultBackArrow(
            modifier = Modifier
                .padding(top = 6.dp, start = 4.dp)
                .align(alignment = Alignment.TopStart),
            onclick = { navController.popBackStack() }
        )
        Icon(
            modifier = Modifier
                .padding(top = 75.dp)
                .align(Alignment.TopCenter),
            imageVector = ImageVector.vectorResource(id = R.drawable.app_logo_color),
            tint = Color.Unspecified,
            contentDescription = "app_logo"
        )

        Image(
            modifier = Modifier
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.signup_login_bottom_bg),
            contentDescription = "Bottom bg",
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 160.dp, start = 20.dp, end = 20.dp)
        ) {

            Text(
                text = "Update Password",
                style = mobileTitle
            )
            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = "Change your  password",
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF7C7C7C)
            )

            if (processState) {
                ProgressIndicator()
            }
            Text(
                modifier = Modifier
                    .padding(top = 25.dp),
                text = "New Password",
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C7C7C)
            )
            DefaultTextField(
                text = newPassword,
                onValueChange = {
                    newPasswordError = false
                    if (it.length <= 16) {
                        newPassword = it
                    }
                },
                textError = newPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {


                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                        Icon(
                            imageVector = if (showNewPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showNewPassword) "showNewPassword" else "hidePassword",
                            tint = Color(0xFF7C7C7C)
                        )
                    }
                },
                visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation()
            )


            if (newPasswordError) {
                Text(
                    text = "Field is Empty",
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins,
                    color = Color.Red
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 25.dp),
                text = "Confirm Password",
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C7C7C)
            )
            DefaultTextField(
                text = confirmPassword,
                onValueChange = {
                    confirmPasswordError = false
                    if (it.length <= 16) {
                        confirmPassword = it
                    }
                },
                textError = confirmPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {

                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showConfirmPassword) "showNewPassword" else "hidePassword",
                            tint = Color(0xFF7C7C7C)
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation()
            )


                if (confirmPasswordError) {
                    Text(
                        text = if(!TextUtils.isEmpty(newPassword)) "Password doesn't matched" else "Field is Empty or must be in 8 characters",
                        textAlign = TextAlign.Start,
                        fontFamily = Poppins,
                        color = Color.Red
                    )
                }


        }
        DefaultButton(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(300.dp)
                .height((58.dp))
                .align(alignment = Alignment.BottomCenter),

            text = "Change Password", onclick = {

                processState = true
                newPasswordError = newPassword.length < 8
                confirmPasswordError = TextUtils.isEmpty(confirmPassword)

                if (newPassword == confirmPassword && !newPasswordError && !confirmPasswordError && isConnection){

                    database
                        .child(phone)
                        .child("BasicInfo")
                        .child("password")
                        .setValue(confirmPassword)
                        .addOnSuccessListener {
                            processState = false
                            Toast.makeText(context,"Password has been changed successfully",Toast.LENGTH_LONG).show()
                            navController.popBackStack(
                                route = Screens.decisionScreen.route,
                                inclusive = false
                            )
                        }

                } else {
                    processState = false
                    confirmPasswordError = true
                }

            })

    }
}
