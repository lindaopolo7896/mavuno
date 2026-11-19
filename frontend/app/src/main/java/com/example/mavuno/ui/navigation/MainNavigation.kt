package com.example.mavuno.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mavuno.ui.components.BottomBar
import com.example.mavuno.ui.screens.dashboard.EditProfileScreen
import com.example.mavuno.ui.screens.dashboard.GuideScreen
import com.example.mavuno.ui.screens.dashboard.HistoryScreen
import com.example.mavuno.ui.screens.dashboard.HomeScreen
import com.example.mavuno.ui.screens.dashboard.PlantDetailScreen
import com.example.mavuno.ui.screens.dashboard.ScanScreen
import com.example.mavuno.ui.screens.dashboard.SettingsScreen

@Composable
fun MainNavigation(){
    val navController= rememberNavController()

    Scaffold(
        bottomBar = {BottomBar(navController)}
    ) {
        innerPadding ->
        NavHost(
            startDestination = "home",
            navController=navController,
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ){
            composable("home"){
                HomeScreen(navController)
            }
            composable("scan"){
                ScanScreen(navController)
            }
            composable("plant/{plantId}"){ backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId")
                if (plantId != null) {
                    PlantDetailScreen(scanId = plantId, navController)
                }
            }

            composable("history"){
                HistoryScreen(navController)
            }
            composable("guide"){
                GuideScreen(navController)
            }
            composable("settings"){
                SettingsScreen(navController)
            }
            composable("edit-profile"){
                EditProfileScreen(navController)
            }
        }
    }
}
