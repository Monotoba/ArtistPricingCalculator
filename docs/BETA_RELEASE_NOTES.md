# Beta release — v0.1.0-beta.1

**Distribution:** GitHub prerelease containing automatic source-code archives **and one separately attached debug APK**. No app-store submission is planned. The APK is a development/debug build, not a production-signed release.

**Status:** Experimental / incomplete. The debug APK exposes debugging facilities and must not be treated as a production package. This beta label is a repository milestone, not a claim of production readiness or physical-device compatibility.

## Included

- Modernized Java 17 / Gradle 8.13 / Android Gradle Plugin 8.13.2 project and AndroidX migration.
- Cost-entry field validation, SQLite regression tests, and safeguards against destructive unknown database migrations.
- Decimal retail/wholesale calculations based on user-entered labor, materials, overhead, marketing, packaging, and miscellaneous cost records.
- Android emulator CI on API 26 and API 37, and debug APK artifacts from GitHub Actions. The prerelease debug APK must be taken from a passing build of the same tagged commit, verified, and explicitly attached as a release asset.
- Build, pricing-behavior, signing, and dependency-audit documentation.

## Known limitations

- Cost inputs must be allocated manually per artwork; recurring overhead is not automatically apportioned.
- The database initially contains illustrative example values that must be replaced before meaningful use.
- Labor records must follow the documented `Hourly Rate` / `Hours` structure.
- Print and email functions remain placeholders.
- Min SDK 19 and target SDK 26 are historical configuration choices, not promises of compatibility with particular devices; no Play Store release is intended.
- No physical-device certification or end-to-end manual test claim is made. Unknown schema migrations fail safely rather than deleting records; migration support must be implemented before increasing schema version.
- The historical Android `versionName "1.0"` in the build configuration does not identify this source-only beta milestone.

## Source and licensing

The original project code is available under BSD-2-Clause. Dependencies and any third-party assets retain their own licenses. Review [third-party notices](../THIRD_PARTY_NOTICES.md), the [dependency audit](DEPENDENCY_AUDIT.md), and [Issue #11](https://github.com/Monotoba/ArtistPricingCalculator/issues/11) for outstanding provenance verification. **A source-only GitHub snapshot remains a distribution of source files**, so unresolved artwork rights must not be represented as cleared by the absence of an APK.

## Build and test

**Option 1 — Build from source:** Download or clone the source, open the project root in **Android Studio**, sync Gradle using JDK 17 and the required Android SDK, select the **app** configuration and a device or emulator, then click **Run**. See [Android Studio installation steps](../README.md#build-and-install-with-android-studio).

**Option 2 — Install debug APK:** Download the clearly labeled `artist-pricing-calculator-v0.1.0-beta.1-debug.apk` from this release's **Assets** (once published). Verify its published SHA-256 checksum, then install with `adb install artist-pricing-calculator-v0.1.0-beta.1-debug.apk` on a device with USB debugging enabled, or use Android's package installer if permitted. This is an experimental debug build; Android may warn about sideloading. Do not use it for sensitive or irreplaceable data. APK signing-key changes may prevent in-place updates and uninstalling can remove saved costs.

No official production-signed installer is provided. Device compatibility is not guaranteed; CI covers only documented tests.

## Release presentation

Intended GitHub tag: `v0.1.0-beta.1`. On GitHub Releases, select **pre-release**. Include GitHub's automatic source-code ZIP/TAR.GZ archives, attach one debug APK built from the tagged commit, and provide its SHA-256 checksum. Do not attach an AAB or claim production signing. Record the actual tag and source commit at publication time; neither the tag nor a GitHub Release is created by this document.
