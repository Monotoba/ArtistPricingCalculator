# Alpha release checklist

This checklist is a release gate, not an assertion that the app is ready.

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

Publishing an alpha is distinct from publishing to Google Play. An alpha GitHub release should still be clearly labeled, tested, and accompanied by a usable artifact or installation instructions.
