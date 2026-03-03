plugins {
    id("handyplay.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sls.handbook.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}
