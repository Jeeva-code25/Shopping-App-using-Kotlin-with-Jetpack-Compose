package com.example.narumanam

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.narumanam.ui.theme.Poppins
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DecisionScreen(navController : NavController) {
    val context = LocalContext.current
    val isConnection = networkConnection(context = context)

    rememberSystemUiController().isStatusBarVisible= false
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME,Context.MODE_PRIVATE)
    val sharedEdit = sharedPreference.edit()


   Box(modifier = Modifier
       .fillMaxSize(),
       contentAlignment = Alignment.BottomCenter
   ) {
       Image(
           modifier = Modifier
               .fillMaxSize(),
           painter = painterResource(R.drawable.decision_onboarding_bg),
           contentDescription = "OnBoarding Background",
           contentScale = ContentScale.FillBounds
       )
       Column(
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Icon(
               modifier = Modifier
                   .width(49.dp)
                   .height(66.dp),
               imageVector = ImageVector.vectorResource(R.drawable.narumanam_logo),
               contentDescription = "AppLogo",
               tint = Color.Unspecified
           )
           Text(
               text = "Welcome to our store",
               fontFamily = Poppins,
               fontWeight = FontWeight.Bold,
               fontStyle = FontStyle.Italic,
               color = Color.White,
               fontSize = 28.sp

           )
           Text(
               text = "Start your journey as,",
               fontFamily = Poppins,
               color = Color.White,
               fontSize = 13.sp
           )

           Spacer(modifier = Modifier.height(30.dp))

           // User select their role at when first time
          DefaultButton(
              onclick = {
                  if (isConnection) {
                      sharedEdit.putString("USERROLE", "Buyers")
                      sharedEdit.apply()
                      navController.navigate(Screens.mobileScreen.route)
                  } else {
                      Toast.makeText(context,"Check your Connection", Toast.LENGTH_SHORT).show()
                  }
              },
              text = "Buyer"
          )
           Spacer(modifier = Modifier.height(17.dp))

           DefaultButton(
               onclick = {
                   if(isConnection) {
                       sharedEdit.putString("USERROLE", "Sellers")
                       sharedEdit.apply()
                       navController.navigate(Screens.mobileScreen.route)
                   } else {
                       Toast.makeText(context,"Check your Connection",Toast.LENGTH_SHORT).show()
                   }
               }
           )

           Spacer(modifier = Modifier.height(60.dp))
       }

   }
}
