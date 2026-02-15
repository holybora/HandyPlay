# :core:common

Shared utilities and helpers used across all modules.

## Module Info

- **Namespace:** `com.sls.handbook.core.common`
- **Type:** Android Library
- **Plugin:** `handyplay.android.library`

## Dependencies

- `androidx.core:core-ktx`
- `kotlinx-coroutines-core`
- No internal module dependencies

## Source

- `src/main/java/com/sls/handbook/core/common/`

## Key Files

### Cache Utilities (`cache/`)

- **`DynamicTtlCache.kt`**: Generic cache with dynamic TTL support. Pass TTL on each `get(ttlMillis)` call. Returns `CacheResult<T>` with metadata (data, fetchTimeMillis, fromCache). Supports `invalidate()`.

- **`CachedNetworkProperty.kt`**: Suspend-based cache with fixed TTL. Use `suspend fun get()` to retrieve cached or fresh data. Thread-safe with Mutex-based double-check locking.

- **`CachedNetworkPropertyProvider.kt`**: Factory for creating `CachedNetworkProperty` instances. Use `create()` method or top-level `cachedNetwork(ttlMillis, fetcher)` function.

## Tests

- `src/test/java/com/sls/handbook/core/common/cache/`
  - `DynamicTtlCacheTest.kt`
  - `CachedNetworkPropertyTest.kt`

Run tests: `./gradlew :core:common:testDebugUnitTest`

## Notes

- No Android framework dependencies beyond core-ktx
- Place extension functions, constants, and utility classes here
- Keep this module free of Compose, Hilt, and network dependencies
