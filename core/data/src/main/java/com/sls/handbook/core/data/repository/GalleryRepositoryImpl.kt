package com.sls.handbook.core.data.repository

import com.sls.handbook.core.domain.repository.GalleryRepository
import com.sls.handbook.core.model.GalleryImage
import com.sls.handbook.core.network.api.PicsumApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleryRepositoryImpl @Inject constructor(
    private val picsumApi: PicsumApi,
) : GalleryRepository {

    override suspend fun getImages(page: Int, limit: Int): List<GalleryImage> {
        return picsumApi.getImages(page = page, limit = limit).map { response ->
            GalleryImage(
                id = response.id,
                author = response.author,
                width = response.width,
                height = response.height,
                downloadUrl = response.downloadUrl,
            )
        }
    }
}
