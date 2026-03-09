plugins {
    id("handyplay.android.feature.impl")
    id("handyplay.android.roborazzi")
}

android {
    namespace = "com.sls.handbook.feature.ttlcache"
}

dependencies {
    api(project(":feature:ttlcache:api"))
}
