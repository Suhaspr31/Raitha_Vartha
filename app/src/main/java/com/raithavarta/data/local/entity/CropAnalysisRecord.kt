package com.raithavarta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crop_analysis_history")
data class CropAnalysisRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val plant: String,
    val disease: String,
    val remedyKannada: String,
    val timestamp: Long = System.currentTimeMillis()
)
