package com.raithavarta.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.raithavarta.data.local.dao.FlashCardDao
import com.raithavarta.data.local.entity.FlashCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class FlashCardRepository(
    private val flashCardDao: FlashCardDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val flashCards: Flow<List<FlashCard>> = flashCardDao.getAllFlashCards()
    
    fun getFlashCardsByCrop(cropId: String): Flow<List<FlashCard>> {
        return flashCardDao.getFlashCardsByCrop(cropId)
    }

    suspend fun refreshFlashCards() {
        try {
            val snapshot = firestore.collection("tips")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val cards = snapshot.documents.mapNotNull { doc ->
                val id = doc.id
                val title = doc.getString("title") ?: ""
                val desc = doc.getString("descriptionKannada") ?: ""
                val image = doc.getString("imageUrl") ?: ""
                val cropType = doc.getString("cropType") ?: ""
                val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                
                FlashCard(id, title, desc, image, cropType, timestamp)
            }

            if (cards.isNotEmpty()) {
                flashCardDao.insertAll(cards)
            } else {
                insertDummyData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            insertDummyData()
        }
    }

    private suspend fun insertDummyData() {
        val dummyCards = listOf(
            FlashCard(
                id = "dummy1",
                title = "ಮಣ್ಣಿನ ತೇವಾಂಶ (Soil Moisture)",
                descriptionKannada = "ಬೆಳಗ್ಗೆಯ ಹೊತ್ತು ನೀರು ಹರಿಸುವುದರಿಂದ ತೇವಾಂಶ ಕಾಪಾಡಬಹುದು. (Watering in the morning helps retain moisture.)",
                imageUrl = "https://images.unsplash.com/photo-1596181938555-52d80d2db7c3?auto=format&fit=crop&q=80&w=600",
                cropType = "1", // Coconut
                timestamp = System.currentTimeMillis()
            ),
            FlashCard(
                id = "dummy2",
                title = "ರಸಗೊಬ್ಬರ (Fertilizers)",
                descriptionKannada = "ಸಾವಯವ ಗೊಬ್ಬರ ಬಳಕೆ ಮಣ್ಣಿನ ಫಲವತ್ತತೆಯನ್ನು ಹೆಚ್ಚಿಸುತ್ತದೆ. (Using organic manure increases soil fertility.)",
                imageUrl = "https://images.unsplash.com/photo-1627993077732-f2887ccf3554?auto=format&fit=crop&q=80&w=600",
                cropType = "2", // Areca nut
                timestamp = System.currentTimeMillis()
            ),
            FlashCard(
                id = "dummy3",
                title = "ಕೀಟ ನಿಯಂತ್ರಣ (Pest Control)",
                descriptionKannada = "ಟೊಮೆಟೊ ಬೆಳೆಗೆ ಬೇವಿನ ಎಣ್ಣೆ ಸಿಂಪಡಿಸಿ. (Spray neem oil on tomato crops to control pests.)",
                imageUrl = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?auto=format&fit=crop&q=80&w=600",
                cropType = "3", // Tomato
                timestamp = System.currentTimeMillis()
            ),
            FlashCard(
                id = "dummy4",
                title = "ನೀರಿನ ನಿರ್ವಹಣೆ (Water Management)",
                descriptionKannada = "ಭತ್ತದ ಗದ್ದೆಯಲ್ಲಿ ಸರಿಯಾದ ನೀರಿನ ಮಟ್ಟ ಕಾಯ್ದುಕೊಳ್ಳಿ. (Maintain proper water level in paddy fields.)",
                imageUrl = "https://images.unsplash.com/photo-1590682680695-43b964a3ae17?auto=format&fit=crop&q=80&w=600",
                cropType = "4", // Paddy
                timestamp = System.currentTimeMillis()
            ),
            FlashCard(
                id = "dummy5",
                title = "ರಾಗಿ ಕಟಾವು (Ragi Harvesting)",
                descriptionKannada = "ರಾಗಿ ತೆನೆಗಳು ಕಂದು ಬಣ್ಣಕ್ಕೆ ತಿರುಗಿದಾಗ ಕಟಾವು ಮಾಡಿ. (Harvest ragi when earheads turn brown.)",
                imageUrl = "https://images.unsplash.com/photo-1586208556495-2eb4d57fdfce?auto=format&fit=crop&q=80&w=600",
                cropType = "5", // Ragi
                timestamp = System.currentTimeMillis()
            ),
            FlashCard(
                id = "dummy6",
                title = "ಕಬ್ಬು ನಿರ್ವಹಣೆ (Sugarcane Management)",
                descriptionKannada = "ಕಬ್ಬಿಗೆ ಸರಿಯಾದ ಸಮಯಕ್ಕೆ ಗೊಬ್ಬರ ಒದಗಿಸಿ. (Provide fertilizers to sugarcane on time.)",
                imageUrl = "https://images.unsplash.com/photo-1622312658908-01d011bb2d6f?auto=format&fit=crop&q=80&w=600",
                cropType = "6", // Sugarcane
                timestamp = System.currentTimeMillis()
            )
        )
        flashCardDao.insertAll(dummyCards)
    }
}
