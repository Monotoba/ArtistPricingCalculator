# Contributing

This is an unfinished Android/Java application. Review the [README](README.md) and [roadmap](docs/ROADMAP.md) before starting.

Keep pull requests focused. Prefer refactoring existing activities and helpers to a rewrite, and preserve unrelated behavior. Include tests for every code change, including invalid inputs and failure cases.

## Current priorities

1. Establish a reproducible build and record the exact environment.
2. Add regression tests for item storage, retrieval, updates, deletion, and resource handling.
3. Define pricing inputs and units, then test and implement the calculation.
4. Verify the UI workflow and document known limitations before a release.

For a build report, include Java, Gradle, Android plugin, SDK, and operating-system versions, the command used, and the relevant error output. Remove credentials and personal data.

Pricing changes need worked examples and explicit assumptions about labor, recurring costs, markup, currency precision, and rounding. Extract testable logic from the existing code where practical.

Commit changes locally with a clear message. In the pull request, explain the problem, resulting behavior, tests performed, and any remaining limitations. Do not claim tests or platforms passed without running the relevant checks.

Original contributions use the repository's [BSD-2-Clause license](LICENSE). Preserve third-party notices and identify the source and license of any added code or assets.
