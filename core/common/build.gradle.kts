plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
}
