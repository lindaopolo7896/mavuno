package com.example.mavuno.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.components.AuthHeader
import com.example.mavuno.ui.components.Btn
import com.example.mavuno.ui.components.Input

@Composable
fun ResetScreen(navController: NavHostController) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AuthHeader(
            "Setup a new password",
            "Your new password must be different from previously used passwords",
            false,
            navController
        )
        Spacer(modifier = Modifier.height(30.dp))

        Input(
            label = "New Password",
            value = password,
            onValueChange = { password = it },
            placeholder = "Setup your password",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        Input(
            label = "Confirm Password",
            value = confirmPass,
            onValueChange = { confirmPass = it },
            placeholder = "Confirm your password",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        Btn(onClick = { navController.navigate("signin") }, "Confirm")

        Spacer(modifier = Modifier.height(32.dp))
    }
}
