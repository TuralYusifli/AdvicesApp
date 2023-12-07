package com.example.advicesapp.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advicesapp.common.Constants
import com.example.advicesapp.common.State
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.usecases.DeleteFromSavedAdviceUseCase
import com.example.advicesapp.domain.usecases.GetAdviceByTextUseCase
import com.example.advicesapp.domain.usecases.GetSavedAdvicesUseCase
import com.example.advicesapp.domain.usecases.InsertToSavedAdviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAdviceByTextUseCase: GetAdviceByTextUseCase,
    private val insertToSavedAdviceUseCase: InsertToSavedAdviceUseCase,
    getSavedAdvicesUseCase: GetSavedAdvicesUseCase,
    private val deleteFromSavedAdviceUseCase: DeleteFromSavedAdviceUseCase,
) : ViewModel() {

    private val _adviceResponseFlow = MutableSharedFlow<State<List<Advice>>>()
    val adviceResponseFlow = _adviceResponseFlow.asSharedFlow()

    fun insertToSavedAdvices(advice: Advice) {
        viewModelScope.launch {
            insertToSavedAdviceUseCase(advice)
        }
    }

    fun deleteFromSavedAdvices(advice: Advice) {
        viewModelScope.launch {
            deleteFromSavedAdviceUseCase(advice)
        }
    }

    fun getAdvice(text: String) {
        if (text.isEmpty()) {
            viewModelScope.launch {
                _adviceResponseFlow.emit(State.Error("Enter search advice"))
            }
            return
        }
        viewModelScope.launch {
            getAdviceByTextUseCase(text)
                .collectLatest {
                    when (it) {
                        is State.Loading -> {
                            _adviceResponseFlow.emit(State.Loading)
                        }

                        is State.Success -> {
                            updateApiData(it.data)
                        }

                        is State.Error -> {
                            _adviceResponseFlow.emit(State.Error(it.errorMessage))
                        }
                    }
                }
        }
    }

    private val localAdviceFlow: Flow<List<Advice>> = getSavedAdvicesUseCase()
    private val apiAdviceFlow = MutableStateFlow<List<Advice>>(emptyList())

    private val mergedFlow: Flow<Pair<List<Advice>, List<Advice>>> = combine(
        apiAdviceFlow,
        localAdviceFlow
    ) { apiList, localList ->
        Pair(apiList, localList)
    }

    val mergedLiveData = MutableLiveData<List<Advice>>()

    init {
        viewModelScope.launch {
            mergedFlow.collect {
                mergedLiveData.value = mergeTwoList(it)
            }
        }
    }

    private fun updateApiData(apiData: List<Advice>) {
        viewModelScope.launch {
            apiAdviceFlow.emit(apiData)
        }
    }

    private fun mergeTwoList(
        pair: Pair<List<Advice>, List<Advice>>
    ): List<Advice> {
        if (pair.first.isNotEmpty() && pair.second.isNotEmpty()) {
            val listForAdapter = mutableListOf<Advice>()
            for (item in pair.first) {
                if (pair.second.contains(item)) {
                    listForAdapter.add(
                        Advice(
                            id = item.id,
                            advice = item.advice,
                            query = item.query,
                            isSaved = Constants.SAVED
                        )
                    )
                } else {
                    listForAdapter.add(item)
                }
            }
            return listForAdapter
        } else {
            return pair.first
        }
    }
}