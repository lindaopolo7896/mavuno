package com.example.mavuno.data.model

data class ScanPageResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Scan>
)
