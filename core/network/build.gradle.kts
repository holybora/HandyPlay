plugins {
    id("handyplay.android.library")
    id("handyplay.android.hilt")
}

android {
    namespace = "com.sls.handbook.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        // FIXME: This should ideally be placed in local.properties,
        // but I'm leaving it here so the app can be built and run without any additional changes
        buildConfigField("String", "OPENWEATHER_API_KEY", "\"ae103060692fe13422deb98285505dc6\"")
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    testImplementation(libs.junit)
}
