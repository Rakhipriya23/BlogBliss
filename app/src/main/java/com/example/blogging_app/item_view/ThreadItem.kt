package com.example.blogging_app.item_view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.blogging_app.model.ThreadModel
import com.example.blogging_app.model.UserModel
import com.example.blogging_app.navigation.Routes
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navHostController: NavHostController,
    userId: String
) {
    Log.d("ThreadItem", "Displaying thread: ${thread.title} by user: ${users.username}")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .background(Color.White)
            .clickable {
                val routes = Routes.OtherUsers.route.replace("{data}", users.uid)
                Log.d("ThreadItem", "Navigating to: $routes")
                navHostController.navigate(routes)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = "user image",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = users.username,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatTime(thread.timeStamp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = formatDate(thread.timeStamp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = thread.title,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = thread.description,
            style = TextStyle(
                fontSize = 17.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (thread.image.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = thread.image),
                    contentDescription = "thread image",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

fun formatTime(timestamp: String): String {
    return try {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(Date(timestamp.toLong()))
    } catch (e: Exception) {
        Log.e("DateFormat", "Error formatting time: ${e.message}")
        ""
    }
}

fun formatDate(timestamp: String): String {
    return try {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        sdf.format(Date(timestamp.toLong()))
    } catch (e: Exception) {
        Log.e("DateFormat", "Error formatting date: ${e.message}")
        ""
    }
}
