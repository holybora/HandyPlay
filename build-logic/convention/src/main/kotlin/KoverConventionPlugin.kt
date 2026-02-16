import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<KoverProjectExtension> {
                reports {
                    filters {
                        excludes {
                            androidGeneratedClasses()
                            annotatedBy(
                                "androidx.compose.ui.tooling.preview.Preview",
                                "dagger.Module",
                                "dagger.hilt.InstallIn",
                            )
                            classes(
                                "*_Hilt*",
                                "*_Factory",
                                "*_MembersInjector",
                                "Dagger*",
                                "*_Impl",
                                "*_Impl$*",
                                "*.di.*Module",
                                "*.di.*Module_*",
                                "hilt_aggregated_deps.*",
                                "dagger.hilt.internal.*",
                            )
                        }
                    }
                }
            }
        }
    }
}
