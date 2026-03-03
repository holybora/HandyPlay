plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.welcome"
}

dependencies {
    api(project(":feature:welcome:api"))
}
