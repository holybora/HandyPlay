# :feature:gallery

Photo gallery screen with infinite scroll pagination and full-screen interactive photo viewer.

## Module Info

- **Namespace:** `com.sls.handbook.feature.gallery`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature`

## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`

## Dependencies (explicit)

- `:core:data` (implementation) — repository abstraction
- `androidx.activity.compose` (implementation) — `BackHandler` for full-screen viewer
- `coil.compose` (implementation) — async image loading
- `coil.network.okhttp` (implementation) — OkHttp support for Coil

## Key Files

- `GalleryRoute.kt` — Navigation wrapper that injects `GalleryViewModel` and collects state
- `GalleryScreen.kt` — Main composable with grid + full-screen overlay logic
  - `GalleryContent()` — Manages selected image state and bounds tracking
  - `GalleryGrid()` — `LazyVerticalGrid` (2 columns) with infinite scroll detection
  - `GalleryImageItem()` — Clickable image card with `onGloballyPositioned` tracking
- `GalleryViewModel.kt` — `@HiltViewModel` with `StateFlow<GalleryUiState>`, pagination (page size 30)
- `GalleryUiState.kt` — Sealed interface: `Loading`, `Content(images, isLoadingMore, currentPage)`, `Error`
- `FullScreenImageViewer.kt` — Full-screen overlay composable with enter/exit animations
  - `FullScreenImageContent()` — Image + author bar + close button rendering
  - `AuthorBar()` — Semi-transparent bottom bar showing photographer name
  - `CloseButton()` — Close icon in top-right corner
  - Modifier extension `dragToDismiss()` — Handles drag gesture + spring cancellation

## Source

- `src/main/kotlin/com/sls/handbook/feature/gallery/`

## Patterns

- **ViewModel + StateFlow**: ViewModel exposes `uiState: StateFlow<GalleryUiState>` collected via `collectAsStateWithLifecycle()` in `GalleryRoute`
- **Infinite scroll**: Detects when last visible grid item is within 6 items of the end, triggers `loadMore()`
- **Expand-from-thumbnail animation**: Tracks thumbnail position/size via `onGloballyPositioned`, passes `Rect` bounds to viewer, animates from thumbnail center + scale to full screen center + 1.0 scale
- **Drag-to-dismiss gesture**: Uses `pointerInput` + `detectDragGestures`, `Animatable` for smooth snap-back or dismissal, `graphicsLayer` for efficient rendering
- **Back press handling**: `BackHandler` in full-screen viewer dismisses and returns to thumbnail position
