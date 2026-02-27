package com.sls.handbook.recording

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val TAP_MAX_DISTANCE_DP = 20f
private const val TAP_MAX_DURATION_MS = 300L

@Composable
fun RecordingOverlay(
    controller: RecordingController,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val tapMaxDistancePx = with(density) { TAP_MAX_DISTANCE_DP.dp.toPx() }

    var stopButtonBounds by remember { mutableStateOf(Rect.Zero) }

    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    Box(modifier = modifier.fillMaxSize()) {
        // Touch interceptor layer — observes all events without consuming
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(tapMaxDistancePx) {
                    awaitPointerEventScope {
                        var downPosition: Offset? = null
                        var downTimeMs: Long? = null

                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)

                            for (change in event.changes) {
                                if (change.pressed && !change.previousPressed) {
                                    downPosition = change.position
                                    downTimeMs = System.currentTimeMillis()
                                }

                                if (!change.pressed && change.previousPressed) {
                                    val startPos = downPosition ?: continue
                                    val startTime = downTimeMs ?: continue
                                    val endPos = change.position
                                    val durationMs = System.currentTimeMillis() - startTime

                                    // Skip taps on the stop button
                                    if (stopButtonBounds.contains(endPos)) {
                                        downPosition = null
                                        downTimeMs = null
                                        continue
                                    }

                                    val distance = sqrt(
                                        (endPos.x - startPos.x) * (endPos.x - startPos.x) +
                                            (endPos.y - startPos.y) * (endPos.y - startPos.y),
                                    )

                                    if (distance < tapMaxDistancePx && durationMs < TAP_MAX_DURATION_MS) {
                                        controller.recordTap(endPos.x, endPos.y)
                                    } else {
                                        controller.recordSwipe(
                                            startX = startPos.x,
                                            startY = startPos.y,
                                            endX = endPos.x,
                                            endY = endPos.y,
                                            durationMs = durationMs,
                                        )
                                    }

                                    downPosition = null
                                    downTimeMs = null
                                }
                            }
                        }
                    }
                },
        ) {
            content()
        }

        // Pulsing recording indicator (top-left)
        RecordingIndicator(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp, top = 8.dp),
        )

        // Draggable stop button (bottom-end, movable)
        StopButton(
            onClick = onStopRecording,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 48.dp)
                .offset { IntOffset(dragOffsetX.roundToInt(), dragOffsetY.roundToInt()) }
                .onGloballyPositioned { coordinates ->
                    stopButtonBounds = coordinates.boundsInRoot()
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        dragOffsetX += dragAmount.x
                        dragOffsetY += dragAmount.y
                    }
                },
        )
    }
}

@Composable
private fun StopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .background(Color.Red, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        // White square "stop" icon
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(Color.White, RoundedCornerShape(2.dp)),
        )
    }
}

@Composable
private fun RecordingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "recording-pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "recording-alpha",
    )

    Box(
        modifier = modifier
            .size(12.dp)
            .graphicsLayer { this.alpha = alpha }
            .background(Color.Red, CircleShape),
    )
}
