plugins {
    id("handyplay.android.library")
    id("handyplay.android.library.compose")
}

android {
    namespace = "com.sls.handbook.core.ui"
}

dependencies {
    api(project(":core:designsystem"))
    api(project(":core:model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
