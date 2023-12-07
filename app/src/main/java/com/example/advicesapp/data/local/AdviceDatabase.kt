package com.example.advicesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedAdviceEntity::class, SearchAdviceHistoryEntity::class], version = 2)
abstract class AdviceDatabase : RoomDatabase() {
    abstract fun adviceDao(): AdviceDao
}