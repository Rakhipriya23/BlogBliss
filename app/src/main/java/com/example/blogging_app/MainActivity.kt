package com.example.blogging_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.blogging_app.Screens.*
import com.example.blogging_app.ui.theme.BloggingAppTheme
import androidx.navigation.compose.composable
import com.example.blogging_app.navigation.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloggingAppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) { SplashScreen(navController) }
        composable(Routes.SignIn.route) { SignInScreen(navController) }
        composable(Routes.SignUp.route) { SignupScreen(navController) }
        composable(Routes.BottomNav.route) { BottomNav(navController) }
    }
}
