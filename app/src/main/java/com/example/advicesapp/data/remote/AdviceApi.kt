package com.example.advicesapp.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AdviceApi {

    @GET("/advice/search/{text}")
    suspend fun getAdviceByText(@Path("text") keyText: String): Response<SearchAdviceResponse>

}