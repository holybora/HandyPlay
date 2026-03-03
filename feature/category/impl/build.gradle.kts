plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.category"
}

dependencies {
    api(project(":feature:category:api"))
}
