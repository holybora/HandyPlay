package com.sls.handbook.core.data.di

import com.sls.handbook.core.data.BuildConfig
import com.sls.handbook.core.network.ApiKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Hilt module that provides the [ApiKeyProvider] implementation backed by BuildConfig. */
@Module
@InstallIn(SingletonComponent::class)
internal object ApiKeyModule {

    @Provides
    fun provideApiKeyProvider(): ApiKeyProvider = object : ApiKeyProvider {
        override fun getApiKey(): String = BuildConfig.OPENWEATHER_API_KEY
    }
}
