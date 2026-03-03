# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HandyPlay is an Android application built with Jetpack Compose and Material 3.
Package: `com.sls.handbook`. Multi-module clean architecture project.

## Figma MCP server rules

- The Figma MCP server provides an assets endpoint which can serve image and SVG assets
- IMPORTANT: If the Figma MCP server returns a localhost source for an image or an SVG, use that image or SVG source
  directly
- IMPORTANT: DO NOT import/add new icon packages, all the assets should be in the Figma payload
- IMPORTANT: do NOT use or create placeholders if a localhost source is provided

## Build & Test Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease                    # Minified release (R8 + resource shrinking enabled)

# Install and Run
./gradlew installAndRun                      # Installs debug APK and launches app
./gradlew installAndRunRelease               # Installs minified release and launches app (requires device)

# Unit tests (JVM) — all modules
./gradlew test

# Single module tests
./gradlew :app:testDebugUnitTest
./gradlew :feature:home:impl:testDebugUnitTest

# Single test class
./gradlew testDebugUnitTest --tests "com.sls.handbook.ExampleUnitTest"

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean assembleDebug

# Code coverage (Kover — aggregated in :app module)
./gradlew test :app:koverHtmlReport      # HTML report → app/build/reports/kover/html/
./gradlew test :app:koverXmlReport       # XML report → app/build/reports/kover/report.xml
./gradlew test :app:koverVerify          # Check coverage thresholds

# Static analysis (detekt)
./gradlew detekt                              # All modules, all source sets
./gradlew detektMain                          # Main sources only
./gradlew :feature:home:impl:detektMain       # Single module
./gradlew installGitHooks                     # Install pre-commit hook manually

# Android Lint
./gradlew lint                                # All modules
./gradlew :app:lintDebug                      # Single module
./gradlew :feature:home:impl:lintDebug        # Single feature module
```

## SDK & Tooling

- **Compile/Target SDK:** 36 | **Min SDK:** 33
- **Kotlin:** 2.0.21 with Compose compiler plugin
- **AGP:** 9.0.0
- **Java compatibility:** 11
- **Compose BOM:** 2026.01.01
- **Hilt:** 2.59
- **Navigation Compose:** 2.9.0 (type-safe with kotlinx.serialization)
- **Kover:** 0.9.7 (per-project plugin, aggregated in `:app` via `kover()` dependencies)
- **Detekt:** 1.23.8 with Compose rules (`io.nlopez.compose.rules`)
- **Product flavors:** `demo` (fake data) / `prod` (real APIs)
- **Dependency versions:** `gradle/libs.versions.toml`

## Module Structure

| Module | Type | Purpose |
|--------|------|---------|
| `:app` | Application | Entry point, NavHost, Hilt setup, product flavors |
| `:app-nia-catalog` | Application | Standalone component showcase app |
| `:feature:*:api` | Android Library | Navigation keys and shared feature interfaces |
| `:feature:*:impl` | Feature | Screens, ViewModels, feature-specific logic |
| `:core:model` | JVM Library | Data models (`@Serializable`) |
| `:core:domain` | JVM Library | Use cases / business logic |
| `:core:data` | Android Library | Repositories, data sources |
| `:core:database` | Android Library | Room database (placeholder) |
| `:core:datastore` | Android Library | Proto DataStore for user preferences (placeholder) |
| `:core:network` | Android Library | Retrofit API client, network DI |
| `:core:designsystem` | Android Library | Material3 theme (Color, Type, Theme) |
| `:core:ui` | Android Library | Shared composables |
| `:core:common` | Android Library | Shared utilities |
| `:core:analytics` | Android Library | Analytics abstraction (placeholder) |
| `:core:navigation` | Android Library | Type-safe route definitions |
| `:core:notifications` | Android Library | Push notification handling (placeholder) |
| `:core:testing` | Android Library | Test runner, shared test utilities (placeholder) |
| `:core:data-test` | Android Library | Fake repositories for tests (placeholder) |
| `:core:screenshot-testing` | Android Library | Roborazzi screenshot test helpers (placeholder) |
| `:lint` | JVM Library | Custom lint rules (placeholder) |
| `:sync:work` | Android Library | WorkManager sync tasks (placeholder) |
| `:benchmarks` | Android Library | Macrobenchmark + baseline profile generation (placeholder) |
| `:build-logic` | Included Build | Convention plugins |

### Feature Modules

| Feature | API module | Impl module |
|---------|-----------|-------------|
| Welcome/onboarding | `:feature:welcome:api` | `:feature:welcome:impl` |
| Home (category grid + search) | `:feature:home:api` | `:feature:home:impl` |
| Category (topic grid + search) | `:feature:category:api` | `:feature:category:impl` |
| TTL Cache demo | `:feature:ttlcache:api` | `:feature:ttlcache:impl` |
| Photo gallery | `:feature:gallery:api` | `:feature:gallery:impl` |
| Weather (Fever) | `:feature:fever:api` | `:feature:fever:impl` |
| Creational patterns | `:feature:dp-creational:api` | `:feature:dp-creational:impl` |
| Structural patterns | `:feature:dp-structural:api` | `:feature:dp-structural:impl` |
| Behavioral patterns | `:feature:dp-behavioral:api` | `:feature:dp-behavioral:impl` |

## Convention Plugins (`build-logic/`)

| Plugin ID | What it provides |
|-----------|-----------------|
| `handyplay.android.application` | Android app config (SDK, Kotlin, Java 11) |
| `handyplay.android.application.flavors` | Demo/prod product flavors |
| `handyplay.android.library` | Android library config (SDK, Kotlin, Java 11) |
| `handyplay.android.library.compose` | Compose compiler + BOM |
| `handyplay.android.feature` | Library + Compose + Hilt + core module deps (legacy, use api/impl) |
| `handyplay.android.feature.api` | Library + serialization + `:core:navigation` API |
| `handyplay.android.feature.impl` | Library + Compose + Hilt + core module deps |
| `handyplay.android.hilt` | KSP + Dagger Hilt |
| `handyplay.android.test` | Common test dependencies (JUnit, MockK, Turbine, Coroutines Test) |
| `handyplay.jvm.library` | Pure JVM Kotlin (Java 11) |
| `handyplay.detekt` | Detekt static analysis + Compose rules |
| `handyplay.kover` | Kover code coverage with exclusions for generated code, Hilt, and Compose (`@Composable`) |
| `handyplay.android.lint` | Android Lint with `warningsAsErrors`, HTML/XML reports, `config/lint/lint.xml` |

## Dependency Graph

```
:app
├── :core:common, :core:designsystem, :core:ui, :core:domain, :core:data, :core:model, :core:network
├── :core:navigation
└── :feature:*:impl

:feature:*:impl (via handyplay.android.feature.impl plugin)
├── :feature:*:api (own API module)
├── :core:ui, :core:designsystem, :core:domain, :core:model
└── :core:navigation

:feature:*:api (via handyplay.android.feature.api plugin)
└── :core:navigation

:core:data → :core:domain, :core:model, :core:common, :core:network
:core:domain → :core:model
:core:ui → :core:designsystem, :core:model
:core:network → :core:common
```

## Architecture Patterns

- **Single Activity:** `MainActivity` with Compose `NavHost`
- **DI:** Hilt (`@AndroidEntryPoint`, `@HiltViewModel`, `@Module`)
- **State:** ViewModel + `StateFlow` + sealed `UiState` interfaces
- **Navigation:** Type-safe destinations via `@Serializable` objects/data classes
- **Feature modules:** Split into `:api` (navigation contracts) and `:impl` (screens/logic)
- **Theming:** Material3 dynamic colors (API 31+) with static fallbacks
- **Edge-to-edge** display enabled
