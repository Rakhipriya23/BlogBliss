package com.example.blogging_app

import android.app.Notification
import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.blogging_app.Screens.SignInScreen
import com.example.blogging_app.Screens.SignupScreen
import com.example.blogging_app.Screens.SplashScreen
import com.example.blogging_app.ui.theme.BloggingAppTheme
import androidx.navigation.NavHost as NavHost
import androidx.navigation.compose.composable
import com.example.blogging_app.ui.theme.BloggingAppTheme
import com.example.blogging_app.Screens.AddPost
import com.example.blogging_app.Screens.BottomNav
import com.example.blogging_app.Screens.HomePage
import com.example.blogging_app.Screens.Notification
import com.example.blogging_app.Screens.Profile
import com.example.blogging_app.Screens.Search
import com.example.blogging_app.model.BottomNavItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloggingAppTheme {
                MyApp()
//                HomePage()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("bottomnav") { BottomNav(navController) }

    }
}
// Add more composables for other screens as needed