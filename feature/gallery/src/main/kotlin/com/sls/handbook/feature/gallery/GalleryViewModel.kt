package com.sls.handbook.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sls.handbook.core.domain.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            try {
                val images = galleryRepository.getImages(page = 1, limit = PAGE_SIZE)
                _uiState.value = GalleryUiState.Content(
                    images = images,
                    currentPage = 1,
                )
            } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") e: Exception) {
                _uiState.value = GalleryUiState.Error(
                    message = e.message ?: "Failed to load images",
                )
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState !is GalleryUiState.Content || currentState.isLoadingMore) return

        val nextPage = currentState.currentPage + 1
        _uiState.value = currentState.copy(isLoadingMore = true)

        viewModelScope.launch {
            try {
                val newImages = galleryRepository.getImages(page = nextPage, limit = PAGE_SIZE)
                _uiState.value = GalleryUiState.Content(
                    images = currentState.images + newImages,
                    currentPage = nextPage,
                    isLoadingMore = false,
                )
            } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") e: Exception) {
                _uiState.value = currentState.copy(isLoadingMore = false)
            }
        }
    }

    fun retry() {
        _uiState.value = GalleryUiState.Loading
        loadImages()
    }

    private companion object {
        const val PAGE_SIZE = 30
    }
}
