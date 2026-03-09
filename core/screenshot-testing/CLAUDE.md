# :core:screenshot-testing

Screenshot testing infrastructure with Roborazzi for JVM-based visual regression testing.

## Module Info

- **Namespace:** `com.sls.handbook.core.screenshottesting`
- **Type:** Android Library with Compose
- **Plugins:** `handyplay.android.library`, `handyplay.android.library.compose`

## Dependencies

- `api`: roborazzi-core, roborazzi-compose, roborazzi-junit-rule, robolectric, junit, compose-ui-test-junit4, compose-bom
- `implementation`: `:core:designsystem`

## Key Files

- `ScreenshotHelper.kt` — `defaultRoborazziOptions()` factory (0.1% threshold, 0.5x resize) and `ComposeContentTestRule.captureScreenshot()` extension for consistent screenshot capture to `src/test/screenshots/`. Options are created per-call (not singleton) so system properties for record/verify/compare mode are read at test time.
- `ScreenshotTestActivity.kt` — Empty `ComponentActivity` subclass used as Compose test host for Robolectric
- `AndroidManifest.xml` — Declares `ScreenshotTestActivity` for manifest merging into test modules

## Source

- `src/main/kotlin/com/sls/handbook/core/screenshottesting/`

## Notes

- All Roborazzi/Robolectric/Compose test deps are exposed as `api` so consuming test modules get them transitively
- Feature modules add this as `testImplementation` via the `handyplay.android.roborazzi` convention plugin
- Record screenshots: `./gradlew testDebugUnitTest -Proborazzi.test.record=true`
- Verify screenshots (CI): `./gradlew testDebugUnitTest -Proborazzi.test.verify=true`
- Compare (diff without failing): `./gradlew testDebugUnitTest -Proborazzi.test.compare=true`
- Single module: `./gradlew :feature:home:impl:cleanTestDebugUnitTest :feature:home:impl:testDebugUnitTest -Proborazzi.test.verify=true`
- **Important:** Use `cleanTestDebugUnitTest` before verify/record to bypass Gradle test caching (PNG changes don't invalidate the cache)
