import com.android.build.api.dsl.LibraryExtension
import com.sls.handbook.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("handyplay.detekt")
            pluginManager.apply("handyplay.kover")
            pluginManager.apply("handyplay.android.lint")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
        }
    }
}
