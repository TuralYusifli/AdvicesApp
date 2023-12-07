package com.example.advicesapp.common

sealed class State<out T> {
    data class Success<out T : Any>(val data: T) : State<T>()

    data class Error(val errorMessage: String) : State<Nothing>()

    object Loading : State<Nothing>()

}
