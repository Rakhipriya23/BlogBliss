package com.example.blogging_app.navigation

import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blogging_app.Screens.BottomNav
import com.example.blogging_app.Screens.Notification
import com.example.blogging_app.Screens.Profile
import com.example.blogging_app.Screens.Search
import com.example.blogging_app.Screens.SignInScreen
import com.example.blogging_app.Screens.SignupScreen
import com.example.blogging_app.Screens.SplashScreen
import com.example.blogging_app.Screens.VerificationScreen

@Composable fun NavGraph(NavController:NavHostController){
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Splash.route) {

        composable(Routes.Splash.route) { SplashScreen( navController) }
        composable(Routes.SignIn.route) { SignInScreen(navController) }
        composable(Routes.SignUp.route) { SignupScreen(navController) }
        composable(Routes.BottomNav.route) { BottomNav(navController) }
        composable(Routes.Verfication.route){ VerificationScreen()}
        composable(Routes.Home.route){ HomeScreen()}
        composable(Routes.Verfication.route){ Notification(navController) }
        composable(Routes.Verfication.route){ Search(navController) }
        composable(Routes.Verfication.route){ Profile(navController) }



    }

}

@Composable
fun HomeScreen() {
    TODO("Not yet implemented")
}
