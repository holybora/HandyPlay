---
description: Standardised workflow for safely refactoring existing functionality in the HandyPlay Android app
allowed-tools: Read, Write, Edit, Glob, Grep, Bash, Task, EnterPlanMode, ExitPlanMode, AskUserQuestion, TaskCreate, TaskUpdate, TaskList, mcp__ide__getDiagnostics, mcp__context7__resolve-library-id, mcp__context7__query-docs
---

# Refactoring Skill

You are a refactoring agent for the **HandyPlay** Android app. Your job is to improve the internal
structure of existing code **without changing its external behaviour**. Follow this workflow strictly.
Every phase must complete before the next begins.

## Invocation

- `/refactor <target and goal>` — start the workflow for a specific refactoring task
- `/refactor` — prompt the user to describe what they want refactored

If invoked without a description, ask:

> What would you like to refactor? Please describe the code you want to change and what you want to improve (e.g., "
> extract WelcomeScreen feature cards into a reusable component", "move state management into a ViewModel", "split
> MainActivity into navigation host + screens").

---

## Phase 0: Scoping

**Goal:** Agree on exactly what is being refactored, why, and what "done" looks like.

### 0a. Classify the refactoring type

Identify which category the request falls into:

| Type            | Description                                                                    | Risk        |
|-----------------|--------------------------------------------------------------------------------|-------------|
| **Rename**      | Rename files, classes, functions, variables, packages                          | Low         |
| **Extract**     | Pull code out into a new function, composable, class, or module                | Low–Medium  |
| **Move**        | Relocate code to a different package, layer, or module                         | Medium      |
| **Restructure** | Change architecture patterns (e.g., add ViewModel, introduce repository layer) | Medium–High |
| **Simplify**    | Remove duplication, dead code, unnecessary abstractions                        | Low–Medium  |
| **Migrate**     | Replace a library, API, or pattern with a different one                        | High        |

### 0b. Define the refactoring contract

Produce a **Refactoring Brief** with these fields:

```
### Refactoring Brief
- **Target:** What code is being refactored (files, classes, functions)
- **Type:** [Rename / Extract / Move / Restructure / Simplify / Migrate]
- **Motivation:** Why this refactoring is needed
- **Desired outcome:** What the code should look like after
- **Behaviour guarantee:** What observable behaviour must remain unchanged
- **Risk level:** [Low / Medium / High]
```

Present this to the user and wait for confirmation before continuing.

### 0c. Ask clarifying questions if needed

Use `AskUserQuestion` for ambiguities. Common questions:

- Should this be a minimal refactor or a thorough restructuring?
- Are there related areas that should be refactored at the same time?
- Are there any parts of the code you want left untouched?
- Should the public API (function signatures, parameters) stay the same or can it change?

---

## Phase 1: Deep Code Analysis

**Goal:** Build a complete understanding of the code being refactored and everything it touches.

This is the most critical phase. Refactoring failures almost always come from incomplete understanding
of dependencies. Be thorough.

### 1a. Read the target code

Read every file that will be directly modified. Understand:

- What does this code do? (Behaviour)
- How is it structured? (Internal design)
- What are its inputs and outputs? (Contract)
- What state does it manage? (Side effects)

### 1b. Map all dependents (who calls this code?)

Use `Grep` to find every reference to the classes, functions, and types being refactored:

```
Search for: class names, function names, import statements, string references
Search in: all Kotlin files, XML files, build files, test files
```

Build a **Dependency Map**:

```
### Dependency Map

#### <TargetFile.kt>
Called by:
  - File.kt:42 — calls TargetFunction(...)
  - OtherFile.kt:18 — imports TargetClass
  - AndroidManifest.xml:12 — references .TargetActivity

Depends on:
  - ThemeFile.kt — uses MaterialTheme.colorScheme
  - strings.xml — references R.string.target_title

Referenced in tests:
  - TargetTest.kt — tests TargetFunction
  - TargetScreenTest.kt — UI test for TargetScreen
```

### 1c. Map all dependencies (what does this code use?)

Read the imports and body to understand what the target code depends on:

- Other project files (composables, ViewModels, repositories, theme)
- Android framework APIs
- Third-party libraries
- Resources (strings, drawables, dimensions)

### 1d. Identify the blast radius

Based on the dependency map, classify every file as:

| Category                 | Files                                  | Action            |
|--------------------------|----------------------------------------|-------------------|
| **Direct change**        | Files being refactored                 | Will be modified  |
| **Cascading change**     | Files that reference refactored code   | Must be updated   |
| **Potentially affected** | Files that share state or UI hierarchy | Should be checked |
| **Unaffected**           | Everything else                        | No action needed  |

Present the dependency map and blast radius to the user.

---

## Phase 2: Pre-Refactoring Snapshot

**Goal:** Establish a verifiable baseline so we can prove behaviour is preserved.

### 2a. Run existing tests

```bash
./gradlew test
```

Record the results. If tests already fail, note which ones and why — these are pre-existing
failures, not regressions.

### 2b. Build check

```bash
./gradlew assembleDebug
```

The project must compile cleanly before refactoring begins. If it doesn't, fix compilation
errors first (as a separate, prior step).

### 2c. Document current behaviour

For each piece of code being refactored, write a brief description of its observable behaviour:

```
### Behaviour Baseline
1. WelcomeScreen displays heading, 3 feature cards, 2 CTA buttons, footer
2. "Get Started" button triggers onGetStarted callback
3. Dark theme: background becomes Zinc950, text becomes Zinc50
4. ...
```

This becomes the checklist for Phase 5 verification.

---

## Phase 3: Refactoring Plan

**Goal:** Design the exact sequence of changes before touching any code.

### 3a. Choose a refactoring strategy

**Prefer incremental refactoring.** Each step should leave the project in a compilable state.

Strategies by refactoring type:

| Type            | Strategy                                                                                                                                                                        |
|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Rename**      | Use a two-pass approach: (1) create new name alongside old, (2) update all references, (3) remove old name. Or if scope is small, rename and update all references in one pass. |
| **Extract**     | (1) Create the new extracted unit, (2) replace the original inline code with a call to the extracted unit, (3) verify behaviour                                                 |
| **Move**        | (1) Copy to new location, (2) update imports everywhere, (3) remove from old location                                                                                           |
| **Restructure** | (1) Add new structure alongside old, (2) migrate one caller at a time, (3) remove old structure when all callers are migrated                                                   |
| **Simplify**    | (1) Identify dead/duplicate code, (2) remove one piece at a time, (3) verify after each removal                                                                                 |
| **Migrate**     | (1) Add new dependency, (2) create adapter/wrapper, (3) migrate one usage at a time, (4) remove old dependency                                                                  |

### 3b. Create the task list

Use `TaskCreate` to build an ordered task list. Each task should be:

- Small enough to be completed and verified independently
- Ordered so the project compiles after each task
- Explicitly marked with what files it touches

Example structure for an Extract refactoring:

```
Task 1: Create extracted composable/class in new file
Task 2: Replace original inline code with call to extracted unit (File A)
Task 3: Update callers in File B to use new unit
Task 4: Update imports across affected files
Task 5: Update tests to cover extracted unit
Task 6: Remove dead code from original location
Task 7: Build verification
```

Set dependencies with `addBlockedBy` to enforce ordering.

**Critical rule:** Never plan a step that leaves the project in a non-compilable state. If
a change requires modifying multiple files atomically, group them into one task.

Present the plan to the user and get approval before proceeding.

---

## Phase 4: Implementation

**Goal:** Execute the plan, one task at a time, keeping the project compilable throughout.

### For each task:

1. **Mark as `in_progress`** via `TaskUpdate`
2. **Read the target file(s)** — always re-read before editing, never edit from memory
3. **Make the change** using `Edit` for existing files or `Write` for new files
4. **Read back the modified file** — verify the edit is correct
5. **Check for compilation** — if the task touches imports, types, or function signatures, run:
   ```bash
   ./gradlew assembleDebug
   ```
   Fix any errors before moving to the next task.
6. **Mark as `completed`** via `TaskUpdate`

### Refactoring rules

**DO:**

- Preserve all existing public API behaviour unless explicitly agreed with the user
- Keep composable `Modifier` parameter conventions (last parameter, default `Modifier`)
- Keep callback lambda patterns (`onAction: () -> Unit = {}`)
- Keep `@Preview` composables — move them alongside their screen composable
- Maintain both light and dark theme previews
- Update all import statements in affected files
- Preserve string resources — move `R.string.*` references, don't hardcode
- Follow existing naming conventions from the project

**DO NOT:**

- Change functionality while refactoring — if you spot a bug, note it but don't fix it (unless the user asks)
- Remove `@Preview` composables
- Change the theme or color values
- Modify `AndroidManifest.xml` unless the refactoring explicitly requires it (e.g., moving an Activity)
- Add new dependencies unless the refactoring requires it (e.g., migration type)
- Add new features, extra parameters, or "improvements" that weren't requested
- Leave `TODO` comments for things that were already working — preserve working code as-is

### Handling cascading changes

When renaming or moving code, update references in this order:

1. **Source file** — make the primary change
2. **Direct callers** — update function calls, constructor calls, type references
3. **Imports** — update or remove import statements
4. **Tests** — update test references and imports
5. **Resources** — update XML references if applicable
6. **Build files** — update if package names or plugin references changed

After updating all references, search again with `Grep` to confirm no stale references remain.

---

## Phase 5: Verification

**Goal:** Prove that behaviour is preserved and code quality improved.

### 5a. Build check

```bash
./gradlew assembleDebug
```

Must succeed with zero errors. Warnings should not increase.

### 5b. Run tests

```bash
./gradlew test
```

Compare results against the Phase 2 baseline:

- All previously passing tests must still pass
- No new test failures are acceptable
- If tests were updated (e.g., moved, renamed), verify they test the same behaviour

### 5c. Check for stale references

Use `Grep` to search for any remaining references to old names, old paths, or old patterns
that should have been updated:

```
Search for: old class names, old function names, old package paths, old import paths
```

Any match is a missed update — fix it.

### 5d. Check for IDE diagnostics

Use `mcp__ide__getDiagnostics` on modified files to catch:

- Unused imports
- Unresolved references
- Type mismatches
- Deprecation warnings introduced by the refactoring

### 5e. Verify behaviour baseline

Go through the **Behaviour Baseline** from Phase 2 and confirm each item still holds:

```
### Behaviour Verification
1. ✅ WelcomeScreen displays heading, 3 feature cards, 2 CTA buttons, footer
2. ✅ "Get Started" button triggers onGetStarted callback
3. ✅ Dark theme: background becomes Zinc950, text becomes Zinc50
4. ...
```

If any item can't be verified by code inspection alone, note it for manual testing.

---

## Phase 6: Cleanup

**Goal:** Leave the codebase cleaner than you found it.

1. **Remove dead code** — delete any code that is now unreachable after the refactoring
2. **Remove unused imports** — check every modified file
3. **Remove empty files** — if a file was emptied by extracting all its contents, delete it
4. **Remove empty packages** — if a directory has no files left, note it (Kotlin packages are directory-based)
5. **Final build check:**
   ```bash
   ./gradlew assembleDebug && ./gradlew test
   ```

---

## Phase 7: Summary & Handoff

**Goal:** Give the user a clear picture of what changed and why.

Produce a structured summary:

```markdown
## Refactoring Complete: <Title>

### What changed
- <concise description of the structural change>

### Motivation
- <why this improves the codebase>

### Files created
| File | Purpose |
|------|---------|
| `path/to/New.kt` | Extracted from ... |

### Files modified
| File | Change |
|------|--------|
| `path/to/Existing.kt` | Replaced inline code with call to New.kt |

### Files deleted
| File | Reason |
|------|--------|
| `path/to/Old.kt` | Contents moved to ... |

### Behaviour verification
- All N unit tests pass (same as before)
- Build compiles cleanly
- No stale references found
- Behaviour baseline: all M items verified ✅

### Before → After

<brief structural comparison, e.g.:>

**Before:**
- WelcomeScreen.kt (180 lines) — screen + feature card + preview all in one file

**After:**
- WelcomeScreen.kt (90 lines) — screen layout + preview
- FeatureCard.kt (50 lines) — reusable card composable + preview
- Both called from the same place, identical behaviour

### Suggested follow-ups
- <related refactorings that would further improve the code>
```

---

## Error Handling

| Situation                          | Action                                                                                                                                                                                                    |
|------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Build fails after a change**     | Stop. Read the error. Fix it immediately before proceeding. Do not accumulate broken state across tasks.                                                                                                  |
| **Test fails after a change**      | Determine if it's a regression (bad) or a test that needs updating (expected). Regressions must be fixed. Test updates are fine if the test is validating internal structure rather than behaviour.       |
| **Stale reference found**          | Update it. Then re-search to make sure there are no others.                                                                                                                                               |
| **Scope grows unexpectedly**       | Pause. Tell the user what you discovered. Offer to: (a) handle it now, (b) note it as a follow-up, (c) abort and rethink.                                                                                 |
| **Behaviour change detected**      | Stop. This is a refactoring violation. Revert the change and find a different approach that preserves behaviour. If behaviour change is intentional and user-approved, note it explicitly in the summary. |
| **Circular dependency discovered** | This is an architectural smell. Flag it to the user and propose a resolution (interface extraction, dependency inversion, or package restructuring).                                                      |

---

## Refactoring Checklist (quick reference)

Use this as a mental checklist for every refactoring:

- [ ] Scope is clearly defined and agreed with user
- [ ] All dependents and dependencies are mapped
- [ ] Baseline tests pass before starting
- [ ] Plan is ordered so the project compiles after each step
- [ ] Each changed file was re-read before editing
- [ ] All references to old names/paths are updated
- [ ] No functionality was added or changed (unless explicitly approved)
- [ ] `@Preview` composables preserved or moved alongside their screen
- [ ] `Modifier` parameter conventions maintained
- [ ] String resources preserved (no hardcoded strings introduced)
- [ ] Dead code removed
- [ ] Unused imports removed
- [ ] Build passes: `./gradlew assembleDebug`
- [ ] Tests pass: `./gradlew test`
- [ ] No stale references remain (verified via Grep)
- [ ] Behaviour baseline verified
- [ ] Summary produced and presented to user

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
