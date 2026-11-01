package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.components.SettingsCard
import com.example.mavuno.ui.components.SwitchComponent
import com.example.mavuno.ui.layouts.DashboardLayout

@Composable
fun SettingsScreen(navController: NavHostController){
    DashboardLayout {
        var save by remember{ mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = "NOTIFICATIONS",

                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xff10241C)
                ),
                modifier = Modifier
                    .border(1.dp, Color(0xff405045), shape = RoundedCornerShape(12.dp))

            ) {

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SettingsCard(
                        "Reading reminders",
                        "Your preferences and information",
                        {
                            Row(){
                                Text("English")
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = "Arrow",

                            )

                        }
                    )
                    HorizontalDivider()
                    SettingsCard(
                        "Reading reminders",
                        "Your preferences and information",
                        {
                            SwitchComponent(
                                checked = save,
                                onChecked = {save=it}
                            )

                        }
                    )

                }
            }
        }
    }
}