"""Exercise CI launcher failure handling without downloading an Android SDK."""
import os
from pathlib import Path
import subprocess
import tempfile
import unittest


SCRIPTS = Path(__file__).resolve().parent


class EmulatorScriptsTest(unittest.TestCase):
    def run_launcher(self, mode="success", emulator_exists=True, explicit_avd_home=True):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            sdk = root / "sdk/emulator"
            sdk.mkdir(parents=True)
            commands = {
                root / "sdkmanager": "exit 0",
                root / "avdmanager": '''cat >/dev/null
while (( $# )); do
  if [[ "$1" == --path ]]; then
    mkdir -p "$2"
    touch "$2/config.ini"
    touch "$ANDROID_AVD_HOME/modern.ini"
    exit 0
  fi
  shift
done
echo 'Explicit AVD path is required' >&2
exit 1''',
                root / "adb": '''case "$*" in
*"getprop sys.boot_completed"*)
  if [[ "$TEST_MODE" == early-exit ]]; then echo 0; else echo 1; fi ;;
*"service check input"*)
  if [[ "$TEST_MODE" == missing-service ]]; then
    echo 'Service input: not found'
  else echo 'Service input: found'; fi ;;
*"service check package"*) echo 'Service package: found' ;;
*"shell am instrument"*)
  if [[ "$TEST_MODE" == failed-test ]]; then echo 'FAILURES!!!'
  else echo 'OK (12 tests)'; fi ;;
*"logcat -d"*) echo 'Diagnostics captured' ;;
esac''',
            }
            if emulator_exists:
                commands[sdk / "emulator"] = (
                    "exit 1" if mode == "early-exit" else
                    '[[ -f "$ANDROID_AVD_HOME/modern.ini" ]] || exit 1; exec sleep 30'
                )
            for path, body in commands.items():
                path.write_text("#!/bin/bash\n" + body + "\n")
                path.chmod(0o755)
            scripts = root / ".github/scripts"
            scripts.mkdir(parents=True)
            launcher = (SCRIPTS / "run-modern-emulator.sh").read_text()
            launcher = launcher.replace("SECONDS + 600", "SECONDS + 2")
            launcher = launcher.replace("sleep 2", "sleep 0.1")
            (scripts / "run-modern-emulator.sh").write_text(launcher)
            (scripts / "run-instrumented-tests.sh").write_text(
                (SCRIPTS / "run-instrumented-tests.sh").read_text()
            )
            env = dict(os.environ, PATH=f"{root}:/usr/bin:/bin",
                       ANDROID_HOME=str(root / "sdk"),
                       ANDROID_USER_HOME=str(root / "android-user"), TEST_MODE=mode)
            env.pop("ANDROID_AVD_HOME", None)
            if explicit_avd_home:
                env["ANDROID_AVD_HOME"] = str(root / "avds")
            result = subprocess.run(
                ["bash", str(scripts / "run-modern-emulator.sh")],
                cwd=root, env=env, capture_output=True, text=True, timeout=8,
            )
            diagnostic = root / "app/build/reports/emulator/logcat.txt"
            return result, diagnostic.read_text() if diagnostic.exists() else ""

    def test_workflow_uses_node24_compatible_actions(self):
        workflow = (SCRIPTS.parent / "workflows/android.yml").read_text()
        for action in (
            "android-actions/setup-android@v4",
            "gradle/actions/wrapper-validation@v6",
            "actions/upload-artifact@v7",
        ):
            self.assertIn("uses: " + action, workflow)
        for deprecated in (
            "android-actions/setup-android@v3",
            "gradle/actions/wrapper-validation@v4",
            "actions/upload-artifact@v4",
        ):
            self.assertNotIn(deprecated, workflow)

    def test_workflow_avoids_duplicate_pr_branch_runs(self):
        workflow = (SCRIPTS.parent / "workflows/android.yml").read_text()
        self.assertIn("branches: [master]", workflow)
        self.assertIn("pull_request:", workflow)
        self.assertIn("cancel-in-progress: true", workflow)
        self.assertIn("github.event.pull_request.number || github.ref", workflow)

    def test_sdk_executable_works_without_emulator_on_path(self):
        result, diagnostic = self.run_launcher()
        self.assertEqual(0, result.returncode, result.stderr)
        self.assertIn("OK (12 tests)", result.stdout)
        self.assertIn("Diagnostics captured", diagnostic)

    def test_user_home_fallback_shares_avd_registry(self):
        result, diagnostic = self.run_launcher(explicit_avd_home=False)
        self.assertEqual(0, result.returncode, result.stderr)
        self.assertIn("OK (12 tests)", result.stdout)
        self.assertIn("Diagnostics captured", diagnostic)

    def test_missing_executable_fails_immediately(self):
        result, _ = self.run_launcher(emulator_exists=False)
        self.assertNotEqual(0, result.returncode)
        self.assertIn("executable is missing", result.stderr)

    def test_dead_emulator_fails_and_collects_diagnostics(self):
        result, diagnostic = self.run_launcher("early-exit")
        self.assertNotEqual(0, result.returncode)
        self.assertIn("emulator exited", result.stderr)
        self.assertIn("Diagnostics captured", diagnostic)

    def test_missing_services_do_not_pass_readiness(self):
        result, diagnostic = self.run_launcher("missing-service")
        self.assertNotEqual(0, result.returncode)
        self.assertIn("services did not become ready", result.stderr)
        self.assertIn("Diagnostics captured", diagnostic)

    def test_instrumentation_failure_fails_launcher(self):
        result, diagnostic = self.run_launcher("failed-test")
        self.assertNotEqual(0, result.returncode)
        self.assertIn("FAILURES!!!", result.stdout)
        self.assertIn("Diagnostics captured", diagnostic)


if __name__ == "__main__":
    unittest.main()
