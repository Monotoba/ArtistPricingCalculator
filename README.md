# Artist Pricing Calculator

![Status: work in progress](https://img.shields.io/badge/status-work%20in%20progress-orange)
![Platform: Android](https://img.shields.io/badge/platform-Android-green)
[![Android validation](https://github.com/Monotoba/ArtistPricingCalculator/actions/workflows/android.yml/badge.svg)](https://github.com/Monotoba/ArtistPricingCalculator/actions/workflows/android.yml)
![Language: Java](https://img.shields.io/badge/language-Java-blue)
[![License: BSD-2-Clause](https://img.shields.io/badge/license-BSD--2--Clause-blue)](LICENSE)

An unfinished Android app intended to help artists organize labor, materials, overhead, marketing, and packaging costs when estimating artwork prices.

**The pricing calculation is not implemented.** The results screen calls an empty `doCalc()` method. Existing screens and database code are a development starting point; the app is not ready for pricing decisions or an application release.

## What is in the repository?

- Java activities and Android layouts for cost categories, settings, help, and results.
- SQLite item storage and sample cost records.
- Historical design assets in [docs](docs).
- A Gradle wrapper and Android Studio project.
- Ten SQLite regression tests and a home-screen launch test, plus the original generated example tests. Pricing and complete UI behavior are not yet tested.

## Build status

The build uses Android Gradle Plugin 8.13.2, Gradle 8.13, Java 17, and compile SDK 36. Dependency resolution uses Google Maven and Maven Central. The Android Support Library is retained for a separate AndroidX migration. Minimum SDK 19 and target SDK 26 remain unchanged; minimum-version support and Play Store eligibility are not established by these settings.

GitHub Actions builds with Java 17 and runs database and home-screen launch tests on Android 8.0 (API 26) and Android 17 (API 37) emulators. It also validates the Gradle wrapper and launcher failure handling. Passing this workflow establishes only the behavior covered by these tests; complete workflows, physical-device compatibility, and release readiness remain unverified. See [build and validation notes](docs/BUILD_VALIDATION.md).

For development, clone this repository and use Java 17 with SDK platform 36 and build tools 35.0.0, as described in the validation notes. No release APK is available yet. Do not interpret the internal `versionName "1.0"` as a published or complete release.

## Help move it forward

Read the [roadmap](docs/ROADMAP.md) and [contribution guide](CONTRIBUTING.md). Useful contributions include assessing current Android compatibility, expanding UI and database tests, defining the cost units, and implementing the pricing calculation with tests.

The formula in the source comments is a proposal. Allocation of recurring costs, labor hours, markup, and rounding must be specified before it becomes reliable behavior.

Report reproducible problems or suggest a concrete artist workflow through [GitHub issues](https://github.com/Monotoba/ArtistPricingCalculator/issues).

## License

Original project code and documentation are licensed under [BSD-2-Clause](LICENSE). Dependencies and third-party components retain their own licenses. See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
