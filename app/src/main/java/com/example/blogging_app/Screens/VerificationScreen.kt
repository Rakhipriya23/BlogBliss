package com.example.blogging_app.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blogging_app.R
import com.google.firebase.auth.FirebaseAuth


@Composable
fun VerificationScreen() {
    Box(modifier = Modifier
        .fillMaxSize().background(Color(0xFFfafafa))) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_blog),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = "Verify Your Email", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "A verification email has been sent to your email address. Please verify your email before logging in.",
                fontSize = 20.sp,
                color = Color(0xFF48444D),

//                fontWeight = FontWeight(200),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

