package com.sls.handbook.feature.home

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val allCategories = categoryRepository.getCategories()

    private val _uiState = MutableStateFlow<HomeUiState>(
        HomeUiState.Success(categories = allCategories)
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            if (currentState is HomeUiState.Success) {
                val filtered = if (query.isBlank()) {
                    allCategories
                } else {
                    allCategories.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
                currentState.copy(categories = filtered, searchQuery = query)
            } else {
                currentState
            }
        }
    }
}
