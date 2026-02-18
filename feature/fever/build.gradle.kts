plugins {
    id("handyplay.android.feature")
}

android {
    namespace = "com.sls.handbook.feature.fever"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
