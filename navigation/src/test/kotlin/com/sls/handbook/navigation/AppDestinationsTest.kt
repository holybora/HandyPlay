package com.sls.handbook.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Test

class AppDestinationsTest {

    @Test
    fun `WelcomeDestination is instantiable`() {
        assertNotNull(WelcomeDestination)
    }

    @Test
    fun `HomeDestination is instantiable`() {
        assertNotNull(HomeDestination)
    }

    @Test
    fun `WelcomeDestination and HomeDestination are different types`() {
        val welcome: Any = WelcomeDestination
        val home: Any = HomeDestination
        assertNotSame(welcome::class, home::class)
    }

    @Test
    fun `CategoryDestination can be constructed with parameters`() {
        val destination = CategoryDestination(
            categoryId = "kotlin_fundamentals",
            categoryName = "Kotlin Fundamentals",
        )
        assertNotNull(destination)
        assertEquals("kotlin_fundamentals", destination.categoryId)
        assertEquals("Kotlin Fundamentals", destination.categoryName)
    }

    @Test
    fun `CategoryDestination holds categoryId and categoryName properties`() {
        val destination = CategoryDestination(
            categoryId = "test_id",
            categoryName = "Test Name",
        )
        assertEquals("test_id", destination.categoryId)
        assertEquals("Test Name", destination.categoryName)
    }

    @Test
    fun `CategoryDestinations with same data are equal`() {
        val a = CategoryDestination(categoryId = "id", categoryName = "Name")
        val b = CategoryDestination(categoryId = "id", categoryName = "Name")
        assertEquals(a, b)
    }

    @Test
    fun `CategoryDestinations with different categoryIds are not equal`() {
        val a = CategoryDestination(categoryId = "id1", categoryName = "Name")
        val b = CategoryDestination(categoryId = "id2", categoryName = "Name")
        assertNotEquals(a, b)
    }

    @Test
    fun `CategoryDestinations with different categoryNames are not equal`() {
        val a = CategoryDestination(categoryId = "id", categoryName = "Name1")
        val b = CategoryDestination(categoryId = "id", categoryName = "Name2")
        assertNotEquals(a, b)
    }

    @Test
    fun `CategoryDestination copy works correctly`() {
        val original = CategoryDestination(
            categoryId = "original_id",
            categoryName = "Original Name",
        )
        val copied = original.copy(categoryName = "Copied Name")
        assertEquals("original_id", copied.categoryId)
        assertEquals("Copied Name", copied.categoryName)
    }

    @Test
    fun `equal CategoryDestinations have same hashCode`() {
        val a = CategoryDestination(categoryId = "id", categoryName = "Name")
        val b = CategoryDestination(categoryId = "id", categoryName = "Name")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `TtlCacheDestination is instantiable`() {
        assertNotNull(TtlCacheDestination)
    }

    @Test
    fun `all destination types are distinct`() {
        val welcome: Any = WelcomeDestination
        val home: Any = HomeDestination
        val category: Any = CategoryDestination("id", "name")
        val ttlCache: Any = TtlCacheDestination

        assertNotSame(welcome::class, home::class)
        assertNotSame(welcome::class, category::class)
        assertNotSame(welcome::class, ttlCache::class)
        assertNotSame(home::class, category::class)
        assertNotSame(home::class, ttlCache::class)
        assertNotSame(category::class, ttlCache::class)
    }
}
