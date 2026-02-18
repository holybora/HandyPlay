package com.sls.handbook.feature.gallery

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Content())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()
}
