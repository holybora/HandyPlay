plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.fever"
}

dependencies {
    api(project(":feature:fever:api"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.compose.material.icons.extended)
}
