package com.sls.handbook.core.network

/**
 * Abstraction for supplying the OpenWeatherMap API key.
 *
 * Implemented in `:core:data` to decouple the network layer from BuildConfig access.
 */
interface ApiKeyProvider {
    fun getApiKey(): String
}
