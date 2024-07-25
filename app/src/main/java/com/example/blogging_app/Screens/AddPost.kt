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
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import com.example.blogging_app.R

@Composable
fun  AddPost(navController: NavController) {
    val context = LocalContext.current
   Column(modifier = Modifier
       .fillMaxSize()
       .padding(16.dp)
   ) {
       Image(painter = painterResource(id = R.drawable.baseline_close_24), contentDescription ="close",
           modifier = Modifier.clickable {  })
       Text(text = "Add Post")


   }
    }



