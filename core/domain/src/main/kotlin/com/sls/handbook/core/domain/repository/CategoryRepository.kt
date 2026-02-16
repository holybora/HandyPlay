package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.Category
import com.sls.handbook.core.model.Topic

interface CategoryRepository {
    fun getCategories(): List<Category>
    fun getTopicsByCategoryId(categoryId: String): List<Topic>
}
