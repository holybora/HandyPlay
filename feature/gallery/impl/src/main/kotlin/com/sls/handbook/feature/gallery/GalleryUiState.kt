package com.sls.handbook.feature.gallery

import com.sls.handbook.core.model.GalleryImage

sealed interface GalleryUiState {

    data object Loading : GalleryUiState

    data class Content(
        val images: List<GalleryImage>,
        val isLoadingMore: Boolean = false,
        val currentPage: Int = 1,
    ) : GalleryUiState

    data class Error(val message: String) : GalleryUiState
}
