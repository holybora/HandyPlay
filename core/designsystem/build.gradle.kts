plugins {
    id("handyplay.android.library")
    id("handyplay.android.library.compose")
}

android {
    namespace = "com.sls.handbook.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
