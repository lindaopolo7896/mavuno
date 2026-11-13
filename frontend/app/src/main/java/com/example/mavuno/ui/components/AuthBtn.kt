package com.example.mavuno.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mavuno.R

@Composable
fun AuthBtn(
    onClick: () -> Unit,
    text: String
){
    Column() {
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )

        ) {
            Text(text, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f),
                modifier = Modifier.weight(0.8f))
            Text("or continue with", modifier= Modifier.padding(horizontal = 10.dp),color = Color.Black.copy(alpha = 0.4f))
            HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f),
                modifier = Modifier.weight(0.8f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick={},
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.google),
                        contentDescription = "google icon",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text("Google", fontSize = 18.sp, color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 5.dp))
                }
            }

            Spacer(modifier = Modifier.width(20.dp))
            OutlinedButton(onClick={},
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.apple),
                        contentDescription = "apple icon",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text("Apple", fontSize = 18.sp, color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 5.dp))
                }
            }

        }


        }
    }
