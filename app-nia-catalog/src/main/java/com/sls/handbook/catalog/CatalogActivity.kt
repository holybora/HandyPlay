package com.sls.handbook.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme

class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandyPlayTheme {
                Text("HandyPlay Component Catalog")
            }
        }
    }
}
