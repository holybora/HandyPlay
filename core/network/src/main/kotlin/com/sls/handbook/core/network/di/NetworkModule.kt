package com.sls.handbook.core.network.di

import com.sls.handbook.core.network.ApiKeyProvider
import com.sls.handbook.core.network.BuildConfig
import com.sls.handbook.core.network.api.JokeApi
import com.sls.handbook.core.network.api.PicsumApi
import com.sls.handbook.core.network.api.WeatherApi
import com.sls.handbook.core.network.interceptor.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import com.sls.handbook.core.network.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONNECT_TIMEOUT_SECONDS = 15L
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val WRITE_TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideApiKeyProvider(): ApiKeyProvider = object : ApiKeyProvider {
        override fun getApiKey(): String = BuildConfig.OPENWEATHER_API_KEY
    }

    @Provides
    @Singleton
    fun provideBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                }
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherOkHttpClient(
        baseOkHttpClient: OkHttpClient,
        apiKeyProvider: ApiKeyProvider,
    ): OkHttpClient {
        return baseOkHttpClient.newBuilder()
            .addInterceptor(ApiKeyInterceptor(apiKeyProvider))
            .build()
    }

    @Provides
    @Singleton
    @Named("joke")
    fun provideJokeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("picsum")
    fun providePicsumRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJokeApi(@Named("joke") retrofit: Retrofit): JokeApi {
        return retrofit.create(JokeApi::class.java)
    }

    @Provides
    @Singleton
    fun providePicsumApi(@Named("picsum") retrofit: Retrofit): PicsumApi {
        return retrofit.create(PicsumApi::class.java)
    }

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(@Named("weather") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(@Named("weather") retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}
