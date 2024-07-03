package com.example.narumanam.buyersidepages

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.narumanam.buyersidepages.navigation.BuyerBottomNavScreens
import com.example.narumanam.buyersidepages.navigation.BuyerNavGraph
import com.example.narumanam.networkConnection
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import kotlinx.coroutines.delay

class BuyersActivity : ComponentActivity() {
    private lateinit var navController : NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            BuyersMainScreen(navController = navController)
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BuyersMainScreen(navController: NavHostController) {

    val context = LocalContext.current
    var showBottomBar by remember { mutableStateOf(true) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute  = navBackStackEntry?.destination?.route

    var isVisible by remember { mutableStateOf(false) }

    if (isVisible && networkConnection(context = context)) {
        LaunchedEffect(isVisible && networkConnection(context = context)) {
            delay(3000)
            isVisible = false
        }
    }

    val alpha: Float by animateFloatAsState(
        targetValue = 1f ,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing), label = "tween"
    )

    showBottomBar = when (currentRoute){

        BuyerBottomNavScreens.Home.route -> true
        BuyerBottomNavScreens.Cart.route -> true
        BuyerBottomNavScreens.Favourite.route -> true
        BuyerBottomNavScreens.Account.route -> true
        else -> false
    }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding())
            ) {
                BuyerNavGraph(navController = navController)
            }
            if (isVisible && networkConnection(context = context)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .background(color = Green01)
                        .alpha(alpha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Back Online",
                        color = Color.White,
                        fontFamily = Poppins
                    )
                }
            }
            if (!networkConnection(context = context)) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .background(color = Color.Black)
                        .alpha(alpha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =  "No Connection",
                        color = Color.White,
                        fontFamily = Poppins
                    )
                }
                isVisible = true

            }

        }
    )

}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BuyerBottomNavScreens.Home,
        BuyerBottomNavScreens.Cart,
        BuyerBottomNavScreens.Favourite,
        BuyerBottomNavScreens.Account
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(modifier = Modifier
        .height(66.dp)
        .clip(shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
        .shadow(
            20.dp, shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
            clip = false, ambientColor = Color.Black, spotColor = Color.Black,
        ),
        containerColor = Color.White

    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BuyerBottomNavScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {


    NavigationBarItem(
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
                modifier = Modifier.height(20.dp),
            )
        },

        label = {
            Text(
                text = screen.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 9.sp,
            )
        },

        selected = currentDestination?.hierarchy?.any{
            it.route == screen.route
        } == true,
        onClick ={
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = androidx.compose.material3.NavigationBarItemDefaults
            .colors(
                selectedTextColor = Green01,
                selectedIconColor = Green01,
                indicatorColor = Color.White )


    )


}
