plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.home"
}

dependencies {
    api(project(":feature:home:api"))
}
