package com.raithavarta.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raithavarta.ai.GeminiService
import com.raithavarta.ai.GroqService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.raithavarta.data.local.dao.CropAnalysisDao
import com.raithavarta.data.local.entity.CropAnalysisRecord
import com.google.firebase.auth.FirebaseAuth

class CameraViewModel(
    private val groqService: GroqService,
    private val geminiService: GeminiService,
    private val cropAnalysisDao: CropAnalysisDao
) : ViewModel() {

    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState

    fun analyzeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _analysisState.value = AnalysisState.Analyzing
            
            try {
                val (plant, disease) = groqService.analyzePlantDisease(bitmap)
                
                if (plant == "error" || plant == "unknown") {
                    _analysisState.value = AnalysisState.Error("ವಿಶ್ಲೇಷಣೆ ವಿಫಲವಾಗಿದೆ: $disease")
                    return@launch
                }
                
                val result = groqService.getDiseaseExplanation(plant, disease)
                
                // Save to history
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
                val timestamp = System.currentTimeMillis()
                cropAnalysisDao.insertRecord(
                    CropAnalysisRecord(
                        userId = userId,
                        plant = plant,
                        disease = disease,
                        remedyKannada = result,
                        timestamp = timestamp
                    )
                )

                // Save to Firebase
                if (userId != "guest") {
                    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    try {
                        db.collection("users").document(userId)
                            .collection("scan_history").add(
                                mapOf(
                                    "plant" to plant,
                                    "disease" to disease,
                                    "remedyKannada" to result,
                                    "timestamp" to timestamp
                                )
                            )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                _analysisState.value = AnalysisState.Success(result)
            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun dismissPopup() {
        _analysisState.value = AnalysisState.Idle
    }

    sealed class AnalysisState {
        object Idle : AnalysisState()
        object Analyzing : AnalysisState()
        data class Success(val resultKannada: String) : AnalysisState()
        data class Error(val message: String) : AnalysisState()
    }
}
