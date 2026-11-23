package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mavuno.data.remote.NetworkResult
import com.example.mavuno.ui.components.Btn
import com.example.mavuno.ui.components.DashHeader
import com.example.mavuno.ui.components.Input
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun EditProfileScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState by profileViewModel.profileState.collectAsState()
    val updateState by profileViewModel.updateState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val user = (profileState as ProfileState.Success).user
            name = user.full_name ?: ""
            email = user.email
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is NetworkResult.Success) {
            profileViewModel.resetUpdateState()
            navController.popBackStack()
        }
    }

    DashboardLayout {
        // TOPBAR
        DashHeader(
            head = "Edit profile",
            onBack = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // AVATAR & CHANGE PHOTO
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile avatar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(52.dp)
                )
            }

            Row(
                modifier = Modifier
                    .clickable(onClick = { /* Change photo action */ })
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Change photo",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // INPUT FIELDS
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Input(
                label = "Full name",
                value = name,
                onValueChange = { name = it },
                placeholder = "Enter your full name"
            )

            Input(
                label = "Email (Read-only)",
                value = email,
                onValueChange = { /* Email is read-only on backend */ },
                placeholder = "Email address"
            )

            if (updateState is NetworkResult.Error) {
                Text(
                    text = (updateState as NetworkResult.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // BUTTONS
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Btn(
                onClick = {
                    profileViewModel.updateProfile(fullName = name, phoneNumber = null)
                },
                text = if (updateState is NetworkResult.Exception || updateState is NetworkResult.Error || profileState is ProfileState.Loading) "Saving..." else "Save changes"
            )

            OutlinedButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, CropTheme.colors.inputBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CropTheme.colors.heading
                )
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CropTheme.colors.heading
                )
            }
        }
    }
}
