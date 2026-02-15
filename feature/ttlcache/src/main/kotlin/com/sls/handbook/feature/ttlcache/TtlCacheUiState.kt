package com.sls.handbook.feature.ttlcache

sealed interface TtlCacheUiState {

    data class Idle(
        val ttlSeconds: Int = 5,
        val lastFetchedTime: String = "",
        val data: String = "",
        val isLoading: Boolean = false,
    ) : TtlCacheUiState

    data class Error(val message: String) : TtlCacheUiState
}
