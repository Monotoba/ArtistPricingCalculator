# Artist Pricing Calculator

![Status: work in progress](https://img.shields.io/badge/status-work%20in%20progress-orange)
![Platform: Android](https://img.shields.io/badge/platform-Android-green)
[![Android validation](https://github.com/Monotoba/ArtistPricingCalculator/actions/workflows/android.yml/badge.svg)](https://github.com/Monotoba/ArtistPricingCalculator/actions/workflows/android.yml)
![Language: Java](https://img.shields.io/badge/language-Java-blue)
[![License: BSD-2-Clause](https://img.shields.io/badge/license-BSD--2--Clause-blue)](LICENSE)

An unfinished Android app intended to help artists organize labor, materials, overhead, marketing, and packaging costs when estimating artwork prices.

The Results screen calculates retail and wholesale estimates from stored costs and a custom multiplier. The app remains a work in progress: complete artist workflows, data migrations, and release readiness have not yet been established.

## What is in the repository?

- Java activities and Android layouts for cost categories, settings, help, and results.
- SQLite item storage and sample cost records.
- Historical design assets in [docs](docs).
- A Gradle wrapper and Android Studio project.
- Ten SQLite regression tests, two database-isolation checks, and a home-screen launch test, plus the original generated example tests. Twelve pure pricing tests and five Results/help UI tests cover calculations, invalid input, database integration, and activity recreation. Complete UI workflows remain unverified.

## Build status

The build uses Android Gradle Plugin 8.13.2, Gradle 8.13, Java 17, and compile SDK 36. Dependency resolution uses Google Maven and Maven Central. The Android Support Library is retained for a separate AndroidX migration. Minimum SDK 19 and target SDK 26 remain unchanged; minimum-version support and Play Store eligibility are not established by these settings.

GitHub Actions builds with Java 17 and runs database, home-screen, and Results/help tests on Android 8.0 (API 26) and Android 17 (API 37) emulators. It also validates the Gradle wrapper and launcher failure handling. Passing this workflow establishes only the behavior covered by these tests; complete workflows, physical-device compatibility, and release readiness remain unverified. See [build and validation notes](docs/BUILD_VALIDATION.md).

For development, clone this repository and use Java 17 with SDK platform 36 and build tools 35.0.0, as described in the validation notes. No release APK is available yet. Do not interpret the internal `versionName "1.0"` as a published or complete release.

## Help move it forward

Read the [roadmap](docs/ROADMAP.md) and [contribution guide](CONTRIBUTING.md). Useful contributions include assessing current Android compatibility, expanding UI and database tests, reviewing cost allocations, and validating complete artist workflows.

## Pricing behavior

Enter all non-labor amounts as costs allocated to **one artwork**, using one currency. The app does not automatically divide monthly or annual overhead or marketing costs. Materials, packaging, and miscellaneous records are added directly.

Labor requires exactly one `Hourly Rate` and one `Hours` record (names ignore case and surrounding whitespace). Fractional hours are supported. Omitting both gives zero labor; incomplete, duplicate, or unrecognized labor records show an error.

`total cost = overhead + marketing + hourly rate × hours + materials + packaging + miscellaneous`

`retail = total cost × custom multiplier`

`wholesale = unrounded retail / 2`

The multiplier must be at least 1; the initial value of 3 is an editable starting value, not a recommended margin. Use a decimal point. Each price is independently rounded to two decimals using half-up rounding; currencies with other minor-unit precision are not modeled. Decimal arithmetic avoids additional floating-point accumulation, but existing database records are still read as doubles and their original precision cannot be recovered.

Replace the automatically seeded example records before using the estimates. An empty cost list gives zero. Taxes, commissions, shipping, and profitability are not automatically modeled; allocate applicable costs explicitly. The multiplier survives screen recreation but resets to 3 for a new Results screen. Print/email remain placeholders.

Report reproducible problems or suggest a concrete artist workflow through [GitHub issues](https://github.com/Monotoba/ArtistPricingCalculator/issues).

## License

Original project code and documentation are licensed under [BSD-2-Clause](LICENSE). Dependencies and third-party components retain their own licenses. See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
