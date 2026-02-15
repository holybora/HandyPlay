package com.sls.handbook.feature.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sls.handbook.core.model.Topic
import com.sls.handbook.navigation.CategoryDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val destination = savedStateHandle.toRoute<CategoryDestination>()
    private val categoryId = destination.categoryId
    private val categoryName = destination.categoryName

    private val allTopics = mockTopicsForCategory(categoryId)

    private val _uiState = MutableStateFlow<CategoryUiState>(
        CategoryUiState.Success(
            categoryName = categoryName,
            topics = allTopics,
        )
    )
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            if (currentState is CategoryUiState.Success) {
                val filtered = if (query.isBlank()) {
                    allTopics
                } else {
                    allTopics.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
                currentState.copy(topics = filtered, searchQuery = query)
            } else {
                currentState
            }
        }
    }
}

private fun mockTopicsForCategory(categoryId: String): List<Topic> =
    when (categoryId) {
        "kotlin_fundamentals" -> listOf(
            Topic("kf_1", "Variables & Types", categoryId),
            Topic("kf_2", "Control Flow", categoryId),
            Topic("kf_3", "Functions", categoryId),
            Topic("kf_4", "Classes & Objects", categoryId),
            Topic("kf_5", "Coroutines", categoryId),
            Topic("kf_6", "Collections", categoryId),
            Topic(Topic.ID_TTL_CACHE, "TTL Cache", categoryId),
            Topic(Topic.ID_LIST_VS_SEQUENCE, "List vs Sequences", categoryId),
        )
        "android_core" -> listOf(
            Topic("ac_1", "Activities", categoryId),
            Topic("ac_2", "Fragments", categoryId),
            Topic("ac_3", "Services", categoryId),
            Topic("ac_4", "Broadcast Receivers", categoryId),
            Topic("ac_5", "Content Providers", categoryId),
            Topic("ac_6", "Permissions", categoryId),
        )
        "jetpack_compose" -> listOf(
            Topic("jc_1", "Composables", categoryId),
            Topic("jc_2", "State Management", categoryId),
            Topic("jc_3", "Layouts", categoryId),
            Topic("jc_4", "Modifiers", categoryId),
            Topic("jc_5", "Theming", categoryId),
            Topic("jc_6", "Animation", categoryId),
        )
        "architecture" -> listOf(
            Topic("ar_1", "MVVM", categoryId),
            Topic("ar_2", "Clean Architecture", categoryId),
            Topic("ar_3", "Repository Pattern", categoryId),
            Topic("ar_4", "Dependency Injection", categoryId),
            Topic("ar_5", "Use Cases", categoryId),
            Topic("ar_6", "Modularization", categoryId),
        )
        "testing" -> listOf(
            Topic("te_1", "Unit Testing", categoryId),
            Topic("te_2", "UI Testing", categoryId),
            Topic("te_3", "Mocking", categoryId),
            Topic("te_4", "TDD", categoryId),
            Topic("te_5", "Integration Tests", categoryId),
            Topic("te_6", "Test Coverage", categoryId),
        )
        "performance" -> listOf(
            Topic("pe_1", "Memory Leaks", categoryId),
            Topic("pe_2", "Profiling", categoryId),
            Topic("pe_3", "Lazy Loading", categoryId),
            Topic("pe_4", "Baseline Profiles", categoryId),
            Topic("pe_5", "App Startup", categoryId),
            Topic("pe_6", "R8 Optimization", categoryId),
        )
        else -> emptyList()
    }
