# :core:model

Pure Kotlin data models shared across the app. **JVM-only** — no Android dependencies.

## Module Info

- **Type:** JVM Library (no Android)
- **Plugins:** `handyplay.jvm.library`

## Dependencies

- No internal module dependencies

## Key Files

- `Category.kt` — data class with `id: String`, `name: String`
- `Topic.kt` — data class with `id: String`, `name: String`, `categoryId: String`, plus companion with `ID_TTL_CACHE` constant
- `Joke.kt` — data class with `setup: String`, `punchline: String`
- `Weather.kt` — data class for current weather with lat, lon, temperature, icon, description, wind, humidity, pressure, visibility, feelsLike
- `DailyForecast.kt` — data class with date, high, low, icon, description
- `HourlyForecast.kt` — data class with dt (Unix timestamp), temperature, icon, description, pop (precipitation probability)
- `Coordinates.kt` — data class with lat, lon
- `ForecastData.kt` — data class combining daily and hourly forecasts
- `ForecastItem.kt` — data class for individual forecast items
- `GalleryImage.kt` — data class for gallery images (id, author, dimensions, URLs)
- `PatternContent.kt` — data class for design pattern content

## Source

- `src/main/kotlin/com/sls/handbook/core/model/`

## Tests

- `src/test/` — JVM unit tests
  - `TopicTest.kt` — Data class properties, equality, copy, hashCode, companion constant tests
  - `JokeTest.kt` — Data class properties, equality, copy operations tests

## Notes

- Keep models as plain data classes — no business logic
- This module is depended on by `:core:domain`, `:core:data`, `:core:ui`, and all feature modules
