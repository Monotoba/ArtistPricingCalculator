# Source-only beta snapshot — v0.1.0-beta.1

**Distribution:** Public source-code snapshot only. **No APK, AAB, or other installable application is included or distributed.** This project is not planned for publication to an app store.

**Status:** Experimental / incomplete. This beta label is a repository milestone, not a claim of production readiness or physical-device compatibility.

## Included

- Modernized Java 17 / Gradle 8.13 / Android Gradle Plugin 8.13.2 project and AndroidX migration.
- Cost-entry field validation, SQLite regression tests, and safeguards against destructive unknown database migrations.
- Decimal retail/wholesale calculations based on user-entered labor, materials, overhead, marketing, packaging, and miscellaneous cost records.
- Android emulator CI on API 26 and API 37, plus downloadable **debug artifacts for internal testing** in GitHub Actions. These debugging artifacts are not official release downloads and are not included in this source beta.
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

To install this source-only beta, download or clone the source, open the project root in **Android Studio**, allow Gradle to sync using JDK 17 and the required Android SDK, select the **app** configuration, then select an emulator or a USB-debugging-enabled Android device and click **Run**. Android Studio builds, installs, and launches a local **debug build**. See the detailed [Android Studio installation steps](../README.md#build-and-install-with-android-studio) and [build validation](BUILD_VALIDATION.md). No precompiled or officially signed installation package is provided. Device compatibility is not guaranteed; CI covers only documented tests.

## Release presentation

Intended GitHub tag: `v0.1.0-beta.1`. On GitHub Releases, select **pre-release** and attach **no APK/AAB**. GitHub's generated source archives are sufficient for this source snapshot. Record the actual tag and source commit at publication time; neither the tag nor a GitHub Release is created by this document.
