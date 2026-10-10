# Beta release checklist

## GitHub beta: source archives and debug APK

The first public milestone is a **GitHub prerelease** containing GitHub-generated ZIP/TAR.GZ source archives and a clearly labeled, experimental **debug APK**. No app-store submission, AAB or production-signed APK is planned. Device testing and artwork/license provenance remain relevant because an executable is being distributed.

- [x] Document both Android Studio self-build and optional debug APK installation.
- [ ] Verify final CI passes on **both API 26 and API 37** for the exact release commit.
- [ ] Verify source-repository and APK-bundled assets may be redistributed; resolve issue #11 or explicitly remove/replace assets of uncertain provenance.
- [ ] Confirm debug APK originates from that same release commit and verify the signed APK, package name, debuggable status and SHA-256 checksum.
- [ ] Test the attached debug APK on a real Android device (issue #13), including first-run, core pricing flow and persisted test data, or clearly record the untested status and the decision to accept that beta risk.
- [ ] Ensure signing-key/upgrades and sensitive-data caveats are prominently documented; a CI debug key can change across workflows.
- [ ] Create tag `v0.1.0-beta.1` from the validated commit and publish a GitHub **pre-release** with source archives and `artist-pricing-calculator-v0.1.0-beta.1-debug.apk` plus SHA-256 checksum.
- [ ] Confirm GitHub Assets, source archive contents, release text, tag and published checksum match the validated commit.

## Later production-signed APK distribution (not planned)

This separate checklist remains for any eventual production-signed distributable and is **not** a claim that store distribution is planned.

## Required checks

- [ ] Latest PR commit: Java unit tests, APK builds, and all Android instrumentation pass on API 26 and API 37; inspect both reports.
- [ ] Real UI workflow: add, edit, delete, navigate categories, and verify Results refresh across activity recreation.
- [ ] Error handling: blank/malformed input, deleted or missing records, empty costs, partial labor data, and invalid multipliers.
- [ ] Verify that existing user records remain intact across ordinary application upgrades. Schema version stays at 1; unsupported schema migrations fail rather than erase user data.
- [ ] Verify install and launch on at least one physical Android device and document the OS version; the CI emulators do not establish physical-device compatibility.
- [ ] Decide minimum and target SDK policy, supported distribution channel, and whether old Android 4.4 devices remain in scope. Target SDK 26 is not represented as current Google Play compatibility.
- [ ] Audit the resolved release runtime dependency graph, notices, and provenance of bundled graphics/icons and documentation assets. Resolve any unlicensed redistributable material.
- [ ] Validate a built, installable APK from the exact release commit with checksums; avoid relying only on a debug CI build.
- [ ] Document example data, per-artwork cost allocation, unsupported automated overhead allocation, price rounding, and print/email placeholders.
- [ ] Choose a prerelease version/tag, update versionCode/versionName appropriately, create a changelog, and mark GitHub release as prerelease.
- [ ] Merge only after the latest commit's required checks pass and document any waived gates with justification.

## Current constraints

This is a historical Android/Java project. Current CI uses API 26 and API 37 emulators, but minSdk remains 19 and targetSdk remains 26. AndroidX migration alone does not establish current Play Store eligibility. Sample costs inserted on initial database creation are illustrative, not real per-artwork estimates. The production database upgrade code explicitly refuses unknown schema migrations to prevent silent data loss; no data-preserving schema version migration is implemented yet.

Publishing an experimental debug APK through GitHub is different from publishing a production-signed APK or submitting an app to Google Play. The release checklist above applies to this experimental distribution.
