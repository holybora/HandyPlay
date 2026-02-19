# :core:network

Network/API layer with HTTP client setup and DI.

## Module Info

- **Namespace:** `com.sls.handbook.core.network`
- **Type:** Android Library
- **Plugins:** `handyplay.android.library`, `handyplay.android.hilt`, `kotlin.serialization`

## Dependencies

- `:core:common`
- `kotlinx-serialization-json`

## Key Files

- `api/JokeApi.kt` — Retrofit interface: `@GET("random_joke") suspend fun getRandomJoke(): JokeResponse`
- `api/WeatherApi.kt` — Retrofit interface: `@GET("data/2.5/weather") suspend fun getWeather(lat, lon, appId, units): WeatherResponse`
- `api/HourlyForecastApi.kt` — Retrofit interface: `@GET("data/2.5/forecast") suspend fun getHourlyForecast(lat, lon, appId, units): HourlyForecastResponse`
- `api/DailyForecastApi.kt` — Retrofit interface: `@GET("data/2.5/forecast") suspend fun getDailyForecast(lat, lon, appId, units): DailyForecastResponse`
- `model/JokeResponse.kt` — Gson-annotated response model with `type`, `setup`, `punchline`, `id`
- `model/WeatherResponse.kt` — Serializable response model for current weather from OpenWeatherMap
- `model/HourlyForecastResponse.kt` — Serializable response model with `list` (hourly entries), `city` (timezone info)
- `model/DailyForecastResponse.kt` — Serializable response model for 5-day forecast
- `di/NetworkModule.kt` — Hilt `@Module` providing OkHttp, Retrofit instances for joke API (`official-joke-api.appspot.com`) and OpenWeatherMap (`api.openweathermap.org`), and API service providers

## Source

- `src/main/kotlin/com/sls/handbook/core/network/`

## Tests

- `src/test/` — JVM unit tests
  - `JokeResponseTest.kt` — Data class properties, equality, copy operations tests

## Notes

- All API service interfaces and network configuration live here
- Uses Gson for JSON parsing
- Hilt provides singleton network instances (HTTP client, API services)
