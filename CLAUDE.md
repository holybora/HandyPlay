# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HandyPlay is an Android application built with Jetpack Compose and Material 3.
Package: `com.sls.handbook`. Multi-module clean architecture project.

## Build & Test Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

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
```

## SDK & Tooling

- **Compile/Target SDK:** 36 | **Min SDK:** 33
- **Kotlin:** 2.0.21 with Compose compiler plugin
- **AGP:** 9.0.0
- **Java compatibility:** 11
- **Compose BOM:** 2024.09.00
- **Hilt:** 2.59
- **Navigation Compose:** 2.9.0 (type-safe with kotlinx.serialization)
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
| `:build-logic` | Included Build | Convention plugins |

## Convention Plugins (`build-logic/`)

| Plugin ID | What it provides |
|-----------|-----------------|
| `handyplay.android.application` | Android app config (SDK, Kotlin, Java 11) |
| `handyplay.android.library` | Android library config (SDK, Kotlin, Java 11) |
| `handyplay.android.library.compose` | Compose compiler + BOM |
| `handyplay.android.feature` | Library + Compose + Hilt + core module deps |
| `handyplay.android.hilt` | KSP + Dagger Hilt |
| `handyplay.jvm.library` | Pure JVM Kotlin (Java 11) |

## Dependency Graph

```
:app
├── :core:common, :core:designsystem, :core:ui, :core:domain, :core:data, :core:model, :core:network
├── :navigation
└── :feature:welcome, :feature:home, :feature:category

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
