package com.sls.handbook.core.domain.exception

import java.io.IOException

/**
 * Domain-level exception hierarchy for weather data operations.
 *
 * Maps network and parsing failures into typed exceptions that the presentation
 * layer can handle with specific user-facing messages.
 */
sealed class WeatherException(message: String, cause: Throwable?) : Exception(message, cause) {
    /** Connectivity failure caused by an underlying [IOException]. */
    class Network(cause: IOException) : WeatherException("Network error", cause)

    /**
     * Non-successful HTTP response from the weather API.
     *
     * @property code HTTP status code returned by the server
     */
    class Server(val code: Int, message: String, cause: Throwable) :
        WeatherException("HTTP $code: $message", cause)

    /** Failure to deserialize or map the API response into domain models. */
    class DataParsing(cause: Throwable) : WeatherException("Failed to parse response", cause)
}
