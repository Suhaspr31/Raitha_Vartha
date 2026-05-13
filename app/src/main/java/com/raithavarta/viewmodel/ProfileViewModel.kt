package com.raithavarta.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raithavarta.data.local.dao.CropAnalysisDao
import com.raithavarta.data.local.dao.FlashCardDao
import com.raithavarta.data.local.entity.CropAnalysisRecord
import com.raithavarta.data.local.entity.FlashCard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val cropAnalysisDao: CropAnalysisDao,
    private val flashCardDao: FlashCardDao,
    private val context: Context
) : ViewModel() {

    private val _history = MutableStateFlow<List<CropAnalysisRecord>>(emptyList())
    val history: StateFlow<List<CropAnalysisRecord>> = _history

    private val _savedTips = MutableStateFlow<List<FlashCard>>(emptyList())
    val savedTips: StateFlow<List<FlashCard>> = _savedTips

    private val _userName = MutableStateFlow("Loading...")
    val userName: StateFlow<String> = _userName

    private val _userRole = MutableStateFlow("...")
    val userRole: StateFlow<String> = _userRole

    init {
        loadScanHistory()
        refreshSavedTips()
        fetchFirestoreProfile()
    }

    fun fetchFirestoreProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            try {
                val document = db.collection("users").document(uid).get().await()
                if (document.exists()) {
                    _userName.value = document.getString("name") ?: "Farmer"
                    _userRole.value = document.getString("role") ?: "Farmer"
                }
            } catch (e: Exception) {
                _userName.value = "User"
            }
        }
    }

    private fun loadScanHistory() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
        viewModelScope.launch {
            cropAnalysisDao.getHistoryForUser(uid).collect {
                _history.value = it
            }
        }
    }

    fun refreshSavedTips() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
        viewModelScope.launch {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            try {
                if (uid != "guest") {
                    val snapshot = db.collection("users").document(uid).collection("saved_tips").get().await()
                    val savedIds = snapshot.documents.map { it.id }
                    
                    flashCardDao.getAllFlashCards().collect { allCards ->
                        val saved = allCards.filter { it.id in savedIds }
                        _savedTips.value = saved
                    }
                } else {
                    fallbackToSharedPrefs(uid)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                fallbackToSharedPrefs(uid)
            }
        }
    }

    private fun fallbackToSharedPrefs(uid: String) {
        viewModelScope.launch {
            val sharedPrefs = context.getSharedPreferences("saved_tips_$uid", android.content.Context.MODE_PRIVATE)
            flashCardDao.getAllFlashCards().collect { allCards ->
                val saved = allCards.filter { sharedPrefs.getBoolean(it.id, false) }
                _savedTips.value = saved
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
