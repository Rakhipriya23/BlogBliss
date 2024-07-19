package com.example.blogging_app.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.blogging_app.R

@Composable
fun  AddPost(navController: NavController) {
    val textState = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        // Top Bar
        TopAppBar(
            title = { Text("Add Thread") },
            navigationIcon = {
                IconButton(onClick = { /* Handle back navigation */ }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            backgroundColor = Color(0xFF00796B),
            contentColor = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.man), // Replace with your image resource
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "1902shubh", fontSize = 18.sp)
        }

            Spacer(modifier = Modifier.height(16.dp))

            // Text Field
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Write your thread here...") }
            )
        }
    }

