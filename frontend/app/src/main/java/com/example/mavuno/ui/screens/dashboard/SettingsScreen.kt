package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.data.local.TokenManager
import com.example.mavuno.data.repository.AuthRepository
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme
import com.example.mavuno.ui.theme.ThemeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context.applicationContext) }
    val profileViewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = AuthRepository(tokenManager = tokenManager)
                return ProfileViewModel(repository) as T
            }
        }
    )
    val profileState by profileViewModel.profileState.collectAsState()
    val deletionState by profileViewModel.deletionState.collectAsState()

    var appearanceSelection by remember { mutableStateOf(ThemeManager.selectedTheme) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val displayName = when (val state = profileState) {
        is ProfileState.Success -> state.user.full_name?.takeIf { it.isNotBlank() } ?: "Farmer"
        else -> "Loading..."
    }

    val displayEmail = when (val state = profileState) {
        is ProfileState.Success -> state.user.email
        else -> ""
    }

    DashboardLayout {
        // TOPBAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                fontSize = 30.sp,
                color = CropTheme.colors.heading,
                fontWeight = FontWeight.SemiBold
            )
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // PROFILE CARD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = CropTheme.colors.inputBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(onClick = { navController.navigate("edit-profile") })
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CropTheme.colors.heading
                    )
                    Text(
                        text = displayEmail,
                        fontSize = 14.sp,
                        color = CropTheme.colors.tag
                    )
                    Text(
                        text = "Edit profile",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(onClick = {
                                navController.navigate("edit-profile")
                            })
                            .padding(top = 2.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open profile",
                tint = CropTheme.colors.heading,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // PREFERENCES SECTION
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Preferences",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = CropTheme.colors.heading
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CropTheme.colors.inputBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                // Use BoxWithConstraints so the appearance chips stack below
                // the label on narrow screens instead of overflowing
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val isNarrow = maxWidth < 360.dp
                    if (isNarrow) {
                        // Narrow screen: stack label and chips vertically
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Language,
                                    contentDescription = "Appearance",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "Appearance",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CropTheme.colors.heading
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf("Light", "Dark", "System default").forEach { mode ->
                                    val isSelected = appearanceSelection == mode
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                width = if (isSelected) 1.dp else 0.5.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else CropTheme.colors.inputBorder,
                                                shape = RoundedCornerShape(50.dp)
                                            )
                                            .clickable(onClick = {
                                                appearanceSelection = mode
                                                ThemeManager.selectedTheme = mode
                                            })
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = mode,
                                            fontSize = 12.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else CropTheme.colors.tag,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Normal screen: label and chips side by side
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Language,
                                    contentDescription = "Appearance",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "Appearance",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CropTheme.colors.heading
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf("Light", "Dark", "System default").forEach { mode ->
                                    val isSelected = appearanceSelection == mode
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                width = if (isSelected) 1.dp else 0.5.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else CropTheme.colors.inputBorder,
                                                shape = RoundedCornerShape(50.dp)
                                            )
                                            .clickable(onClick = {
                                                appearanceSelection = mode
                                                ThemeManager.selectedTheme = mode
                                            })
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = mode,
                                            fontSize = 12.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else CropTheme.colors.tag,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // ACCOUNT & PRIVACY SECTION
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Account & privacy",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = CropTheme.colors.heading
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CropTheme.colors.inputBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Change password item
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { /* Change password action */ })
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "Change password",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Change password",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = CropTheme.colors.heading
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open change password",
                            tint = CropTheme.colors.tag,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    HorizontalDivider(
                        color = CropTheme.colors.inputBorder,
                        thickness = 1.dp
                    )

                    // Delete account item
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                profileViewModel.resetDeletionState()
                                showDeleteDialog = true
                            })
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Security,
                                contentDescription = "Delete account",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Delete account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = CropTheme.colors.heading
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open delete account",
                            tint = CropTheme.colors.tag,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // LOG OUT BUTTON
        OutlinedButton(
            onClick = {
                val tokenManager = TokenManager(context.applicationContext)
                val repository = AuthRepository(tokenManager = tokenManager)
                coroutineScope.launch(Dispatchers.IO) {
                    repository.logout()
                }
                navController.navigate("signin") {
                    popUpTo("main") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, Color(0xFF405045).copy(alpha = 0.4f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = CropTheme.colors.heading
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Log out",
                    modifier = Modifier.size(20.dp),
                    tint = CropTheme.colors.heading
                )
                Text(
                    text = "Log out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CropTheme.colors.heading
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                if (deletionState !is AccountDeletionState.Loading) {
                    showDeleteDialog = false
                    profileViewModel.resetDeletionState()
                }
            },
            title = {
                Text(
                    text = "Delete Account?",
                    fontWeight = FontWeight.Bold,
                    color = CropTheme.colors.heading
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Are you sure you want to delete your account? This action may permanently delete your account and associated information. You will not be able to recover your account once deletion is completed.",
                        color = CropTheme.colors.tag
                    )
                    if (deletionState is AccountDeletionState.Error) {
                        Text(
                            text = (deletionState as AccountDeletionState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        profileViewModel.deleteAccount {
                            showDeleteDialog = false
                            navController.navigate("signin") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    enabled = deletionState !is AccountDeletionState.Loading
                ) {
                    if (deletionState is AccountDeletionState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Delete Account")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (deletionState !is AccountDeletionState.Loading) {
                            showDeleteDialog = false
                            profileViewModel.resetDeletionState()
                        }
                    },
                    enabled = deletionState !is AccountDeletionState.Loading
                ) {
                    Text(
                        text = "Cancel",
                        color = CropTheme.colors.heading
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
