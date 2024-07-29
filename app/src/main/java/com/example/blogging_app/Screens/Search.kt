package com.example.blogging_app.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.blogging_app.item_view.UserItem
import com.example.blogging_app.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.users.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    //for color of icon
    var isFocused by remember { mutableStateOf(false) }

    // Filter users based on the search query
    val filteredUsers = userList.filter { user ->
        user.username.contains(searchQuery, ignoreCase = true)
    }

    Column(
        Modifier.background(Color.White)
            .fillMaxSize()
            .padding(17.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = {
                Text("Search by username",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = if (isFocused) Color(0xFFab06f6) else Color.Gray
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFab06f6), // Purple color
                unfocusedBorderColor = Color.DarkGray, // Grey color
                focusedLeadingIconColor = Color(0xFFab06f6), // Purple color
                unfocusedLeadingIconColor = Color.Gray // Grey color
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
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
