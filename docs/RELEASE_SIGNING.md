# Reproducible alpha APK signing and installation

**Status:** procedure only; no release key or physical-device validation has been performed.

## Prerequisites

- Java 17, Android SDK platform 36 and build tools 35.0.0.
- A private upload/signing key, stored **outside the repository**. Back it up securely.
- A deliberate alpha versionCode/versionName change before tagging.
- Completed license/artwork audit (issue #11), physical device checks (issue #13), and Android compatibility/signing decisions (issue #15).

## Prepare a signing key

Create a private directory with restrictive permissions, then use the JDK `keytool` tool. Example on Linux:

```sh
umask 077
mkdir -p "$HOME/.local/share/artist-pricing-calculator/signing"
keytool -genkeypair -v \
  -keystore "$HOME/.local/share/artist-pricing-calculator/signing/alpha-release.jks" \
  -alias artist-pricing-alpha -keyalg RSA -keysize 3072 \
  -validity 3650
```

Use strong distinct passwords and retain the alias, passwords, and keystore securely. Never add the keystore, password files, or credentials to GitHub. Losing the key may prevent signed in-place upgrades.

## Build and sign locally

This project does **not** yet define a `release` signingConfig. The ordinary `assembleRelease` task may therefore produce an **unsigned** APK; do not distribute it as installable until it is signed.

```sh
./gradlew --no-daemon clean assembleRelease
# Locate the generated APK; filenames differ with Android Gradle Plugin versions.
find app/build/outputs/apk/release -maxdepth 1 -type f -name '*.apk'
```

Use Android SDK Build Tools `zipalign` **before** `apksigner`. For example, replace the input path below with the unsigned APK produced by Gradle:

```sh
BUILD_TOOLS="$ANDROID_HOME/build-tools/35.0.0"
"$BUILD_TOOLS/zipalign" -f -p 4 \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  app-release-aligned.apk

"$BUILD_TOOLS/apksigner" sign \
  --ks "$HOME/.local/share/artist-pricing-calculator/signing/alpha-release.jks" \
  --ks-key-alias artist-pricing-alpha \
  --out app-release-alpha.apk app-release-aligned.apk

"$BUILD_TOOLS/apksigner" verify --verbose --print-certs app-release-alpha.apk
sha256sum app-release-alpha.apk > app-release-alpha.apk.sha256
```

Do not place secrets on a command line or in a shell history. By omitting password arguments, `apksigner` can prompt for passwords interactively.

## Device acceptance

Verify the generated checksum, install on a physical Android device, and record Android version, device model, signed APK checksum, test steps, and the exact Git commit:

```sh
sha256sum -c app-release-alpha.apk.sha256
adb install -r app-release-alpha.apk
```

Perform the full checks in issue #13. Android rejects an in-place update signed with a different key. The CI debug APK and the release-signed APK are signed with **different keys**; uninstalling the app to switch keys can delete local artwork-cost data. Back up user data before testing a key change, and never treat a forced reinstall as proof of safe upgrades.

## Publishing gate

Publish an explicitly marked GitHub **prerelease** only after signed APK verification, licensing review, physical-device tests, accurate limitations/versioning, and completed CI for the exact release commit. This guide does not assert those checks have passed.
