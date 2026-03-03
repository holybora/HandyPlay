plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.core.database"
}

dependencies {
    implementation(project(":core:model"))
}
