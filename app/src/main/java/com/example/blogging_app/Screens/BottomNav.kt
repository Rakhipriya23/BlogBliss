package com.example.blogging_app.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blogging_app.model.BottomNavItem
import com.example.blogging_app.R
import com.example.blogging_app.viewmodel.AuthViewModel

@Composable
fun BottomNav(navController: NavHostController) {
    val navController1 = rememberNavController()
    Scaffold(
        bottomBar = { MyBottomBar(navController1) }
    ) { innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = "home_route",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_route") { HomePage(navController) }
            composable("search_route") { Search(navController) }
            composable("add_route") { AddPost(navController1) }
            composable("notification_route") { Notification(navController1) }
            composable("profile_route") { Profile(navController) }
        }
    }
}
@Composable
fun MyBottomBar(navController1: NavHostController){
    val backStackEntry=navController1.currentBackStackEntryAsState()
    val items = listOf(
        BottomNavItem("Home", "home_route", ImageVector.vectorResource(id = R.drawable.baseline_home_24)),
        BottomNavItem("Search", "search_route", ImageVector.vectorResource(id = R.drawable.baseline_search_24)),
        BottomNavItem("Add", "add_route", ImageVector.vectorResource(id = R.drawable.baseline_add_24)),
        BottomNavItem("Notification", "notification_route", ImageVector.vectorResource(id = R.drawable.baseline_favorite_border_24)),
        BottomNavItem("Proile", "profile_route", ImageVector.vectorResource(id = R.drawable.baseline_person_24)),
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