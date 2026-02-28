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
            Topic.KotlinFundamental.TtlCache,
        ),
        "ui" to listOf(
            Topic.Ui.Gallery,
            Topic.Ui.Fever,
        ),
        "design_patterns" to listOf(
            Topic.DesignPattern.FactoryMethod,
            Topic.DesignPattern.AbstractFactory,
            Topic.DesignPattern.Prototype,
            Topic.DesignPattern.Adapter,
            Topic.DesignPattern.Decorator,
            Topic.DesignPattern.Facade,
            Topic.DesignPattern.Observer,
            Topic.DesignPattern.Strategy,
            Topic.DesignPattern.Command,
            Topic.DesignPattern.StateMachine,
        ),
    )

    override fun getCategories(): List<Category> = categories

    override fun getTopicsByCategoryId(categoryId: String): List<Topic> =
        topicsByCategory[categoryId].orEmpty()
}
