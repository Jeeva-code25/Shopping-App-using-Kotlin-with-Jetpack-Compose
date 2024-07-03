package com.example.narumanam.sellersidepages.products

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.narumanam.DefaultBackArrow
import com.example.narumanam.DefaultNextButton
import com.example.narumanam.DefaultTextField
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.accounts.Address
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.SellerScreens
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.mobileTitle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import kotlinx.coroutines.delay

@Composable
fun ProductUploadScreen2(
    navController:NavHostController,
    productId:String,
    productCategory:String,
    itemName:String,
    brandName:String,
    quantity:String,
    quantityCategory:String,
    actualPrice:String,
    inHand:String,
    offer:String
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val database = FirebaseDatabase.getInstance().getReference("Sellers")
    val globalDatabase = FirebaseDatabase.getInstance().getReference("GlobalProducts")
    val globImageDatabase = Firebase.storage.getReference("GlobalProductImages")
    val globProductId = globalDatabase.push().key?:""
    var sellerAddress = Address()


    val isConnection = networkConnection(context = context)


    database
        .child(phoneNumber?:"")
        .child("MyAddress")
        .child(AddressId)
        .get()
        .addOnSuccessListener {snapshot->
            sellerAddress = snapshot.getValue(Address::class.java)?:Address()
        }


    var selectedImageUris by rememberSaveable{
        mutableStateOf<List<Uri>>(emptyList())
    }

    val uploadedImageUrl = rememberSaveable {
        mutableListOf<Uri>()
    }

    var selectedImageError by rememberSaveable{
        mutableStateOf(false)
    }

    var description by rememberSaveable{
        mutableStateOf("")
    }
    var descriptionError by rememberSaveable{
        mutableStateOf(false)
    }

    var processState by remember {
        mutableStateOf(false)
    }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 4)){ uri->
        selectedImageError = false
        processState = false
        selectedImageUris = uri
    }




    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        if (processState){
            ProgressIndicator()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                DefaultBackArrow(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onclick = {
                        navController.popBackStack()
                    }
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Product Upload",
                    style = mobileTitle
                )

            }

        }

        LazyColumn(
            modifier= Modifier
                .fillMaxWidth()
        ){
            item {

                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(12.dp),

                    ) {

                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "Product Images:",
                        fontSize = 13.sp,
                        fontFamily = Poppins,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    LazyHorizontalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .drawBehind {
                                drawRoundRect(
                                    color = if (selectedImageError) Color.Red else Color(0xFFB3B3B3),
                                    style = stroke,
                                    cornerRadius = CornerRadius(12.dp.toPx())
                                )
                            }
                            .padding(8.dp),
                        rows = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {


                        items(selectedImageUris){uri->
                            AsyncImage(
                                modifier = Modifier
                                    .size(150.dp),
                                model = ImageRequest.Builder(context)
                                    .data(uri)
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "selected Image",
                                contentScale = ContentScale.Crop,
                            )

                        }
                        if (selectedImageUris.size < 4) {

                            item {

                                IconButton(
                                    onClick = {
                                        processState = true
                                        multiplePhotoPicker.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }, modifier = Modifier
                                        .background(color = Color(0xFFD9D9D9))
                                        .size(150.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Icon(
                                            imageVector = Icons.Outlined.AddAPhoto,
                                            contentDescription = "addImage",
                                            tint = Green01
                                        )
                                        Text(
                                            text = "add image",
                                            fontFamily = Poppins,
                                            fontSize = 10.sp,
                                            color = Green01,
                                        )
                                    }
                                }


                            }
                        }


                    }

                    Text(
                        modifier = Modifier.padding(top = 10.dp,bottom = 10.dp),
                        text = "Description:",
                        fontSize = 13.sp,
                        fontFamily = Poppins,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        value = description ,
                        onValueChange = {
                            descriptionError = false
                            description = it
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            errorContainerColor = Color.White,
                            errorIndicatorColor = Color.Red,
                            focusedPlaceholderColor =  Color(0xFF7C7C7C),
                            unfocusedPlaceholderColor = Color(0xFF7C7C7C),
                            unfocusedIndicatorColor = Color(0xFFB3B3B3),
                            focusedIndicatorColor = Green01),
                        placeholder = {
                            Text(
                                text = "eg. Write about Product" ,
                                fontFamily = Poppins,
                                fontSize = 13.sp
                            )
                        },
                        isError = descriptionError,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                selectedImageError = selectedImageUris.isEmpty()
                                descriptionError = description.isEmpty()
                                if (!selectedImageError && !descriptionError){
                                    focusManager.clearFocus()
                                }
                            }
                        )

                    )



                    Row(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        DefaultNextButton(
                            onclick = {

                                if (isConnection) {
                                    processState = true
                                    selectedImageError = selectedImageUris.isEmpty()
                                    descriptionError = TextUtils.isEmpty(description)

                                    if (!selectedImageError && !descriptionError) {


                                        for (uri in selectedImageUris) {

                                            globImageDatabase
                                                .child(phoneNumber ?: "")
                                                .child("$itemName($globProductId)")
                                                .child(getFilename(context, uri).toString())
                                                .putFile(uri)
                                                .addOnSuccessListener {

                                                    globImageDatabase
                                                        .child(phoneNumber ?: "")
                                                        .child("$itemName($globProductId)")
                                                        .child(getFilename(context, uri).toString())
                                                        .downloadUrl.addOnSuccessListener { uploadUri ->

                                                            uploadedImageUrl.add(uploadUri)

                                                            if (selectedImageUris.size == uploadedImageUrl.size) {

                                                                val imageUrlString =
                                                                    uploadedImageUrl
                                                                        .toString()
                                                                        .filterNot { it == '[' || it == ']' || it == ' ' }

                                                                val product = productObject(
                                                                    globProductId = globProductId,
                                                                    phoneNumber = phoneNumber ?: "",
                                                                    productId = productId,
                                                                    productCategory = productCategory,
                                                                    itemName = itemName,
                                                                    brandName = brandName,
                                                                    quantity = quantity,
                                                                    quantityCategory = quantityCategory,
                                                                    actualPrice = actualPrice,
                                                                    inHand = inHand,
                                                                    offer = offer,
                                                                    imageUrl = imageUrlString,
                                                                    description = description,
                                                                    addressDetail = sellerAddress
                                                                )

                                                                globalDatabase
                                                                    .child(globProductId)
                                                                    .setValue(product[0])
                                                                    .addOnCompleteListener { globTask ->

                                                                        if (globTask.isSuccessful) {
                                                                            database
                                                                                .child(
                                                                                    phoneNumber
                                                                                        ?: ""
                                                                                )
                                                                                .child("MyInventory")
                                                                                .child(productId)
                                                                                .setValue(product[1])
                                                                                .addOnCompleteListener { localTask ->
                                                                                    processState =
                                                                                        false
                                                                                    if (localTask.isSuccessful) {
                                                                                        Toast.makeText(
                                                                                            context,
                                                                                            "Product successfully uploaded",
                                                                                            Toast.LENGTH_SHORT
                                                                                        )
                                                                                            .show()

                                                                                        navController.navigate(
                                                                                            SellerScreens.productScreen.route
                                                                                        ) {
                                                                                            popUpTo(
                                                                                                SellerScreens.productScreen.route
                                                                                            ) {
                                                                                                inclusive =
                                                                                                    true
                                                                                            }
                                                                                        }

                                                                                    } else {
                                                                                        Toast.makeText(
                                                                                            context,
                                                                                            "Product failed to upload",
                                                                                            Toast.LENGTH_SHORT
                                                                                        )
                                                                                            .show()

                                                                                    }

                                                                                }
                                                                        } else {
                                                                            processState = false
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Product failed to upload",
                                                                                Toast.LENGTH_SHORT
                                                                            )
                                                                                .show()
                                                                        }
                                                                    }
                                                            }

                                                        }

                                                }.addOnFailureListener {
                                                    processState = false
                                                    Toast.makeText(
                                                        context,
                                                        "Product failed to upload$it",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }

                                        }


                                    } else {
                                        processState = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .size(67.dp),
                            icon = ImageVector.vectorResource(id = R.drawable.done)
                        )
                    }
                }

            }
        }

    }
}

fun productObject(
    globProductId: String,
    phoneNumber: String?,
    productId: String,
    productCategory: String,
    itemName: String,
    brandName: String,
    quantity: String,
    quantityCategory: String,
    actualPrice: String,
    inHand: String,
    offer: String,
    imageUrl: String,
    description: String,
    addressDetail : Address
): List<Any> {

    val product = ProductData(
        globProductId= globProductId,
        productId = productId,
        productCategory = productCategory,
        itemName = itemName,
        brandName = brandName,
        quantity = quantity,
        quantityCategory = quantityCategory,
        actualPrice = actualPrice,
        inHand = inHand,
        offer = offer,
        productImages = imageUrl,
        description = description,
    )
    val globProduct = GlobProduct(
        globProductId = globProductId,
        sellerPhoneNumber = phoneNumber?:"",
        productId = productId,
        productCategory = productCategory,
        itemName = itemName,
        brandName = brandName,
        quantity = quantity,
        quantityCategory = quantityCategory,
        actualPrice = actualPrice,
        inHand = inHand,
        offer = offer,
        productImages = imageUrl,
        description = description,
        addressId = addressDetail.addressId,
        phoneNumber = addressDetail.phoneNumber,
        userName = addressDetail.userName,
        houseNo = addressDetail.houseNo,
        streetName = addressDetail.streetName,
        nearestLandMark = addressDetail.nearestLandMark,
        pinCode = addressDetail.pinCode,
        district = addressDetail.district,
        state = addressDetail.state
    )

    return listOf(globProduct,product)
}

@SuppressLint("Range")
private fun getFilename(context : Context, uri: Uri): String? {
    if (uri.scheme == "content") {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
    }
    return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
}

