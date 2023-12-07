package com.example.advicesapp.domain.model

import com.example.advicesapp.data.local.SavedAdviceEntity
import com.example.advicesapp.data.local.SearchAdviceHistoryEntity

data class Advice(
    val id: Int? = null,
    val advice: String? = null,
    val query: String? = null,
    var isSaved: Boolean = false
)

fun Advice.toSavedAdviceEntity(): SavedAdviceEntity {
    return SavedAdviceEntity(
        id = id,
        advice = advice,
        query = query,
        isSaved = isSaved
    )
}

fun Advice.toSearchAdviceHistoryEntity(): SearchAdviceHistoryEntity {
    return SearchAdviceHistoryEntity(
        id = id,
        advice = advice,
        query = query,
        isSaved = isSaved
    )
}
