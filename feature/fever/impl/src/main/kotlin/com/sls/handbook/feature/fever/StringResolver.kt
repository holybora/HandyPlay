package com.sls.handbook.feature.fever

import android.content.Context
import androidx.annotation.StringRes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

interface StringResolver {
    fun getString(@StringRes resId: Int, vararg args: Any): String
}

@Module
@InstallIn(SingletonComponent::class)
internal object StringResolverModule {
    @Provides
    fun provideStringResolver(@ApplicationContext context: Context): StringResolver =
        object : StringResolver {
            override fun getString(@StringRes resId: Int, vararg args: Any): String =
                context.getString(resId, *args)
        }
}
