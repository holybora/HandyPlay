package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: String,
    val name: String,
    val categoryId: String,
) {
    companion object {
        const val ID_TTL_CACHE = "kf_7"
    }
}
