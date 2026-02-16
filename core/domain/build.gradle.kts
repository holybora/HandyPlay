plugins {
    id("handyplay.jvm.library")
}

dependencies {
    implementation(project(":core:model"))

    testImplementation(libs.junit)
}
