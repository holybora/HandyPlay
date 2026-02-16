package com.sls.handbook.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TopicTest {

    @Test
    fun `topic holds id, name, and categoryId`() {
        val topic = Topic(id = "test_id", name = "Test Name", categoryId = "cat_1")
        assertEquals("test_id", topic.id)
        assertEquals("Test Name", topic.name)
        assertEquals("cat_1", topic.categoryId)
    }

    @Test
    fun `topics with same data are equal`() {
        val a = Topic(id = "1", name = "A", categoryId = "cat")
        val b = Topic(id = "1", name = "A", categoryId = "cat")
        assertEquals(a, b)
    }

    @Test
    fun `topics with different ids are not equal`() {
        val a = Topic(id = "1", name = "A", categoryId = "cat")
        val b = Topic(id = "2", name = "A", categoryId = "cat")
        assertNotEquals(a, b)
    }

    @Test
    fun `topics with different names are not equal`() {
        val a = Topic(id = "1", name = "A", categoryId = "cat")
        val b = Topic(id = "1", name = "B", categoryId = "cat")
        assertNotEquals(a, b)
    }

    @Test
    fun `topics with different categoryIds are not equal`() {
        val a = Topic(id = "1", name = "A", categoryId = "cat1")
        val b = Topic(id = "1", name = "A", categoryId = "cat2")
        assertNotEquals(a, b)
    }

    @Test
    fun `topic copy works correctly`() {
        val original = Topic(id = "1", name = "Original", categoryId = "cat")
        val copied = original.copy(name = "Copied")
        assertEquals("1", copied.id)
        assertEquals("Copied", copied.name)
        assertEquals("cat", copied.categoryId)
    }

    @Test
    fun `equal topics have same hashCode`() {
        val a = Topic(id = "1", name = "A", categoryId = "cat")
        val b = Topic(id = "1", name = "A", categoryId = "cat")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `companion constant ID_TTL_CACHE has correct value`() {
        assertEquals("kf_7", Topic.ID_TTL_CACHE)
    }
}
