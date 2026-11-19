package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.ui.components.ScanCard
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme

data class Option(
    val id: Int,
    val icon: ImageVector,
    val contentDescription: String,
    val head: String,
    val tag: String,
    val link: String
)

val options = listOf(
    Option(
        1,
        Icons.Default.History,
        "history",
        "Scan History",
        "View your previous scans",
        "scan-history"
    ),
    Option(
        2,
        Icons.Outlined.MenuBook,
        "book",
        "Disease Guide",
        "Learn about common diseases",
        "scan-history"
    )
)

@Composable
fun HomeScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState by profileViewModel.profileState.collectAsState()

    val scanHistoryViewModel: ScanHistoryViewModel = viewModel()
    val historyState by scanHistoryViewModel.historyState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        scanHistoryViewModel.loadScans()
    }

    val displayName = when (val state = profileState) {
        is ProfileState.Success -> state.user.full_name?.takeIf { it.isNotBlank() } ?: "Farmer"
        else -> "User"
    }

    DashboardLayout {
        // TOPBAR
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp)
                )
                Column {
                    Text(
                        "Hello, $displayName",
                        fontSize = 20.sp,
                        color = CropTheme.colors.heading,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        "Healthy Crops | Better Harvest",
                        fontSize = 15.sp,
                        color = CropTheme.colors.tag,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(100.dp))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "notifications icon",
                        modifier = Modifier.align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(100.dp))
                        .size(40.dp)
                        .clickable(onClick = { navController.navigate("settings") })
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "person",
                        modifier = Modifier.align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // SCAN BANNER
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val bannerHeight = when {
                maxWidth <= 360.dp -> 240.dp
                maxWidth <= 600.dp -> 220.dp
                else -> 200.dp
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Color.Transparent,
                        RoundedCornerShape(20.dp)
                    ),
            ) {
                Image(
                    painter = painterResource(R.drawable.banner2),
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 16.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Check your crop health",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF014844),
                        modifier = Modifier.fillMaxWidth(0.55f),
                    )
                    Text(
                        "Scan a leaf to detect possible diseases and get treatment advice",
                        fontSize = 14.sp,
                        color = Color.Black.copy(0.8f),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(0.7f),
                    )
                    Button(
                        onClick = { navController.navigate("scan") },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .heightIn(48.dp)
                            .border(1.dp, Color.White, shape = RoundedCornerShape(100.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    imageVector = Icons.Default.CameraAlt,
                                    tint = Color.White,
                                    contentDescription = "Camera"
                                )

                                Text(
                                    "Scan Plant",
                                    color = Color.White
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Arrow",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }

        // RECENT SCANS
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Recent Scans",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CropTheme.colors.heading
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.clickable(onClick = { navController.navigate("history") })
                ) {
                    Text("View all", color = CropTheme.colors.tag)
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Arrow",
                        modifier = Modifier.size(15.dp),
                        tint = CropTheme.colors.tag
                    )
                }
            }

            when (val state = historyState) {
                is ScanHistoryState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is ScanHistoryState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
                is ScanHistoryState.Success -> {
                    val recentScans = state.scans.take(4)
                    if (recentScans.isEmpty()) {
                        Text(
                            text = "No recent scans yet. Tap 'Scan Plant' above to get started!",
                            color = CropTheme.colors.tag,
                            fontSize = 14.sp
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            recentScans.forEach { scan ->
                                key(scan.id) {
                                    ScanCard(
                                        img = scan.image ?: R.drawable.maize_diseased_1,
                                        plantName = scan.plant_name.ifBlank { "Plant Scan" },
                                        healthLabel = if (scan.is_healthy) "Healthy" else scan.disease.ifBlank { "Disease detected" },
                                        status = if (scan.is_healthy) "Healthy" else "Diseased",
                                        scanDate = scan.scanned_at.take(10),
                                        onClick = { navController.navigate("plant/${scan.id}") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
