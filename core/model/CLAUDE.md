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
- `Topic.kt` — `@Serializable` data class with `id: String`, `name: String`, `categoryId: String`
- `Joke.kt` — Data class with `setup: String`, `punchline: String`

## Source

- `src/main/kotlin/com/sls/handbook/core/model/`

## Notes

- All models use `@Serializable` for kotlinx.serialization
- Keep models as plain data classes — no business logic
- This module is depended on by `:core:domain`, `:core:data`, `:core:ui`, and all feature modules
