package com.example.advicesapp.domain.usecases

import com.example.advicesapp.common.State
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.repository.AdviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdviceByTextUseCase @Inject constructor(private val adviceRepository: AdviceRepository) {
    suspend operator fun invoke(text: String): Flow<State<List<Advice>>> {
        return adviceRepository.getAdviceByText(text)
    }
}