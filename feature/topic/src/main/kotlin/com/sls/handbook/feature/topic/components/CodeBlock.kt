package com.sls.handbook.feature.topic.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sls.handbook.core.designsystem.theme.Zinc100
import com.sls.handbook.core.designsystem.theme.Zinc900

@Composable
fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Zinc900,
    ) {
        Text(
            text = code,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(16.dp),
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = Zinc100,
        )
    }
}
