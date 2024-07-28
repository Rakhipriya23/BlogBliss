package com.example.blogging_app.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.blogging_app.R
import com.example.blogging_app.model.BottomNavItem
import com.example.blogging_app.navigation.Routes
import com.example.blogging_app.viewmodel.AuthViewModel

@Composable
fun BottomNav(navController: NavHostController) {
    val navController1 = rememberNavController()
    Scaffold(
        bottomBar = { MyBottomBar(navController1) }
    ) { innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home.route) { HomePage(navController) }
            composable(Routes.Search.route) { Search(navController) }
            composable(Routes.Add.route) { AddPost(navController1) }
            composable(Routes.Notification.route) { Notification(navController1) }
            composable(Routes.Profile.route) { Profile(navController) }
        }
    }
}

@Composable
fun MyBottomBar(navController1: NavHostController) {
    val backStackEntry = navController1.currentBackStackEntryAsState()
    val items = listOf(
        BottomNavItem(
            "Home",
            Routes.Home.route,
            ImageVector.vectorResource(id = R.drawable.baseline_home_24)
        ),
        BottomNavItem(
            "Search",
            Routes.Search.route,
            ImageVector.vectorResource(id = R.drawable.baseline_search_24)
        ),
        BottomNavItem(
            "Add",
            Routes.Add.route,
            ImageVector.vectorResource(id = R.drawable.baseline_add_24)
        ),
        BottomNavItem(
            "Saved",
            Routes.Notification.route,
            ImageVector.vectorResource(id = R.drawable.baseline_bookmarks_24)
        ),
        BottomNavItem(
            "Profile",
            Routes.Profile.route,
            ImageVector.vectorResource(id = R.drawable.baseline_person_24)
        ),
    )

    BottomAppBar {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) },
                selected = backStackEntry.value?.destination?.route == item.route,
                onClick = {
                    navController1.navigate(item.route) {
                        popUpTo(navController1.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
