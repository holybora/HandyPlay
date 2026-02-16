# :navigation

Type-safe navigation route definitions.

## Module Info

- **Namespace:** `com.sls.handbook.navigation`
- **Type:** Android Library
- **Plugins:** `handyplay.android.library`, `kotlin.serialization`

## Dependencies (exposed as `api`)

- `androidx.navigation:navigation-compose`
- `kotlinx-serialization-json`

## Key Files

- `AppDestinations.kt` — `@Serializable` destination objects: `WelcomeDestination`, `HomeDestination`

## Source

- `src/main/java/com/sls/handbook/navigation/`

## Tests

- `src/test/` — JVM unit tests
  - `AppDestinationsTest.kt` — Navigation destination construction, properties, equality, copy operations

## Notes

- All navigation destinations are `@Serializable` objects/data classes
- Add new screen destinations here as the app grows
- Feature modules depend on this module to reference destinations
- The `:app` module's `NavHost` wires destinations to feature composables
