---
description: Record user interactions as a replayable E2E test on a connected Android device
allowed-tools: Bash, Read, Write, Glob, Grep, AskUserQuestion
---

# Record E2E Test Skill

You are an E2E test recording agent for the HandyPlay Android app. You launch the app in recording mode
with a floating red stop button, wait for the user to interact, then save the recorded actions as a
replayable test.

## Invocation

- `/record-e2e` — start a new recording session (will ask for test name after recording)
- `/record-e2e <test-name>` — start recording and save with the given name

## Phase 1: Pre-flight Checks

### 1a. Detect ADB

```bash
ADB=$(which adb 2>/dev/null || echo "$HOME/Library/Android/sdk/platform-tools/adb")
$ADB version
```

If ADB is not found, inform the user and stop.

### 1b. Verify device connected

```bash
$ADB devices | grep -w "device"
```

If no device/emulator is connected, inform the user and stop.

### 1c. Build the debug APK

The debug APK includes the RecordingActivity overlay. Build it:

```bash
cd <PROJECT_ROOT> && ./gradlew assembleDebug
```

If the build fails, report the error and stop.

### 1d. Install the APK

```bash
$ADB install -r <PROJECT_ROOT>/app/build/outputs/apk/debug/app-debug.apk
```

### 1e. Clear app data for a clean state

```bash
$ADB shell pm clear com.sls.handbook
```

## Phase 2: Start Recording

### 2a. Launch RecordingActivity

```bash
$ADB shell am start -a com.sls.handbook.E2E_RECORD
```

### 2b. Inform the user

Tell the user:

> Recording has started. You should see a floating red stop button on the app.
>
> - Interact with the app normally — all taps and swipes are being recorded
> - The red button is draggable — move it if it blocks important UI elements
> - When you're done, tap the red stop button to finish recording
>
> I'm monitoring for the recording to complete...

### 2c. Wait for completion

Poll the device for the recording file. The file is written when the user taps the stop button.
Use a polling loop instead of logcat (which has shell piping issues):

```bash
# Poll every 2 seconds for up to 10 minutes
for i in $(seq 1 300); do
    if $ADB shell run-as com.sls.handbook ls files/e2e_recording.json 2>/dev/null | grep -q e2e_recording; then
        echo "Recording complete!"
        break
    fi
    sleep 2
done
```

Alternatively, after informing the user, simply ask them to confirm when they've tapped the
stop button using the AskUserQuestion tool. Then proceed to pull the file.

## Phase 3: Retrieve Recording

### 3a. Pull the recording JSON

```bash
$ADB shell run-as com.sls.handbook cat files/e2e_recording.json > /tmp/e2e_recording.json
```

### 3b. Read and validate

Use the Read tool to read `/tmp/e2e_recording.json`. Verify it contains valid JSON with `metadata`
and `events` arrays.

### 3c. Check for empty recording

If the events array is empty, inform the user that no interactions were captured and ask if they
want to try again.

## Phase 4: Save Test

### 4a. Get test name

If no test name was provided in the invocation, ask the user:

> What would you like to name this test? (use lowercase with hyphens, e.g. `welcome-flow`,
> `browse-categories`)

### 4b. Create test directory

```bash
mkdir -p <PROJECT_ROOT>/e2e-tests/<test-name>/screenshots
```

### 4c. Save test.json

Copy the recording JSON to the test directory:

```bash
cp /tmp/e2e_recording.json <PROJECT_ROOT>/e2e-tests/<test-name>/test.json
```

### 4d. Generate run.sh

Create a bash replay script from the recorded events. Read the `test.json` to extract events
and generate the script.

The `run.sh` script should follow this structure:

```bash
#!/bin/bash
# E2E Test: <test-name>
# Recorded: <date from metadata>
# Device: <deviceModel> (<screenWidth>x<screenHeight>)
set -e

ADB=${ADB:-adb}
PACKAGE="com.sls.handbook"

echo "Running E2E test: <test-name>"

# Clear app data and start fresh
$ADB shell pm clear $PACKAGE
$ADB shell am start -n $PACKAGE/.MainActivity
sleep 3

# Step 1: TAP at (<x>, <y>) — delay <N>ms from start
sleep <delay_seconds>
$ADB shell input tap <x_rounded> <y_rounded>

# Step 2: SWIPE from (<startX>,<startY>) to (<endX>,<endY>) over <duration>ms
sleep <delay_seconds>
$ADB shell input swipe <startX> <startY> <endX> <endY> <durationMs>

# Step N: BACK_PRESS
sleep <delay_seconds>
$ADB shell input keyevent KEYCODE_BACK

# Step M: TEXT_INPUT "<text>"
sleep <delay_seconds>
$ADB shell input text "<text>"

echo "Test '<test-name>' completed successfully"
```

**Delay calculation:**
- For the first event: use its `timestampMs` converted to seconds (minimum 0.5s)
- For subsequent events: use the difference between consecutive `timestampMs` values (minimum 0.5s)
- Round coordinates to integers for ADB `input` commands

Make the script executable:

```bash
chmod +x <PROJECT_ROOT>/e2e-tests/<test-name>/run.sh
```

## Phase 5: Report

Present a summary to the user:

```markdown
## Recording Complete: <test-name>

**Events captured:** <N>
**Duration:** <last_event_timestampMs / 1000>s
**Device:** <deviceModel>
**Resolution:** <screenWidth>x<screenHeight>

### Actions recorded:
1. TAP at (540, 1200)
2. SWIPE from (540,1800) to (540,600) over 300ms
3. BACK_PRESS
...

### Files saved:
- `e2e-tests/<test-name>/test.json` — raw recording data
- `e2e-tests/<test-name>/run.sh` — replay script

### To replay this test:
```
/run-e2e <test-name>
```
Or manually: `./e2e-tests/<test-name>/run.sh`
```

## Important Notes

- The recording captures coordinates in physical pixels. Replay only works reliably on a
  device/emulator with the same screen resolution.
- Text input is NOT automatically captured by the overlay. If the user typed text into a field,
  they should note this. The skill can add `TEXT_INPUT` events manually to the test.json.
- The red stop button's position is excluded from the recording — taps on it won't appear in
  the test.
- Orientation is locked to portrait during recording for coordinate consistency.
- The debug APK must be used — release builds don't include the RecordingActivity.
