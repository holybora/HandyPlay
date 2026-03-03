plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.gallery"
}

dependencies {
    api(project(":feature:gallery:api"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.activity.compose)
}
