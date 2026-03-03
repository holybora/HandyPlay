package com.sls.handbook.feature.fever

import com.sls.handbook.feature.fever.RandomCoordinatesGenerator.Companion.LAT_MAX
import com.sls.handbook.feature.fever.RandomCoordinatesGenerator.Companion.LAT_MIN
import com.sls.handbook.feature.fever.RandomCoordinatesGenerator.Companion.LON_MAX
import com.sls.handbook.feature.fever.RandomCoordinatesGenerator.Companion.LON_MIN
import org.junit.Assert.assertTrue
import org.junit.Test

class RandomCoordinatesGeneratorTest {

    private val generator = RandomCoordinatesGenerator()

    @Test
    fun `latitude is within valid range`() {
        repeat(1000) {
            val (lat, _) = generator.generate()
            assertTrue("latitude $lat should be >= $LAT_MIN", lat >= LAT_MIN)
            assertTrue("latitude $lat should be < $LAT_MAX", lat < LAT_MAX)
        }
    }

    @Test
    fun `longitude is within valid range`() {
        repeat(1000) {
            val (_, lon) = generator.generate()
            assertTrue("longitude $lon should be >= $LON_MIN", lon >= LON_MIN)
            assertTrue("longitude $lon should be < $LON_MAX", lon < LON_MAX)
        }
    }

    @Test
    fun `successive calls produce different coordinates`() {
        val results = (1..10).map { generator.generate() }.toSet()
        assertTrue("expected multiple distinct coordinates, got ${results.size}", results.size > 1)
    }
}
