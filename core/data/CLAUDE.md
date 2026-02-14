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

## Source

- `src/main/java/com/sls/handbook/core/data/`

## Notes

- Implements repository interfaces from `:core:domain`
- Uses Hilt `@Module` + `@Binds` to provide repository implementations
- Coordinates between local and remote data sources
- Exposes `:core:domain` and `:core:model` transitively via `api`
