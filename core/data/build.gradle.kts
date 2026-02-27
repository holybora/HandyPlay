plugins {
    id("handyplay.android.library")
    id("handyplay.android.hilt")
}

android {
    namespace = "com.sls.handbook.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))

    testImplementation(libs.okhttp.core)
    implementation(libs.retrofit.core)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
