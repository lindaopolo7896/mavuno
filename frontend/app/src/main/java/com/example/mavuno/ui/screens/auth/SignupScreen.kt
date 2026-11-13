package com.example.mavuno.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.components.AuthBtn
import com.example.mavuno.ui.components.AuthHeader
import com.example.mavuno.ui.components.AuthNav
import com.example.mavuno.ui.components.Input

@Composable
fun SignupScreen(navController: NavHostController){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }





    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)


    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AuthHeader("Create an Account","Already have an account?", signup = true,navController)
            Spacer(modifier = Modifier.height(30.dp))
            AuthNav(login = false, register = true,navController)
            Spacer(modifier = Modifier.height(20.dp))
            Input(
                "FullName",
                value = name,
                onValueChange = {name = it},
                placeholder = "Enter your full name"

            )
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
            Spacer(modifier = Modifier.height(20.dp))
            Input(
                label = "Confirm Password",
                value=confirmPass,
                onValueChange = {confirmPass = it},
                placeholder = "Confirm your password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(30.dp))




            AuthBtn(onClick = {navController.navigate("main")},"Create Account",)


        }

    }
}