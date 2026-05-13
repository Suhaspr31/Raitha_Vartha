package com.raithavarta.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raithavarta.data.local.entity.FlashCard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM flash_cards ORDER BY timestamp DESC")
    fun getAllFlashCards(): Flow<List<FlashCard>>
    
    @Query("SELECT * FROM flash_cards WHERE cropType = :cropId ORDER BY timestamp DESC")
    fun getFlashCardsByCrop(cropId: String): Flow<List<FlashCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flashCards: List<FlashCard>)

    @Query("DELETE FROM flash_cards")
    suspend fun clearAll()
}
