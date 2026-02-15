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
    implementation(project(":core:model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    testImplementation(libs.junit)
}
