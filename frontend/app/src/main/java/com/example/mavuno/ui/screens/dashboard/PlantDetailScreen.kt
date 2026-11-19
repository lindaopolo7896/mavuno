package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mavuno.R
import com.example.mavuno.ui.components.DashHeader
import com.example.mavuno.ui.components.HealthLabel
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun PlantDetailScreen(scanId: String, navController: NavHostController) {
    val detailViewModel: ScanDetailViewModel = viewModel()
    val detailState by detailViewModel.detailState.collectAsState()

    LaunchedEffect(scanId) {
        detailViewModel.loadScanDetail(scanId)
    }

    DashboardLayout {
        DashHeader(
            head = "Scan Details",
            onBack = { navController.popBackStack() }
        )

        when (val state = detailState) {
            is ScanDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is ScanDetailState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 15.sp
                    )
                }
            }
            is ScanDetailState.Success -> {
                val scan = state.scan
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Plant Image & Basic Info Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CropTheme.colors.inputBackground, RoundedCornerShape(16.dp))
                            .border(0.2.dp, CropTheme.colors.inputBorder.copy(0.3f), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (scan.image != null) {
                            AsyncImage(
                                model = scan.image,
                                contentDescription = scan.plant_name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 160.dp, max = 240.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.maize_diseased_1),
                                contentDescription = scan.plant_name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 160.dp, max = 240.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = scan.plant_name.ifBlank { "Plant Scan" },
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CropTheme.colors.heading
                                )
                                Text(
                                    text = if (scan.is_healthy) "Healthy" else scan.disease,
                                    fontSize = 15.sp,
                                    color = CropTheme.colors.tag
                                )
                            }
                            HealthLabel(if (scan.is_healthy) "Healthy" else "Diseased")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarToday,
                                    contentDescription = "Scan Date",
                                    modifier = Modifier.size(14.dp),
                                    tint = CropTheme.colors.tag
                                )
                                Text(
                                    text = scan.scanned_at.take(10),
                                    fontSize = 14.sp,
                                    color = CropTheme.colors.tag
                                )
                            }

                            if (scan.confidence != null) {
                                Text(
                                    text = "Confidence: ${(scan.confidence * 100).toInt()}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Delete button
                    OutlinedButton(
                        onClick = {
                            detailViewModel.deleteScan(scan.id) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Delete Scan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
