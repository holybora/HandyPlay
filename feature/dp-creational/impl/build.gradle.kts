plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.dp.creational"
}

dependencies {
    api(project(":feature:dp-creational:api"))
}
