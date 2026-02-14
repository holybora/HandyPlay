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

- `di/NetworkModule.kt` — Hilt `@Module` providing network dependencies

## Source

- `src/main/java/com/sls/handbook/core/network/`

## Notes

- All API service interfaces and network configuration live here
- Uses kotlinx.serialization for JSON parsing
- Hilt provides singleton network instances (HTTP client, API services)
