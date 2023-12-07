package com.example.advicesapp.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.usecases.DeleteFromSearchAdviceHistoryUseCase
import com.example.advicesapp.domain.usecases.GetSearchAdviceHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    getSearchAdviceHistoryUseCase: GetSearchAdviceHistoryUseCase,
    private val deleteFromSearchAdviceHistoryUseCase: DeleteFromSearchAdviceHistoryUseCase
) : ViewModel() {

    val adviceSearchHistory = getSearchAdviceHistoryUseCase().asLiveData()

    fun deleteFromSearchAdviceHistory(advice: Advice) {
        viewModelScope.launch {
            deleteFromSearchAdviceHistoryUseCase(advice)
        }
    }
}