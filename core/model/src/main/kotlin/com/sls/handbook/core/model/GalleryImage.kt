package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class GalleryImage(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val downloadUrl: String,
)
