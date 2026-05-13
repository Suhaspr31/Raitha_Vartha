package com.raithavarta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.raithavarta.data.model.SuccessStory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpoorthiViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _stories = MutableStateFlow<List<SuccessStory>>(emptyList())
    val stories: StateFlow<List<SuccessStory>> = _stories

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchStories()
    }

    private fun fetchStories() {
        viewModelScope.launch {
            _isLoading.value = true
            firestore.collection("success_stories").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    loadDummyStories()
                    _isLoading.value = false
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val storyList = snapshot.documents.mapNotNull { doc ->
                        val farmerName = doc.getString("farmerName") ?: ""
                        val quoteKannada = doc.getString("quoteKannada") ?: ""
                        val profilePicUrl = doc.getString("profilePicUrl") ?: ""
                        val beforeAfterPicUrl = doc.getString("beforeAfterPicUrl") ?: ""
                        val cropType = doc.getString("cropType") ?: "General"
                        val rating = (doc.get("rating") as? Number)?.toFloat() ?: 5f
                        val yieldIncrease = doc.getString("yieldIncrease") ?: ""
                        val isVerified = doc.getBoolean("isVerified") ?: true
                        
                        SuccessStory(
                            id = doc.id,
                            farmerName = farmerName,
                            quoteKannada = quoteKannada,
                            profilePicUrl = profilePicUrl,
                            beforeAfterPicUrl = beforeAfterPicUrl,
                            cropType = cropType,
                            rating = rating,
                            yieldIncrease = yieldIncrease,
                            isVerified = isVerified
                        )
                    }
                    if (storyList.isEmpty()) {
                        loadDummyStories()
                    } else {
                        _stories.value = storyList
                    }
                } else {
                    loadDummyStories()
                }
                _isLoading.value = false
            }
        }
    }

    private fun loadDummyStories() {
        val dummyList = listOf(
            SuccessStory(
                farmerName = "ರಮೇಶ್ ಕುಮಾರ್ (Ramesh Kumar)",
                quoteKannada = "ರೈತ ವಾರ್ತೆ ಸಲಹೆಯಂತೆ ಜೈವಿಕ ಗೊಬ್ಬರ ಬಳಸಿ ಶೇ.30 ಹೆಚ್ಚು ಇಳುವರಿ ಪಡೆದೆ.",
                profilePicUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=150",
                beforeAfterPicUrl = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?auto=format&fit=crop&q=80&w=150",
                cropType = "Rice",
                yieldIncrease = "30%",
                rating = 5f
            ),
            SuccessStory(
                farmerName = "ಗೌರಮ್ಮ (Gouramma)",
                quoteKannada = "ತೆಂಗಿನ ಬೆಳೆಗೆ ಸರಿಯಾದ ಸಮಯದಲ್ಲಿ ನೀರು ಹರಿಸುವುದರಿಂದ ಇಳುವರಿ ದ್ವಿಗುಣಗೊಂಡಿದೆ.",
                profilePicUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=150",
                beforeAfterPicUrl = "https://images.unsplash.com/photo-1596181938555-52d80d2db7c3?auto=format&fit=crop&q=80&w=150",
                cropType = "Coconut",
                yieldIncrease = "50%",
                rating = 5f
            )
        )
        _stories.value = dummyList
    }
}
