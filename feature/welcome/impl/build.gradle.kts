plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.welcome"
}

dependencies {
    api(project(":feature:welcome:api"))
}
