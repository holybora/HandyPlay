package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentSection(
    val title: String,
    val description: String,
    val code: String,
)

@Serializable
data class TopicContent(
    val topicId: String,
    val topicName: String,
    val sections: List<ContentSection>,
)
