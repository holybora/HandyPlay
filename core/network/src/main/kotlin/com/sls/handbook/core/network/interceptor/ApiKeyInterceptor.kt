package com.sls.handbook.core.network.interceptor

import com.sls.handbook.core.network.ApiKeyProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor that appends the `appid` query parameter to every request.
 *
 * @param apiKeyProvider supplier of the OpenWeatherMap API key
 */
internal class ApiKeyInterceptor(
    private val apiKeyProvider: ApiKeyProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder()
            .addQueryParameter(QUERY_PARAM, apiKeyProvider.getApiKey())
            .build()
        return chain.proceed(originalRequest.newBuilder().url(url).build())
    }

    private companion object {
        const val QUERY_PARAM = "appid"
    }
}
