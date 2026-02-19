package com.sls.handbook.core.network.di

import com.sls.handbook.core.network.api.HourlyForecastApi
import com.sls.handbook.core.network.api.JokeApi
import com.sls.handbook.core.network.api.PicsumApi
import com.sls.handbook.core.network.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
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
    fun provideWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit {
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

    @Provides
    @Singleton
    fun provideHourlyForecastApi(@Named("weather") retrofit: Retrofit): HourlyForecastApi {
        return retrofit.create(HourlyForecastApi::class.java)
    }
}
