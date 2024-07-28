package com.example.blogging_app.Screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.blogging_app.R
import com.example.blogging_app.viewmodel.AddThreadViewModel
import com.example.blogging_app.navigation.Routes
import com.example.blogging_app.utils.SharedPref
import com.example.blogging_app.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.DefaultExecutor.thread

@Composable
fun confirmDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    title: String,
    description: String,
){
    AlertDialog(
        title = {
            Text(text = dialogTitle, style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
        },
        text = {
            Column (horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center){
                Text(text = title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                Text(text = description, style = TextStyle(fontSize = 12.sp), maxLines = 2)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun AddPost(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val threadViewModel: AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Handle permission granted case
        } else {
            // Handle permission denied case
        }
    }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(isPosted) {
        if (isPosted) {
            threadViewModel.clearInputs()
            Toast.makeText(context, "Thread added", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Add.route) {
                    inclusive = true
                }
            }
        }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val openAlertDialog = remember { mutableStateOf(false) }



    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header and buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "close",
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.Home.route)
                        }
                    )
                    Text(
                        text = "Add a new Post",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = "confirm",
                    modifier = Modifier.clickable { openAlertDialog.value = true }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            //for user img and username
            val username = remember { SharedPref.getUserName(context) }
            val userimg = remember { SharedPref.getImage(context) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                if (userimg.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(userimg),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = username,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            }

            // Upload poster image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color.LightGray)
                    .border(
                        border = BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (imageUri != null) {
                    // If an image is selected, display it inside the box
//                    threadViewModel.saveData(thread,FirebaseAuth.getInstance().currentUser!!.uid,imageUri!!)
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Show the placeholder text when no image is selected
                    //threadViewMode.saveData(thread,FirebaseAuth.getInstance().currentUser!!.uid,"")
                    Text(
                        text = "+ Upload poster image",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Gray)
                    )
                }
            }

            // Title
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Add Title", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp)
            )
            Spacer(modifier = Modifier.height(2.dp))

            // Description
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text(text = "Add a description", style = TextStyle(fontSize = 18.sp)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                textStyle = TextStyle(fontSize = 18.sp)
            )

            if (openAlertDialog.value) {
                confirmDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        openAlertDialog.value = false
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        threadViewModel.postThread(title, description, imageUri, userId)
                    },
                    dialogTitle = "Are you sure you want to post?",
                    title = title,
                    description = description
                )
            }
        }
    }
}
