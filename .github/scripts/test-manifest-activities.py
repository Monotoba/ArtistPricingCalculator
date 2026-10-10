"""Check that every relative Android activity declaration resolves to a Java source."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
MANIFEST = ROOT / "app/src/main/AndroidManifest.xml"
SOURCES = ROOT / "app/src/main/java/com/example/randy/artistpricingcalculator"
ANDROID_NAME = "{http://schemas.android.com/apk/res/android}name"


def missing_activities(manifest, sources):
    tree = ET.parse(manifest)
    missing = []
    for activity in tree.findall(".//activity"):
        name = activity.get(ANDROID_NAME, "")
        if name.startswith(".") and not (sources / (name[1:] + ".java")).is_file():
            missing.append(name)
    return missing


class ManifestActivityTest(unittest.TestCase):
    def test_registered_relative_activities_have_source_files(self):
        self.assertEqual([], missing_activities(MANIFEST, SOURCES))

    def test_detects_unknown_activity(self):
        import tempfile
        with tempfile.TemporaryDirectory() as tmp:
            manifest = Path(tmp) / "AndroidManifest.xml"
            manifest.write_text('<manifest xmlns:android="http://schemas.android.com/apk/res/android"><application><activity android:name=".MissingActivity"/></application></manifest>')
            self.assertEqual([".MissingActivity"], missing_activities(manifest, SOURCES))


if __name__ == "__main__":
    unittest.main()
