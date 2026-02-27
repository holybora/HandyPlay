# :core:network

Network/API layer with HTTP client setup, API key management, and DI.

## Module Info

- **Namespace:** `com.sls.handbook.core.network`
- **Type:** Android Library
- **Plugins:** `handyplay.android.library`, `handyplay.android.hilt`

## Dependencies

- `:core:common`

## Key Files

- `api/JokeApi.kt` — Retrofit interface: `@GET("random_joke") suspend fun getRandomJoke(): JokeResponse`
- `api/WeatherApi.kt` — Retrofit interface: current weather and forecast endpoints for OpenWeatherMap
- `api/PicsumApi.kt` — Retrofit interface: `@GET("v2/list") suspend fun getImages(page, limit): List<PicsumImageResponse>`
- `model/JokeResponse.kt` — Gson-annotated response model with `type`, `setup`, `punchline`, `id`
- `model/WeatherResponse.kt` — Response models for current weather, forecast, and city data
- `model/PicsumImageResponse.kt` — Response model for Picsum image listing
- `interceptor/ApiKeyInterceptor.kt` — OkHttp interceptor that appends `appid` to weather API requests
- `ApiKeyProvider.kt` — Interface abstraction for API key supply
- `di/NetworkModule.kt` — Hilt `@Module` providing OkHttp clients (base + weather-specific), Retrofit instances, API services, and `ApiKeyProvider`

## Source

- `src/main/kotlin/com/sls/handbook/core/network/`

## Tests

- `src/test/` — JVM unit tests
  - `JokeResponseTest.kt` — Data class properties, equality, copy operations tests

## Notes

- All API service interfaces and network configuration live here
- API key (`OPENWEATHER_API_KEY`) is defined as `buildConfigField` in this module
- `ApiKeyInterceptor` is scoped to weather API requests only (separate OkHttpClient)
- Uses Gson for JSON parsing
- Hilt provides singleton network instances (HTTP client, API services)
