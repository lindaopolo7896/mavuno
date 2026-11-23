package com.example.mavuno.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.ui.components.AuthHeader
import com.example.mavuno.ui.components.Btn
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun VerifyScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(R.drawable.code),
            contentDescription = "Verify code illustration",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        AuthHeader(
            "Enter Code",
            "A code has been sent to your email",
            false,
            navController
        )
        Spacer(modifier = Modifier.height(20.dp))

        // OTP input row — each box is a single numeric character field
        // heightIn instead of fixed height so the box grows with font scale
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = androidx.compose.ui.unit.TextUnit(
                            18f, androidx.compose.ui.unit.TextUnitType.Sp
                        )
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = CropTheme.colors.inputBackground,
                        focusedContainerColor = CropTheme.colors.inputBackground,
                        unfocusedTextColor = CropTheme.colors.heading,
                        focusedTextColor = CropTheme.colors.heading,
                        unfocusedIndicatorColor = CropTheme.colors.inputBorder,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Btn(onClick = { navController.navigate("reset") }, "Confirm")

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow Back",
                    tint = CropTheme.colors.tag
                )
                Text(
                    "Back to Login",
                    color = CropTheme.colors.tag,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable(onClick = { navController.navigate("signin") })
                        .padding(horizontal = 5.dp)
                )
            }
            Text(
                "Request again?",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = {})
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
