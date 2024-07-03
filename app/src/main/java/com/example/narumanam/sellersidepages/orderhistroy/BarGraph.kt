package com.example.narumanam.sellersidepages.orderhistroy

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.narumanam.ui.theme.Poppins
import kotlin.math.round

@Composable
fun BarGraph(
    graphBarData : List<Float>,
    xAxisScaleData : List<String>,
    barDataFunc : List<Int>,
    height : Dp,
    roundType : BarType,
    barWidth : Dp,
    barColor : Color,
    barArrangement : Arrangement.Horizontal
) {

    val barData by remember { mutableStateOf(barDataFunc+0) }

    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp

    val xAxisScaleHeight = 40.dp

    val yAxisScalePadding by remember { mutableFloatStateOf(100f) }
    val yAxisTextWidth by remember { mutableStateOf(100.dp) }

    val barShape =
        when(roundType){
            BarType.CIRCULAR_TYPE -> CircleShape
            BarType.TOP_CURVED -> RoundedCornerShape(topStart = 3.dp,topEnd = 3.dp)
        }

    val density = LocalDensity.current

    val textPaint = remember(density){

        android.graphics.Paint().apply {
            color = Color.Black.hashCode()
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    val yCoordinates = mutableListOf<Float>()

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)

    val lineHeightAxis = 10.dp
    val horizontalLineHeight = 5.dp

    Box(
        modifier= Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {

        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Canvas(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxSize(),
                ){
                val yAxisScaleText = (barData.max()) / 3f
                (0..3).forEach {i->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            round( barData.min()+yAxisScaleText*i).toString(),
                            30f,
                            size.height - yAxisScalePadding-i*size.height/3f,
                            textPaint
                        )
                    }
                    yCoordinates.add(size.height - yAxisScalePadding-i*size.height/3f)
                }

                (1..3).forEach {
                    drawLine(
                        start = Offset(x= yAxisScalePadding+30f, y = yCoordinates[it]),
                        end = Offset(x = size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 5f,
                        pathEffect = pathEffect
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                modifier  = Modifier
                    .width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {

                graphBarData.forEachIndexed { index, value ->
                    var  animationTriggered by remember { mutableStateOf(false) }

                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            delayMillis = 0
                        ), label = ""
                    )
                    LaunchedEffect(key1 = true){
                        animationTriggered = true
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Top,
                        horizontalAlignment = CenterHorizontally
                    ){

                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(barShape)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = BottomCenter
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(barShape)
                                    .fillMaxWidth()
                                    .fillMaxHeight(graphBarHeight)
                                    .background(barColor)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .height(xAxisScaleHeight),
                            verticalArrangement = Top,
                            horizontalAlignment = CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp
                                        )
                                    )
                                    .width(horizontalLineHeight)
                                    .height(lineHeightAxis)
                                    .background(Color.Gray)
                            )
                            
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = xAxisScaleData[index].toString(),
                                fontSize = 10.sp,
                                fontFamily = Poppins,
                                color  = Color.Black
                            )
                        }
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = CenterHorizontally
            ) {

                Box(modifier = Modifier
                    .padding(bottom = 3.dp + xAxisScaleHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .fillMaxWidth()
                    .height(horizontalLineHeight)
                    .background(Color.Gray)
                )
            }
        }
    }

}