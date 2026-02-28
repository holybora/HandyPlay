# :core:designsystem

Material3 design system — theme tokens, colors, and typography.

## Module Info

- **Namespace:** `com.sls.handbook.core.designsystem`
- **Type:** Android Library with Compose
- **Plugins:** `handyplay.android.library`, `handyplay.android.library.compose`

## Dependencies

- `androidx.compose.ui:ui`
- `androidx.compose.ui:ui-graphics`
- `androidx.compose.material3:material3`

## Key Files

- `theme/Color.kt` — Color palette definitions
- `theme/Theme.kt` — `HandyPlayTheme` composable (dynamic colors API 31+, static fallback)
- `theme/Type.kt` — Typography scale

## Source

- `src/main/kotlin/com/sls/handbook/core/designsystem/`

## Notes

- This is the single source of truth for theming
- All Compose modules get Material3 transitively through this module
- Add new design tokens (shapes, spacing) here
- Do NOT add business components — those go in `:core:ui`
