package com.example.mavuno.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.ui.theme.CropTheme


data class Item(
    val id: Int,
    val icon: ImageVector,
    val text: String,
    val link: String
)

val items=listOf(
    Item(
        1,
        Icons.Outlined.Home,
        "Home",
        "home"

    ),
    Item(
        2,
        Icons.Outlined.CameraAlt,
        "Scan",
        "scan"

    ),
    Item(
        3,
        Icons.Outlined.History,
        "History",
        "history"

    ),

    Item(
        4,
        Icons.Outlined.Settings,
        "Settings",
        "settings"

    ),

)

@Composable
fun BottomBar(
    navController: NavHostController
){
    val inputBorder = CropTheme.colors.inputBorder
    Column(
        modifier = Modifier.fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = inputBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical=15.dp, horizontal = 20.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.map { item ->
            Column(
                modifier = Modifier.clickable(
                    onClick = { navController.navigate(item.link) }
                ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = "icon"
                )

                Text(
                    item.text
                )
            }
        }
    }
    }
}
