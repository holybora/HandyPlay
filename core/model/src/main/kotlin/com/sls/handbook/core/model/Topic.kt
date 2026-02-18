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
        const val ID_GALLERY = "ui_1"
        const val ID_FEVER = "ui_2"
    }
}
