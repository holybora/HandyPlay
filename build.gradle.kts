plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover) apply false
}

tasks.register("installGitHooks") {
    description = "Installs pre-commit git hooks for detekt"
    group = "git hooks"
    val hookSource = file("scripts/pre-commit")
    inputs.file(hookSource)
    doLast {
        val gitCommonDir = providers.exec {
            commandLine("git", "rev-parse", "--git-common-dir")
        }.standardOutput.asText.get().trim()
        val hooksDir = file("$gitCommonDir/hooks")
        hooksDir.mkdirs()
        val hookDest = File(hooksDir, "pre-commit")
        hookSource.copyTo(hookDest, overwrite = true)
        hookDest.setExecutable(true, false)
        logger.lifecycle("Installed pre-commit hook to ${hookDest.absolutePath}")
    }
}

tasks.matching { it.name == "prepareKotlinBuildScriptModel" }.configureEach {
    dependsOn("installGitHooks")
}
