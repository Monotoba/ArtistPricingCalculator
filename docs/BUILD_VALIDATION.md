# Build and runtime compatibility validation

The app is built with its historical toolchain. CI runs the same APK and instrumented tests on the original Android generation and a current Android emulator. Coverage is limited to database behavior and home-screen launch; it does not establish complete device compatibility or Play Store readiness.

## Environment

- Java 8 for Gradle 4.1 and Android Gradle Plugin 3.0.1.
- Android SDK platform 26 and build tools 26.0.2.
- Android 8.0 / API 26 x86_64 emulator (`default` image).
- Android 17 / API 37 x86_64 emulator (`google_apis` image; SDK package suffix `37.0`).
- Java 17 for current SDK-management tools, separate from the Gradle build.

The [Android validation workflow](../.github/workflows/android.yml) installs these components and uploads test reports. It also runs the original generated local unit test; that test is not pricing coverage.

With the SDK installed and Java 8 selected:

```sh
./gradlew --no-daemon assembleDebug assembleDebugAndroidTest testDebugUnitTest
./gradlew --no-daemon connectedDebugAndroidTest
```

The second command requires a running emulator or attached device. The workflow matrix runs the suite separately on API 26 and API 37. The database tests use a prefixed test database in the application's writable context. The launch test starts the real `MainActivity`, checks its title and visible clickable Start control, and closes it; it does not exercise subsequent navigation or pricing.

## Regression scope

Ten database tests cover counts, empty reads, Unicode and quotes, persistence flags, single and bulk insertion, empty bulk input, updates, deletion, missing IDs, repeated reads, reopening, transaction rollback, and invalid item categories.

Bulk insertion is atomic: an invalid row rolls back earlier rows. Unsupported count/update/insert types raise `IllegalArgumentException`; unsupported list queries retain their existing `null` result. Missing-ID lookup returns `null`. Callers must handle missing records before using them; comprehensive activity error handling remains future work.

The pre-fix baseline reproduced seven failing database tests on Android 26. Local execution in the cleanup environment was blocked by unavailable Gradle downloads and absence of an Android SDK, so executable validation takes place in GitHub Actions.

## Remaining release blockers

- The pricing calculation is empty.
- Database upgrades still recreate the table and require a data-preserving migration design.
- Full UI workflows and failure handling are not validated.
- The old support libraries, SDK targets, Gradle stack, and dependency repositories require a separate modernization assessment.
- API 19 minimum support, physical devices, other screen sizes, and complete workflows on modern Android have not been tested.
- Binary dependency and asset licensing review remains incomplete.
