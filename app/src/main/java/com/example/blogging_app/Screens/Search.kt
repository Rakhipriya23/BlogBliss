package com.example.blogging_app.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.blogging_app.item_view.UserItem
import com.example.blogging_app.viewmodel.SearchViewModel

@Composable
fun Search(navHostController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.users.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    // Filter users based on the search query
    val filteredUsers = userList.filter { user ->
        user.username.contains(searchQuery, ignoreCase = true)
    }

    Column {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = {  Row{
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
                Text("Search by username",
                    color = Color.DarkGray,
                fontWeight = FontWeight.Bold)
            }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // User list
        LazyColumn {
            items(filteredUsers) { user ->
                UserItem(
                    users = user,
                    navHostController = navHostController
                )
            }
        }
    }
}
