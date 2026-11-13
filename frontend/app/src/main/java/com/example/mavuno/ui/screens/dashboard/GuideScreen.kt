package com.example.mavuno.ui.screens.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.mavuno.ui.layouts.DashboardLayout

@Composable
fun GuideScreen(navController: NavHostController){
    DashboardLayout {
        Text("Settings")
    }
}