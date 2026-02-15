package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Joke(
    val setup: String,
    val punchline: String,
)
