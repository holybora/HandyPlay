plugins {
    id("handyplay.android.library")
    id("handyplay.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sls.handbook.core.network"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}
