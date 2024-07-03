package com.example.narumanam.sellersidepages

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

import com.example.narumanam.DefaultSearchBar
import com.example.narumanam.ProgressIndicator
import com.example.narumanam.R
import com.example.narumanam.Screens
import com.example.narumanam.SubTitleText
import com.example.narumanam.networkConnection
import com.example.narumanam.sellersidepages.products.ProductData
import com.example.narumanam.ui.theme.Green01
import com.example.narumanam.ui.theme.Poppins
import com.example.narumanam.ui.theme.Red01
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

var SELECTED_PRODUCT : ProductData = ProductData()
var PRODUCT_IMAGES : List<String> = emptyList()

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavHostController) {


    var processState by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val myProducts =  remember { mutableStateListOf<ProductData>() }
    val searchHistory = remember { mutableStateListOf("Apple", "Honey", "Onion") }

    val scope  = rememberCoroutineScope()
    val snackBarHostState  =   remember{SnackbarHostState()}

    val productCategory = remember { mutableStateListOf<String>() }

    val sharedPreference  = context.getSharedPreferences(Screens.APPNAME, Context.MODE_PRIVATE)
    val phoneNumber = sharedPreference.getString("PHONENUMBER","")
    val isConnection = networkConnection(context = context)


    val myInventoryDatabase = FirebaseDatabase
        .getInstance()
        .getReference("Sellers")
        .child(phoneNumber?:"")
        .child("MyInventory")

    myInventoryDatabase.addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myProducts.clear()
                productCategory.clear()
                snapshot.children.forEach { item->

                    item.getValue(ProductData::class.java)?.let {
                        myProducts.add(it)
                        if (!productCategory.contains(it.productCategory)) {
                            productCategory.add(it.productCategory)
                        }
                    }
                }

                processState = false

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error", Toast.LENGTH_SHORT).show()
            }

        }
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            generatePDF(
                context = context,
                fileName = "narumanam_stocks",
                myProducts = myProducts,
                productCategory = productCategory,
                scope = scope,
                snackBarHostState = snackBarHostState
            )
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()

        }
    }


    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }



    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },

        modifier = Modifier
            .fillMaxSize(),
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(12.dp),
                ) {



                    if (processState) {
                        ProgressIndicator()
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        SearchBar(
                            modifier = Modifier.weight(1f),
                            query = query,
                            onQueryChange = {
                                query = it
                            },
                            onSearch = {
                                isActive = false
                                searchHistory.add(query)
                            },
                            active = isActive,
                            onActiveChange = {
                                isActive = it
                            },
                            placeholder = {
                                Text(
                                    text = "Search Product", fontFamily = Poppins,
                                    fontSize = 12.sp, color = Color(0xFF7C7C7C),
                                    textAlign = TextAlign.Justify
                                )
                            },
                            colors = SearchBarDefaults.colors(
                                containerColor = Color(0xFFF2F3F2),
                            ),
                            shape = RoundedCornerShape(15.dp),
                        )
                        {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Color.White)
                                    .padding(top = 15.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                val searchProducts = myProducts.filter {
                                    it.itemName.lowercase()
                                        .contains(Regex(query.lowercase())) || query == ""
                                }

                                items(items = searchProducts) { product ->

                                    ItemTemplate(
                                        product = product,
                                        navController,
                                        phoneNumber = phoneNumber ?: "",
                                        context = context
                                    )

                                }

                            }
                        }

                        IconButton(
                            onClick = {

                                if (myProducts.size > 0 && isConnection) {
                                    when (PackageManager.PERMISSION_GRANTED) {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ) -> {
                                            generatePDF(
                                                context = context,
                                                fileName = "narumanam_stocks",
                                                myProducts = myProducts,
                                                productCategory = productCategory,
                                                scope = scope,
                                                snackBarHostState = snackBarHostState
                                            )
                                        }

                                        else -> launcher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                                    }
                                } else {
                                    Toast.makeText(context, "Unable to generate a stock report", Toast.LENGTH_LONG).show()
                                }

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FileDownload,
                                contentDescription ="PDF Download"
                            )
                        }

                    }



                    if (myProducts.size > 0) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {


                            productCategory.forEach {
                                stickyHeader {
                                    SubTitleText(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = Color.White)
                                            .padding(top = 6.dp, bottom = 8.dp)
                                        ,
                                        text = it,
                                        fontSize = 18.sp
                                    )
                                }
                                items(items = myProducts) { product ->
                                    if (product.productCategory == it) {
                                        ItemTemplate(
                                            product = product,
                                            navController,
                                            phoneNumber = phoneNumber?:"",
                                            context = context
                                        )
                                    }
                                }
                            }

                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "Empty Store!!",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "add product to start your journey",
                                    fontFamily = Poppins,
                                    fontSize = 20.sp
                                )
                            }
                        }

                    }


                }
            }
        }
    )




}

private fun generatePDF(
    context: android.content.Context,
    fileName: String,
    myProducts : SnapshotStateList<ProductData>,
    productCategory : SnapshotStateList<String>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {


    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val now = Date()
    val fileNameTimeStamp = dateFormat.format(now)

    val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName + "_" + fileNameTimeStamp + ".pdf"

    val document = Document()
    val file = File(filePath)
    PdfWriter.getInstance(document, FileOutputStream(file))
    document.open()

    val headerFont = Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD,BaseColor(Color.White.toArgb()))
    val categoryFont = Font(Font.FontFamily.TIMES_ROMAN, 17f,Font.BOLD)
    val titleFont = Font(Font.FontFamily.TIMES_ROMAN, 20f, Font.BOLD)
    val cellFont = Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.NORMAL)

    // Create a table for image, title, and subtitle
    val table = PdfPTable(2)
    table.widthPercentage = 100f
    table.spacingBefore = 10f
    table.spacingAfter = 10f

    // Add image to the table
    val drawable = ContextCompat.getDrawable(context, R.drawable.vegetables)
    val bitmap = (drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val image = com.itextpdf.text.Image.getInstance(stream.toByteArray())
    image.scaleToFit(150f, 150f)
    val imageCell = PdfPCell(image, true)
    imageCell.verticalAlignment = Element.ALIGN_TOP
    imageCell.border = PdfPCell.NO_BORDER
    table.addCell(imageCell)

    // Add title and subtitle to the table
    val titleText = "A stock report for Sellers"
    val subtitleText = "Narumanam"
    val titleSubtitleCell = PdfPCell()
    titleSubtitleCell.addElement(Paragraph(titleText, titleFont))
    titleSubtitleCell.addElement(Paragraph(subtitleText, titleFont))
    titleSubtitleCell.verticalAlignment = Element.ALIGN_TOP
    titleSubtitleCell.border = PdfPCell.NO_BORDER
    table.addCell(titleSubtitleCell)

    document.add(table)

 // Add table headers
    val tableHeaders = arrayOf("Product Id", "Product Name", "In Hand","Status")
    val headersTable = PdfPTable(tableHeaders.size)
    headersTable.widthPercentage = 100f
    headersTable.spacingBefore = 10f
    headersTable.spacingAfter = 10f
    tableHeaders.forEach { headerText ->
        val cell = PdfPCell(Phrase(headerText, headerFont))
        cell.backgroundColor = BaseColor(Green01.toArgb())
        cell.setPadding(12f)
        cell.verticalAlignment = PdfPCell.ALIGN_CENTER
        cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
        headersTable.addCell(cell)
    }


    productCategory.forEach {category->

        val categoryText = PdfPCell(Phrase(category, categoryFont))
        categoryText.colspan = 4
        categoryText.backgroundColor = BaseColor(192, 192, 192)
        categoryText.setPadding(12f)
        categoryText.horizontalAlignment = PdfPCell.ALIGN_CENTER
        categoryText.verticalAlignment = PdfPCell.ALIGN_CENTER


        headersTable.addCell(categoryText)

        myProducts.forEach { productData ->

            if (category == productData.productCategory) {
                for (i in tableHeaders.indices) {
                    val inHandQty = productData.inHand.filter { it.isDigit() }
                    val cell = when (i) {
                        0 -> PdfPCell(Phrase(productData.productId, cellFont))
                        1 -> PdfPCell(Phrase(productData.itemName, cellFont))
                        2 -> PdfPCell(Phrase(productData.inHand, cellFont))
                        3 -> PdfPCell(
                            Phrase(
                                if (Integer.parseInt(inHandQty.ifEmpty { "0" }) <= 0) "Out of Stock" else "In Stock",
                                cellFont
                            )
                        )

                        else -> PdfPCell(Phrase("No data", cellFont))
                    }

                    cell.setPadding(12f)
                    cell.horizontalAlignment = PdfPCell.ALIGN_CENTER
                    cell.verticalAlignment = PdfPCell.ALIGN_CENTER

                    if (Integer.parseInt(inHandQty.ifEmpty { "0" }) <= 0) {
                        cell.backgroundColor = BaseColor(Red01.toArgb())
                    } else if (Integer.parseInt(inHandQty) <= 5) {
                        cell.backgroundColor = BaseColor(Color.Yellow.toArgb())
                    }
                    headersTable.addCell(cell)
                }
            }
        }
    }

    document.add(headersTable)

    document.close()

    scope.launch {
       snackBarHostState.showSnackbar(
            message = "Stock Report Downloaded",
            duration = SnackbarDuration.Short,
        )

    }
}


@Composable
fun ItemTemplate(
product: ProductData,
navController: NavHostController,
phoneNumber : String,
context : Context
) {

    val isConnection = networkConnection(context = context)

    val database = FirebaseDatabase.getInstance().getReference("Sellers")
    val globalDatabase = FirebaseDatabase.getInstance().getReference("GlobalProducts")

    var showDialog by remember { mutableStateOf(false) }
    var deleteProcess by remember { mutableStateOf(false) }

    val imageList = product.productImages.split(",")

    val image = imageList[0].replace("[","")

    val inHandQty = product.inHand.filter { it.isDigit() }


    if (deleteProcess){
        ProgressIndicator()
    }

    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
            },
            title = {
                Text(
                    text = "Are you sure want to delete?",
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    text = "Product will be removed permanently ",
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    textAlign = TextAlign.Justify,
                )

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isConnection) {
                            showDialog = false
                            deleteProcess = true

                            var count = 0

                            for (url in imageList) {

                                count += 1
                                val productImage = url.filterNot { it == '[' || it == ']' }

                                val globImageDatabase =
                                    Firebase.storage.getReferenceFromUrl(productImage.trim())
                                globImageDatabase.delete().addOnSuccessListener {

                                    if (count == imageList.size) {

                                        globalDatabase
                                            .child(product.globProductId)
                                            .removeValue()
                                            .addOnSuccessListener {

                                                database
                                                    .child(phoneNumber)
                                                    .child("MyInventory")
                                                    .child(product.productId)
                                                    .removeValue()
                                                    .addOnCompleteListener { task ->

                                                        if (task.isSuccessful && deleteProcess) {
                                                            deleteProcess = false
                                                            Toast.makeText(
                                                                context,
                                                                "Successfully product removed",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            navController.popBackStack()
                                                        } else if (deleteProcess) {
                                                            deleteProcess = false
                                                            Toast.makeText(
                                                                context,
                                                                "Failed to  remove a product",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    }
                                            }
                                            .addOnFailureListener {
                                                deleteProcess = false
                                                Toast.makeText(
                                                    context,
                                                    "Failed to  remove a product",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                }


                            }
                        }
                    })
                {

                    Text(
                        text = "Confirm",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                    )
                }
            },
            dismissButton = {

                TextButton(onClick = {
                    showDialog = false
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color = Color(0XFFB3B3B3), shape = RoundedCornerShape(15.dp))
            .padding(12.dp)
            .clickable {

                if (isConnection) {
                    SELECTED_PRODUCT = product
                    PRODUCT_IMAGES = imageList
                    navController.navigate(
                        SellerScreens.productEditScreen.route
                    )
                }

            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = "Product Image",
            modifier = Modifier
                .weight(1f)
                .size(55.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 8.dp),
        ) {
            Text(
                text = product.itemName,
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "In Hand: "+product.inHand,
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Color(0xFF344356)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    modifier = Modifier
                        .size(15.dp),
                    imageVector = Icons.Outlined.CurrencyRupee,
                    contentDescription = "CurrencyRupee",
                    tint =  Color(0xFF344356)
                )
                Text(
                    text = product.actualPrice,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFF344356)
                )
            }

        }
        Text(modifier = Modifier
            .weight(1f)
            .border(width = 0.5.dp, color = Color(0xFF53B175), shape = RoundedCornerShape(5.dp))
            .padding(3.dp),
            fontFamily = Poppins,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            text =  if (Integer.parseInt(inHandQty.ifEmpty { "0" }) <= 0) "Out of Stock" else "In Stock",
            color = if (Integer.parseInt(inHandQty.ifEmpty { "0" } ) <= 0) Color(0xFF979797) else Color(0xFF3D6DEB)
            )
        IconButton(
            modifier = Modifier
                .size(24.dp)
                .weight(0.5f),
            onClick = {
                if (isConnection) {
                    showDialog = !showDialog
                }
            }) {
            Icon(imageVector = Icons.Outlined.Delete,
                contentDescription = "delete",
                tint = Color(0xFFF3603F)
            )
        }
    }
}



