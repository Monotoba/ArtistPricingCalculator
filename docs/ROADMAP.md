# Roadmap to an initial alpha

Status: work in progress. Modern build validation, database regression coverage, limited Android 8/17 runtime checks, and pricing calculations have been added; complete workflows and release readiness remain unfinished. No delivery date is committed.

## 1. Reproduce the build

Establish a working Java/Gradle/Android SDK environment. Evaluate repository and dependency availability, then make the smallest necessary build changes. Record a repeatable build command and add CI once it succeeds. Validate any toolchain migration with tests.

## 2. Stabilize item storage

Add regression tests before fixing database behavior. Review cursor lifetime, missing records, invalid item types, boolean persistence, and bulk insertion. The helper now uses bound inserts in a transaction, closes read cursors, handles missing IDs, and preserves persistence flags. Ten regression tests cover these paths, including rollback and invalid categories. Expand coverage as the application develops.

Review destructive database upgrade behavior before supporting existing user data.

Acceptance: CRUD and bulk insertion work with empty data, Unicode names, quotes, and invalid input; resources are closed correctly and migrations preserve intended data.

## 3. Specify and implement pricing

Implemented with a pure decimal calculator and the existing Results activity. Stored costs are allocated to one artwork; labor uses exactly one Hourly Rate and one Hours record. Retail uses a custom multiplier of at least 1 (initial value 3); wholesale is half unrounded retail. Prices round independently to two decimal places, half up. Miscellaneous records are included as per-artwork costs.

Twelve unit tests cover worked examples, all categories, zero costs, fractional hours, custom multipliers, decimal arithmetic, invalid records, and rounding. Five instrumented tests cover SQLite-to-results integration, invalid input clearing stale prices, missing labor, empty costs, multiplier restoration after recreation, and help launch.

Allocation remains manual: the app does not automatically convert recurring expenses into per-artwork costs. Existing double-valued records retain their precision limits. Full workflow verification remains the next release gate.

## 4. Verify the artist workflow

Exercise adding, editing, and deleting costs; settings; navigation; rotation; persistence; and results on an emulator or device. Add meaningful instrumented tests and a manual checklist. Publish screenshots only from a running app, with sample data.

## 5. Alpha release gate

Require a reproducible build, passing meaningful tests and CI, documented setup, verified main workflow, dependency/license review, and explicit known limitations. Choose an alpha version based on actual readiness; the existing internal 1.0 label does not establish release readiness.
