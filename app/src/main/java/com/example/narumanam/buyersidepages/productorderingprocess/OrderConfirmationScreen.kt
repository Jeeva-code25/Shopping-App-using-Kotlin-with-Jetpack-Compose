package com.example.narumanam.buyersidepages.productorderingprocess

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.narumanam.DefaultButton
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.buyersidepages.BuyerScreens
import com.example.narumanam.buyersidepages.ORDERED_ITEMS
import com.example.narumanam.buyersidepages.OrderPlacingProductModel
import com.example.narumanam.buyersidepages.navigation.BuyerBottomNavScreens
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import kotlinx.coroutines.delay

@Composable
fun OrderConfirmationScreen(
    navController : NavHostController
) {

    var startAnimation by remember{ mutableStateOf(false) }
    val animAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        ), label = "splash_animation"
    )


    LaunchedEffect(key1 = true ) {
        startAnimation = true

    }
        Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ){
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.TopCenter),
            painter = painterResource(id = R.drawable.confirmationbackground),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.signup_login_bottom_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .background(color = Color.Transparent)
                .padding(top = 30.dp, start = 20.dp,end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                modifier = Modifier
                    .width(250.dp)
                    .height(220.dp)
                    .alpha(animAlpha.value),
                painter = painterResource(id = R.drawable.productconfirmation),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Your Order has been placed",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Your items has been placed and is on \n" +
                        "it’s way to being processed",
                fontFamily = Poppins,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF7C7C7C)
            )
        }


    }
    Spacer(modifier = Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.Bottom,
    ) {

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onclick = {


                      navController.navigate(BuyerBottomNavScreens.Home.route){
                          popUpTo(navController.graph.id){
                              inclusive = true
                          }
                      }
            },
            text = "Back to Home",
        )
    }
}

