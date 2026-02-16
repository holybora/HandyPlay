# :core:domain

Business logic and use cases layer. **JVM-only** — no Android dependencies.

## Module Info

- **Type:** JVM Library (no Android)
- **Plugin:** `handyplay.jvm.library`

## Dependencies

- `:core:model`

## Key Files

- `repository/JokeRepository.kt` — Repository interface `getJoke(ttlMillis)` + `JokeResult` data class
- `repository/CategoryRepository.kt` — Repository interface `getCategories()` + `getTopicsByCategoryId()`

## Source

- `src/main/kotlin/com/sls/handbook/core/domain/`

## Notes

- Place use case classes and repository interfaces here
- Use cases should be single-responsibility (`operator fun invoke()` pattern)
- Repository interfaces defined here, implementations in `:core:data`
- No Hilt, Compose, or Android framework references allowed
