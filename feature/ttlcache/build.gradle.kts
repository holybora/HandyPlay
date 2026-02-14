plugins {
    id("handyplay.android.feature")
}

android {
    namespace = "com.sls.handbook.feature.ttlcache"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
}
