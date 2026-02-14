# :core:ui

Shared composable components reused across feature modules.

## Module Info

- **Namespace:** `com.sls.handbook.core.ui`
- **Type:** Android Library with Compose
- **Plugins:** `handyplay.android.library`, `handyplay.android.library.compose`

## Dependencies (exposed as `api`)

- `:core:designsystem` — theme tokens
- `:core:model` — data models for composable parameters

## Key Files

- `BreadcrumbBar.kt` — Breadcrumb navigation bar with path segments joined by arrows
- `SearchBar.kt` — Search input composable with Material3 styling

## Source

- `src/main/kotlin/com/sls/handbook/core/ui/`

## Notes

- Place reusable composables here (cards, lists, loading states, error views)
- Components should accept model classes from `:core:model` as parameters
- Feature-specific components belong in their feature module, not here
- Gets Material3 transitively from `:core:designsystem`
