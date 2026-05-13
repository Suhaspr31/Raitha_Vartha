package com.raithavarta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flash_cards")
data class FlashCard(
    @PrimaryKey val id: String,
    val title: String,
    val descriptionKannada: String,
    val imageUrl: String,
    val cropType: String = "",
    val timestamp: Long
)
