package com.example.mavuno.ui.screens.dashboard

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mavuno.R
import com.example.mavuno.ui.components.DashHeader
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun ReviewPhotoScreen(
    imageUri: Uri?,
    onRetake: () -> Unit,
    onAnalyze: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    val scanViewModel: ScanViewModel = viewModel()
    val scanState by scanViewModel.scanState.collectAsState()

    val deepGreen = MaterialTheme.colorScheme.primary
    val paleGreen = MaterialTheme.colorScheme.primaryContainer
    val textGreen = CropTheme.colors.heading
    val mutedGreen = CropTheme.colors.tag

    DashboardLayout {
        // TOPBAR
        DashHeader(
            head = "Review photo",
            onBack = {
                scanViewModel.resetState()
                onRetake()
            }
        )

        Text(
            text = "Make sure the leaf and its marks are clear.",
            fontSize = 15.sp,
            color = mutedGreen
        )

        // IMAGE PREVIEW
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color.LightGray)
        ) {
            // Scale preview height with available width so it fits on small screens
            val previewHeight = (maxWidth * 0.85f).coerceAtMost(380.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(previewHeight)
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Captured plant photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.maize_diseased_1),
                        contentDescription = "Default plant photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // RESULT OR INITIAL CARDS
        when (val state = scanState) {
            is ScanState.Success -> {
                val scan = state.scan
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(paleGreen, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Analysis Result",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepGreen
                    )
                    Text(
                        text = "Plant: ${scan.plant_name.ifBlank { "Unknown Plant" }}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textGreen
                    )
                    Text(
                        text = "Condition: ${if (scan.is_healthy) "Healthy" else scan.disease.ifBlank { "Diseased" }}",
                        fontSize = 15.sp,
                        color = textGreen
                    )
                    if (scan.confidence != null) {
                        Text(
                            text = "Confidence: ${String.format(java.util.Locale.US, "%.1f%%", scan.confidence * 100)}",
                            fontSize = 14.sp,
                            color = mutedGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            scanViewModel.resetState()
                            onRetake()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = deepGreen
                        )
                    ) {
                        Text(
                            text = "Scan Another Plant",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            else -> {
                // YOUR PLANT PHOTO CARD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CropTheme.colors.inputBackground, RoundedCornerShape(16.dp))
                        .border(0.2.dp, CropTheme.colors.inputBorder.copy(0.3f), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(paleGreen, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = "Photo icon",
                                tint = deepGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Your plant photo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = CropTheme.colors.heading
                            )
                            Text(
                                text = "22 October 2026 · Camera",
                                fontSize = 13.sp,
                                color = mutedGreen
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(deepGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checked",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // NOTICE BOX
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(paleGreen)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        tint = deepGreen,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "We'll look for visible signs of disease and suggest care steps. A photo alone cannot confirm a diagnosis.",
                        fontSize = 13.sp,
                        color = textGreen,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (state is ScanState.Error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ACTION BUTTONS
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (imageUri != null) {
                                scanViewModel.predictScan(context, imageUri)
                            } else {
                                onAnalyze()
                            }
                        },
                        enabled = scanState !is ScanState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = deepGreen
                        )
                    ) {
                        if (scanState is ScanState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Analyze",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Analyze plant",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            scanViewModel.resetState()
                            onRetake()
                        },
                        enabled = scanState !is ScanState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.dp, deepGreen),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = deepGreen
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Retake",
                                tint = deepGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Retake photo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = deepGreen
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Blurry or too dark? Retake it in natural light.",
                        fontSize = 13.sp,
                        color = mutedGreen
                    )
                }
            }
        }
    }
}
