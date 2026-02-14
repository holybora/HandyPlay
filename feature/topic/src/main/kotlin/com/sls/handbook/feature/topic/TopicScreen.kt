package com.sls.handbook.feature.topic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.ContentSection
import com.sls.handbook.core.model.TopicContent
import com.sls.handbook.core.ui.BreadcrumbBar
import com.sls.handbook.feature.topic.components.ContentSectionCard

@Composable
fun TopicScreen(
    uiState: TopicUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is TopicUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is TopicUiState.Error -> {
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

        is TopicUiState.Success -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                item {
                    BreadcrumbBar(
                        pathSegments = listOf(
                            "Home",
                            uiState.categoryName,
                            uiState.topicName,
                        ),
                    )
                }

                items(
                    items = uiState.content.sections,
                    key = { it.title },
                ) { section ->
                    ContentSectionCard(section = section)
                }
            }
        }
    }
}

private val sampleSections = listOf(
    ContentSection(
        title = "Example Section",
        description = "This is a sample section.",
        code = "val x = 42",
    ),
)

@Preview(showBackground = true)
@Composable
private fun TopicScreenLightPreview() {
    HandyPlayTheme(darkTheme = false) {
        TopicScreen(
            uiState = TopicUiState.Success(
                topicName = "TTL-cached",
                categoryName = "Kotlin Fundamentals",
                content = TopicContent(
                    topicId = "kf_7",
                    topicName = "TTL-cached",
                    sections = sampleSections,
                ),
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicScreenDarkPreview() {
    HandyPlayTheme(darkTheme = true) {
        TopicScreen(
            uiState = TopicUiState.Success(
                topicName = "TTL-cached",
                categoryName = "Kotlin Fundamentals",
                content = TopicContent(
                    topicId = "kf_7",
                    topicName = "TTL-cached",
                    sections = sampleSections,
                ),
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicScreenLoadingPreview() {
    HandyPlayTheme {
        TopicScreen(uiState = TopicUiState.Loading)
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicScreenErrorPreview() {
    HandyPlayTheme {
        TopicScreen(uiState = TopicUiState.Error("Content not found"))
    }
}
