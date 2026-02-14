package com.sls.handbook.feature.ttlcache

import app.cash.turbine.test
import com.sls.handbook.core.domain.repository.CatFactsRepository
import com.sls.handbook.core.domain.repository.CatFactsResult
import com.sls.handbook.core.model.CatFacts
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TtlCacheViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository: CatFactsRepository = mockk()
    private lateinit var viewModel: TtlCacheViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TtlCacheViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle with default TTL`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as TtlCacheUiState.Idle
            assertEquals("10", state.ttlSeconds)
            assertEquals("", state.data)
            assertEquals("", state.lastFetchedTime)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `onTtlChange updates TTL value`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial state
            viewModel.onTtlChange("30")
            val updated = awaitItem() as TtlCacheUiState.Idle
            assertEquals("30", updated.ttlSeconds)
        }
    }

    @Test
    fun `onGetClick with invalid TTL shows error in data`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial state
            viewModel.onTtlChange("abc")
            awaitItem() // TTL updated
            viewModel.onGetClick()
            val errorState = awaitItem() as TtlCacheUiState.Idle
            assertTrue(errorState.data.contains("Error"))
        }
    }

    @Test
    fun `onGetClick fetches data successfully`() = runTest {
        coEvery { repository.getCatFacts(any()) } returns CatFactsResult(
            catFacts = CatFacts(facts = listOf("Cats sleep 16 hours a day")),
            fetchTimeMillis = 1_700_000_000_000L,
            fromCache = false,
        )

        viewModel.uiState.test {
            awaitItem() // initial state
            viewModel.onGetClick()
            // With UnconfinedTestDispatcher, loading and result may be conflated
            val result = expectMostRecentItem() as TtlCacheUiState.Idle
            assertEquals("Cats sleep 16 hours a day", result.data)
            assertTrue(result.lastFetchedTime.contains("(fresh)"))
            assertFalse(result.isLoading)
        }
    }

    @Test
    fun `onGetClick shows from cache label when cached`() = runTest {
        coEvery { repository.getCatFacts(any()) } returns CatFactsResult(
            catFacts = CatFacts(facts = listOf("Cached fact")),
            fetchTimeMillis = 1_700_000_000_000L,
            fromCache = true,
        )

        viewModel.uiState.test {
            awaitItem() // initial state
            viewModel.onGetClick()
            val result = expectMostRecentItem() as TtlCacheUiState.Idle
            assertTrue(result.lastFetchedTime.contains("(from cache)"))
        }
    }

    @Test
    fun `onGetClick handles network error`() = runTest {
        coEvery { repository.getCatFacts(any()) } throws java.io.IOException("Network error")

        viewModel.uiState.test {
            awaitItem() // initial state
            viewModel.onGetClick()
            val result = expectMostRecentItem() as TtlCacheUiState.Idle
            assertTrue(result.data.contains("Error"))
            assertTrue(result.data.contains("Network error"))
            assertFalse(result.isLoading)
        }
    }
}
