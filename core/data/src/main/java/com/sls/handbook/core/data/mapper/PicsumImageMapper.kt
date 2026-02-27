package com.sls.handbook.core.data.mapper

import com.sls.handbook.core.model.GalleryImage
import com.sls.handbook.core.network.model.PicsumImageResponse

internal fun PicsumImageResponse.toDomain(): GalleryImage =
    GalleryImage(
        id = id,
        author = author,
        width = width,
        height = height,
        downloadUrl = downloadUrl,
    )
