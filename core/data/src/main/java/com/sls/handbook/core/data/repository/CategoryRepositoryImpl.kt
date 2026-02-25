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
        Category(id = "ui", name = "UI"),
        Category(id = "design_patterns", name = "Design Patterns"),
    )

    private val topicsByCategory: Map<String, List<Topic>> = mapOf(
        "kotlin_fundamentals" to listOf(
            Topic(Topic.ID_TTL_CACHE, "TTL Cache", "kotlin_fundamentals"),
        ),
        "ui" to listOf(
            Topic(Topic.ID_GALLERY, "Gallery", "ui"),
            Topic(Topic.ID_FEVER, "Fever", "ui"),
        ),
        "design_patterns" to listOf(
            Topic(Topic.ID_DP_FACTORY_METHOD, "Factory Method", "design_patterns"),
            Topic(Topic.ID_DP_ABSTRACT_FACTORY, "Abstract Factory", "design_patterns"),
            Topic(Topic.ID_DP_PROTOTYPE, "Prototype", "design_patterns"),
            Topic(Topic.ID_DP_ADAPTER, "Adapter", "design_patterns"),
            Topic(Topic.ID_DP_DECORATOR, "Decorator", "design_patterns"),
            Topic(Topic.ID_DP_FACADE, "Facade", "design_patterns"),
            Topic(Topic.ID_DP_OBSERVER, "Observer", "design_patterns"),
            Topic(Topic.ID_DP_STRATEGY, "Strategy", "design_patterns"),
            Topic(Topic.ID_DP_COMMAND, "Command", "design_patterns"),
            Topic(Topic.ID_DP_STATE_MACHINE, "State Machine", "design_patterns"),
        ),
    )

    override fun getCategories(): List<Category> = categories

    override fun getTopicsByCategoryId(categoryId: String): List<Topic> =
        topicsByCategory[categoryId].orEmpty()
}
