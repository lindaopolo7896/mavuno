package com.example.mavuno.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun AuthHeader(
    headLine: String,
    tagLine: String,
    signup: Boolean = false,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // softWrap = true (default) + textAlign Center ensures the headline
        // wraps gracefully at any screen width instead of overflowing
        Text(
            headLine,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            color = CropTheme.colors.heading,
            softWrap = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                tagLine,
                color = CropTheme.colors.tag,
                textAlign = TextAlign.Center,
                softWrap = true
            )
            if (signup) {
                Text(
                    " Sign in",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = { navController.navigate("signin") })
                )
            }
        }
    }
}
