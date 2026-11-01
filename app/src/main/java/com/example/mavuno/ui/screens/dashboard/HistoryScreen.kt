package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.data.SamplePlantData
import com.example.mavuno.ui.components.Input
import com.example.mavuno.ui.components.ScanCard
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun HistoryScreen(navController: NavHostController){

    var search by remember { mutableStateOf("") }
    DashboardLayout {
//        TOPBAR

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row() {
                Text(
                    "Scan History",
                    fontSize = 30.sp,
                    color = CropTheme.colors.heading,
                    fontWeight = FontWeight.SemiBold
                )

            }
            Text(
                "Your crop health journey, in one place.",
                fontSize = 16.sp,
                color = CropTheme.colors.tag,
                fontWeight = FontWeight.Medium
            )
        }
//SEARCH INPUT
Column(
    modifier = Modifier.offset(y=-20.dp)
) {
    Input(
        label = "",
        value = search,
        onValueChange = { search = it },
        placeholder = "Search crop or disease",
        isSearch = true

    )
}

        ScanHist(navController)

    }
}

data class Scan(
    val id:Int,
    val text: String
)

val scans=listOf(
    Scan(
        1,
        "All scans"
    ),
    Scan(
        2,
        "Healthy"
    ),
    Scan(
        3,
        "Diseased"
    )

)

@Composable
fun ScanHist(navController: NavHostController){
    var active by remember { mutableStateOf(1) }

    val filteredScans=when(active){
        2-> SamplePlantData.scans.filter { it.status=="Healthy" }
        3-> SamplePlantData.scans.filter { it.status=="Diseased" }
        else -> SamplePlantData.scans
    }
    Column(modifier = Modifier.fillMaxWidth()
        .offset(y=-15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          scans.map { scan ->
              key(scan.id) {
                  Text(scan.text
                  ,
                      modifier = Modifier
                          .clickable(onClick = {active=scan.id})
                          .background(if (active==scan.id){
                              MaterialTheme.colorScheme.secondary
                          }else{
                              MaterialTheme.colorScheme.tertiary
                          },
                              shape = RoundedCornerShape(100.dp)
                          )
                          .padding(5.dp)
                      ,
                      color = if(active==scan.id){
                          MaterialTheme.colorScheme.onSecondary
                      }else{
                          MaterialTheme.colorScheme.onTertiary
                      }
                      )
              }
          }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            filteredScans.forEach {
                scan ->
                ScanCard(
                    img=scan.imageUrl,
                    plantName = scan.plantName,
                    healthLabel = scan.healthLabel,
                    scanDate = scan.scanDate,
                    status = scan.status,
                    onClick = {navController.navigate("plant/{plantId}")}
                )
            }
        }
    }

}
