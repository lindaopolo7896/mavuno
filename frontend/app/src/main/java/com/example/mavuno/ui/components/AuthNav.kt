package com.example.mavuno.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun AuthNav(login: Boolean, register: Boolean, navController: NavHostController) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            // heightIn instead of fixed height so the tab bar grows with font scale
            .heightIn(min = 50.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = CropTheme.colors.inputBackground
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("signin") },
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (login)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    "Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (login)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        CropTheme.colors.heading
                )
            }
            Button(
                onClick = { navController.navigate("signup") },
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (register)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    "Register",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (register)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        CropTheme.colors.heading
                )
            }
        }
    }
}
