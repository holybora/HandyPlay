package com.sls.handbook.core.data.repository

import com.sls.handbook.core.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        repository = CategoryRepositoryImpl()
    }

    @Test
    fun `getCategories returns two categories`() {
        val categories = repository.getCategories()
        assertEquals(2, categories.size)
    }

    @Test
    fun `getCategories returns expected category ids`() {
        val ids = repository.getCategories().map { it.id }
        assertEquals(
            listOf("kotlin_fundamentals", "ui"),
            ids,
        )
    }

    @Test
    fun `getCategories returns expected category names`() {
        val names = repository.getCategories().map { it.name }
        assertEquals(
            listOf("Kotlin Fundamentals", "UI"),
            names,
        )
    }

    @Test
    fun `getTopicsByCategoryId returns topics for ui`() {
        val topics = repository.getTopicsByCategoryId("ui")
        assertEquals(2, topics.size)
        assertTrue(topics.any { it.id == Topic.ID_GALLERY })
        assertTrue(topics.any { it.id == Topic.ID_FEVER })
    }

    @Test
    fun `getTopicsByCategoryId returns topics for kotlin_fundamentals`() {
        val topics = repository.getTopicsByCategoryId("kotlin_fundamentals")
        assertEquals(1, topics.size)
        assertTrue(topics.any { it.id == Topic.ID_TTL_CACHE })
    }

    @Test
    fun `getTopicsByCategoryId returns topics for each category`() {
        val categoryIds = repository.getCategories().map { it.id }
        for (id in categoryIds) {
            val topics = repository.getTopicsByCategoryId(id)
            assertTrue("Expected topics for category $id", topics.isNotEmpty())
            assertTrue(
                "All topics should have matching categoryId",
                topics.all { it.categoryId == id },
            )
        }
    }

    @Test
    fun `getTopicsByCategoryId returns empty list for unknown category`() {
        val topics = repository.getTopicsByCategoryId("unknown_category")
        assertTrue(topics.isEmpty())
    }

    @Test
    fun `getCategories returns same instance on repeated calls`() {
        val first = repository.getCategories()
        val second = repository.getCategories()
        assertEquals(first, second)
    }
}
