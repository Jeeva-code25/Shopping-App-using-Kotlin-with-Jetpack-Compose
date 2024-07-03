package com.example.narumanam

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit


@Composable
fun MobileNumberScreen(navController : NavHostController) {

    rememberSystemUiController().isStatusBarVisible= true

    val context = LocalContext.current
    val isConnection = networkConnection(context = context)

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME,Context.MODE_PRIVATE)
    val userRole = sharedPreference.getString("USERROLE","")
    val database = FirebaseDatabase.getInstance().getReference(userRole.toString())


    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var callBacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    var countryCode by  remember {
        mutableStateOf("91")
    }

    var mobileNo by  remember {
        mutableStateOf("")
    }

    var phoneNumberFormat by remember { mutableStateOf("") }

    var message by remember {
        mutableStateOf("")
    }

    var processState by remember{ mutableStateOf(false) }

    var isErrorMobile by remember{mutableStateOf(false)}


    val verificationID = remember {
        mutableStateOf("")
    }



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
            navController.navigate(Screens.OtpVerificationScreen.route+"/${verificationID.value}" +
                    "/${phoneNumberFormat}/${"MOBILE"}")

        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .height(300.dp),
            painter = painterResource(id = R.drawable.mobilenumber_bg),
            contentDescription = "Mobile Number Screen Bg",
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.FillBounds
        )


        Text(
            modifier = Modifier
                .weight(0.5f)
                .width(280.dp)
                .padding(start = 30.dp, top = 20.dp),
            text = "Get your Products with Narumanam",
            style = mobileTitle
        )
        if (processState) {
            ProgressIndicator()
        }

        Row (modifier = Modifier
            .weight(1f)
            .padding(30.dp),
            verticalAlignment = Alignment.Top){

            TextField(
                modifier = Modifier
                    .weight(1f)
                ,
                value = countryCode,

                onValueChange = {
                    isErrorMobile = false
                    if(it.length <= 3 && it.isDigitsOnly()){
                        countryCode = it
                    }
                    },

               textStyle = TextStyle().copy(fontFamily = Poppins,letterSpacing = 1.sp),

                colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor =  Color(0xFFF9F9F9),
                    unfocusedIndicatorColor = Color(0xFFE2E2E2),
                    errorContainerColor = Color(0xFFF9F9F9),
                    errorTextColor = Color.Red,
                    errorIndicatorColor = Color.Red,
                    focusedIndicatorColor = Color.Black
                ),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                isError = isErrorMobile

            )

            TextField(
                modifier = Modifier
                    .weight(4f)
                    .padding(start = 10.dp),

                value = mobileNo,

                onValueChange = {
                    isErrorMobile =false
                    if (it.length <= 10 && it.isDigitsOnly()) {
                        mobileNo = it
                    }
                },

                textStyle = TextStyle().copy(fontFamily = Poppins, letterSpacing = 1.sp),

                colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor =  Color(0xFFF9F9F9),
                    unfocusedIndicatorColor = Color(0xFFE2E2E2),
                    errorContainerColor = Color(0xFFF9F9F9),
                    errorTextColor = Color.Red,
                    errorIndicatorColor = Color.Red,
                    errorPlaceholderColor = Color.Red,
                    focusedIndicatorColor = Color.Black
                    ),
                placeholder = {
                    Text(
                        text = "Enter your mobile number",
                        fontFamily = Poppins,
                        fontSize = 13.sp
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                isError = isErrorMobile,

            )
        }

        DefaultNextButton(
            onclick = {
                if (isConnection) {
                    if ((!TextUtils.isEmpty(mobileNo) && !TextUtils.isEmpty(countryCode)) && mobileNo.length == 10) {
                        isErrorMobile = false
                        processState = true
                        phoneNumberFormat = "+$countryCode$mobileNo"


                        database.child(phoneNumberFormat).child("BasicInfo")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    if (snapshot.exists()) {
                                        processState = false
                                        val userProfile = snapshot.getValue(ProfileData::class.java)

                                        navController.navigate(
                                            Screens.loginScreen.route +
                                                    "/${userProfile?.phoneNumber.toString()}" +
                                                    "/${userProfile?.password.toString()}" +
                                                    "/${userProfile?.email.toString()}" +
                                                    "/${userProfile?.userName.toString()}"
                                        )

                                    } else {
                                        sendVerificationCode(
                                            auth = auth,
                                            mobileNo = phoneNumberFormat,
                                            context = context,
                                            callBacks = callBacks,
                                            timeUnit = if (processState) 30L else 0L
                                        )
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    processState = false
                                    Toast.makeText(
                                        context,
                                        "DatabaseError " + error.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            })

                    } else {
                        isErrorMobile = true
                        Toast.makeText(context, "Enter valid mobile number", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Check your Connection", Toast.LENGTH_LONG)
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .size(67.dp)
                .align(alignment = Alignment.End)
        )

    }

}



fun sendVerificationCode(
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

