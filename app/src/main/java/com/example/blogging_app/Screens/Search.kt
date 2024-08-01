package com.example.blogging_app.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.blogging_app.R
import com.example.blogging_app.item_view.UserItem
import com.example.blogging_app.navigation.Routes
import com.example.blogging_app.viewmodel.AuthViewModel
import com.example.blogging_app.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.users.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    // Fetch logged-in user ID (assume you have a method to get this)
    val authViewModel: AuthViewModel = viewModel() // Assuming you have an AuthViewModel
    val loggedInUserId by authViewModel.loggedInUserId.observeAsState("")

    // Filter users based on the search query and exclude the logged-in user
    val filteredUsers = userList.filter { user ->
        user.uid != loggedInUserId && user.username.contains(searchQuery, ignoreCase = true)
    }

    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(17.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 10.dp, start = 16.dp, bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "close",
                modifier = Modifier.clickable {
                    navHostController.navigate(Routes.Home.route)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = {
                Text(
                    "Search by username",
                    color = if (isFocused) Color(0xFFab06f6) else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = if (isFocused) Color(0xFFab06f6) else Color.Gray
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.Black,
                cursorColor = Color(0xFFab06f6),
                focusedBorderColor = Color(0xFFab06f6),
                unfocusedBorderColor = Color.DarkGray,
                focusedLeadingIconColor = Color(0xFFab06f6),
                unfocusedLeadingIconColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // User List
        LazyColumn {
            items(filteredUsers) { user ->
                UserItem(
                    user = user,
                    navHostController = navHostController
                )
            }
        }
    }
}

