package com.example.blogging_app.Screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.blogging_app.R
import com.example.blogging_app.viewmodel.AuthViewModel


@Composable
fun SignupScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val permissionLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission())
    {
        isGranded:Boolean->
        if (isGranded){

        }
        else{

        }
    }
    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val permissionToRequest= if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    }
    else{
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    var launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            // Resetting firebaseUser after successful registration
            authViewModel.logout()
            navController.navigate("signin") {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()

        .background(Color(0xFFfafafa))) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_blog),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = "Welcome!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Create your account", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = if (imageUri == null)
                    painterResource(id = R.drawable.man)
                else
                    rememberAsyncImagePainter(model = imageUri),
                contentDescription = "upload Image",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(color = Color.LightGray)
                    .clickable {
                        //for direct choose image not require permission
                       // launcher.launch("image/*")
                        //for permission
                        val isGranted=ContextCompat.checkSelfPermission(
                            context,permissionToRequest
                        )==PackageManager.PERMISSION_GRANTED

                        if (isGranted){
                            launcher.launch("image/*")

                        }else{
                            permissionLauncher.launch(permissionToRequest)

                        }
                               },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Username
            OutlinedTextField(
                value = username,
                onValueChange = {username=it},
                label = { Text("Username",fontWeight = FontWeight.Bold, ) },
                leadingIcon = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Username Icon", )},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            //Email
            OutlinedTextField(
                value = email,
                onValueChange = {email = it },
                label = { Text("Email",
                    fontWeight = FontWeight.Bold,) },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Username Icon") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                label = { Text("Password",fontWeight = FontWeight.Bold,) },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Password Icon",) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Phone/Email ID Sign-In Button
            Button(
                onClick = {
                    if (username.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
                        Toast.makeText(context, "Please! fill all details", Toast.LENGTH_SHORT).show()
                    } else {
                        val imageUriForUpload = imageUri!!
                        authViewModel.register(email, password, username, imageUriForUpload, context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9E6BFF),
                    contentColor = Color.White
                )
            ) {
                androidx.compose.material3.Text(
                    text = "Register",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            TextButton(onClick = {  navController.navigate("signin") {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop=true
            } })
            {
                Row {
                    Text(text = "Already have an account?",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold)
                    Text("Sign In", color = Color.Blue,fontWeight = FontWeight.Normal)
                }
            }


        }
    }
}