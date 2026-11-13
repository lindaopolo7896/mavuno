package com.example.mavuno.ui.screens.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.data.SamplePlantData
import com.example.mavuno.ui.layouts.DashboardLayout

@Composable
fun PlantDetailScreen(plantId: Int,navController: NavHostController){
val plant= SamplePlantData.scans.find { it.id==plantId }

    DashboardLayout {
        if(plant!=null){
            Text(
                "Scan Result",
                fontSize = 20.sp,
                color = Color(0xffffffff)

            )
        }
    }
}