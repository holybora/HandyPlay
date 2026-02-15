package com.sls.handbook.core.network.model

import com.google.gson.annotations.SerializedName

data class JokeResponse(
    @SerializedName("type") val type: String,
    @SerializedName("setup") val setup: String,
    @SerializedName("punchline") val punchline: String,
    @SerializedName("id") val id: Int,
)
