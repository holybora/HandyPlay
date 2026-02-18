package com.sls.handbook.core.data.di

import com.sls.handbook.core.data.repository.CategoryRepositoryImpl
import com.sls.handbook.core.data.repository.GalleryRepositoryImpl
import com.sls.handbook.core.data.repository.JokeRepositoryImpl
import com.sls.handbook.core.domain.repository.CategoryRepository
import com.sls.handbook.core.domain.repository.GalleryRepository
import com.sls.handbook.core.domain.repository.JokeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindJokeRepository(
        impl: JokeRepositoryImpl,
    ): JokeRepository

    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl,
    ): CategoryRepository

    @Binds
    abstract fun bindGalleryRepository(
        impl: GalleryRepositoryImpl,
    ): GalleryRepository
}
