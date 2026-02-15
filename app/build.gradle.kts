plugins {
    id("handyplay.android.application")
    id("handyplay.android.hilt")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sls.handbook"

    defaultConfig {
        applicationId = "com.sls.handbook"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    // Navigation
    implementation(project(":navigation"))

    // Features
    implementation(project(":feature:welcome"))
    implementation(project(":feature:home"))
    implementation(project(":feature:category"))
    implementation(project(":feature:ttlcache"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

tasks.register("installAndRun") {
    group = "install"
    description = "Installs the Debug build and launches the app on a connected device"

    dependsOn("installDebug")

    doLast {
        val packageName = android.defaultConfig.applicationId ?: "com.sls.handbook"

        println("Launching $packageName...")

        project.providers.exec {
            commandLine(
                "adb",
                "shell",
                "am",
                "start",
                "-n",
                "$packageName/.MainActivity",
                "-a",
                "android.intent.action.MAIN",
                "-c",
                "android.intent.category.LAUNCHER"
            )
        }.result.get().assertNormalExitValue()

        println("App launched successfully!")
    }
}
