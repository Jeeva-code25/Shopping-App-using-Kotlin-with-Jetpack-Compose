package com.example.narumanam.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle

@Composable
fun AboutScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
        },
        content = { it ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding()
                    )
                    .background(color = Color.White),
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                ) {
                    item {
                        DividerHorizontal()
                    }
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                text = "About Us",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                color = Green01,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp

                            )
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .background(color = Color(0XFFBFBFBF)),
                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(color = Color(0XFFC1BFBF))
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    text = "NARUMANAM is an online trading platform designed for buying and selling various products. It's provided by Annamalai University Chidambaram.\n" +
                                            "\n" +
                                            "Users on this platform can find products uploaded by both tribal and common people, ensuring a diverse range of offerings including fresh, locally cultivated items.\n" +
                                            "\n" +
                                            "With the NARUMANAM app, you can easily browse products and place orders, with delivery handled by third-party courier services.\n" +
                                            "\n" +
                                            "Our primary aim is to offer a global marketplace for all products, connecting customers directly with farmers. This direct connection means cost savings compared to purchasing from traditional local markets.\n" +
                                            "\n" +
                                            "The NARUMANAM app will be accessible for Android devices.",
                                    fontFamily = Poppins,
                                    fontSize = 18.sp
                                )
                            }


                        }
                    }

                }

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavHostController
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = Color.White),
        title = {
            Text(
                text = "About Us",
                style = mobileTitle
            )
        },
        navigationIcon = {
            DefaultBackArrow {
                navController.popBackStack()
            }
        }
    )

}