---
description: Analyse an Android module and update its CLAUDE.md to reflect current state
allowed-tools: Read, Write, Edit, Glob, Grep, Bash, AskUserQuestion
---

# Analyse Module Skill

You are a documentation agent for the **HandyPlay** Android app. Your job is to analyse a module's
build configuration and source code, then generate or update its `CLAUDE.md` file so it accurately
reflects the current state of the module.

## Invocation

- `/analyse-module :feature:home` — analyse a specific module (Gradle notation)
- `/analyse-module feature/home` — analyse a specific module (file path)
- `/analyse-module --all` — analyse every module in the project
- `/analyse-module` — prompt the user to pick a module

If invoked without arguments, ask:

> Which module would you like to analyse? You can use Gradle notation (e.g., `:feature:home`) or a
> file path (e.g., `feature/home`). Use `--all` to analyse every module.

---

## Phase 1: Resolve Module Path

### Single module

1. If the argument starts with `:`, convert Gradle notation to a file path by replacing `:` with `/`
   and stripping the leading `/` (e.g., `:feature:home` → `feature/home`).
2. If the argument is a file path, convert to Gradle notation by replacing `/` with `:` and
   prepending `:` (e.g., `feature/home` → `:feature:home`).
3. Verify that `<module-path>/build.gradle.kts` exists using `Read`. If it doesn't exist, tell the
   user and stop.

### `--all` mode

1. Read `settings.gradle.kts` at the project root.
2. Extract all `include(...)` entries to get the list of modules.
3. For each module, convert the Gradle path to a file path.
4. Process each module through Phases 2–5 sequentially.

---

## Phase 2: Parse Build Configuration

Read `<module-path>/build.gradle.kts` and extract:

### 2a. Plugins

Look for the `plugins { }` block. Extract each plugin ID:

```
id("handyplay.android.application")  →  handyplay.android.application
id("handyplay.android.feature")      →  handyplay.android.feature
alias(libs.plugins.kotlin.compose)    →  kotlin.compose (alias)
```

### 2b. Module type

Map the primary convention plugin to a type label:

| Plugin | Type label |
|--------|-----------|
| `handyplay.android.application` | Application |
| `handyplay.android.feature` | Feature module |
| `handyplay.android.library` + `handyplay.android.library.compose` | Android Library with Compose |
| `handyplay.android.library` (alone) | Android Library |
| `handyplay.jvm.library` | JVM Library |

### 2c. Namespace

Look for `namespace = "..."` inside the `android { }` block. JVM libraries won't have one.

### 2d. Application-specific fields

If the module uses `handyplay.android.application`, also extract:
- `applicationId`
- `versionCode` and `versionName`

### 2e. Dependencies

Parse the `dependencies { }` block. Categorise each dependency:

- **Project dependencies**: `implementation(project(":core:data"))` or `api(project(":core:model"))`
  — record the module path and scope (`api` or `implementation`).
- **Library dependencies**: `implementation(libs.androidx.core.ktx)` — record the alias and scope.
- **Test dependencies**: `testImplementation(...)`, `androidTestImplementation(...)` — record separately.

### 2f. Feature module auto-includes

If the plugin is `handyplay.android.feature`, note that the following are auto-included by the
convention plugin and should NOT be listed under explicit dependencies:

- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`
- Compose BOM + Material3, Lifecycle runtime + viewmodel + compose, Hilt

Instead, add a dedicated section:

```markdown
## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`
```

Only list dependencies in the `## Dependencies` section if they are **explicitly declared** in the
module's `build.gradle.kts` beyond what the convention plugin provides.

---

## Phase 3: Scan Source Files

### 3a. Determine source root

- Android modules: `<module-path>/src/main/java/`
- JVM libraries: `<module-path>/src/main/kotlin/`

Use `Glob` to find all `*.kt` files under the source root. If both `java/` and `kotlin/` exist,
check both.

Also check for resource files: `<module-path>/src/main/res/` (Android modules only).

### 3b. Read and classify each file

For each Kotlin source file, read it and identify the primary construct:

| Pattern to detect | Classification | Description format |
|-------------------|---------------|-------------------|
| `@HiltViewModel class FooViewModel` | ViewModel | `@HiltViewModel` with `StateFlow<FooUiState>`, describe injected deps |
| `@Composable fun FooScreen(` | Screen composable | Main composable, note layout type (Column, LazyColumn, Scaffold, etc.) |
| `@Composable fun FooBar(` (not Screen/Route) | Component composable | Reusable composable, note purpose |
| `sealed interface FooUiState` | UI State | Sealed interface with variant names (Loading, Success, Error, etc.) |
| `interface FooRepository` | Repository contract | Interface with method signatures |
| `class FooRepositoryImpl` with `@Inject` | Repository implementation | Implements FooRepository, note data sources |
| `@Module` or `@Provides` or `@Binds` | Hilt DI module | DI bindings provided |
| `@Serializable data class` or `@Serializable object` | Serializable model/destination | Note if nav destination or data model |
| `data class Foo(` (no @Serializable) | Data model | Plain data class with key fields |
| `object Foo` | Singleton/constants | Note purpose |
| `fun Foo(` (top-level, not composable) | Utility function | Note purpose |

### 3c. Build the Key Files list

Create a list of files with one-line descriptions. Use relative paths from the module root's source
directory. Group files in subdirectories using the subdirectory prefix:

```markdown
- `HomeScreen.kt` — Main composable with `LazyVerticalGrid` (2-column layout)
- `HomeViewModel.kt` — `@HiltViewModel` with `StateFlow<HomeUiState>`, search filtering
- `HomeUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/CategoryCard.kt` — Card composable for category display
```

### 3d. Identify architectural patterns

Look for recurring patterns across the module's files and document them:

- ViewModel + StateFlow pattern: "ViewModel exposes `uiState: StateFlow<X>` collected via `collectAsStateWithLifecycle()`"
- Repository pattern: "Implements interfaces from `:core:domain` using Hilt `@Binds`"
- Navigation pattern: "Uses `@Serializable` objects for type-safe navigation"
- Any other notable patterns specific to this module

Only document patterns that are **actually present** in the source code. Do not guess or infer
patterns that aren't there.

---

## Phase 4: Read Existing CLAUDE.md

If `<module-path>/CLAUDE.md` already exists:

1. Read the full file content.
2. Store the entire existing content — it will be shown as a diff to the user.
3. Extract the existing one-line description (the line immediately after the `# :module:path` heading).

If no CLAUDE.md exists, note that this is a new file.

---

## Phase 5: Generate and Confirm

### 5a. Propose module description

Based on your analysis, propose a one-line description for the module. Use `AskUserQuestion` to ask
the user to confirm or edit it:

> I'd describe this module as: **"<proposed description>"**
> Is this accurate, or would you like to change it?

Wait for the user's response. Use their version if they provide one.

In `--all` mode, batch the descriptions: present all proposed descriptions at once and let the user
confirm or edit any of them before proceeding.

### 5b. Assemble the CLAUDE.md content

Follow this template exactly. Include a section only if it has content:

```markdown
# :<gradle:path>

<one-line description confirmed by user>

## Module Info

- **Namespace:** `<namespace>` (omit for JVM libraries)
- **Type:** <type label>
- **Plugin:** `<primary plugin>` (or **Plugins:** `<plugin1>`, `<plugin2>`)

## Auto-included by `<plugin>` (only for feature modules)

- <what the convention plugin provides>
- <auto-included module dependencies>

## Dependencies (only if explicit deps exist beyond convention plugin)

- `:module:path` (api/implementation) — brief purpose
- `library-alias` — brief purpose

## Dependencies (exposed as `api`) (use this variant when module exposes deps transitively)

- `library-name`

## Key Files

- `FileName.kt` — One-line description

## Source

- `src/main/java/<package/path>/` (or `src/main/kotlin/...` for JVM)

## Patterns (only if notable patterns found)

- Pattern description

## Build Commands (only for :app module)

\`\`\`bash
./gradlew :<module>:assembleDebug
./gradlew :<module>:testDebugUnitTest
\`\`\`

## Tests (only if test sources exist)

- `src/test/` — JVM unit tests
- `src/androidTest/` — Compose UI / instrumented tests

## Notes (preserved from existing file, or omit if none)

- <preserved notes>
```

### 5c. Diff and confirm before writing

If a CLAUDE.md already exists, show the user a clear comparison:

1. Present the **existing** content.
2. Present the **proposed** new content.
3. Highlight key differences (added/removed/changed sections).
4. Use `AskUserQuestion` to ask:

> Here's the updated CLAUDE.md for `:<module>`. Should I write this file?

If the user approves, write the file. If they request changes, incorporate them and re-present.

If this is a **new** CLAUDE.md (no existing file), show the proposed content and ask for confirmation
before writing.

### 5d. Write the file

Use the `Write` tool to write `<module-path>/CLAUDE.md`.

---

## `--all` Mode Flow

When `--all` is specified:

1. Parse `settings.gradle.kts` for all modules.
2. Run Phases 2–3 for every module (collect data).
3. Present a summary table of all modules with proposed descriptions. Ask user to confirm/edit.
4. For each module where a CLAUDE.md already exists, show diffs and ask for batch confirmation:
   > I've analysed N modules. M have existing CLAUDE.md files that would be updated. Would you like
   > to see diffs for each, or approve all updates?
5. Write approved files.
6. Report a summary: which files were created, updated, or left unchanged.

---

## Edge Cases

| Situation | Action |
|-----------|--------|
| Module has no source files | Generate CLAUDE.md with build info only. Key Files and Patterns sections are omitted. |
| Module has no `build.gradle.kts` | Report error and skip. This isn't a valid module. |
| `build-logic/convention` module | Read its `build.gradle.kts` for plugin registrations. List plugins as Key Files instead of source classes. |
| Source in both `java/` and `kotlin/` dirs | Scan both. Note both in the Source section. |
| Module has custom sections in CLAUDE.md | Preserve any section that isn't in the standard template (treat as part of Notes). |
| Very large module (50+ files) | Summarise by subdirectory rather than listing every file. Group related files. |

---

## Quality Rules

- **Never hallucinate**: Only document patterns you can verify from the source code. If a file is
  empty or you can't determine its purpose, say so honestly.
- **Match existing style**: Look at other CLAUDE.md files in the project for formatting cues. Keep
  descriptions concise (one line per file).
- **Don't over-document**: Key Files should list the important files, not every file. Skip test
  utilities, generated files, and trivial one-liners.
- **Preserve human intent**: The Notes section often contains architectural guidance ("Do NOT add
  business components here"). Always preserve these.
