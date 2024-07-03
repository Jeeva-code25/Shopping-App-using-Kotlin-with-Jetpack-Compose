package com.example.narumanam

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.narumanam.buyersidepages.BuyersActivity
import com.example.narumanam.sellersidepages.MainActivity
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private lateinit var navController : NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val activity = LocalContext.current as Activity

            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            navController = rememberNavController()

            // Start NavHost to Handle All Pages
            InitialNavGraph(navController = navController)


        }
    }
}

@Composable
fun AnimatedSplashScreen(navController : NavHostController) {

    val context = LocalContext.current as Activity
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val userSigned = sharedPreference.getBoolean("USERSIGNED", false)
    val userRole = sharedPreference.getString("USERROLE", "")

    var startAnimation by remember{ mutableStateOf(false) }
    val animAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        ), label = "splash_animation"
    )



    LaunchedEffect(key1 = true ) {
        startAnimation = true
        delay(4000)


        if (userSigned){

            if (userRole == "Sellers"){
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                context.finish()

            } else {
                val intent = Intent(context, BuyersActivity::class.java)
                context.startActivity(intent)
                context.finish()

            }
        } else {
            navController.popBackStack()
            navController.navigate(Screens.decisionScreen.route)
        }

    }
    SplashScreen(alpha = animAlpha.value)


}

@Composable
fun SplashScreen(alpha : Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Green01)
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .alpha(alpha = alpha),
            verticalAlignment = Alignment.Bottom
        ) {

            Image(
                modifier = Modifier
                    .width(54.dp)
                    .height(100.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.narumanam_logo),
                contentDescription = "AppLogo",

            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Narumanam",
                    fontSize = 30.sp,
                    color = Color(0xFFFFFFFF),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.17.sp,
                )

                Text(
                    text = "Live with nature,",
                    fontSize = 16.sp,
                    color = Color(0xFFFFFFFF),
                    fontFamily = Poppins,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.17.sp,
                )
            }
        }
    }


}