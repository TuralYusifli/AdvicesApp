package com.example.advicesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao

interface AdviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToSavedAdvice(advice: SavedAdviceEntity)

    @Delete
    suspend fun deleteFromSavedAdvice(advice: SavedAdviceEntity)

    @Query(" SELECT * FROM saved_advice")
    fun getSavedAdvices(): Flow<List<SavedAdviceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToSearchAdviceHistory(advice: List<SearchAdviceHistoryEntity>)

    @Delete
    suspend fun deleteFromSearchAdviceHistory(advice: SearchAdviceHistoryEntity)

    @Query(" SELECT * FROM search_advice_history")
    fun getSearchAdviceHistory(): Flow<List<SearchAdviceHistoryEntity>>
}