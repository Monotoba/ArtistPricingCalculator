#!/usr/bin/env bash
set -euo pipefail

# Build with historical Gradle before installing the modern emulator SDK.
# Execute the already-built APKs with current adb, avoiding old SDK metadata parsing.
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
mkdir -p app/build/reports/instrumentation
report=app/build/reports/instrumentation/result.txt
adb shell am instrument -w -r \
  com.example.randy.artistpricingcalculator.test/android.support.test.runner.AndroidJUnitRunner \
  | tee "$report"

# adb can return success even when instrumentation fails; require the runner's summary.
grep -Eq '^OK \([0-9]+ tests?\)' "$report"
