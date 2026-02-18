package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.GalleryImage

interface GalleryRepository {
    suspend fun getImages(page: Int, limit: Int): List<GalleryImage>
}
