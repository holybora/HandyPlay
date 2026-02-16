package com.sls.handbook.feature.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sls.handbook.core.domain.repository.CategoryRepository
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
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val destination = savedStateHandle.toRoute<CategoryDestination>()
    private val categoryId = destination.categoryId
    private val categoryName = destination.categoryName

    private val allTopics = categoryRepository.getTopicsByCategoryId(categoryId)

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
