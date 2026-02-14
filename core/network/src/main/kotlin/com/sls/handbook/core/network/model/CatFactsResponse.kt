package com.sls.handbook.core.network.model

import com.google.gson.annotations.SerializedName

data class CatFactsResponse(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val data: List<CatFactDto>,
)

data class CatFactDto(
    @SerializedName("fact") val fact: String,
    @SerializedName("length") val length: Int,
)
