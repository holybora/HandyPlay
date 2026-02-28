package com.sls.handbook.core.domain.usecase

import com.sls.handbook.core.domain.repository.CategoryRepository
import com.sls.handbook.core.model.Category
import com.sls.handbook.core.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetTopicsByCategoryIdUseCaseTest {

    private val fakeRepository = object : CategoryRepository {
        override fun getCategories(): List<Category> = emptyList()

        override fun getTopicsByCategoryId(categoryId: String): List<Topic> =
            when (categoryId) {
                "design_patterns" -> listOf(
                    Topic.DesignPattern.FactoryMethod,
                    Topic.DesignPattern.Observer,
                    Topic.DesignPattern.Strategy,
                )
                else -> emptyList()
            }
    }

    private val useCase = GetTopicsByCategoryIdUseCase(fakeRepository)

    @Test
    fun `returns topics for known category`() {
        val topics = useCase("design_patterns")
        assertEquals(3, topics.size)
        assertEquals("Factory Method", topics[0].name)
        assertEquals("Observer", topics[1].name)
        assertEquals("Strategy", topics[2].name)
    }

    @Test
    fun `returns empty list for unknown category`() {
        val topics = useCase("unknown")
        assertTrue(topics.isEmpty())
    }

    @Test
    fun `returned topics have correct categoryId`() {
        val topics = useCase("design_patterns")
        assertTrue(topics.all { it.categoryId == "design_patterns" })
    }

    @Test
    fun `delegates to repository with correct categoryId`() {
        var receivedId: String? = null
        val trackingRepository = object : CategoryRepository {
            override fun getCategories(): List<Category> = emptyList()
            override fun getTopicsByCategoryId(categoryId: String): List<Topic> {
                receivedId = categoryId
                return emptyList()
            }
        }
        val trackingUseCase = GetTopicsByCategoryIdUseCase(trackingRepository)

        trackingUseCase("my_category")

        assertEquals("my_category", receivedId)
    }
}
