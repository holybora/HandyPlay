# build-logic/convention

Convention plugins for standardised Gradle module configuration.

## Plugin Catalog

| Plugin ID | Class | What it does |
|-----------|-------|-------------|
| `handyplay.android.application` | `AndroidApplicationConventionPlugin` | Android app defaults (SDK 36/33, Kotlin, Java 11, auto-applies detekt + kover + lint) |
| `handyplay.android.application.flavors` | `AndroidApplicationFlavorsConventionPlugin` | Product flavors (demo/prod with contentType dimension) |
| `handyplay.android.library` | `AndroidLibraryConventionPlugin` | Android library defaults (SDK, Kotlin, Java 11, auto-applies detekt + kover + lint) |
| `handyplay.android.library.compose` | `AndroidLibraryComposeConventionPlugin` | Adds Compose compiler plugin + Compose BOM |
| `handyplay.android.feature` | `AndroidFeatureConventionPlugin` | Library + Compose + Hilt + core module deps + Lifecycle |
| `handyplay.android.feature.api` | `AndroidFeatureApiConventionPlugin` | Feature API module (library + serialization + :core:navigation) |
| `handyplay.android.feature.impl` | `AndroidFeatureImplConventionPlugin` | Feature implementation module (library + compose + hilt + test) |
| `handyplay.android.hilt` | `AndroidHiltConventionPlugin` | KSP + Dagger Hilt (android + compiler) |
| `handyplay.android.test` | `AndroidTestConventionPlugin` | Common test dependencies (JUnit, MockK, Turbine, Coroutines Test, Robolectric, Compose UI test) |
| `handyplay.jvm.library` | `JvmLibraryConventionPlugin` | Pure JVM Kotlin (Java 11, no Android, auto-applies detekt + kover) |
| `handyplay.detekt` | `DetektConventionPlugin` | Static analysis with detekt + formatting + Compose rules |
| `handyplay.kover` | `KoverConventionPlugin` | Code coverage with exclusions for generated code, Hilt, and Compose (`@Composable`) |
| `handyplay.android.lint` | `AndroidLintConventionPlugin` | Android Lint with `warningsAsErrors`, HTML/XML reports, `config/lint/lint.xml` |
| `handyplay.android.roborazzi` | `AndroidRoborazziConventionPlugin` | Roborazzi screenshot testing (includeAndroidResources, system property forwarding, roborazzi + :core:screenshot-testing deps) |

## Key Files

- `AndroidFeatureConventionPlugin.kt` — Backward-compatibility plugin; auto-adds `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:core:navigation` + Compose/Lifecycle/Hilt deps
- `AndroidFeatureApiConventionPlugin.kt` — Feature API modules (library + serialization + `:core:navigation`)
- `AndroidFeatureImplConventionPlugin.kt` — Feature implementation modules (library + compose + hilt + test)
- `AndroidApplicationFlavorsConventionPlugin.kt` — Configures demo/prod product flavors
- `KoverConventionPlugin.kt` — Excludes `@Composable`, `@Preview`, Hilt, and generated Android classes from coverage reports
- `DetektConventionPlugin.kt` — Configures detekt with custom rules from `config/detekt/detekt.yml`
- `KotlinAndroid.kt` — Shared Kotlin/Android configuration (SDK levels, JVM target)
- `ProjectExtensions.kt` — `libs` extension for accessing version catalog
- `AndroidRoborazziConventionPlugin.kt` — Roborazzi screenshot testing setup (enables `isIncludeAndroidResources`, forwards `roborazzi.test.*` Gradle properties as JVM system properties, adds roborazzi + `:core:screenshot-testing` test deps)

## Build Config

- `build.gradle.kts` — Uses `java-gradle-plugin` + `kotlin-dsl`
- Compiles with Java 17 / Kotlin JVM 17 (build tooling only, app targets Java 11)

## Notes

- Plugin IDs are registered in `build.gradle.kts` via `gradlePlugin { plugins { ... } }`
- All convention plugins read versions from `gradle/libs.versions.toml` via the `libs` accessor
- When adding a new module, pick the appropriate convention plugin to avoid manual dependency setup
