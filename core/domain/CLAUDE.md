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
- `repository/WeatherRepository.kt` — Repository interface with `getWeatherWithForecast(lat, lon)` and `getHourlyForecast(lat, lon)` for weather data access

## Source

- `src/main/kotlin/com/sls/handbook/core/domain/`

## Tests

- `src/test/` — JVM unit tests
  - `JokeResultTest.kt` — Data class properties, equality, copy operations tests

## Notes

- Place use case classes and repository interfaces here
- Use cases should be single-responsibility (`operator fun invoke()` pattern)
- Repository interfaces defined here, implementations in `:core:data`
- No Hilt, Compose, or Android framework references allowed
