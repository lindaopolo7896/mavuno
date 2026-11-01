package com.example.mavuno.ui.components

import android.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun AuthNav(login: Boolean,register: Boolean,navController: NavHostController){

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(30.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Row(){
            Button(
                onClick = {navController.navigate("signin")},
                modifier= Modifier
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor =if(login==true)
                        Color(0xff014844)
                    else
                        Color.Transparent
                ),
                shape = RoundedCornerShape(30.dp)
                ) {
                Text(
                    "Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if(login==true)
                        Color.White
                    else
                        Color.Black
                )
            }
            Button(
                onClick = {navController.navigate("signup")},
                modifier= Modifier
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor =if(register==true)
                        Color(0xff014844)
                    else
                        Color.Transparent
                ),
            ) {
                Text(
                    "Register",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if(register==true)
                        Color.White
                    else
                        Color.Black)

            }
        }
    }
}