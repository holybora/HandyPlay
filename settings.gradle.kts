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
include(":app-nia-catalog")

// Core
include(":core:common")
include(":core:designsystem")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:analytics")
include(":core:navigation")
include(":core:notifications")
include(":core:testing")
include(":core:data-test")
include(":core:screenshot-testing")

// Features
include(":feature:welcome:api")
include(":feature:welcome:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:category:api")
include(":feature:category:impl")
include(":feature:ttlcache:api")
include(":feature:ttlcache:impl")
include(":feature:gallery:api")
include(":feature:gallery:impl")
include(":feature:fever:api")
include(":feature:fever:impl")
include(":feature:dp-creational:api")
include(":feature:dp-creational:impl")
include(":feature:dp-structural:api")
include(":feature:dp-structural:impl")
include(":feature:dp-behavioral:api")
include(":feature:dp-behavioral:impl")

// Lint
include(":lint")

// Sync
include(":sync:work")

// Benchmarks
include(":benchmarks")
