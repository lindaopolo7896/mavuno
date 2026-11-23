package com.example.mavuno.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardLayout(
    content: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scale horizontal padding with available width so dashboard screens
        // are neither cramped on phones nor too wide on large-screen devices
        val horizontalPadding = when {
            maxWidth <= 600.dp -> 18.dp
            maxWidth <= 840.dp -> 26.dp
            else -> 34.dp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                // statusBarsPadding keeps content below the status bar when the
                // app is running edge-to-edge (enableEdgeToEdge is set in MainActivity)
                .statusBarsPadding()
                .padding(
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = 16.dp,
                    bottom = 10.dp
                )
                // rememberScrollState() (not remember { rememberScrollState() })
                // correctly restores scroll position across configuration changes
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            content()
        }
    }
}
