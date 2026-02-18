package com.sls.handbook.feature.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.GalleryImage
import com.theapache64.rebugger.Rebugger

@Composable
fun GalleryScreen(
    uiState: GalleryUiState,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Rebugger(
        composableName = "GalleryScreen",
        trackMap = mapOf(
            "uiState" to uiState,
        ),
    )

    when (uiState) {
        is GalleryUiState.Loading -> LoadingContent(modifier = modifier)
        is GalleryUiState.Content -> GalleryContent(
            uiState = uiState,
            onLoadMore = onLoadMore,
            modifier = modifier,
        )
        is GalleryUiState.Error -> ErrorContent(
            message = uiState.message,
            onRetry = onRetry,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun GalleryContent(
    uiState: GalleryUiState.Content,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedImage by remember { mutableStateOf<GalleryImage?>(null) }
    var selectedImageBounds by remember { mutableStateOf(Rect.Zero) }
    val imageCoordinates = remember { mutableMapOf<String, LayoutCoordinates>() }
    var parentCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { parentCoordinates = it },
    ) {
        GalleryGrid(
            uiState = uiState,
            onLoadMore = onLoadMore,
            onImageClick = { image ->
                val coords = imageCoordinates[image.id]
                val parent = parentCoordinates
                if (coords != null && parent != null && coords.isAttached) {
                    val pos = parent.localPositionOf(coords, Offset.Zero)
                    val size = Size(coords.size.width.toFloat(), coords.size.height.toFloat())
                    selectedImageBounds = Rect(pos, size)
                }
                selectedImage = image
            },
            onImageLayout = { id, coords -> imageCoordinates[id] = coords },
        )

        selectedImage?.let { image ->
            FullScreenImageViewer(
                image = image,
                thumbnailBounds = selectedImageBounds,
                onDismiss = { selectedImage = null },
            )
        }
    }
}

@Composable
private fun GalleryGrid(
    uiState: GalleryUiState.Content,
    onLoadMore: () -> Unit,
    onImageClick: (GalleryImage) -> Unit,
    onImageLayout: (String, LayoutCoordinates) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = gridState.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisibleIndex >= totalItems - 6
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            currentOnLoadMore()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = uiState.images,
            key = { it.id },
        ) { image ->
            GalleryImageItem(
                image = image,
                onClick = { onImageClick(image) },
                onImageLayout = { onImageLayout(image.id, it) },
            )
        }

        if (uiState.isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun GalleryImageItem(
    image: GalleryImage,
    onClick: () -> Unit,
    onImageLayout: (LayoutCoordinates) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        AsyncImage(
            model = image.downloadUrl,
            contentDescription = "Photo by ${image.author}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .onGloballyPositioned(onImageLayout),
        )
        Text(
            text = image.author,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenLoadingPreview() {
    HandyPlayTheme {
        GalleryScreen(
            uiState = GalleryUiState.Loading,
            onLoadMore = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenErrorPreview() {
    HandyPlayTheme {
        GalleryScreen(
            uiState = GalleryUiState.Error(message = "Network error"),
            onLoadMore = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenContentPreview() {
    HandyPlayTheme {
        GalleryScreen(
            uiState = GalleryUiState.Content(
                images = listOf(
                    GalleryImage("1", "Author One", 300, 300, "https://picsum.photos/id/1/300"),
                    GalleryImage("2", "Author Two", 300, 300, "https://picsum.photos/id/2/300"),
                ),
            ),
            onLoadMore = {},
            onRetry = {},
        )
    }
}
