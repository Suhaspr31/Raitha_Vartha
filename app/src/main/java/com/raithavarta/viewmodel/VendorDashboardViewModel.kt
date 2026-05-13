package com.raithavarta.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.raithavarta.ai.GeminiService
import com.raithavarta.utils.PdfReaderUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class VendorDashboardViewModel(
    private val geminiService: GeminiService,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    fun processAndUploadPdf(context: Context, pdfUri: Uri, title: String) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Processing("Extracting text from PDF...")
            
            try {
                // 1. Read PDF Text
                val text = PdfReaderUtil.extractTextFromPdf(context, pdfUri)
                
                // 2. Generate Kannada Summary using Gemini
                _uploadState.value = UploadState.Processing("Generating AI summary...")
                val summary = geminiService.summarizeToKannada(text)
                
                // 3. Save to Firestore 'tips' collection
                _uploadState.value = UploadState.Processing("Saving to Database...")
                val tipData = hashMapOf(
                    "title" to title,
                    "descriptionKannada" to summary,
                    "imageUrl" to "", // Vendor might upload an image later, or we can generate one
                    "timestamp" to System.currentTimeMillis()
                )
                
                firestore.collection("tips")
                    .document(UUID.randomUUID().toString())
                    .set(tipData)
                    .await()
                    
                _uploadState.value = UploadState.Success("Tip successfully created!")
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _uploadState.value = UploadState.Idle
    }

    sealed class UploadState {
        object Idle : UploadState()
        data class Processing(val message: String) : UploadState()
        data class Success(val message: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }
}
