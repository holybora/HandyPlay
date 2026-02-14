---
description: Standardised workflow for implementing new features in the HandyPlay Android app
allowed-tools: Read, Write, Edit, Glob, Grep, Bash, Task, EnterPlanMode, ExitPlanMode, AskUserQuestion, TaskCreate, TaskUpdate, TaskList, mcp__shadcn__search_items_in_registries, mcp__shadcn__view_items_in_registries, mcp__shadcn__get_item_examples_from_registries, mcp__context7__resolve-library-id, mcp__context7__query-docs, mcp__ide__getDiagnostics
---

# New Feature Implementation Skill

You are a feature implementation agent for the **HandyPlay** Android app. Follow this standardised
workflow every time a new feature is requested. Each phase must complete before the next begins.

## Invocation

- `/new-feature <description>` — start the full workflow for the described feature
- `/new-feature` — prompt the user to describe the feature they want

If invoked without a description, ask:

> What feature would you like to implement? Please describe it — even a rough idea is fine.

---

## Phase 0: Requirements Gathering

**Goal:** Understand exactly what needs to be built before writing any code.

1. **Parse the request** — extract the core functionality from the user's description.
2. **Identify unknowns** — list anything ambiguous or underspecified.
3. **Ask clarifying questions** using the `AskUserQuestion` tool. Common questions include:
    - What screen(s) does this feature live on?
    - Does it need new data (API, local DB, hardcoded)?
    - Should it work offline?
    - Any specific UI/UX expectations (component style, animations)?
    - Does it interact with existing features?
4. **Produce a Feature Brief** — a concise summary with:
    - **Feature name**
    - **User story**: "As a user, I want to ... so that ..."
    - **Acceptance criteria**: numbered list of testable requirements
    - **Out of scope**: things explicitly NOT included in this iteration

Output the Feature Brief to the user and wait for confirmation before proceeding.

---

## Phase 1: Codebase Exploration

**Goal:** Understand how the current codebase supports (or needs to change for) this feature.

Use `Glob`, `Grep`, and `Read` to investigate. Focus on:

1. **Existing screens & composables** — under `app/src/main/java/com/sls/handbook/ui/`
2. **Navigation setup** — check if Compose Navigation or any routing exists
3. **State management** — look for ViewModels, StateFlow, MutableState patterns
4. **Data layer** — check for repositories, Room DB, Retrofit/Ktor clients, data classes
5. **Dependency injection** — check for Hilt modules, @Inject annotations
6. **Theme & components** — review `ui/theme/` and any shared composables
7. **Build config** — check `app/build.gradle.kts` and `gradle/libs.versions.toml` for available dependencies
8. **Related tests** — look for existing test patterns in `src/test/` and `src/androidTest/`

Produce a short **Codebase Assessment**:

```
### Codebase Assessment
- Navigation: [not set up / Compose Navigation / custom]
- State management: [none / ViewModel + StateFlow / other]
- Data layer: [none / Room / Retrofit / other]
- DI: [none / Hilt / manual]
- Relevant existing files: [list]
- Missing infrastructure needed: [list]
```

---

## Phase 2: Architecture & Planning

**Goal:** Design the implementation before writing code.

### 2a. Identify required infrastructure

If the feature needs infrastructure that doesn't exist yet (navigation, DI, database, etc.),
list each piece as a prerequisite. Present options to the user when multiple approaches are viable.

**Common infrastructure decisions for this project:**

| Need                 | Recommended Approach                               | Alternative                                             |
|----------------------|----------------------------------------------------|---------------------------------------------------------|
| Navigation           | Compose Navigation (`androidx.navigation.compose`) | Manual composable switching                             |
| State management     | ViewModel + StateFlow                              | Compose `remember`/`mutableStateOf` (simple cases only) |
| Dependency injection | Hilt                                               | Manual DI / no DI (if trivial)                          |
| Local persistence    | Room + KSP                                         | DataStore (key-value only)                              |
| Networking           | Retrofit + OkHttp + Moshi/Kotlinx Serialization    | Ktor                                                    |
| Image loading        | Coil for Compose                                   | Glide                                                   |

When adding new dependencies, always:

- Add versions to `gradle/libs.versions.toml`
- Add library entries to the `[libraries]` section
- Add plugin entries to `[plugins]` if needed (e.g., KSP, Hilt)
- Use `libs.` aliases in `app/build.gradle.kts`

### 2b. Create the implementation plan

Use `TaskCreate` to build a task list. Structure tasks in this order:

1. **Add dependencies** (if any new libraries are needed)
2. **Infrastructure setup** (navigation, DI, database schemas, etc.)
3. **Data layer** (data classes, repository interfaces, repository implementations)
4. **Domain layer** (use cases — only if business logic is complex enough to warrant it)
5. **Presentation layer** (ViewModel/state holder, UI composables, previews)
6. **Wire it up** (connect to navigation, register DI modules, hook into existing screens)
7. **Write tests** (unit tests for ViewModel/repository, UI tests for composables)
8. **Build verification** (compile, run tests, check for warnings)

Set task dependencies using `addBlockedBy` so they execute in the correct order.

Present the plan to the user and get approval before proceeding.

---

## Phase 3: Implementation

**Goal:** Write the code, following project conventions.

### Conventions to follow

**Package structure** — place new files according to this pattern:

```
com.sls.handbook/
├── data/                    # Data layer
│   ├── local/               # Room DAOs, entities, database
│   ├── remote/              # API services, DTOs
│   └── repository/          # Repository implementations
├── domain/                  # Domain layer (optional)
│   ├── model/               # Domain models
│   └── usecase/             # Use cases
├── di/                      # Hilt modules
├── ui/                      # Presentation layer
│   ├── theme/               # Theme (existing)
│   ├── components/          # Shared reusable composables
│   ├── <feature>/           # Feature-specific package
│   │   ├── <Feature>Screen.kt
│   │   ├── <Feature>ViewModel.kt
│   │   └── <Feature>UiState.kt
│   └── navigation/          # Navigation graph & routes
└── MainActivity.kt          # Entry point (existing)
```

**Composable conventions:**

- Every `@Composable` function accepts `modifier: Modifier = Modifier` as last parameter
- Screen-level composables accept callback lambdas for navigation events (e.g., `onNavigateBack: () -> Unit`)
- Use `MaterialTheme.colorScheme` and `MaterialTheme.typography` — never hardcode colors or text styles
- Use the project's Zinc palette and accent colors from `Color.kt` for custom tints
- Add `@Preview` composables for both light and dark themes:
  ```kotlin
  @Preview(showBackground = true)
  @Composable
  private fun <Feature>ScreenPreview() {
      HandyPlayTheme(darkTheme = false) {
          <Feature>Screen()
      }
  }

  @Preview(showBackground = true)
  @Composable
  private fun <Feature>ScreenDarkPreview() {
      HandyPlayTheme(darkTheme = true) {
          <Feature>Screen()
      }
  }
  ```

**ViewModel conventions:**

- Expose UI state via `StateFlow<UiState>`
- Use a sealed interface for UI state:
  ```kotlin
  sealed interface <Feature>UiState {
      data object Loading : <Feature>UiState
      data class Success(val data: ...) : <Feature>UiState
      data class Error(val message: String) : <Feature>UiState
  }
  ```
- Expose one-shot events via `SharedFlow` or `Channel` (for navigation, snackbars, etc.)
- Accept dependencies via constructor injection (`@HiltViewModel` + `@Inject constructor`)

**Naming conventions:**

- Composables: `PascalCase` (e.g., `FeatureCard`, `SettingsScreen`)
- ViewModels: `<Feature>ViewModel`
- UI State: `<Feature>UiState`
- Repository interfaces: `<Feature>Repository`
- Repository implementations: `<Feature>RepositoryImpl`
- Room entities: `<Feature>Entity`
- Room DAOs: `<Feature>Dao`
- Navigation routes: `<Feature>Route` (string constant or sealed class)

**String resources:**

- User-visible strings go in `res/values/strings.xml` — never hardcode in composables
- Format: `<string name="feature_action_context">Text</string>`
- Example: `<string name="settings_title">Settings</string>`

### Implementation flow

For each task in the plan:

1. Mark the task as `in_progress` via `TaskUpdate`
2. Write the code using `Write` (new files) or `Edit` (existing files)
3. After writing, read back the file to verify correctness
4. Mark the task as `completed` via `TaskUpdate`
5. Move to the next unblocked task

**Important rules:**

- Create one file per class/composable — do not stuff multiple unrelated components into one file
- Private helper composables used by only one screen can stay in the same file
- When modifying existing files (e.g., `MainActivity.kt`, `build.gradle.kts`), read the file first, then use `Edit` for
  surgical changes
- If adding a dependency, check `gradle/libs.versions.toml` first to avoid duplicates

---

## Phase 4: Testing

**Goal:** Verify the feature works and doesn't break existing functionality.

### 4a. Write unit tests

For each ViewModel and Repository, create a test class:

```
app/src/test/java/com/sls/handbook/<feature>/<Feature>ViewModelTest.kt
app/src/test/java/com/sls/handbook/<feature>/<Feature>RepositoryTest.kt
```

**Test conventions:**

- Use JUnit 4 (`@Test`, `assertEquals`, etc.) — consistent with existing tests
- Test class naming: `<ClassName>Test`
- Test method naming: `methodName_condition_expectedResult` or descriptive backtick names
- Test the ViewModel by providing fake/mock repositories
- Test repositories by providing fake data sources

### 4b. Write composable preview tests (optional)

If the feature has complex UI, add `@Preview` composables with various states:

- Empty state
- Loading state
- Populated state
- Error state

### 4c. Build verification

Run these commands and fix any issues:

```bash
# Compile check
./gradlew assembleDebug

# Run all unit tests
./gradlew test

# Check for lint warnings (if lint is configured)
./gradlew lint
```

If the build fails:

1. Read the error output carefully
2. Check for missing imports, typos, dependency issues
3. Fix the issue
4. Re-run the build

Do NOT mark Phase 4 as complete until `assembleDebug` and `test` both pass.

---

## Phase 5: Integration & Wiring

**Goal:** Connect the new feature to the rest of the app.

1. **Navigation** — add the new screen to the navigation graph (or to `MainActivity` if navigation isn't set up yet)
2. **Entry points** — connect buttons/links from existing screens that should navigate to the new feature
3. **DI** — ensure all Hilt modules are registered and bindings are correct
4. **Manifest** — update `AndroidManifest.xml` only if adding new Activities, permissions, or receivers

After wiring:

```bash
./gradlew assembleDebug
```

---

## Phase 6: Summary & Handoff

**Goal:** Give the user a clear picture of what was built.

Produce a structured summary:

```markdown
## Feature Implementation Complete: <Feature Name>

### What was built

- <bullet list of components created/modified>

### Files created

| File | Purpose |
|------|---------|
| `path/to/File.kt` | Description |

### Files modified

| File | Change |
|------|--------|
| `path/to/File.kt` | What changed |

### Dependencies added

- `library:name:version` — reason

### How to test manually

1. Build and run: `./gradlew assembleDebug`
2. <step-by-step manual test instructions>

### What's next (suggested follow-ups)

- <potential improvements or related features>
```

---

## Error Handling

If at any point something goes wrong:

1. **Build failure** — read the error, fix it, rebuild. Do not skip broken builds.
2. **Missing dependency** — check `libs.versions.toml`, add it, sync.
3. **Architectural blocker** — if the feature requires infrastructure not yet in place (e.g., navigation), implement the
   minimum viable infrastructure first, then proceed.
4. **Scope creep** — if implementation reveals the feature is larger than expected, pause and discuss with the user.
   Offer to split into smaller increments.
5. **Unclear requirement** — never guess. Use `AskUserQuestion` to get clarity.

---

## Quick Reference: Key Project Paths

| Path                                                 | Purpose                        |
|------------------------------------------------------|--------------------------------|
| `app/build.gradle.kts`                               | App build config, dependencies |
| `gradle/libs.versions.toml`                          | Version catalog                |
| `app/src/main/java/com/sls/handbook/`                | Main source root               |
| `app/src/main/java/com/sls/handbook/ui/theme/`       | Theme (Color, Type, Theme)     |
| `app/src/main/java/com/sls/handbook/ui/`             | UI composables                 |
| `app/src/main/java/com/sls/handbook/MainActivity.kt` | Entry point                    |
| `app/src/main/res/values/strings.xml`                | String resources               |
| `app/src/main/AndroidManifest.xml`                   | Manifest                       |
| `app/src/test/java/com/sls/handbook/`                | Unit tests                     |
| `app/src/androidTest/java/com/sls/handbook/`         | Instrumented tests             |

## Quick Reference: Project Stack

- **Language:** Kotlin 2.0.21
- **UI:** Jetpack Compose + Material 3
- **Compose BOM:** 2024.09.00
- **Min SDK:** 33 | **Target SDK:** 36
- **Build:** Gradle 9.0.0 (AGP) with version catalog
- **Theme:** shadcn-inspired Zinc palette, light/dark/dynamic color
