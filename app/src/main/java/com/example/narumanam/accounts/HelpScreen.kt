package com.example.narumanam.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DividerHorizontal
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle

@Composable
fun HelpScreen(
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
                                text = "Contact Us for Support",
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
                                Column(
                                    modifier = Modifier.background(Color.Transparent),
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            ) {
                                                append("Support Mobile: ")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    color = Color(0xFF7C7C7C)
                                                )
                                            ) {
                                                append("+91 8838357996")
                                            }
                                        },
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            ) {
                                                append("Support E-mail: ")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 15.sp,
                                                    fontFamily = Poppins,
                                                    color = Color(0xFF7C7C7C)
                                                )
                                            ) {
                                                append("narumanam24@gmail.com")
                                            }
                                        },
                                    )
                                }
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
                text = "Help",
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