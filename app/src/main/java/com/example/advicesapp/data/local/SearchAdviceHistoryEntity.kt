package com.example.advicesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.advicesapp.domain.model.Advice

@Entity(tableName = "search_advice_history")
data class SearchAdviceHistoryEntity(
    @PrimaryKey
    val id: Int?,
    val query: String?,
    val advice: String?,
    val isSaved: Boolean = false
)

fun List<SearchAdviceHistoryEntity>.asAdviceModel(): List<Advice> = this.map {
    Advice(id = it.id, advice = it.advice, query = it.query, isSaved = it.isSaved)
}

fun List<Advice>.asSearchAdviceHistoryEntity(): List<SearchAdviceHistoryEntity> = this.map {
    SearchAdviceHistoryEntity(
        id = it.id,
        query = it.query,
        advice = it.advice,
        isSaved = it.isSaved
    )
}
