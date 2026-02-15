#!/bin/bash
# E2E Test: ttl-cache-demo
# Recorded: 2026-02-15T10:18:54Z
# Device: sdk_gphone64_arm64 (1280x2856)
set -e

ADB=${ADB:-adb}
PACKAGE="com.sls.handbook"

echo "Running E2E test: ttl-cache-demo"

# Clear app data and start fresh
$ADB shell pm clear $PACKAGE
$ADB shell am start -n $PACKAGE/.MainActivity
sleep 3

# Step 1: TAP at (627, 772) — 4.6s from start
sleep 4.6
$ADB shell input tap 627 772

# Step 2: TAP at (385, 472) — 3.9s after step 1
sleep 3.9
$ADB shell input tap 385 472

# Step 3: TAP at (416, 1764) — 3.4s after step 2
sleep 3.4
$ADB shell input tap 416 1764

# Step 4: TAP at (672, 1058) — 3.2s after step 3
sleep 3.2
$ADB shell input tap 672 1058

# Step 5: SWIPE from (133,807) to (146,820) over 1693ms — 8.1s after step 4
sleep 8.1
$ADB shell input swipe 133 807 146 820 1693

# Step 6: TAP at (177, 834) — 0.5s after step 5
sleep 0.5
$ADB shell input tap 177 834

# Step 7: TAP at (177, 834) — 0.5s after step 6
sleep 0.5
$ADB shell input tap 177 834

# Step 8: TAP at (177, 834) — 0.5s after step 7
sleep 0.5
$ADB shell input tap 177 834

# Step 9: TAP at (769, 468) — 0.9s after step 8
sleep 0.9
$ADB shell input tap 769 468

# Step 10: TAP at (702, 1195) — 4.9s after step 9
sleep 4.9
$ADB shell input tap 702 1195

# Step 11: TAP at (649, 1133) — 3.3s after step 10
sleep 3.3
$ADB shell input tap 649 1133

# Step 12: TAP at (689, 1199) — 2.3s after step 11
sleep 2.3
$ADB shell input tap 689 1199

# Step 13: TAP at (689, 1199) — 1.5s after step 12
sleep 1.5
$ADB shell input tap 689 1199

echo "Test 'ttl-cache-demo' completed successfully"
