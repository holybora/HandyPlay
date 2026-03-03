plugins {
    id("handyplay.android.application")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sls.handbook.catalog"

    defaultConfig {
        applicationId = "com.sls.handbook.catalog"
        versionCode = 1
        versionName = "0.0.1"
    }

    buildFeatures {
        compose = true
    }

    lint {
        disable.add("MissingApplicationIcon")
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
}
