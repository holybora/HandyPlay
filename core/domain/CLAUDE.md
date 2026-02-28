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
- `repository/WeatherRepository.kt` — Repository interface with `getWeather(lat, lon, lang)` and
  `getForecastData(lat, lon, lang)` for weather data access
- `repository/GalleryRepository.kt` — Repository interface for gallery image listing
- `exception/WeatherException.kt` — Custom exception for weather API errors

### Use Cases (`usecase/`)

- `GetTopicsByCategoryIdUseCase.kt` — Fetches topics filtered by category ID
- `GetCurrentWeatherUseCase.kt` — Fetches current weather for coordinates
- `GetFiveDayForecastUseCase.kt` — Fetches 5-day daily forecast
- `GetForecastDataUseCase.kt` — Fetches combined forecast data
- `GetTodayHourlyForecastUseCase.kt` — Fetches today's hourly forecast
- `GenerateRandomCoordinatesUseCase.kt` — Generates random lat/lon coordinates

## Source

- `src/main/kotlin/com/sls/handbook/core/domain/`

## Tests

- `src/test/` — JVM unit tests
  - `JokeResultTest.kt` — Data class properties, equality, copy operations tests
  - `GetTopicsByCategoryIdUseCaseTest.kt` — Use case logic tests

## Notes

- Place use case classes and repository interfaces here
- Use cases should be single-responsibility (`operator fun invoke()` pattern)
- Repository interfaces defined here, implementations in `:core:data`
- No Hilt, Compose, or Android framework references allowed
