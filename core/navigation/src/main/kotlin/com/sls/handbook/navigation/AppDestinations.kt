package com.sls.handbook.navigation

import kotlinx.serialization.Serializable

@Serializable
object WelcomeDestination

@Serializable
object HomeDestination

@Serializable
data class CategoryDestination(
    val categoryId: String,
    val categoryName: String,
)

@Serializable
object TtlCacheDestination

@Serializable
object GalleryDestination

@Serializable
object FeverDestination

// Design Patterns - Creational
@Serializable
object FactoryMethodDestination

@Serializable
object AbstractFactoryDestination

@Serializable
object PrototypeDestination

// Design Patterns - Structural
@Serializable
object AdapterPatternDestination

@Serializable
object DecoratorDestination

@Serializable
object FacadeDestination

// Design Patterns - Behavioral
@Serializable
object ObserverDestination

@Serializable
object StrategyDestination

@Serializable
object CommandDestination

@Serializable
object StateMachineDestination
