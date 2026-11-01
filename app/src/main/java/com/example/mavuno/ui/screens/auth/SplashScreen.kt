package com.example.mavuno.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.R

@Composable
fun SplashScreen(navController: NavHostController){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.back),
            contentDescription = "farmers",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.15f)
                )
        )
        Card(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .align(Alignment.BottomCenter)


                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(28.dp)
                )
                ,
            shape = RoundedCornerShape(28.dp)
            ,
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier
                .fillMaxHeight().padding(horizontal = 20.dp, vertical = 60.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "AI-Powered Detection for Healthier Crops and Better Harvests",
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                )

                Text(
                    "Simply capture or upload a crop photo to identify diseases early and care for your plants.",
color = Color.Black.copy(alpha=0.73f),
                    textAlign = TextAlign.Center,
                    )
                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White)

                        .padding(horizontal = 10.dp)
                        ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ){
                Button(
                    onClick = {navController.navigate("signup")},
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(47.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Get Started", color = Color.White)
                }

                    Row() {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "arrow",
                            modifier = Modifier
                                .size(20.dp),
                            tint= Color(0xff014844)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "arrow",
                            modifier = Modifier
                                .size(20.dp),
                            tint= Color(0x80014844)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "arrow",
                            modifier = Modifier
                                .size(20.dp),
                            tint= Color(0x40014844)
                        )
                    }

                }

            }
        }

    }
}