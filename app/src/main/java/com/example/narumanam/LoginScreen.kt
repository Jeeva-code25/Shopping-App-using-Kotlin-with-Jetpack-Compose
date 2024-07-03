package com.example.narumanam

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.narumanam.buyersidepages.BuyersActivity
import com.example.narumanam.sellersidepages.MainActivity
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase

@Composable
fun LoginScreen(navController: NavHostController,
                phoneNumber: String,
                signupPassword: String,
                email: String,
                userName : String) {
    val context = LocalContext.current as Activity
    val isConnection = networkConnection(context = context)

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val sharedEdit = sharedPreference.edit()
    val userRole = sharedPreference.getString("USERROLE","")
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var callBacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    var message by remember {
        mutableStateOf("")
    }

    var processState by remember{ mutableStateOf(false) }


    val verificationID = remember {
        mutableStateOf("")
    }

    var loginPassword by remember { mutableStateOf("") }
    var loginPasswordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    callBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            otpProcessState = false
            processState = false
            message = "Verification Successful"
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            otpProcessState = false
            processState = false
            message = p0.message.toString()
            Toast.makeText(context,"Verification Failed $message",Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(verificationid: String, resendingtoken: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationid, resendingtoken)
            otpProcessState = false
            processState = false
            Toast.makeText(context,"OTP has been sent",Toast.LENGTH_SHORT).show()
            verificationID.value = verificationid
            Screens.callBacks = callBacks
            navController.navigate(Screens.OtpVerificationScreen.route+"/${verificationID.value}/${phoneNumber}/${"LOGIN"}")

        }
    }


    val forgotPassword = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontFamily = Poppins,
                color = Color.Black,
            )
        ){
            append("Forgot Password?")
        }
    }




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
                text = "Log In",
                style = mobileTitle
            )
            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = "Enter your mobile and password",
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
                text = "Mobile",
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C7C7C)
            )

            DefaultTextField(
                text = phoneNumber,
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
                    .padding(top = 25.dp),
                text = "Password",
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C7C7C)
            )
            DefaultTextField(
                text = loginPassword,
                onValueChange = {
                    loginPasswordError = false
                    if (it.length <= 16) {
                        loginPassword = it
                    }
                },
                textError = loginPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        processState = true
                        loginPasswordError =  loginPassword.length < 8

                        if (!loginPasswordError && (signupPassword == loginPassword)) {
                            processState = false

                            Toast.makeText(context,"Login success",Toast.LENGTH_SHORT).show()

                            sharedEdit.putBoolean("USERSIGNED", true)
                            sharedEdit.putString("EMAIL",email)
                            sharedEdit.putString("PHONENUMBER",phoneNumber)
                            sharedEdit.putString("USERNAME", userName)
                            sharedEdit.apply()

                            if (userRole == "Sellers") {

                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                context.finish()

                            } else {

                                val intent = Intent(context, BuyersActivity::class.java)
                                context.startActivity(intent)
                                context.finish()
                            }

                        } else {
                            processState = false
                            loginPasswordError = true
                            Toast.makeText(context,"Failed to Login",Toast.LENGTH_SHORT).show()
                        }

                    }
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
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
                horizontalArrangement = Arrangement.End) {

                if (loginPasswordError) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = "Password doesn't matched",
                        textAlign = TextAlign.Start,
                        fontFamily = Poppins,
                        color = Color.Red
                    )
                }

                ClickableText(
                    text = forgotPassword,
                    onClick = {
                        processState = true
                        sendVerificationCode(
                            auth = auth,
                            mobileNo = phoneNumber,
                            context = context,
                            callBacks = callBacks,
                            timeUnit = 30L
                        )

                    },
                )

            }




        }
        DefaultButton(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(300.dp)
                .height((58.dp))
                .align(alignment = Alignment.BottomCenter),

            text = "Log In", onclick = {
                if (isConnection) {
                    processState = true
                    loginPasswordError = loginPassword.length < 8

                    if (!loginPasswordError && (signupPassword == loginPassword)) {
                        processState = false
                        Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()

                        sharedEdit.putBoolean("USERSIGNED", true)
                        sharedEdit.putString("EMAIL", email)
                        sharedEdit.putString("PHONENUMBER", phoneNumber)
                        sharedEdit.putString("USERNAME", userName)
                        sharedEdit.apply()

                        if (userRole == "Sellers") {

                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            context.finish()


                        } else {

                            val intent = Intent(context, BuyersActivity::class.java)
                            context.startActivity(intent)
                            context.finish()

                        }

                    } else {
                        processState = false
                        Toast.makeText(context, "Failed to Login", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Check your Connection", Toast.LENGTH_SHORT).show()
                }

            })

    }
}