# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HandyPlay is an Android application built with Jetpack Compose and Material 3. Package: `com.sls.handbook`.
Single-module project (`:app`).

## Build & Test Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Unit tests (JVM)
./gradlew test

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
- **Dependency versions:** defined in `gradle/libs.versions.toml`

## Architecture

Currently a bootstrap project with a single Activity entry point. No navigation, DI, or layered architecture is set up
yet.

- `MainActivity` → `HandyPlayTheme` → `Scaffold` → composable content
- Theme supports dynamic colors (API 31+) with static color fallbacks
- Edge-to-edge display enabled

## Key Paths

- App build config: `app/build.gradle.kts`
- Version catalog: `gradle/libs.versions.toml`
- Main source: `app/src/main/java/com/sls/handbook/`
- Theme: `app/src/main/java/com/sls/handbook/ui/theme/`
- Unit tests: `app/src/test/java/com/sls/handbook/`
- Instrumented tests: `app/src/androidTest/java/com/sls/handbook/`