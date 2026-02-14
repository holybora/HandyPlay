package com.sls.handbook.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Category
import com.sls.handbook.feature.home.components.BreadcrumbBar
import com.sls.handbook.feature.home.components.CategoryCard
import com.sls.handbook.feature.home.components.HomeSearchBar

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        is HomeUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    BreadcrumbBar(pathSegments = listOf("Home"))
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeSearchBar(
                        query = uiState.searchQuery,
                        onQueryChanged = onSearchQueryChanged,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                items(
                    items = uiState.categories,
                    key = { it.id },
                ) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category) },
                    )
                }
            }
        }
    }
}

private val sampleCategories = listOf(
    Category(id = "1", name = "Kotlin Fundamentals"),
    Category(id = "2", name = "Android Core"),
    Category(id = "3", name = "Jetpack Compose"),
    Category(id = "4", name = "Architecture"),
    Category(id = "5", name = "Testing"),
    Category(id = "6", name = "Performance"),
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenLightPreview() {
    HandyPlayTheme(darkTheme = false) {
        HomeScreen(
            uiState = HomeUiState.Success(categories = sampleCategories),
            onSearchQueryChanged = {},
            onCategoryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenDarkPreview() {
    HandyPlayTheme(darkTheme = true) {
        HomeScreen(
            uiState = HomeUiState.Success(categories = sampleCategories),
            onSearchQueryChanged = {},
            onCategoryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    HandyPlayTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onSearchQueryChanged = {},
            onCategoryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenEmptyPreview() {
    HandyPlayTheme {
        HomeScreen(
            uiState = HomeUiState.Success(categories = emptyList(), searchQuery = "xyz"),
            onSearchQueryChanged = {},
            onCategoryClick = {},
        )
    }
}
