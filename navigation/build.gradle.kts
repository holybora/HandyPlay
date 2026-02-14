plugins {
    id("handyplay.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sls.handbook.navigation"
}

dependencies {
    api(libs.androidx.navigation.compose)
    api(libs.kotlinx.serialization.json)
}
