plugins {
    id("handyplay.android.library")
}

android {
    namespace = "com.sls.handbook.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
}
