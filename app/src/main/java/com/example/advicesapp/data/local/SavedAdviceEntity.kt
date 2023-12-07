package com.example.advicesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.advicesapp.domain.model.Advice

@Entity(tableName = "saved_advice")
data class SavedAdviceEntity(
    @PrimaryKey
    val id: Int?,
    val query: String?,
    val advice: String?,
    val isSaved: Boolean = false
)

fun List<SavedAdviceEntity>.asAdviceModel(): List<Advice> = this.map {
    Advice(id = it.id, advice = it.advice, query = it.query, isSaved = it.isSaved)
}
