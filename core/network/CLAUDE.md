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
- `model/JokeResponse.kt` — Gson-annotated response model with `type`, `setup`, `punchline`, `id`
- `di/NetworkModule.kt` — Hilt `@Module` providing OkHttp, Retrofit (base URL: `official-joke-api.appspot.com`), and `JokeApi`

## Source

- `src/main/kotlin/com/sls/handbook/core/network/`

## Notes

- All API service interfaces and network configuration live here
- Uses kotlinx.serialization for JSON parsing
- Hilt provides singleton network instances (HTTP client, API services)
