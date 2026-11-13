package com.example.mavuno.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun SwitchComponent(
    checked: Boolean,
    onChecked:(Boolean)-> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            if(checked==true){
                "On"
            }else{
                "Off"
            },
            fontSize = 12.sp,
            color = if(checked==true){
                MaterialTheme.colorScheme.primary
            }else{
                MaterialTheme.colorScheme.secondary
            },


            )
        Switch(
            checked = checked,
            onCheckedChange = onChecked,
            modifier = Modifier.scale(0.8f),

            colors = SwitchDefaults.colors(
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent,
                uncheckedTrackColor = Color(0xff405045),
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.background,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary
            )

        )
    }
}