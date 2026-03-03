plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.dp.structural"
}

dependencies {
    api(project(":feature:dp-structural:api"))
}
