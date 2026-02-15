---
description: Replay a recorded E2E test by name and generate a pass/fail report
allowed-tools: Bash, Read, Write, Glob, Grep, AskUserQuestion
---

# Run E2E Test Skill

You are an E2E test replay agent for the HandyPlay Android app. You replay previously recorded
interactions and verify the app behaves as expected using screenshots and Claude vision analysis.

## Invocation

- `/run-e2e` — list available tests and prompt user to pick one
- `/run-e2e <test-name>` — replay a specific recorded test

## Phase 1: Load Test

### 1a. List available tests (if no name provided)

```bash
ls -d <PROJECT_ROOT>/e2e-tests/*/
```

If no tests exist, inform the user and suggest they run `/record-e2e` first.

If multiple tests exist, present the list and ask the user to pick one.

### 1b. Read test.json

Use the Read tool to read `<PROJECT_ROOT>/e2e-tests/<test-name>/test.json`.

Validate it contains `metadata` and `events` arrays.

### 1c. Display test info

Show the user:
- Test name
- Number of events
- Device it was recorded on
- Screen resolution
- Recording date

## Phase 2: Setup

### 2a. Detect ADB

```bash
ADB=$(which adb 2>/dev/null || echo "$HOME/Library/Android/sdk/platform-tools/adb")
```

### 2b. Verify device connected

```bash
$ADB devices | grep -w "device"
```

### 2c. Check resolution compatibility

```bash
$ADB shell wm size
```

Compare the device's resolution with the recording's `metadata.screenWidth` and
`metadata.screenHeight`. If they differ, warn the user:

> Warning: Current device resolution (<actual>) differs from recording resolution
> (<recorded>). Tap coordinates may not match. Continue anyway?

### 2d. Lock orientation to portrait

```bash
$ADB shell settings put system accelerometer_rotation 0
$ADB shell settings put system user_rotation 0
```

### 2e. Build and install latest debug APK

```bash
cd <PROJECT_ROOT> && ./gradlew assembleDebug
$ADB install -r <PROJECT_ROOT>/app/build/outputs/apk/debug/app-debug.apk
```

### 2f. Clear app data

```bash
$ADB shell pm clear com.sls.handbook
```

### 2g. Create replay screenshot directory

```bash
REPLAY_DIR="<PROJECT_ROOT>/e2e-tests/<test-name>/replay-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$REPLAY_DIR"
```

### 2h. Launch the app normally

```bash
$ADB shell am start -n com.sls.handbook/.MainActivity
```

Wait 3 seconds for the app to render.

## Phase 3: Replay

For each event in `test.json`, in chronological order:

### 3a. Calculate delay

- For the first event: delay = `timestampMs / 1000` seconds (minimum 0.5s)
- For subsequent events: delay = `(current.timestampMs - previous.timestampMs) / 1000` seconds
  (minimum 0.5s)

Wait for the calculated delay.

### 3b. Take BEFORE screenshot

```bash
$ADB shell screencap -p /sdcard/e2e_before.png
$ADB pull /sdcard/e2e_before.png "$REPLAY_DIR/step_<N>_before.png"
$ADB shell rm /sdcard/e2e_before.png
```

### 3c. Execute the action

Based on the event `type`:

**TAP:**
```bash
$ADB shell input tap <x> <y>
```

**SWIPE:**
```bash
$ADB shell input swipe <startX> <startY> <endX> <endY> <durationMs>
```

**BACK_PRESS:**
```bash
$ADB shell input keyevent KEYCODE_BACK
```

**TEXT_INPUT:**
```bash
$ADB shell input text "<text>"
```

Round all coordinates to integers for ADB `input` commands.

### 3d. Wait for UI to settle

Sleep 1 second after each action.

### 3e. Take AFTER screenshot

```bash
$ADB shell screencap -p /sdcard/e2e_after.png
$ADB pull /sdcard/e2e_after.png "$REPLAY_DIR/step_<N>_after.png"
$ADB shell rm /sdcard/e2e_after.png
```

### 3f. Analyze screenshot

Use the Read tool on the AFTER screenshot. Claude's vision will analyze the screen state.
Determine if the action had the expected visual effect:

- For TAP: Did the screen change in response? (navigation, button state, etc.)
- For SWIPE: Did the content scroll/move?
- For BACK_PRESS: Did we navigate back?
- For TEXT_INPUT: Is the text visible in the field?

Record PASS or FAIL with a brief note.

### 3g. Compare with reference (if available)

If reference screenshots exist from a previous run or recording, compare the AFTER screenshot
with the reference to detect visual regressions.

## Phase 4: Report

After all steps complete, generate a markdown report:

```markdown
# E2E Replay Report — <test-name>

**Date:** <current date>
**Device:** <adb shell getprop ro.product.model>
**Android:** <adb shell getprop ro.build.version.release>
**App:** com.sls.handbook (debug)
**Recording device:** <metadata.deviceModel>
**Recording resolution:** <metadata.screenWidth>x<metadata.screenHeight>

## Results Summary

| Step | Action | Result | Notes |
|------|--------|--------|-------|
| 1 | TAP (540, 1200) | PASS | Screen navigated to Home |
| 2 | SWIPE (540,1800 → 540,600) | PASS | Content scrolled |
| 3 | BACK_PRESS | PASS | Returned to previous screen |

**Overall:** X/N passed

## Screenshots

All replay screenshots saved to: `e2e-tests/<test-name>/replay-<timestamp>/`

## Issues Found

- <list any failures or unexpected behaviors>
```

## Phase 5: Cleanup

Clean up temporary files on the device:

```bash
$ADB shell rm -f /sdcard/e2e_before.png /sdcard/e2e_after.png
```

## Important Notes

- Replay uses the same ADB `input` commands that a user would trigger — it's a true end-to-end test.
- Coordinate-based replay is resolution-dependent. Always replay on the same resolution device
  that was used for recording.
- If a test step fails, continue with remaining steps (don't abort the entire test).
- The app is launched normally (not in recording mode) during replay — no overlay is present.
- If the test includes TEXT_INPUT events, the text field must be focused (via a preceding TAP
  event) for the input to work.
