package com.sls.handbook.core.data.repository

import com.sls.handbook.core.data.mapper.toDomain
import com.sls.handbook.core.domain.repository.GalleryRepository
import com.sls.handbook.core.model.GalleryImage
import com.sls.handbook.core.network.api.PicsumApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleryRepositoryImpl @Inject constructor(
    private val picsumApi: PicsumApi,
) : GalleryRepository {

    override suspend fun getImages(page: Int, limit: Int): List<GalleryImage> =
        picsumApi.getImages(page = page, limit = limit).map { it.toDomain() }
}
