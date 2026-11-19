package com.example.mavuno.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mavuno.ui.theme.CropPalette
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun HealthLabel(
    status:String,
){
    val isHealthy = status == "Healthy"
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    val backgroundColor = if (isHealthy) {
        if (isDark) CropPalette.ForestContainer else CropPalette.MintPale
    } else {
        if (isDark) CropPalette.ErrorDark else CropPalette.ErrorPale
    }
    val contentColor = if (isHealthy) {
        if (isDark) CropPalette.MintAccent else CropPalette.GreenIndicator
    } else {
        if (isDark) CropPalette.ErrorBright else CropPalette.ErrorRed
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)

    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isHealthy) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.Error
                },
                contentDescription = "status",
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
            Text(
                status,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
        }
    }

}
