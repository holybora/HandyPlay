package com.sls.handbook.feature.listvssequence

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.AccentBlue
import com.sls.handbook.core.designsystem.theme.AccentEmerald
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme

@Composable
fun ListVsSequenceScreen(
    uiState: ListVsSequenceUiState,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is ListVsSequenceUiState.Idle -> ListVsSequenceContent(
            uiState = uiState,
            onStartClick = onStartClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun ListVsSequenceContent(
    uiState: ListVsSequenceUiState.Idle,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TitleRow()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CodeSnippetBox(
                code = ListCodeSnippet,
                modifier = Modifier.weight(1f),
            )
            CodeSnippetBox(
                code = SequenceCodeSnippet,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            BenchmarkColumn(
                result = uiState.listResult,
                color = AccentBlue,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            BenchmarkColumn(
                result = uiState.sequenceResult,
                color = AccentEmerald,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartClick,
            enabled = !uiState.isRunning,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = if (uiState.isRunning) "Running..." else "Start")
        }
    }
}

@Composable
private fun TitleRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "List",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AccentBlue,
        )
        Text(
            text = "  vs  ",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Sequence",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AccentEmerald,
        )
    }
}

@Composable
private fun CodeSnippetBox(
    code: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
            ),
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
private fun BenchmarkColumn(
    result: BenchmarkResult,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = result.progress,
        animationSpec = tween(durationMillis = ProgressAnimationMs),
        label = "benchmarkProgress",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalProgressBar(
            progress = animatedProgress,
            color = color,
            modifier = Modifier
                .height(150.dp)
                .width(32.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Heap allocated: ${formatBytes(result.heapAllocatedBytes)}",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Time spent: ${"%.2f".format(result.timeSpentMs)} ms",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun VerticalProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = progress)
                .clip(RoundedCornerShape(8.dp))
                .background(color),
        )
    }
}

private fun formatBytes(bytes: Long): String {
    val mb = bytes / BytesPerMb
    return "%.2f MB".format(mb)
}

private const val ProgressAnimationMs = 300
private const val BytesPerMb = 1_048_576.0

private const val ListCodeSnippet = """(1..1_000)
  .toList()
  .map { it * 2 }
  .filter { it > 100 }
  .take(10)"""

private const val SequenceCodeSnippet = """(1..1_000)
  .asSequence()
  .map { it * 2 }
  .filter { it > 100 }
  .take(10)
  .toList()"""

@Preview(showBackground = true)
@Composable
private fun ListVsSequenceScreenIdlePreview() {
    HandyPlayTheme {
        ListVsSequenceScreen(
            uiState = ListVsSequenceUiState.Idle(),
            onStartClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListVsSequenceScreenWithResultsPreview() {
    HandyPlayTheme {
        ListVsSequenceScreen(
            uiState = ListVsSequenceUiState.Idle(
                listResult = BenchmarkResult(
                    heapAllocatedBytes = 57_000_000L,
                    timeSpentMs = 85.3,
                    progress = 1f,
                ),
                sequenceResult = BenchmarkResult(
                    heapAllocatedBytes = 200L,
                    timeSpentMs = 1.2,
                    progress = 1f,
                ),
            ),
            onStartClick = {},
        )
    }
}
