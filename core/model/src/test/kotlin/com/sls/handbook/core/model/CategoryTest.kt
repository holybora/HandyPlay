package com.sls.handbook.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoryTest {

    @Test
    fun `category holds id and name`() {
        val category = Category(id = "test_id", name = "Test Name")
        assertEquals("test_id", category.id)
        assertEquals("Test Name", category.name)
    }

    @Test
    fun `categories with same data are equal`() {
        val a = Category(id = "1", name = "A")
        val b = Category(id = "1", name = "A")
        assertEquals(a, b)
    }

    @Test
    fun `categories with different ids are not equal`() {
        val a = Category(id = "1", name = "A")
        val b = Category(id = "2", name = "A")
        assertNotEquals(a, b)
    }

    @Test
    fun `categories with different names are not equal`() {
        val a = Category(id = "1", name = "A")
        val b = Category(id = "1", name = "B")
        assertNotEquals(a, b)
    }

    @Test
    fun `category copy works correctly`() {
        val original = Category(id = "1", name = "Original")
        val copied = original.copy(name = "Copied")
        assertEquals("1", copied.id)
        assertEquals("Copied", copied.name)
    }

    @Test
    fun `equal categories have same hashCode`() {
        val a = Category(id = "1", name = "A")
        val b = Category(id = "1", name = "A")
        assertEquals(a.hashCode(), b.hashCode())
    }
}
