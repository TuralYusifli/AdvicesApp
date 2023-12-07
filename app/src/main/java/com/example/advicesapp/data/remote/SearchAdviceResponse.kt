package com.example.advicesapp.data.remote

import com.example.advicesapp.domain.model.Advice
import com.google.gson.annotations.SerializedName

data class SearchAdviceResponse(
    @SerializedName("message")
    var notFoundMessageDetail: MessageDetail?,
    @SerializedName("total_results")
    val totalResults: String? = null,
    val query: String? = null,
    @SerializedName("slips")
    val adviceResponses: ArrayList<AdviceResponse> = arrayListOf()
)

fun SearchAdviceResponse.toAdviceList(): List<Advice> {
    return adviceResponses.map { it.toAdvice(query) }
}

fun AdviceResponse.toAdvice(query: String? = null): Advice {
    return Advice(
        id = id,
        advice = advice,
        query = query
    )
}
