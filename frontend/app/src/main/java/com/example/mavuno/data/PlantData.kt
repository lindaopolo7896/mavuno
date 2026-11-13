package com.example.mavuno.data

import androidx.compose.ui.graphics.vector.ImageVector

data class PlantScan(
    val id: Int,
    val plantName: String,
    val plantType: String,
    val imageUrl: Int,
    val scanDate: String,
    val status: String,
    val healthLabel: String,
    val confidence: Float,
    val disease: DiseaseInfo? = null
)

data class DiseaseInfo(
    val name: String,
    val description: String,
    val symptoms: List<String>,
    val causes: List<String>,
    val recommendations: List<String>
)

data class CareTip(
    val id: Int,
    val title: String,
    val description: String,
    val category: String
)