package com.example.mavuno.screens.auth

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.components.AuthHeader
import com.example.mavuno.components.Btn

@Composable
fun VerifyScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }





    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
            .background(Color(0xffF8FDF6))

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Image(
                painter = painterResource(R.drawable.code),
                contentDescription = "farmers",

                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            AuthHeader(
                "Enter Code",
                "A code has been sent to your email",
                false,
                navController
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier
                .fillMaxWidth()) {
                for (i in 0 until 5) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f).padding(horizontal = 7.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))




            Btn(onClick = { navController.navigate("reset") }, "Confirm",)

            Spacer(modifier = Modifier.height(20.dp))
Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth()

) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Arrow Back",
            tint = Color.Black.copy(alpha = 0.63f)
        )

        Text(
            "Back to Login",
            color = Color.Black.copy(alpha = 0.63f),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = { navController.navigate("signin") })
                .padding(horizontal = 5.dp)
        )
    }
    Text(
        "Request again?",
        color = Color(0xff014844),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clickable(onClick = {})
    )
}

        }

    }
}