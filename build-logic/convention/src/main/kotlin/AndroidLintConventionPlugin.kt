import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.Lint
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val lintConfig: Lint.() -> Unit = {
                warningsAsErrors = true
                abortOnError = true
                checkDependencies = true
                lintConfig = rootProject.file("config/lint/lint.xml")
                htmlReport = true
                xmlReport = true
            }

            extensions.findByType(ApplicationExtension::class.java)?.lint(lintConfig)
            extensions.findByType(LibraryExtension::class.java)?.lint(lintConfig)
        }
    }
}
