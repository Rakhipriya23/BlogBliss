package com.example.blogging_app.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.blogging_app.R
import com.example.blogging_app.model.ThreadModel
import com.example.blogging_app.viewmodel.AuthViewModel
import com.example.blogging_app.viewmodel.UserViewModel

@Composable
fun OtherUserProfile(navHostController: NavHostController, uid: String) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)

    val username = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf("") }

    userViewModel.fetchPost(uid)
    userViewModel.fetchUser(uid)
    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate("signin") {
                popUpTo(navHostController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            authViewModel.fetchUserData(uid)
        }
    }

    userViewModel.users.observeAsState().value?.let { user ->
        username.value = user.username
        imageUri.value = user.imageUrl
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0ECEC))
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navHostController.popBackStack()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (imageUri.value.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri.value),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = username.value,
                color = Color.Black,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                color = Color.DarkGray,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(17.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                if (threads != null && users != null) {
                    threads?.let { threadList ->
                        items(threadList) { thread ->
                            ThreadTitleDescription(
                                thread = thread,
                                userViewModel = userViewModel,
                                navHostController = navHostController,
                                context = context
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThreadTitleDescription(
    thread: ThreadModel,
    userViewModel: UserViewModel,
    context: Context,
    navHostController: NavHostController
)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
            //for background(color)
            .background(Color(0xFFF0ECEC))
    ) {
        if (thread.image.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(thread.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = thread.title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = thread.description,
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}