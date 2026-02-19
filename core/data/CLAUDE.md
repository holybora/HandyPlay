# :core:data

Repository implementations and data sources.

## Module Info

- **Namespace:** `com.sls.handbook.core.data`
- **Type:** Android Library
- **Plugins:** `handyplay.android.library`, `handyplay.android.hilt`

## Dependencies

- `:core:domain` (api) — repository interfaces
- `:core:model` (api) — data models
- `:core:common` — utilities
- `:core:network` — API clients

## Key Files

- `repository/JokeRepositoryImpl.kt` — `@Singleton` implementation using `DynamicTtlCache` and `JokeApi`
- `repository/CategoryRepositoryImpl.kt` — `@Singleton` implementation with static category and topic data
- `repository/WeatherRepositoryImpl.kt` — `@Singleton` implementation providing current weather, 5-day daily forecast, and hourly forecast (filtered to current day only) via OpenWeatherMap API
- `di/DataModule.kt` — Hilt `@Binds` mapping repository implementations to interfaces

## Source

- `src/main/java/com/sls/handbook/core/data/`

## Tests

- `src/test/` — JVM unit tests
  - `JokeRepositoryImplTest.kt` — Repository implementation, MockK-based API mocking, cache behavior, error propagation tests

## Notes

- Implements repository interfaces from `:core:domain`
- Uses Hilt `@Module` + `@Binds` to provide repository implementations
- Coordinates between local and remote data sources
- Exposes `:core:domain` and `:core:model` transitively via `api`
