package com.example.blogging_app.Screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.blogging_app.R
import com.example.blogging_app.navigation.Routes
import com.example.blogging_app.utils.SharedPref
import com.example.blogging_app.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth
@Composable
fun EditPost(navController: NavController, threadId: String) {
    val context = LocalContext.current
    val threadViewModel: AddThreadViewModel = viewModel()
    val isUpdated by threadViewModel.isUpdated.observeAsState(false)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launcher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetch the thread data when the composable is launched
    LaunchedEffect(threadId) {
        threadViewModel.getThreadById(threadId) { thread ->
            if (thread != null) {
                title = thread.title
                description = thread.description
                imageUrl = thread.image
                // Convert imageUrl to Uri if needed
                imageUri = if (thread.image.isNotEmpty()) Uri.parse(thread.image) else null
            } else {
                Toast.makeText(context, "Failed to load post data", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

    // Show toast and navigate back when the update is successful
    LaunchedEffect(isUpdated) {
        if (isUpdated) {
            Toast.makeText(context, "Edit successful", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.BottomNav.route) {
                popUpTo(Routes.BottomNav.route) {
                    inclusive = true
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Edit") },
            text = { Text(text = "Are you sure you want to edit this post?") },
            confirmButton = {
                Button(onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    threadViewModel.updateThread(threadId, title, description, imageUri, userId)
                    showDialog = false
                }
                , colors = ButtonDefaults.buttonColors(Color(0xFF9d6bfe)) )

                {
                    Text("Confirm",color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(Color.Gray)) {
                    Text("Cancel", color = Color.White )
                }
            }
        )
    }

    // Rest of your UI code
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
                            navController.navigate(Routes.Profile.route)
                        }
                    )
                    Text(
                        text = "Edit Your Post",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = "confirm",
                    modifier = Modifier.clickable {
                        showDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // For user img and username
            val username = remember { SharedPref.getUserName(context) }
            val userimg = remember { SharedPref.getImage(context) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
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
                            context, android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (imageUri != null) {
                    // If an image is selected, display it inside the box
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (imageUrl.isNotEmpty()) {
                    // Show the existing image if available
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Show the placeholder text when no image is selected
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
        }
    }
}








