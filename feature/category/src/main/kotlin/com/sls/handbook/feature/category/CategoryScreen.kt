package com.sls.handbook.feature.category

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
import com.sls.handbook.core.model.Topic
import com.sls.handbook.core.ui.BreadcrumbBar
import com.sls.handbook.core.ui.SearchBar
import com.sls.handbook.feature.category.components.TopicCard

@Composable
fun CategoryScreen(
    uiState: CategoryUiState,
    onSearchQueryChange: (String) -> Unit,
    onTopicClick: (Topic) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is CategoryUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is CategoryUiState.Error -> {
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

        is CategoryUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    BreadcrumbBar(pathSegments = listOf("Home", uiState.categoryName))
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                items(
                    items = uiState.topics,
                    key = { it.id },
                ) { topic ->
                    TopicCard(
                        topic = topic,
                        onClick = { onTopicClick(topic) },
                    )
                }
            }
        }
    }
}

private val sampleTopics = listOf(
    Topic(id = "1", name = "Variables & Types", categoryId = "kotlin_fundamentals"),
    Topic(id = "2", name = "Control Flow", categoryId = "kotlin_fundamentals"),
    Topic(id = "3", name = "Functions", categoryId = "kotlin_fundamentals"),
    Topic(id = "4", name = "Classes & Objects", categoryId = "kotlin_fundamentals"),
    Topic(id = "5", name = "Coroutines", categoryId = "kotlin_fundamentals"),
)

@Preview(showBackground = true)
@Composable
private fun CategoryScreenLightPreview() {
    HandyPlayTheme(darkTheme = false) {
        CategoryScreen(
            uiState = CategoryUiState.Success(
                categoryName = "Kotlin Fundamentals",
                topics = sampleTopics,
            ),
            onSearchQueryChange = {},
            onTopicClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenDarkPreview() {
    HandyPlayTheme(darkTheme = true) {
        CategoryScreen(
            uiState = CategoryUiState.Success(
                categoryName = "Kotlin Fundamentals",
                topics = sampleTopics,
            ),
            onSearchQueryChange = {},
            onTopicClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenLoadingPreview() {
    HandyPlayTheme {
        CategoryScreen(
            uiState = CategoryUiState.Loading,
            onSearchQueryChange = {},
            onTopicClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenEmptyPreview() {
    HandyPlayTheme {
        CategoryScreen(
            uiState = CategoryUiState.Success(
                categoryName = "Kotlin Fundamentals",
                topics = emptyList(),
                searchQuery = "xyz",
            ),
            onSearchQueryChange = {},
            onTopicClick = {},
        )
    }
}
