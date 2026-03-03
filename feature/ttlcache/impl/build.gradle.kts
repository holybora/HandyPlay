plugins {
    id("handyplay.android.feature.impl")
}

android {
    namespace = "com.sls.handbook.feature.ttlcache"
}

dependencies {
    api(project(":feature:ttlcache:api"))
}
