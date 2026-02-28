package com.sls.handbook.core.domain.usecase

import com.sls.handbook.core.domain.repository.CategoryRepository
import com.sls.handbook.core.model.Topic
import javax.inject.Inject

/**
 * Retrieves topics belonging to a given category.
 *
 * Delegates to [CategoryRepository.getTopicsByCategoryId].
 *
 * @param categoryRepository data source for category information
 */
class GetTopicsByCategoryIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(categoryId: String): List<Topic> =
        categoryRepository.getTopicsByCategoryId(categoryId)
}
