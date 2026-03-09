plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.category"
}

dependencies {
    api(project(":feature:category:api"))
}
