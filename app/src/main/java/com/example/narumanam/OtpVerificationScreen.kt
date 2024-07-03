package com.example.narumanam

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

var otpProcessState = false
@Composable
fun VerificationScreen(navController: NavHostController,
                       verificationId : String,
                       phoneNumber : String,
                       fromPage : String
) {


    val context = LocalContext.current
    val isConnection = networkConnection(context = context)
    var resendTime by remember { mutableIntStateOf(60) }
    var processState by remember{ mutableStateOf(otpProcessState) }
    var isError by remember{ mutableStateOf(false) }

    val resendText =
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color =  Green01 ,
                )
            ){
                    append("Resend Code")
            }
        }

    var OTP by remember { mutableStateOf("") }
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        Image(
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.verification_bg),
            contentDescription = "Verification Screen bg",
            contentScale = ContentScale.FillBounds
        )

        DefaultBackArrow(
            modifier = Modifier
                .padding(top = 6.dp, start = 4.dp)
                .align(alignment = Alignment.TopStart),
            onclick = { navController.popBackStack() })


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp, start = 20.dp, end = 30.dp)
        ) {

            Text(
                text = "Enter your 6-digit code",
                style = mobileTitle
            )
            Text(
                modifier = Modifier
                    .padding(top = 40.dp),
                text = "Code",
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C7C7C)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),

                value = OTP,

                onValueChange = {
                    isError = false
                    if (it.length <= 6 && it.isDigitsOnly()) {
                        OTP = it
                    }
                },

                textStyle = TextStyle().copy(fontFamily = Poppins, letterSpacing = 1.sp),

                colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedIndicatorColor = Color(0xFFE2E2E2),
                    errorContainerColor = Color(0xFFF9F9F9),
                    errorTextColor = Color.Red,
                    errorIndicatorColor = Color.Red,
                    errorPlaceholderColor = Color.Red,
                    focusedIndicatorColor = Color.Black
                ),
                placeholder = {
                    Text(
                        text = "- - - - - -",
                        fontFamily = Poppins,
                        color = Color(0xFF000000)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                isError = isError
            )
            if (isError){
                Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Enter a valid code",
                fontFamily = Poppins,
                color = Color.Red
                )
            }

        }

        if (processState) {
            ProgressIndicator()
        }

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            if(resendTime > 0) {
                val stringTime = resendTime.toString()
                Text(
                    text = "Resend($stringTime)",
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF7C7C7C),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            } else {

                ClickableText(
                    modifier = Modifier
                        .weight(1f),
                    text = resendText,
                    onClick = { offset ->

                        resendVerificationCode(
                            auth = auth,
                            mobileNo = phoneNumber,
                            context = context,
                            callBacks = Screens.callBacks,
                            timeUnit = if (processState) 30L else 0L
                        )
                        processState = true
                    }
                )
            }

            DefaultNextButton(

                onclick = {

                    if (isConnection) {
                        if (!TextUtils.isEmpty(OTP) && !TextUtils.isEmpty(verificationId)) {

                            processState = true
                            isError = false

                            val credential: PhoneAuthCredential =
                                PhoneAuthProvider.getCredential(verificationId, OTP)

                            auth.signInWithCredential(credential)
                                .addOnCompleteListener(context as Activity) { task ->

                                    if (task.isSuccessful) {

                                        if (fromPage == "LOGIN") {
                                            navController.navigate(
                                                Screens.UpdatePasswordScreen.route +
                                                        "/${phoneNumber}"
                                            )
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Verification successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate(
                                                Screens.signupScreen.route
                                                        + "/${phoneNumber}"
                                            )
                                        }

                                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(
                                            context, "Verification Failed." +
                                                    (task.exception is FirebaseAuthInvalidCredentialsException),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (task.exception is FirebaseAuthInvalidCredentialsException)
                                            isError = true
                                    }

                                    processState = false

                                }

                        } else {
                            isError = true
                        }
                    } else {
                        Toast.makeText(
                            context,"Check your Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                modifier = Modifier
                    .size(67.dp)
            )
        }

        LaunchedEffect(key1 = resendTime  ){
            while (resendTime>0){
                delay(1000)
                resendTime --
            }
        }


    }
}


fun resendVerificationCode(
    auth: FirebaseAuth,
    mobileNo : String,
    context : Context,
    callBacks : OnVerificationStateChangedCallbacks,
    timeUnit: Long
){

    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(mobileNo)
        .setTimeout(timeUnit,TimeUnit.SECONDS)
        .setActivity(context as Activity)
        .setCallbacks(callBacks)
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)

}