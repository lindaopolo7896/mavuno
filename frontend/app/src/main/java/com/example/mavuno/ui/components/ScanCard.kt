package com.example.mavuno.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun ScanCard(
    img:Int,
    plantName: String,
    healthLabel:String,
    scanDate: String,
    status:String,
    onClick: () -> Unit
){
    Row(

        modifier = Modifier.fillMaxWidth()
            .background(CropTheme.colors.inputBackground)
            .border(0.2.dp, CropTheme.colors.inputBorder.copy(0.3f),
                RoundedCornerShape(8.dp))
            .padding(10.dp)
            .clickable(onClick =onClick)
        ,

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(img),
                contentDescription = "leaf image",
                modifier = Modifier.size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),

                )

            Column() {
                Text(
                    plantName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(healthLabel,
                    fontSize = 16.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = "Calendar",
                        modifier = Modifier.size(12.dp)
                    )
                    Text(scanDate,
                        fontSize = 14.sp
                    )
                }



            }
        }

        HealthLabel(status)
    }
}