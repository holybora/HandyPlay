plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.core.notifications"
}

dependencies {
    implementation(project(":core:model"))
}
