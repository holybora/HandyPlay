plugins {
    id("handyplay.android.library")
    id("handyplay.android.library.compose")
}

android {
    namespace = "com.sls.handbook.core.screenshottesting"
}

dependencies {
    api(libs.roborazzi.core)
    api(libs.roborazzi.compose)
    api(libs.roborazzi.junit.rule)
    api(libs.robolectric)
    api(libs.junit)
    api(libs.androidx.compose.ui.test.junit4)
    api(platform(libs.androidx.compose.bom))
    implementation(project(":core:designsystem"))
}
