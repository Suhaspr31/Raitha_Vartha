package com.raithavarta.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raithavarta.data.local.dao.CropAnalysisDao
import com.raithavarta.data.local.dao.FlashCardDao

class ProfileViewModelFactory(
    private val cropAnalysisDao: CropAnalysisDao,
    private val flashCardDao: FlashCardDao,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(cropAnalysisDao, flashCardDao, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
