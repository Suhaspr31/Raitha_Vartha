package com.raithavarta.data.model

data class SuccessStory(
    val id: String = "",
    val farmerName: String = "",
    val quoteKannada: String = "",
    val profilePicUrl: String = "",
    val beforeAfterPicUrl: String = "",
    val cropType: String = "",
    val rating: Float = 5f,
    val yieldIncrease: String = "",
    val isVerified: Boolean = true
)
