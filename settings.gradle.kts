pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HandyPlay"

// App
include(":app")

// Core
include(":core:common")
include(":core:designsystem")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":core:model")
include(":core:network")

// Navigation
include(":navigation")

// Features
include(":feature:welcome")
include(":feature:home")
include(":feature:category")
include(":feature:ttlcache")
include(":feature:gallery")
include(":feature:fever")
