package com.sls.handbook.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicTest {

    @Test
    fun `TtlCache returns correct id, name, and categoryId`() {
        val topic: Topic = Topic.KotlinFundamental.TtlCache
        assertEquals("kf_7", topic.id)
        assertEquals("TTL Cache", topic.name)
        assertEquals("kotlin_fundamentals", topic.categoryId)
    }

    @Test
    fun `Gallery returns correct id, name, and categoryId`() {
        val topic: Topic = Topic.Ui.Gallery
        assertEquals("ui_1", topic.id)
        assertEquals("Gallery", topic.name)
        assertEquals("ui", topic.categoryId)
    }

    @Test
    fun `Fever returns correct id, name, and categoryId`() {
        val topic: Topic = Topic.Ui.Fever
        assertEquals("ui_2", topic.id)
        assertEquals("Fever", topic.name)
        assertEquals("ui", topic.categoryId)
    }

    @Test
    fun `FactoryMethod returns correct id, name, and categoryId`() {
        val topic: Topic = Topic.DesignPattern.FactoryMethod
        assertEquals("dp_1", topic.id)
        assertEquals("Factory Method", topic.name)
        assertEquals("design_patterns", topic.categoryId)
    }

    @Test
    fun `all design pattern topics have design_patterns categoryId`() {
        val patterns: List<Topic> = listOf(
            Topic.DesignPattern.FactoryMethod,
            Topic.DesignPattern.AbstractFactory,
            Topic.DesignPattern.Prototype,
            Topic.DesignPattern.Adapter,
            Topic.DesignPattern.Decorator,
            Topic.DesignPattern.Facade,
            Topic.DesignPattern.Observer,
            Topic.DesignPattern.Strategy,
            Topic.DesignPattern.Command,
            Topic.DesignPattern.StateMachine,
        )
        assertTrue(patterns.all { it.categoryId == "design_patterns" })
    }

    @Test
    fun `TtlCache is KotlinFundamental`() {
        assertTrue(Topic.KotlinFundamental.TtlCache is Topic.KotlinFundamental)
    }

    @Test
    fun `Gallery is Ui`() {
        assertTrue(Topic.Ui.Gallery is Topic.Ui)
    }

    @Test
    fun `FactoryMethod is DesignPattern`() {
        assertTrue(Topic.DesignPattern.FactoryMethod is Topic.DesignPattern)
    }

    @Test
    fun `data objects are singletons`() {
        val a: Topic = Topic.KotlinFundamental.TtlCache
        val b: Topic = Topic.KotlinFundamental.TtlCache
        assertTrue(a === b)
    }

    @Test
    fun `all design pattern topics have unique ids`() {
        val patterns = listOf(
            Topic.DesignPattern.FactoryMethod,
            Topic.DesignPattern.AbstractFactory,
            Topic.DesignPattern.Prototype,
            Topic.DesignPattern.Adapter,
            Topic.DesignPattern.Decorator,
            Topic.DesignPattern.Facade,
            Topic.DesignPattern.Observer,
            Topic.DesignPattern.Strategy,
            Topic.DesignPattern.Command,
            Topic.DesignPattern.StateMachine,
        )
        val ids = patterns.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}
