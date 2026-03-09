plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.dp.behavioral"
}

dependencies {
    api(project(":feature:dp-behavioral:api"))
}
