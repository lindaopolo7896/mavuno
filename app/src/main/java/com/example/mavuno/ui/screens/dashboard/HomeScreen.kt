package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.data.SamplePlantData
import com.example.mavuno.ui.components.HealthLabel
import com.example.mavuno.ui.components.ScanCard
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme


data class Option(
    val id: Int,
    val icon: ImageVector,
    val contentDescription: String,
    val head: String,
    val tag: String,
    val link:String
)

val options=listOf(
    Option(
        1,
        Icons.Default.History,
        "history",
        "Scan History",
        "View your previous scans",
        "scan-history"
    ),
    Option(
        2,
        Icons.Outlined.MenuBook,
        "book",
        "Disease Guide",
        "Learn about common diseases",
        "scan-history"
    )
)

@Composable
fun HomeScreen(navController: NavHostController){
    DashboardLayout() {
  //        TOPBAR
          Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier
                  .fillMaxWidth()
          ) {
              Row() {
                  Image(
                      painter = painterResource(R.drawable.logo),
                      contentDescription = "Logo",
                      contentScale = ContentScale.Fit,
                      modifier = Modifier.size(50.dp)
                  )
                  Column() {
                      Text(
                          "Hello, User",
                          fontSize = 20.sp,
                          color = CropTheme.colors.heading,
                          fontWeight = FontWeight.SemiBold
                      )

                      Text(
                          "Healthy  Crops | Better Harvest",
                          fontSize = 15.sp,
                          color = CropTheme.colors.tag,
                          fontWeight = FontWeight.Medium
                      )

                  }
              }

              Row(
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                  Box(
                      modifier = Modifier
                          .background(Color(0xffE5F4E4), shape = RoundedCornerShape(100.dp))
                          .size(40.dp)
                      ,
                  ){
                      Icon(
                          imageVector = Icons.Outlined.Notifications,
                          contentDescription = "notifications icon",
                          modifier = Modifier
                              .align(Alignment.Center),
                          tint= MaterialTheme.colorScheme.primary
                      )
                  }
                  Box(
                      modifier = Modifier
                          .background(Color(0xffE5F4E4), shape = RoundedCornerShape(100.dp))
                          .size(40.dp)
                      ,
                  ){
                      Icon(
                          imageVector = Icons.Filled.Person,
                          contentDescription = "person",
                          modifier = Modifier
                              .align(Alignment.Center),
                          tint= MaterialTheme.colorScheme.primary
                      )
                  }
              }
          }

  //        SCAN
  Box(
      modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
  ){
      Image(
          painter = painterResource(R.drawable.banner2),
          contentDescription = "Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()

      )

      Column(
          modifier = Modifier
              .padding(horizontal = 15.dp, vertical = 26.dp)
              .fillMaxHeight(),
          verticalArrangement = Arrangement.SpaceBetween,

      ){
          Text(
              "Check your crop health",
              fontSize = 24.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF014844),
              modifier = Modifier.fillMaxWidth(0.4f),

          )
           Text(
              "Scan a leaf to detect possible diseases and get treatment advice",
              fontSize = 14.sp,
               color = Color.Black.copy(0.8f),
              fontWeight = FontWeight.Medium,
              modifier = Modifier.fillMaxWidth(0.6f),

          )
          Button(
              onClick = {},
              modifier = Modifier
                  .fillMaxWidth(0.85f)
                  .height(45.dp)
                  .border(1.dp, Color.White, shape = RoundedCornerShape(100.dp))

              ,
              colors = ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.primary
              ),

          ) {
              Row(
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
              ) {
                  Row(
                      horizontalArrangement = Arrangement.spacedBy(10.dp),
                      verticalAlignment = Alignment.CenterVertically
                  ) {
                      Icon(
                          modifier = Modifier.size(16.dp),
                          imageVector = Icons.Default.CameraAlt,
                          tint= Color.White,
                          contentDescription = "Camera"
                      )

                      Text(
                          "Scan Plant",
                          color = Color.White
                      )
                  }
              Icon(
                  imageVector = Icons.Default.ArrowForward,
                  contentDescription = "Arrow",
                  tint= Color.White,
                  modifier = Modifier.size(15.dp)
              )


              }
          }
      }
  }





  //        RECENT SCANS
          val recentScans= SamplePlantData.scans.take(4)

          Column(
              modifier = Modifier.fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(15.dp)
          ) {
              Row(
                  modifier= Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
              ) {
                  Text(
                      "Recent Scans",
                      fontSize = 20.sp,
                      fontWeight = FontWeight.SemiBold,
                      color= CropTheme.colors.heading

                  )

                  Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(10.dp),
                      modifier = Modifier.clickable(onClick = {navController.navigate("history")})
                  ) {
                      Text("View all",
                          color = CropTheme.colors.tag)
                      Icon(
                          imageVector = Icons.Default.ArrowForward,
                          contentDescription = "Arrow",
                          modifier = Modifier.size(15.dp),
                          tint= CropTheme.colors.tag
                      )
                  }
              }

              Column(
                  modifier = Modifier.fillMaxWidth(),
                  verticalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                  recentScans.map { scan ->
                      key(scan.id) {
                          ScanCard(
                              img = scan.imageUrl,
                              plantName = scan.plantName,
                              healthLabel = scan.healthLabel,
                              status = scan.status,
                              scanDate = scan.scanDate,
                              onClick = {navController.navigate("plant/{plantId}")}
                          )

                      }
                  }
              }
          }

      }
}