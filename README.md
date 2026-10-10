# Artist Pricing Calculator

![Status: work in progress](https://img.shields.io/badge/status-work%20in%20progress-orange)
![Platform: Android](https://img.shields.io/badge/platform-Android-green)
![Language: Java](https://img.shields.io/badge/language-Java-blue)
[![License: BSD-2-Clause](https://img.shields.io/badge/license-BSD--2--Clause-blue)](LICENSE)

An unfinished Android app intended to help artists organize labor, materials, overhead, marketing, and packaging costs when estimating artwork prices.

**The pricing calculation is not implemented.** The results screen calls an empty `doCalc()` method. Existing screens and database code are a development starting point; the app is not ready for pricing decisions or an application release.

## What is in the repository?

- Java activities and Android layouts for cost categories, settings, help, and results.
- SQLite item storage and sample cost records.
- Historical design assets in [docs](docs).
- A Gradle wrapper and Android Studio project.
- Generated example tests, which do not verify pricing or database behavior.

## Build status

The checked-in configuration uses Android Gradle Plugin 3.0.1, Gradle 4.1, Android SDK 26, and the Android Support Library. Its minimum SDK is 19 and target SDK is 26. These are historical configuration values, not verified compatibility claims.

A current build, emulator run, and meaningful application tests have not yet been validated. The project also references JCenter. Build-tool and dependency compatibility must be established before adding CI or a passing build badge.

For development, clone this repository and open it in Android Studio. Expect compatibility work; no tested setup instructions or downloadable APK are available yet. Do not interpret the internal `versionName "1.0"` as a published or complete release.

## Help move it forward

Read the [roadmap](docs/ROADMAP.md) and [contribution guide](CONTRIBUTING.md). Useful contributions include reproducing the historical build, adding database regression tests, defining the cost units, and implementing the pricing calculation with tests.

The formula in the source comments is a proposal. Allocation of recurring costs, labor hours, markup, and rounding must be specified before it becomes reliable behavior.

Report reproducible problems or suggest a concrete artist workflow through [GitHub issues](https://github.com/Monotoba/ArtistPricingCalculator/issues).

## License

Original project code and documentation are licensed under [BSD-2-Clause](LICENSE). Dependencies and third-party components retain their own licenses. See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
