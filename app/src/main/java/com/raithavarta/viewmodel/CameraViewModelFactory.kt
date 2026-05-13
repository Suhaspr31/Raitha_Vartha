package com.raithavarta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raithavarta.ai.GeminiService
import com.raithavarta.ai.GroqService
import com.raithavarta.data.local.dao.CropAnalysisDao

class CameraViewModelFactory(
    private val groqService: GroqService,
    private val geminiService: GeminiService,
    private val cropAnalysisDao: CropAnalysisDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CameraViewModel(groqService, geminiService, cropAnalysisDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
