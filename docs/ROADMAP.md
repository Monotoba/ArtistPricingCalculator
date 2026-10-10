# Roadmap to an initial alpha

Status: work in progress. No delivery date is committed.

## 1. Reproduce the build

Establish a working Java/Gradle/Android SDK environment. Evaluate repository and dependency availability, then make the smallest necessary build changes. Record a repeatable build command and add CI once it succeeds. Validate any toolchain migration with tests.

## 2. Stabilize item storage

Add regression tests before fixing database behavior. Review cursor lifetime, missing records, invalid item types, boolean persistence, and bulk insertion. The current helper includes `substring(0, -1)` in bulk insertion and `Integer.parseInt(null)` for invalid types; these paths need tests and corrections.

Review destructive database upgrade behavior before supporting existing user data.

Acceptance: CRUD and bulk insertion work with empty data, Unicode names, quotes, and invalid input; resources are closed correctly and migrations preserve intended data.

## 3. Specify and implement pricing

The source comment proposes:

`retail = (overhead + marketing + laborRate * hours + materials + packaging) * markup`

`wholesale = retail / 2`

The comment suggests markup of 3 or 4. This is an unimplemented proposal, not a universally appropriate pricing rule. Define per-artwork allocation of recurring costs, valid inputs, markup selection, currency precision, and rounding before implementation.

Acceptance: pure calculation tests cover worked examples, zero costs, fractional hours, invalid inputs, and rounding; the results screen uses tested logic and displays its assumptions.

## 4. Verify the artist workflow

Exercise adding, editing, and deleting costs; settings; navigation; rotation; persistence; and results on an emulator or device. Add meaningful instrumented tests and a manual checklist. Publish screenshots only from a running app, with sample data.

## 5. Alpha release gate

Require a reproducible build, passing meaningful tests and CI, documented setup, verified main workflow, dependency/license review, and explicit known limitations. Choose an alpha version based on actual readiness; the existing internal 1.0 label does not establish release readiness.
