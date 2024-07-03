package com.example.narumanam

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins

@Composable
fun DefaultButton(
    onclick : () -> Unit,
    modifier : Modifier = Modifier
        .width(300.dp)
        .height((58.dp)),
    shape: RoundedCornerShape = RoundedCornerShape(19.dp),
    color : ButtonColors = ButtonDefaults.buttonColors(Green01),
    fontFamily: FontFamily = Poppins,
    fontSize: TextUnit = 21.sp,
    text : String = "Seller",
    textColor : Color = Color.White
    ) {
    Button(
        onClick = {
            onclick()
        },
        modifier = modifier,
        shape = shape,
        colors = color
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun DefaultNextButton(
    modifier: Modifier = Modifier
        .padding(30.dp)
        .size(67.dp),
        onclick: () -> Unit,
        shape: RoundedCornerShape = CircleShape,
        color: ButtonColors = ButtonDefaults.buttonColors(Green01),
        icon : ImageVector = ImageVector.vectorResource(R.drawable.next_arrow),
        iconDescription : String = "Button Icon",
        iconTint : Color = Color.White

) {
    Button(
        onClick = {
                onclick()
        },
        shape = shape,
        modifier = modifier,
        colors = color,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            tint = iconTint
        )
    }
}

@Composable
fun DefaultBackArrow(
    modifier : Modifier = Modifier,
    icon : ImageVector =  ImageVector.vectorResource(id = R.drawable.back_arrow),
    onclick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onclick()
        }
    ) {
        Icon(
            imageVector =icon,
            contentDescription = "Back Icon"
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressIndicator() {

        Box(
            modifier = Modifier
                .background(color = Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            AlertDialog(onDismissRequest = {  }, properties = DialogProperties(
                dismissOnBackPress = true
            )) {

                Column(
                    modifier = Modifier
                        .padding(start = 42.dp, end = 42.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(top = 36.dp, bottom = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val infiniteTransition = rememberInfiniteTransition(label = "infiniteAnimation")
                    val angel by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 600
                            }
                        ), label = "animation"
                    )

                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .rotate(angel)
                            .border(
                                12.dp,
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color.White,
                                        Green01.copy(alpha = 0.1f),
                                        Green01
                                    )
                                ),
                                shape = CircleShape
                            ),
                        strokeWidth = 1.dp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Please wait...",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }

}

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    text: String,
    onValueChange: (String) -> Unit,
    textError: Boolean = false,
    textStyle: TextStyle = TextStyle().copy(fontFamily = Poppins, letterSpacing = 1.sp),
    colors : TextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White,
        unfocusedIndicatorColor = Color(0xFFE2E2E2),
        errorContainerColor = Color.White,
        errorTextColor = Color.Red,
        errorIndicatorColor = Color.Red,
        focusedPlaceholderColor =  Color(0xFF7C7C7C),
        unfocusedPlaceholderColor = Color(0xFF7C7C7C),
        errorPlaceholderColor = Color.Red,
        focusedIndicatorColor = Green01
    ),
    readOnly : Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = {}
    ),
    trailingIcon: @Composable () -> Unit = {
        if (textError) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "error",
                tint = Color.Red
            )
        }
    },
    visualTransformation: VisualTransformation = VisualTransformation
        .None,
    placeholderText: String = ""

) {

    TextField(
        modifier = modifier,

        value = text,

        onValueChange = {
            onValueChange(it)
        },

        textStyle = textStyle,

        colors = colors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        readOnly = readOnly,
        isError = textError,
        trailingIcon =  {trailingIcon()},
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = placeholderText ,
                fontFamily = Poppins,
                fontSize =  13.sp
            )
        }
    )

}

@Composable
fun DefaultDropDown(
    modifier : Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    dropDownModifier : Modifier,
    selectedText : String,
    onValueChange: (String) -> Unit,
    placeholderText:String = "",
    expanded : Boolean,
    trailingIcon: @Composable () -> Unit,
    textError: Boolean,
    onDismiss : ()->Unit,
    dropDownItems: List<String>,
    dropDownOnclick: (String)->Unit,
    ) {

    Column(
        modifier = Modifier
            .padding(top =10.dp)
    ) {

        OutlinedTextField(
            value = selectedText,
            onValueChange = {onValueChange(it)},
            modifier = modifier,
            shape = shape,
            textStyle = TextStyle().copy(fontFamily = Poppins, fontSize = 13.sp, letterSpacing = 1.sp),
            placeholder = {
                Text(
                    text = placeholderText ,
                    fontFamily = Poppins,
                    fontSize = 13.sp
                )
            },
            trailingIcon = { trailingIcon() },
            isError = textError,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                errorIndicatorColor = Color.Red,
                focusedPlaceholderColor =  Color(0xFF7C7C7C),
                unfocusedPlaceholderColor = Color(0xFF7C7C7C),
                unfocusedIndicatorColor = Color(0xFFB3B3B3),
                focusedIndicatorColor = Green01,
            )

        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
            modifier =dropDownModifier,
            ) {

            dropDownItems.forEach {item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        dropDownOnclick(item)
                    })
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultSearchBar(
modifier: Modifier,
query : String,
onQueryChange : (String) -> Unit,
onSearch:() -> Unit,
isActive: Boolean,
activeChange:(Boolean) -> Unit,
placeholderText: String,
searchHistory: List<String>
) {


    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = {onQueryChange(it)},
        onSearch = {
            onSearch()
        },
        active = isActive,
        onActiveChange = { activeChange(it) },
        placeholder = {
                      Text(text = placeholderText, fontFamily = Poppins,
                          fontSize = 12.sp, color = Color(0xFF7C7C7C),
                          textAlign = TextAlign.Justify)
        },
        leadingIcon = {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.search), contentDescription = "Search")
        },
        trailingIcon = {
//            if (isActive) Icon(imageVector = Icons.Outlined., contentDescription = )
        },
        colors = SearchBarDefaults.colors(
            containerColor = Color(0xFFF2F3F2)
        ),
        shape = RoundedCornerShape(15.dp)
        
    ) {

        searchHistory.forEach{ historyItem ->
            Row (
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(modifier = Modifier.padding(end = 12.dp),imageVector = Icons.Outlined.History, contentDescription = "searchHistory")
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontFamily = Poppins,
                            )
                        ){
                            append(historyItem)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SubTitleText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = Poppins,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}



@Composable
fun DividerHorizontal(
    modifier :Modifier = Modifier
        .fillMaxWidth(),
    thickness : Dp = 1.dp,
    color : Color =  Color(0xFFE2E2E2)
) {
    Divider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )

}

@Composable
fun HeadingText(modifier: Modifier = Modifier,
                text: String,
                fontSize: TextUnit = 13.sp,
                ) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = Poppins,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun DefaultOutLinedTextField(
    modifier : Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp),
    text : String,
    onValueChange: (String) -> Unit,
    labelText:String = "",
    placeholderText:String = "",
    textError: Boolean,
    trailingIcon: @Composable () -> Unit = {
        if (textError) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "error",
                tint = Color.Red
            )
        }
    },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = {}
    ),

) {
    OutlinedTextField(
        value = text,
        onValueChange = {onValueChange(it)},
        modifier = modifier,
        shape = shape,
        textStyle = TextStyle().copy(fontFamily = Poppins, fontSize = 12.sp),
        placeholder = {
            Text(
                text = placeholderText ,
                fontFamily = Poppins,
                fontSize = 10.sp
            )
        },
        label = {
            Text(
                text = labelText ,
                fontFamily = Poppins,
                fontSize = 10.sp
            )
        },
        trailingIcon = {
            trailingIcon()
                       },
        isError = textError,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            errorIndicatorColor = Color.Red,
            errorLabelColor = Color.Red,
            focusedLabelColor = Green01,
            focusedPlaceholderColor =  Color(0xFF7C7C7C),
            unfocusedPlaceholderColor = Color(0xFF7C7C7C),
            unfocusedIndicatorColor = Color(0xFFB3B3B3),
            focusedIndicatorColor = Green01,

        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions

    )


}