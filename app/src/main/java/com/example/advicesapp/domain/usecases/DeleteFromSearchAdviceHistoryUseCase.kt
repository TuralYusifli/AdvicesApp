package com.example.advicesapp.domain.usecases

import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.repository.AdviceRepository
import javax.inject.Inject

class DeleteFromSearchAdviceHistoryUseCase @Inject constructor(private val adviceRepository: AdviceRepository) {
    suspend operator fun invoke(advice: Advice) {
        adviceRepository.deleteFromSearchAdviceHistory(advice)
    }
}