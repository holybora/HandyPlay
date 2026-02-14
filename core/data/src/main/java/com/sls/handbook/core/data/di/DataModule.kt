package com.sls.handbook.core.data.di

import com.sls.handbook.core.data.repository.CatFactsRepositoryImpl
import com.sls.handbook.core.domain.repository.CatFactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCatFactsRepository(
        impl: CatFactsRepositoryImpl,
    ): CatFactsRepository
}
