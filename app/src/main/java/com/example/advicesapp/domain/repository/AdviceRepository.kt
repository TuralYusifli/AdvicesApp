package com.example.advicesapp.domain.repository

import com.example.advicesapp.common.State
import com.example.advicesapp.domain.model.Advice
import kotlinx.coroutines.flow.Flow

interface AdviceRepository {

    suspend fun getAdviceByText(text: String): Flow<State<List<Advice>>>

    suspend fun insertToSavedAdvice(advice: Advice)

    suspend fun deleteFromSavedAdvice(advice: Advice)

    fun getSavedAdvices(): Flow<List<Advice>>

    suspend fun deleteFromSearchAdviceHistory(advice: Advice)

    fun getSearchAdviceHistory(): Flow<List<Advice>>
}