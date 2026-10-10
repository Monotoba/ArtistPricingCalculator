# Build and runtime compatibility validation

The app is built with a modernized Gradle toolchain while retaining its original minimum and target SDK settings. CI runs the same APK and instrumented tests on the original Android generation and a current Android emulator. Coverage includes database behavior, home-screen launch, pricing arithmetic, and selected Results/help interactions; it does not establish complete device compatibility or Play Store readiness.

## Environment

- Java 17 for Gradle 8.13, Android Gradle Plugin 8.13.2, and SDK management.
- Android SDK platform 36 and build tools 35.0.0 for compilation.
- Android 8.0 / API 26 x86_64 emulator (`default` image).
- Android 17 / API 37 x86_64 emulator (`google_apis` image; SDK package suffix `37.0`).
- Original Android Support Library dependencies; AndroidX migration remains separate work.

The [Android validation workflow](../.github/workflows/android.yml) installs these components, validates the wrapper JAR, and uploads test reports. The Gradle distribution is pinned by SHA-256. CI runs twelve pure pricing unit tests plus the original generated local unit test. CI builds both APKs, then installs and runs them through current `adb` using [the instrumentation script](../.github/scripts/run-instrumented-tests.sh). The script requires a successful, nonempty instrumentation test summary because `adb` alone may return success when tests fail.

With the SDK installed and Java 17 selected:

```sh
./gradlew --no-daemon assembleDebug assembleDebugAndroidTest testDebugUnitTest
./gradlew --no-daemon connectedDebugAndroidTest
```

The second command requires a running emulator or attached device. The workflow matrix runs the suite separately on API 26 and API 37. The database tests use a prefixed test database in the application's writable context. The launch test starts the real `MainActivity`, checks its title and visible clickable Start control, and closes it; it does not exercise subsequent navigation. Five additional instrumented tests exercise Results/help screens, including live SQLite costs, custom multipliers, invalid input, empty data, and multiplier restoration after activity recreation. These tests snapshot and restore the application database rows, including IDs, within the test installation.

## Regression scope

Six SDK-independent CI launcher tests cover an emulator available only at its SDK path, shared AVD registration with an explicit directory or Android user-home fallback, a missing executable, early emulator exit, unavailable framework services, and failed instrumentation. Android 17 startup additionally waits for input and package services and retains emulator/system diagnostics when startup fails.

Ten database tests cover counts, empty reads, Unicode and quotes, persistence flags, single and bulk insertion, empty bulk input, updates, deletion, missing IDs, repeated reads, reopening, transaction rollback, and invalid item categories.

Two additional fixture tests verify that SQLiteOpenHelper opens the prefixed test database instead of the application's database, and that direct database creation/deletion uses the same prefixed path. The test-only `ContextWrapper` replaces the obsolete `android.test.RenamingDelegatingContext`; application database code is unchanged.

Bulk insertion is atomic: an invalid row rolls back earlier rows. Unsupported count/update/insert types raise `IllegalArgumentException`; unsupported list queries retain their existing `null` result. Missing-ID lookup returns `null`. Callers must handle missing records before using them; comprehensive activity error handling remains future work.

The pre-fix baseline reproduced seven failing database tests on Android 26. The historical build subsequently passed all 12 instrumented tests on API 26 and API 37. The modernized build passed 14 instrumented tests on both versions. The pricing implementation adds 12 unit tests and five instrumented tests (19 instrumented tests total). Local execution in the cleanup environment is blocked by unavailable Gradle downloads and absence of an Android SDK, so executable application validation takes place in GitHub Actions.

Application ID, minimum SDK 19, target SDK 26, and internal version values remain unchanged. The namespace moves from the manifest to Gradle as required by AGP 8. Resource IDs remain final and transitive to preserve existing Java switch statements and resource references. Compilation targets Java 8 bytecode; running the build requires Java 17.

## Remaining release blockers

- Database upgrades still recreate the table and require a data-preserving migration design.
- Full UI workflows and failure handling are not validated.
- AndroidX/support-library migration and updating the target SDK require separate compatibility work. Raising the compile SDK alone does not change the target SDK or establish Play Store eligibility.
- API 19 minimum support, physical devices, other screen sizes, and complete workflows on modern Android have not been tested.
- Binary dependency and asset licensing review remains incomplete.
