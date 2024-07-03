package com.example.narumanam.buyersidepages

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ProfileData
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.SplashActivity
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.AddressExist
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


var ProfileInfo : ProfileData = ProfileData()
@Composable
fun Account(
    navController: NavHostController
){

    val showDialog = remember { mutableStateOf(false) }
    val processState = remember { mutableStateOf(true) }

    val context = LocalContext.current as Activity
    val isConnection = networkConnection(context = context)
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val userSigned = sharedPreference.getBoolean("USERSIGNED",false)
    val sharedEdit = sharedPreference.edit()

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("Buyers")


    database
        .child(phoneNumber ?: "")
        .child("BasicInfo")
        .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    ProfileInfo = snapshot.getValue(ProfileData :: class.java)!!
                    processState.value = false

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Database Error$error",Toast.LENGTH_SHORT).show()
            }

        }
        )


    val iconsList : List<ImageVector> = listOf(
        ImageVector.vectorResource(R.drawable.orders_icon),
        ImageVector.vectorResource(R.drawable.my_details_icon),
        Icons.Outlined.LocationOn,
//        Icons.Outlined.Notifications,
        Icons.Outlined.HelpOutline,
        Icons.Outlined.Info
    )

    val screenList : List<String> = listOf(
        "My Orders",
        "My Details",
        "Address",
        "Help",
        "About"
    )

    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(color = Color.White)
            .fillMaxSize()
            .padding(12.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        if (processState.value){
            ProgressIndicator()
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {  },
                icon = {
                    Icon(imageVector = Icons.Outlined.Logout, contentDescription = "logout")
                },
                title = {
                    Text(
                        text = "Remove account from this device",
                        fontSize = 13.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                    )
                },
                text = {
                    Text(
                        text = "Account details will be removed and Don't forget your credential",
                        fontSize = 13.sp,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Justify,
                    )

                },
                confirmButton = {
                    TextButton(
                        onClick = {

                            if (userSigned) {
                                auth.signOut()
                                sharedEdit.clear().apply()
                                context.finish()
                                val intent = Intent(context, SplashActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "User not signed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            showDialog.value = false
                        })
                    {

                        Text(
                            text = "Logout",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                        )
                    }
                },
                dismissButton = {

                    TextButton(onClick = {
                        showDialog.value = false
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

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ){

            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(
                    model = if(ProfileInfo.profilePhoto == "")R.drawable.profile else ProfileInfo.profilePhoto,
                    contentScale = ContentScale.FillBounds
                ),
                contentDescription = "profile",
                contentScale = ContentScale.FillBounds
            )
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){

                Text(
                    text = if (ProfileInfo.userName == "") "UnKnown" else ProfileInfo.userName,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                )

                Text(text = if (ProfileInfo.phoneNumber == "") "+1 2345673210" else ProfileInfo.phoneNumber,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7C7C7C)
                )
            }

        }

        DividerHorizontal(modifier = Modifier.padding(top = 15.dp))

        for (index in iconsList.indices){

            AccountItems(icon = iconsList[index], text = screenList[index], navController = navController)
            DividerHorizontal()
        }


        Spacer(modifier = Modifier.height(50.dp))
        Button(
            modifier = Modifier
                .width(365.dp)
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {

                showDialog.value = true

            },
            colors = ButtonDefaults.buttonColors(Color(0xFFF2F3F2),
                contentColor = Green01,
            )
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    modifier = Modifier
                        .width(22.dp)
                        .height(22.dp)
                        .align(Alignment.CenterStart),
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = "Logout",)

                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Logout",
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,)
            }


        }
    }
}

@Composable
private fun AccountItems(
    icon : ImageVector,
    text : String,
    navController : NavHostController
) {

    val isConnection = networkConnection(context = LocalContext.current)

    Row (
        modifier = Modifier
            .clickable {
                if (isConnection) {
                    when (text) {
                        "My Orders" -> navController.navigate(Screens.MyOrdersScreen.route)
                        "My Details" -> navController.navigate(Screens.BuyerMyDetailsScreen.route)
                        "Address" -> navController.navigate(Screens.AddressScreen.route + "/${"Account"}")
//                    "Notification"-> navController.navigate(Screens.NotificationScreen.route)
                        "Help" -> navController.navigate(Screens.Help.route)
                        "About" -> navController.navigate(Screens.About.route)
                    }
                }
            }
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(imageVector = icon, contentDescription = "")
            Spacer(modifier = Modifier.width(40.dp))
            Text(text = text, fontFamily = Poppins, fontSize = 15.sp)

        }
        Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "Right")
    }
}
