package com.example.mavuno.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SettingsCard(
    label: String,
    subLabel: String,
    content:@Composable ()-> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
        ) {
            Text(
                label,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 15.sp
            )
            Text(
                subLabel,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 13.sp


            )
        }
        content()
    }
}