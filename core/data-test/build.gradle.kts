plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.core.datatest"
}

dependencies {
    implementation(project(":core:data"))
}
