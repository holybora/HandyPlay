plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.core.analytics"
}

dependencies {
    implementation(project(":core:model"))
}
