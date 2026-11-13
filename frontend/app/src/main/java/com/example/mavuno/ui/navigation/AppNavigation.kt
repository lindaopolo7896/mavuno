package com.example.mavuno.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mavuno.ui.screens.auth.ForgotScreen
import com.example.mavuno.ui.screens.auth.LoginScreen
import com.example.mavuno.ui.screens.auth.ResetScreen
import com.example.mavuno.ui.screens.auth.SignupScreen
import com.example.mavuno.ui.screens.auth.SplashScreen
import com.example.mavuno.ui.screens.auth.VerifyScreen

@Composable
fun AppNavigation(){
    val navController=rememberNavController()

    NavHost(
        navController=navController,
        startDestination = "splash"
    ){
composable("splash"){
    SplashScreen(navController)
}
        composable("signup"){
            SignupScreen(navController)
        }
        composable("signin"){
            LoginScreen(navController)
        }
        composable("forgot"){
            ForgotScreen(navController)
        }
        composable("verify"){
            VerifyScreen(navController)
        }
        composable("reset"){
            ResetScreen(navController)
        }
        composable("main") {
            MainNavigation()
        }
    }
}