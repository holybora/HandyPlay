package com.sls.handbook.navigation

import kotlinx.serialization.Serializable

@Serializable
object WelcomeDestination

@Serializable
object HomeDestination

@Serializable
data class CategoryDestination(
    val categoryId: String,
    val categoryName: String,
)

@Serializable
object TtlCacheDestination

@Serializable
object GalleryDestination

@Serializable
object FeverDestination
