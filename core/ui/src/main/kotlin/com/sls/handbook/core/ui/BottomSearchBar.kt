package com.sls.handbook.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.rebugger.Rebugger

@Composable
fun BottomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    pathSegments: List<String>,
    modifier: Modifier = Modifier,
    onSegmentClick: (Int) -> Unit = {},
) {
    Rebugger(
        composableName = "BottomSearchBar",
        trackMap = mapOf(
            "query" to query,
            "pathSegments" to pathSegments,
        ),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 16.dp),
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        BreadcrumbBar(
            pathSegments = pathSegments,
            onSegmentClick = onSegmentClick,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
