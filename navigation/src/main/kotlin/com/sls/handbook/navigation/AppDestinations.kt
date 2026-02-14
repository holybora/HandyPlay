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
data class TopicDestination(
    val topicId: String,
    val topicName: String,
    val categoryId: String,
    val categoryName: String,
)
