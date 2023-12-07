package com.example.advicesapp.domain.usecases

import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.repository.AdviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchAdviceHistoryUseCase @Inject constructor(private val adviceRepository: AdviceRepository) {
    operator fun invoke(): Flow<List<Advice>> {
        return adviceRepository.getSearchAdviceHistory()
    }
}