plugins {
    id("handyplay.android.library")
    id("handyplay.android.hilt")
}

android {
    namespace = "com.sls.handbook.core.data"
}

dependencies {
    api(project(":core:domain"))
    api(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
}
