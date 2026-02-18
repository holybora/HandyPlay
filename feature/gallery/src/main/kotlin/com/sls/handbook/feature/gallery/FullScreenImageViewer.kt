package com.sls.handbook.feature.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sls.handbook.core.model.GalleryImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.sqrt

private const val DismissThreshold = 150f
private const val MaxDragDistance = 500f
private const val EnterDuration = 300
private const val ExitDuration = 250
private const val MinScaleAtMaxDrag = 0.5f
private const val MinScrimAtMaxDrag = 0.15f
private const val FallbackInitScale = 0.85f

@Composable
fun FullScreenImageViewer(
    image: GalleryImage,
    thumbnailBounds: Rect,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val overlayW = constraints.maxWidth.toFloat()
        val overlayH = constraints.maxHeight.toFloat()
        val hasBounds = thumbnailBounds.width > 0f
        val initOffX = if (hasBounds) thumbnailBounds.center.x - overlayW / 2f else 0f
        val initOffY = if (hasBounds) thumbnailBounds.center.y - overlayH / 2f else 0f
        val initScale = if (hasBounds) (thumbnailBounds.width / overlayW).coerceIn(0.1f, 0.9f) else FallbackInitScale
        val scrimAlpha = remember { Animatable(0f) }
        val enterScale = remember { Animatable(initScale) }
        val enterOffset = remember { Animatable(Offset(initOffX, initOffY), Offset.VectorConverter) }
        val dragOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
        var isDismissing by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            launch { scrimAlpha.animateTo(1f, tween(EnterDuration)) }
            launch { enterScale.animateTo(1f, tween(EnterDuration, easing = EaseOut)) }
            launch { enterOffset.animateTo(Offset.Zero, tween(EnterDuration, easing = EaseOut)) }
        }

        fun dismiss(resetPosition: Boolean) {
            if (isDismissing) return
            isDismissing = true
            scope.launch {
                launch { scrimAlpha.animateTo(0f, tween(ExitDuration)) }
                if (resetPosition) {
                    launch { enterOffset.animateTo(Offset(initOffX, initOffY), tween(ExitDuration, easing = EaseIn)) }
                    launch { enterScale.animateTo(initScale, tween(ExitDuration, easing = EaseIn)) }
                } else {
                    launch { enterScale.animateTo(MinScaleAtMaxDrag, tween(ExitDuration)) }
                }
            }.invokeOnCompletion { onDismiss() }
        }

        BackHandler(enabled = true) { dismiss(resetPosition = true) }

        val dragDist = sqrt(dragOffset.value.x * dragOffset.value.x + dragOffset.value.y * dragOffset.value.y)
        val dragProgress = (dragDist / MaxDragDistance).coerceIn(0f, 1f)
        val dynamicScale = 1f - dragProgress * (1f - MinScaleAtMaxDrag)
        val dynamicScrim = 1f - dragProgress * (1f - MinScrimAtMaxDrag)

        FullScreenImageContent(
            image = image,
            translationX = enterOffset.value.x + dragOffset.value.x,
            translationY = enterOffset.value.y + dragOffset.value.y,
            imageScale = enterScale.value * dynamicScale,
            imageAlpha = scrimAlpha.value,
            overlayAlpha = scrimAlpha.value * dynamicScrim,
            onCloseClick = { dismiss(resetPosition = true) },
            modifier = Modifier.dragToDismiss(
                dragOffset = dragOffset,
                scope = scope,
                onDragDismiss = { dismiss(resetPosition = false) },
            ),
        )
    }
}

private fun Modifier.dragToDismiss(
    dragOffset: Animatable<Offset, AnimationVector2D>,
    scope: CoroutineScope,
    onDragDismiss: () -> Unit,
): Modifier = pointerInput(Unit) {
    detectDragGestures(
        onDrag = { change, dragAmount ->
            change.consume()
            scope.launch {
                dragOffset.snapTo(Offset(dragOffset.value.x + dragAmount.x, dragOffset.value.y + dragAmount.y))
            }
        },
        onDragEnd = {
            val d = sqrt(dragOffset.value.x * dragOffset.value.x + dragOffset.value.y * dragOffset.value.y)
            if (d > DismissThreshold) {
                onDragDismiss()
            } else {
                scope.launch { dragOffset.animateTo(Offset.Zero, spring(stiffness = Spring.StiffnessMedium)) }
            }
        },
        onDragCancel = {
            scope.launch { dragOffset.animateTo(Offset.Zero, spring(stiffness = Spring.StiffnessMedium)) }
        },
    )
}

@Composable
private fun FullScreenImageContent(
    image: GalleryImage,
    translationX: Float,
    translationY: Float,
    imageScale: Float,
    imageAlpha: Float,
    overlayAlpha: Float,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = overlayAlpha)),
    ) {
        AsyncImage(
            model = image.downloadUrl,
            contentDescription = "Full screen photo by ${image.author}",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.translationX = translationX
                    this.translationY = translationY
                    scaleX = imageScale
                    scaleY = imageScale
                    alpha = imageAlpha
                },
        )

        AuthorBar(
            author = image.author,
            alpha = overlayAlpha,
            modifier = Modifier.align(Alignment.BottomStart),
        )

        CloseButton(
            onClick = onCloseClick,
            alpha = overlayAlpha,
            modifier = Modifier.align(Alignment.TopEnd).systemBarsPadding(),
        )
    }
}

@Composable
private fun AuthorBar(
    author: String,
    alpha: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.45f))
            .graphicsLayer { this.alpha = alpha },
    ) {
        Text(
            text = author,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .systemBarsPadding(),
        )
    }
}

@Composable
private fun CloseButton(
    onClick: () -> Unit,
    alpha: Float,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.graphicsLayer { this.alpha = alpha },
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close full screen view",
            tint = Color.White,
        )
    }
}
