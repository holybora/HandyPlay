package com.sls.handbook.navigation

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
}
