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
./gradlew assembleRelease

# Install and Run
./gradlew installAndRun  # Installs debug APK and launches app on connected device

# Unit tests (JVM) — all modules
./gradlew test

# Single module tests
./gradlew :app:testDebugUnitTest
./gradlew :feature:home:testDebugUnitTest

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
./gradlew detekt                    # All modules, all source sets
./gradlew detektMain                # Main sources only
./gradlew :feature:home:detektMain  # Single module
./gradlew installGitHooks           # Install pre-commit hook manually
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
- **Dependency versions:** `gradle/libs.versions.toml`

## Module Structure

| Module | Type | Purpose |
|--------|------|---------|
| `:app` | Application | Entry point, NavHost, Hilt setup |
| `:core:common` | Android Library | Shared utilities |
| `:core:model` | JVM Library | Data models (`@Serializable`) |
| `:core:domain` | JVM Library | Use cases / business logic |
| `:core:data` | Android Library | Repositories, data sources |
| `:core:network` | Android Library | API client, network DI |
| `:core:designsystem` | Android Library | Material3 theme (Color, Type, Theme) |
| `:core:ui` | Android Library | Shared composables |
| `:navigation` | Android Library | Type-safe route definitions |
| `:feature:welcome` | Feature | Welcome/onboarding screen |
| `:feature:home` | Feature | Home screen with category grid + search |
| `:feature:category` | Feature | Category topics screen with topic grid + search |
| `:feature:ttlcache` | Feature | TTL Cache demo screen with joke fetching |
| `:build-logic` | Included Build | Convention plugins |

## Convention Plugins (`build-logic/`)

| Plugin ID | What it provides |
|-----------|-----------------|
| `handyplay.android.application` | Android app config (SDK, Kotlin, Java 11) |
| `handyplay.android.library` | Android library config (SDK, Kotlin, Java 11) |
| `handyplay.android.library.compose` | Compose compiler + BOM |
| `handyplay.android.feature` | Library + Compose + Hilt + core module deps |
| `handyplay.android.hilt` | KSP + Dagger Hilt |
| `handyplay.android.test` | Common test dependencies (JUnit, MockK, Turbine, Coroutines Test) |
| `handyplay.jvm.library` | Pure JVM Kotlin (Java 11) |
| `handyplay.detekt` | Detekt static analysis + Compose rules |
| `handyplay.kover` | Kover code coverage with exclusions for generated code, Hilt, and Compose (`@Composable`) |

## Dependency Graph

```
:app
├── :core:common, :core:designsystem, :core:ui, :core:domain, :core:data, :core:model, :core:network
├── :navigation
└── :feature:welcome, :feature:home, :feature:category, :feature:ttlcache

:feature:* (via handyplay.android.feature plugin)
├── :core:ui, :core:designsystem, :core:domain, :core:model
└── :navigation

:core:data → :core:domain (api), :core:model (api), :core:common
:core:domain → :core:model
:core:ui → :core:designsystem (api), :core:model (api)
:core:network → :core:common
```

## Architecture Patterns

- **Single Activity:** `MainActivity` with Compose `NavHost`
- **DI:** Hilt (`@AndroidEntryPoint`, `@HiltViewModel`, `@Module`)
- **State:** ViewModel + `StateFlow` + sealed `UiState` interfaces
- **Navigation:** Type-safe destinations via `@Serializable` objects/data classes
- **Theming:** Material3 dynamic colors (API 31+) with static fallbacks
- **Edge-to-edge** display enabled
