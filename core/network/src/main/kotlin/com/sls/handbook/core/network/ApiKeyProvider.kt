package com.sls.handbook.core.network

/**
 * Abstraction for supplying the OpenWeatherMap API key.
 */
interface ApiKeyProvider {
    fun getApiKey(): String
}
