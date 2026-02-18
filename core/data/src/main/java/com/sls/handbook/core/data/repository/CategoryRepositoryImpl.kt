package com.sls.handbook.core.data.repository

import com.sls.handbook.core.domain.repository.CategoryRepository
import com.sls.handbook.core.model.Category
import com.sls.handbook.core.model.Topic
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor() : CategoryRepository {

    private val categories = listOf(
        Category(id = "kotlin_fundamentals", name = "Kotlin Fundamentals"),
    )

    private val topicsByCategory: Map<String, List<Topic>> = mapOf(
        "kotlin_fundamentals" to listOf(
            Topic(Topic.ID_TTL_CACHE, "TTL Cache", "kotlin_fundamentals"),
        ),
    )

    override fun getCategories(): List<Category> = categories

    override fun getTopicsByCategoryId(categoryId: String): List<Topic> =
        topicsByCategory[categoryId].orEmpty()
}
