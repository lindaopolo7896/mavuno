package com.example.mavuno.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.ui.components.AuthHeader
import com.example.mavuno.ui.components.Btn
import com.example.mavuno.ui.components.Input
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun ForgotScreen(navController: NavHostController) {
    var email by rememberSaveable { mutableStateOf("") }

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

        Image(
            painter = painterResource(R.drawable.forgot),
            contentDescription = "Forgot password illustration",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        AuthHeader(
            "Forgot Password",
            "Please enter the email associated with your account",
            false,
            navController
        )
        Spacer(modifier = Modifier.height(20.dp))

        Input(
            "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter your email"
        )
        Spacer(modifier = Modifier.height(20.dp))

        Btn(onClick = { navController.navigate("verify") }, "Send Code")
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow Back",
                tint = CropTheme.colors.tag
            )
            Text(
                "Back to Login",
                color = CropTheme.colors.tag,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate("signin") })
                    .padding(horizontal = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
