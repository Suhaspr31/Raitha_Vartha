package com.raithavarta.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raithavarta.data.local.entity.CropAnalysisRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CropAnalysisDao {
    @Query("SELECT * FROM crop_analysis_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getHistoryForUser(userId: String): Flow<List<CropAnalysisRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CropAnalysisRecord)

    @Query("DELETE FROM crop_analysis_history WHERE userId = :userId")
    suspend fun clearHistoryForUser(userId: String)
}
