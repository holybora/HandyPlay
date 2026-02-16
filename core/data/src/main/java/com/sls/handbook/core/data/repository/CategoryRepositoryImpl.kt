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
        Category(id = "android_core", name = "Android Core"),
        Category(id = "jetpack_compose", name = "Jetpack Compose"),
        Category(id = "architecture", name = "Architecture"),
        Category(id = "testing", name = "Testing"),
        Category(id = "performance", name = "Performance"),
    )

    private val topicsByCategory: Map<String, List<Topic>> = mapOf(
        "kotlin_fundamentals" to listOf(
            Topic("kf_1", "Variables & Types", "kotlin_fundamentals"),
            Topic("kf_2", "Control Flow", "kotlin_fundamentals"),
            Topic("kf_3", "Functions", "kotlin_fundamentals"),
            Topic("kf_4", "Classes & Objects", "kotlin_fundamentals"),
            Topic("kf_5", "Coroutines", "kotlin_fundamentals"),
            Topic("kf_6", "Collections", "kotlin_fundamentals"),
            Topic(Topic.ID_TTL_CACHE, "TTL Cache", "kotlin_fundamentals"),
        ),
        "android_core" to listOf(
            Topic("ac_1", "Activities", "android_core"),
            Topic("ac_2", "Fragments", "android_core"),
            Topic("ac_3", "Services", "android_core"),
            Topic("ac_4", "Broadcast Receivers", "android_core"),
            Topic("ac_5", "Content Providers", "android_core"),
            Topic("ac_6", "Permissions", "android_core"),
        ),
        "jetpack_compose" to listOf(
            Topic("jc_1", "Composables", "jetpack_compose"),
            Topic("jc_2", "State Management", "jetpack_compose"),
            Topic("jc_3", "Layouts", "jetpack_compose"),
            Topic("jc_4", "Modifiers", "jetpack_compose"),
            Topic("jc_5", "Theming", "jetpack_compose"),
            Topic("jc_6", "Animation", "jetpack_compose"),
        ),
        "architecture" to listOf(
            Topic("ar_1", "MVVM", "architecture"),
            Topic("ar_2", "Clean Architecture", "architecture"),
            Topic("ar_3", "Repository Pattern", "architecture"),
            Topic("ar_4", "Dependency Injection", "architecture"),
            Topic("ar_5", "Use Cases", "architecture"),
            Topic("ar_6", "Modularization", "architecture"),
        ),
        "testing" to listOf(
            Topic("te_1", "Unit Testing", "testing"),
            Topic("te_2", "UI Testing", "testing"),
            Topic("te_3", "Mocking", "testing"),
            Topic("te_4", "TDD", "testing"),
            Topic("te_5", "Integration Tests", "testing"),
            Topic("te_6", "Test Coverage", "testing"),
        ),
        "performance" to listOf(
            Topic("pe_1", "Memory Leaks", "performance"),
            Topic("pe_2", "Profiling", "performance"),
            Topic("pe_3", "Lazy Loading", "performance"),
            Topic("pe_4", "Baseline Profiles", "performance"),
            Topic("pe_5", "App Startup", "performance"),
            Topic("pe_6", "R8 Optimization", "performance"),
        ),
    )

    override fun getCategories(): List<Category> = categories

    override fun getTopicsByCategoryId(categoryId: String): List<Topic> =
        topicsByCategory[categoryId].orEmpty()
}
