package com.example.mavuno.ui.screens.auth

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
import com.example.mavuno.ui.components.AuthBtn
import com.example.mavuno.ui.components.AuthHeader
import com.example.mavuno.ui.components.AuthNav
import com.example.mavuno.ui.components.Input

@Composable
fun LoginScreen(navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }





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
            AuthHeader("Get started  with smarter farming","Sign in to your account",false,navController)
            Spacer(modifier = Modifier.height(30.dp))
            AuthNav(login = true, register = false,navController)
            Spacer(modifier = Modifier.height(20.dp))

            Input(
                "Email",
                value = email,
                onValueChange = {email = it},
                placeholder = "Enter your email"

            )
            Spacer(modifier = Modifier.height(20.dp))
            Input(
                label = "Password",
                value=password,
                onValueChange = {password = it},
                placeholder = "Setup your password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Forgot Password?",
                color = Color(0xff014844),
                textAlign = TextAlign.Right,

                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable(onClick = {navController.navigate("forgot")})
                    .fillMaxWidth())



            Spacer(modifier = Modifier.height(30.dp))




            AuthBtn(onClick = {navController.navigate("main")},"Login",)
            Spacer(modifier = Modifier.height(30.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account", color = Color(0x83000000))

                    Text(
                        "Sign up",
                        color = Color(0xff014844),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(onClick = {navController.navigate("signup")}))


            }

        }

    }
}