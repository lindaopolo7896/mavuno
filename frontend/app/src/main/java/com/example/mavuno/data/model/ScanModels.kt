package com.example.mavuno.data.model

data class Prediction(
    val label: String,
    val plant_name: String?,
    val disease: String?,
    val is_healthy: Boolean?,
    val confidence: Float?
)

data class Scan(
    val id: String,
    val image: String?,
    val plant_name: String,
    val disease: String,
    val confidence: Float?,
    val is_healthy: Boolean,
    val predictions: List<Prediction>?,
    val scanned_at: String,
    val created_at: String
)
