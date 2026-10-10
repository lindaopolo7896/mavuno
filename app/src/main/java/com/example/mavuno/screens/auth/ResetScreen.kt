package com.example.mavuno.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.components.AuthBtn
import com.example.mavuno.components.AuthHeader
import com.example.mavuno.components.AuthNav
import com.example.mavuno.components.Btn
import com.example.mavuno.components.Input

@Composable
fun ResetScreen(navController: NavHostController){
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }





    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
            .background(Color(0xffF8FDF6))

    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AuthHeader("Setup a new password","Your new password must be different from previously used passwords",false,navController)
            Spacer(modifier = Modifier.height(30.dp))


            Input(
                label = "New Password",
                value=password,
                onValueChange = {password = it},
                placeholder = "Setup your password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            Input(
                label = "Confirm Password",
                value=confirmPass,
                onValueChange = {confirmPass = it},
                placeholder = "Confirm your password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            Btn(onClick = { navController.navigate("signin") }, "Confirm",)









        }

    }
}