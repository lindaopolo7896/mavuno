package com.example.mavuno.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mavuno.R
import com.example.mavuno.ui.components.Input
import com.example.mavuno.ui.components.ScanCard
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun HistoryScreen(navController: NavHostController) {
    val historyViewModel: ScanHistoryViewModel = viewModel()
    val historyState by historyViewModel.historyState.collectAsState()

    var search by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("All scans") }

    LaunchedEffect(activeTab, search) {
        val filter = if (activeTab == "All scans") null else activeTab
        historyViewModel.loadScans(filterStatus = filter, searchQuery = search)
    }

    DashboardLayout {
        // TOPBAR
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text(
                    "Scan History",
                    fontSize = 30.sp,
                    color = CropTheme.colors.heading,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            }
            Text(
                "Your crop health journey, in one place.",
                fontSize = 16.sp,
                color = CropTheme.colors.tag,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
        }

        // SEARCH INPUT
        Column {
            Input(
                label = "",
                value = search,
                onValueChange = { search = it },
                placeholder = "Search crop or disease",
                isSearch = true
            )
        }

        ScanHist(
            historyState = historyState,
            activeTab = activeTab,
            onTabSelected = { activeTab = it },
            navController = navController
        )
    }
}

val scanTabs = listOf("All scans", "Healthy", "Diseased")

@Composable
fun ScanHist(
    historyState: ScanHistoryState,
    activeTab: String,
    onTabSelected: (String) -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            scanTabs.map { tab ->
                key(tab) {
                    Text(
                        tab,
                        modifier = Modifier
                            .clickable(onClick = { onTabSelected(tab) })
                            .background(
                                if (activeTab == tab) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    CropTheme.colors.inputBackground
                                },
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        color = if (activeTab == tab) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            CropTheme.colors.heading
                        }
                    )
                }
            }
        }

        when (historyState) {
            is ScanHistoryState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is ScanHistoryState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = historyState.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 15.sp
                    )
                }
            }
            is ScanHistoryState.Success -> {
                val scans = historyState.scans
                if (scans.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No scan history found.",
                            color = CropTheme.colors.tag,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        scans.forEach { scan ->
                            ScanCard(
                                img = scan.image ?: R.drawable.maize_diseased_1,
                                plantName = scan.plant_name.ifBlank { "Plant Scan" },
                                healthLabel = if (scan.is_healthy) "Healthy" else scan.disease.ifBlank { "Disease detected" },
                                scanDate = scan.scanned_at.take(10),
                                status = if (scan.is_healthy) "Healthy" else "Diseased",
                                onClick = { navController.navigate("plant/${scan.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}
