package com.example.blogging_app.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun HomePage(navController: NavController) {
    Text(text = "Home")
}
//@Composable
//fun BlogPostItem(post: BlogPost) {
//    Card(
//        modifier = Modifier
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//            .fillMaxWidth(),
////        elevation = 4.dp
////        elevation= CardElevation = CardDefaults.cardElevation()
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = post.title, style = MaterialTheme.typography.h6)
//            Text(text = post.author, style = MaterialTheme.typography.caption)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = post.content)
//        }
//    }
//}