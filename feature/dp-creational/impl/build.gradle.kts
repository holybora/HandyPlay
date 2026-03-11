plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.dp.creational"
}

dependencies {
    api(project(":feature:dp-creational:api"))
}
