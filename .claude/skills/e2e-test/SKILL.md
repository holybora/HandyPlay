---
description: Run E2E tests on the Android emulator using ADB + Claude vision analysis
disable-model-invocation: true
allowed-tools: Bash, Read, Grep, Glob, mcp__playwright__browser_navigate, mcp__playwright__browser_take_screenshot, mcp__playwright__browser_snapshot, mcp__playwright__browser_click, mcp__playwright__browser_close, mcp__playwright__browser_tabs
---

# E2E Test Skill for HandyPlay

You are an E2E testing agent for the HandyPlay Android app. You use ADB for device interaction and Claude's vision
capabilities (via the Read tool on screenshots) to verify UI state.

## Invocation

- `/e2e-test` — run the default smoke test (8 steps against WelcomeScreen)
- `/e2e-test <scenario>` — run a custom test scenario described by the user

## Environment

All paths are defined in `$PROJECT_ROOT/.env`. Load them at the start of every run:

```bash
source $PROJECT_ROOT/.env
```

This provides:

- `$PROJECT_ROOT` — project root directory
- `$ANDROID_SDK` — Android SDK path
- `$ADB` — full path to adb binary
- `$EMULATOR` — full path to emulator binary
- `$E2E_SCREENSHOTS_DIR` — local project directory for screenshots (`$PROJECT_ROOT/e2e-screenshots`)

## Phase 1: Setup

Run these steps sequentially. Stop and report if any step fails.

### 1a. Load environment

```bash
source /Users/admin/AndroidStudioProjects/HandyPlay/.env
```

### 1b. Detect or start emulator

```bash
# Check for running emulator
$ADB devices | grep -w "device"
```

If no device is connected:

```bash
# List available AVDs
$EMULATOR -list-avds

# Start the default AVD (Pixel_8_API_35) in background
nohup $EMULATOR -avd Pixel_8_API_35 -no-snapshot-load -no-audio -gpu auto > /tmp/emulator-stdout.log 2>&1 &

# Wait for boot (poll every 5s, timeout 120s)
$ADB wait-for-device
# Then poll: adb shell getprop sys.boot_completed  → must return "1"
```

Wait until `sys.boot_completed` returns `1` before proceeding. Poll every 5 seconds, timeout after 120 seconds.

### 1c. Create screenshot directory

```bash
mkdir -p $E2E_SCREENSHOTS_DIR
```

### 1d. Build and install

```bash
cd $PROJECT_ROOT
./gradlew assembleDebug
$ADB install -r app/build/outputs/apk/debug/app-debug.apk
```

### 1e. Launch the app

```bash
$ADB shell am start -n com.sls.handbook/.MainActivity
```

Wait 3 seconds for the app to render.

## Phase 2: Interaction Primitives

Use these ADB commands as building blocks in Phase 3. Always use the `$ADB` variable (loaded from `.env`).

### Screenshot capture

```bash
TIMESTAMP=$(date +%s)
$ADB shell screencap -p /sdcard/screen_${TIMESTAMP}.png
$ADB pull /sdcard/screen_${TIMESTAMP}.png $E2E_SCREENSHOTS_DIR/step_${STEP_NAME}.png
$ADB shell rm /sdcard/screen_${TIMESTAMP}.png
```

After pulling, use the **Read** tool on the PNG file. Claude will see the image and can analyze what's on screen.

### UI hierarchy dump (for finding tap coordinates)

```bash
$ADB shell uiautomator dump /sdcard/ui_dump.xml
$ADB pull /sdcard/ui_dump.xml $E2E_SCREENSHOTS_DIR/ui_dump.xml
```

Then use **Read** on `$E2E_SCREENSHOTS_DIR/ui_dump.xml` to find element bounds. Bounds format is
`[left,top][right,bottom]`. Compute tap coordinates as:

- `tap_x = (left + right) / 2`
- `tap_y = (top + bottom) / 2`

### Tap

```bash
$ADB shell input tap <x> <y>
```

### Type text

```bash
$ADB shell input text "<text>"
```

### Swipe / Scroll

```bash
# Swipe up (scroll down)
$ADB shell input swipe 540 1800 540 600 300

# Swipe down (scroll up)
$ADB shell input swipe 540 600 540 1800 300
```

### Long press

```bash
$ADB shell input swipe <x> <y> <x> <y> 1000
```

### Key events

```bash
$ADB shell input keyevent KEYCODE_BACK
$ADB shell input keyevent KEYCODE_HOME
$ADB shell input keyevent KEYCODE_APP_SWITCH
```

### Wait for element

Poll with `uiautomator dump` every 2 seconds, up to 10 seconds, checking for a specific text or resource-id in the XML.

### Orientation

```bash
# Landscape
$ADB shell settings put system accelerometer_rotation 0
$ADB shell settings put system user_rotation 1

# Portrait
$ADB shell settings put system accelerometer_rotation 0
$ADB shell settings put system user_rotation 0
```

### Dark mode

```bash
# Enable dark mode
$ADB shell cmd uimode night yes

# Disable dark mode
$ADB shell cmd uimode night no
```

## Phase 3: Test Execution

For **each test step**, follow this exact pattern:

1. **State intent**: Print what you're about to test
2. **Capture BEFORE**: Take a screenshot, read it with the Read tool
3. **Act**: Perform the interaction (tap, swipe, etc.)
4. **Wait**: Sleep 1-2 seconds for UI to settle
5. **Capture AFTER**: Take another screenshot, read it with the Read tool
6. **Assert**: Analyze the AFTER screenshot — does it match expectations?
7. **Record**: Log PASS or FAIL with details

Name screenshots with the pattern: `step_<N>_<name>_<before|after>.png`

### Default Smoke Test Steps

Run these 8 steps when invoked as `/e2e-test` with no arguments:

**Step 1: App Launch — Heading Visible**

- After launching the app in Phase 1, capture a screenshot
- Assert: "Welcome to HandyPlay" heading is visible on screen
- Assert: The "HP" app icon circle is visible
- Assert: The "v1.0 — Now Available" badge is visible

**Step 2: Feature Cards Visible**

- Capture screenshot (may already be visible from step 1)
- Assert: "Curated Guides" card is visible with its description
- Assert: "Interactive Practice" card is visible with its description
- Assert: "Track Progress" card is visible with its description

**Step 3: Scroll to CTA Buttons**

- Swipe up to scroll down and reveal the bottom of the screen
- Capture screenshot
- Assert: "Get Started" button is visible
- Assert: "I already have an account" button is visible
- Assert: Footer text about Terms of Service is visible

**Step 4: Tap "Get Started"**

- Use uiautomator dump to find the "Get Started" button bounds
- Tap the button center coordinates
- Capture screenshot
- Record: What happened after tap (currently a no-op TODO — note this)

**Step 5: Navigate Back and Tap "I already have an account"**

- Press back or re-launch activity if needed to return to WelcomeScreen
- Use uiautomator dump to find the "I already have an account" button
- Tap the button center coordinates
- Capture screenshot
- Record: What happened after tap (currently a no-op TODO — note this)

**Step 6: Orientation Change**

- Return to WelcomeScreen if needed
- Switch to landscape orientation
- Wait 2 seconds
- Capture screenshot
- Assert: Layout adapts — content is still visible and readable
- Switch back to portrait
- Wait 2 seconds

**Step 7: Dark Mode Toggle**

- Enable dark mode via ADB
- Wait 2 seconds
- Capture screenshot
- Assert: App renders in dark theme (dark background, light text)
- Disable dark mode
- Wait 2 seconds
- Capture screenshot
- Assert: App renders in light theme again

**Step 8: Background and Restore**

- Press HOME key to background the app
- Wait 2 seconds
- Re-launch the app: `$ADB shell am start -n com.sls.handbook/.MainActivity`
- Wait 2 seconds
- Capture screenshot
- Assert: WelcomeScreen is displayed, state is preserved (scroll position, theme)

## Phase 4: Reporting

After all steps complete, produce a structured markdown report:

```markdown
# E2E Test Report — HandyPlay
**Date**: <current date and time>
**Device**: <output of adb shell getprop ro.product.model>
**Android**: <output of adb shell getprop ro.build.version.release>
**App**: com.sls.handbook (debug)

## Results Summary

| Step | Test | Result | Notes |
|------|------|--------|-------|
| 1 | App Launch — Heading Visible | PASS/FAIL | ... |
| 2 | Feature Cards Visible | PASS/FAIL | ... |
| 3 | Scroll to CTA Buttons | PASS/FAIL | ... |
| 4 | Tap "Get Started" | PASS/FAIL | ... |
| 5 | Tap "I already have an account" | PASS/FAIL | ... |
| 6 | Orientation Change | PASS/FAIL | ... |
| 7 | Dark Mode Toggle | PASS/FAIL | ... |
| 8 | Background and Restore | PASS/FAIL | ... |

**Overall**: X/8 passed

## Screenshots
All screenshots saved to `e2e-screenshots/` in the project root:
- step_1_launch_after.png
- step_2_feature_cards_after.png
- ...

## Issues Found
- <list any failures or unexpected behaviors>
```

## Playwright Integration

After the test report is generated, optionally open screenshots in the browser for visual review:

```
Use mcp__playwright__browser_navigate to open file://$E2E_SCREENSHOTS_DIR/step_1_launch_after.png
Use mcp__playwright__browser_take_screenshot to capture annotated views
```

This is also available for WebView testing if the app adds web content later.

## Custom Scenarios

When invoked as `/e2e-test <scenario>`, adapt the test steps to match the described scenario. Use the same Phase 1
setup, Phase 2 primitives, Phase 3 execution pattern (intent → before → act → wait → after → assert → record), and Phase
4 reporting format.

## Important Notes

- Always load `.env` first (`source $PROJECT_ROOT/.env`) before using any path variables
- Clean up temporary files on the device (`/sdcard/screen_*.png`, `/sdcard/ui_dump.xml`) after pulling
- If an emulator is already running and the app is already installed, skip those setup steps
- If a step fails, continue with remaining steps (don't abort the entire test)
- The WelcomeScreen buttons (`onGetStarted`, `onSignIn`) currently have TODO implementations — this is expected and
  should be noted, not flagged as a failure
