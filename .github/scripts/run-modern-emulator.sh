#!/usr/bin/env bash
set -euo pipefail

report_dir=app/build/reports/emulator
mkdir -p "$report_dir"
sdkmanager 'emulator' 'system-images;android-37.0;google_apis;x86_64'
echo no | avdmanager create avd --force --name modern \
  --package 'system-images;android-37.0;google_apis;x86_64'
config="${ANDROID_AVD_HOME:-$HOME/.android/avd}/modern.avd/config.ini"
cat >> "$config" <<'CONFIG'
hw.ramSize=4096
vm.heapSize=576
hw.cpu.ncore=2
hw.lcd.width=1080
hw.lcd.height=1920
hw.lcd.density=420
CONFIG

adb start-server
emulator -avd modern -port 5554 -no-window -gpu software \
  -no-snapshot -noaudio -no-boot-anim > "$report_dir/emulator.log" 2>&1 &
emulator_pid=$!
cleanup() {
  timeout 15s adb -s emulator-5554 logcat -d > "$report_dir/logcat.txt" 2>&1 || true
  timeout 10s adb -s emulator-5554 emu kill || true
  kill "$emulator_pid" 2>/dev/null || true
}
trap cleanup EXIT

# Boot-completed can precede availability of framework services on newer images.
# Require input and package services before unlocking or installing APKs.
deadline=$((SECONDS + 600))
ready=false
while (( SECONDS < deadline )); do
  if [[ $(timeout 10s adb -s emulator-5554 shell getprop sys.boot_completed 2>/dev/null | tr -d '\r') == 1 ]] \
      && timeout 10s adb -s emulator-5554 shell service check input 2>/dev/null | grep -q '^Service input: found' \
      && timeout 10s adb -s emulator-5554 shell service check package 2>/dev/null | grep -q '^Service package: found'; then
    ready=true
    break
  fi
  sleep 2
done
if [[ "$ready" != true ]]; then
  echo 'Android 17 framework services did not become ready within 600 seconds.' >&2
  exit 1
fi
adb -s emulator-5554 shell input keyevent 82
adb -s emulator-5554 shell settings put global window_animation_scale 0
adb -s emulator-5554 shell settings put global transition_animation_scale 0
adb -s emulator-5554 shell settings put global animator_duration_scale 0
bash .github/scripts/run-instrumented-tests.sh
