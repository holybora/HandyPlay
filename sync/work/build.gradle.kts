plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.sync"
}

dependencies {
    implementation(project(":core:data"))
}
