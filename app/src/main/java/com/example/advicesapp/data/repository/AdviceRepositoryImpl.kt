package com.example.advicesapp.data.repository

import com.example.advicesapp.common.State
import com.example.advicesapp.data.local.AdviceDao
import com.example.advicesapp.data.local.asAdviceModel
import com.example.advicesapp.data.local.asSearchAdviceHistoryEntity
import com.example.advicesapp.data.remote.AdviceApi
import com.example.advicesapp.data.remote.toAdviceList
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.domain.model.toSavedAdviceEntity
import com.example.advicesapp.domain.model.toSearchAdviceHistoryEntity
import com.example.advicesapp.domain.repository.AdviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdviceRepositoryImpl @Inject constructor(
    private val api: AdviceApi,
    private val adviceDao: AdviceDao,
) : AdviceRepository {

    override suspend fun getAdviceByText(text: String): Flow<State<List<Advice>>> = flow {
        emit(State.Loading)
        val response = api.getAdviceByText(text)
        if (response.isSuccessful) {
            if (response.body()?.notFoundMessageDetail != null) {
                emit(State.Error("${response.body()?.notFoundMessageDetail?.text}"))
            } else {
                insertToSearchAdviceHistory(response.body()!!.toAdviceList())
                emit(State.Success(response.body()!!.toAdviceList()))
            }
        } else {
            emit(State.Error(response.message()))
        }
    }.catch {
        it.printStackTrace()
        emit(State.Error("Network error! Can't get latest posts."))
    }

    override suspend fun insertToSavedAdvice(advice: Advice) {
        adviceDao.insertToSavedAdvice(advice.toSavedAdviceEntity())
    }

    override suspend fun deleteFromSavedAdvice(advice: Advice) {
        adviceDao.deleteFromSavedAdvice(advice.toSavedAdviceEntity())
    }

    override fun getSavedAdvices() = adviceDao.getSavedAdvices().map {
        it.asAdviceModel()
    }

    private suspend fun insertToSearchAdviceHistory(advice: List<Advice>) {
        withContext(Dispatchers.IO) {
            adviceDao.insertToSearchAdviceHistory(advice.asSearchAdviceHistoryEntity())
        }
    }

    override suspend fun deleteFromSearchAdviceHistory(advice: Advice) {
        adviceDao.deleteFromSearchAdviceHistory(advice.toSearchAdviceHistoryEntity())
    }

    override fun getSearchAdviceHistory(): Flow<List<Advice>> {
        return adviceDao.getSearchAdviceHistory().map { it.asAdviceModel() }
    }

}