plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.home"
}

dependencies {
    api(project(":feature:home:api"))
}
