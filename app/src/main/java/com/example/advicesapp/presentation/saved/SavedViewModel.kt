package com.example.advicesapp.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.usecases.DeleteFromSavedAdviceUseCase
import com.example.advicesapp.domain.usecases.GetSavedAdvicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    getSavedAdvicesUseCase: GetSavedAdvicesUseCase,
    private val deleteFromSavedAdviceUseCase: DeleteFromSavedAdviceUseCase,
) : ViewModel() {

    val savedAdvices = getSavedAdvicesUseCase().asLiveData()

    fun deleteFromSavedAdvices(advice: Advice) {
        viewModelScope.launch {
            deleteFromSavedAdviceUseCase(advice)
        }

    }
}