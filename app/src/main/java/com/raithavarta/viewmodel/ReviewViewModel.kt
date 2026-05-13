package com.raithavarta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.raithavarta.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReviewViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _averageRating = MutableStateFlow(0f)
    val averageRating: StateFlow<Float> = _averageRating

    fun loadReviewsForProduct(productId: String) {
        firestore.collection("products").document(productId)
            .collection("reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                if (snapshot != null) {
                    val reviewList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Review::class.java)?.copy(id = doc.id)
                    }
                    _reviews.value = reviewList

                    // Calculate average rating
                    if (reviewList.isNotEmpty()) {
                        val avg = reviewList.map { it.rating }.average().toFloat()
                        _averageRating.value = avg
                        
                        // Update the average rating on the product/vendor document
                        updateProductAverageRating(productId, avg, reviewList.size)
                    } else {
                        _averageRating.value = 0f
                    }
                }
            }
    }

    private fun updateProductAverageRating(productId: String, avgRating: Float, totalReviews: Int) {
        viewModelScope.launch {
            try {
                firestore.collection("products").document(productId)
                    .update(
                        mapOf(
                            "averageRating" to avgRating,
                            "totalReviews" to totalReviews
                        )
                    ).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun submitReview(productId: String, userId: String, userName: String, rating: Float, comment: String) {
        viewModelScope.launch {
            try {
                val review = hashMapOf(
                    "userId" to userId,
                    "userName" to userName,
                    "rating" to rating,
                    "comment" to comment,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("products").document(productId)
                    .collection("reviews")
                    .add(review)
                    .await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
