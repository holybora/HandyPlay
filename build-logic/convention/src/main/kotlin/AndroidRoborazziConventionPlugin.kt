import com.android.build.api.dsl.LibraryExtension
import com.sls.handbook.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidRoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            tasks.withType<Test>().configureEach {
                val roborazziRecord = providers.gradleProperty("roborazzi.test.record")
                val roborazziCompare = providers.gradleProperty("roborazzi.test.compare")
                val roborazziVerify = providers.gradleProperty("roborazzi.test.verify")

                systemProperty("roborazzi.test.record", roborazziRecord.getOrElse("false"))
                systemProperty("roborazzi.test.compare", roborazziCompare.getOrElse("false"))
                systemProperty("roborazzi.test.verify", roborazziVerify.getOrElse("false"))
            }

            dependencies {
                add("testImplementation", libs.findLibrary("roborazzi-core").get())
                add("testImplementation", libs.findLibrary("roborazzi-compose").get())
                add("testImplementation", libs.findLibrary("roborazzi-junit-rule").get())
                add("testImplementation", project(":core:screenshot-testing"))
            }
        }
    }
}
