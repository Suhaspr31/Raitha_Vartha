package com.raithavarta.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raithavarta.data.local.dao.FlashCardDao
import com.raithavarta.data.local.dao.CropAnalysisDao
import com.raithavarta.data.local.entity.FlashCard
import com.raithavarta.data.local.entity.CropAnalysisRecord

@Database(entities = [FlashCard::class, CropAnalysisRecord::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao
    abstract fun cropAnalysisDao(): CropAnalysisDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "raitha_varta_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
