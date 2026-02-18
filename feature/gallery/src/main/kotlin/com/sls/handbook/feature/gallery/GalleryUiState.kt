package com.sls.handbook.feature.gallery

sealed interface GalleryUiState {

    data class Content(
        val title: String = "Gallery",
        val description: String = "Compose UI components and examples will be added here.",
    ) : GalleryUiState
}
