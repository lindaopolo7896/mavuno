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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mavuno.data.SamplePlantData

@Composable
fun HealthLabel(
    status:String,
){
    Box(
        modifier = Modifier
            .background(
                if (status=="Healthy"){
                    Color(0xffE6F4E5)
            }else{
                    Color(0xffF4E5E6)
            },
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 5.dp)

    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (status == "Healthy") {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.Error
                },
                contentDescription = "status",
                tint = if (status == "Healthy") {
                    Color(0xff42A565)

                } else {
                    Color(0xffB03232)

                },
                modifier = Modifier.size(12.dp)
            )
            Text(
                status,
                color = if (status == "Healthy") {
                    Color(0xff42A565)
                } else {
                    Color(0xffB03232)
                },
                fontSize = 12.sp

            )
        }
    }

}
