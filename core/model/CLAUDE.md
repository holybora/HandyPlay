# :core:model

Pure Kotlin data models shared across the app. **JVM-only** — no Android dependencies.

## Module Info

- **Type:** JVM Library (no Android)
- **Plugins:** `handyplay.jvm.library`, `kotlin.serialization`

## Dependencies

- `kotlinx-serialization-json`
- No internal module dependencies

## Key Files

- `Category.kt` — `@Serializable` data class with `id: String`, `name: String`
- `Topic.kt` — `@Serializable` data class with `id: String`, `name: String`, `categoryId: String`, plus companion with `ID_TTL_CACHE` constant
- `Joke.kt` — `@Serializable` data class with `setup: String`, `punchline: String`

## Source

- `src/main/kotlin/com/sls/handbook/core/model/`

## Tests

- `src/test/` — JVM unit tests
  - `TopicTest.kt` — Data class properties, equality, copy, hashCode, companion constant tests
  - `JokeTest.kt` — Data class properties, equality, copy operations tests

## Notes

- All models use `@Serializable` for kotlinx.serialization
- Keep models as plain data classes — no business logic
- This module is depended on by `:core:domain`, `:core:data`, `:core:ui`, and all feature modules
