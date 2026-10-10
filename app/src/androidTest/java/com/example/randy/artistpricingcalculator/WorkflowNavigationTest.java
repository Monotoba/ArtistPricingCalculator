package com.example.randy.artistpricingcalculator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/** Exercises the existing category-to-results navigation using actual activities. */
@RunWith(AndroidJUnit4.class)
public class WorkflowNavigationTest {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

    private <T extends Activity> T launch(Class<T> type) {
        Intent intent = new Intent(instrumentation.getTargetContext(), type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        T activity = type.cast(instrumentation.startActivitySync(intent));
        instrumentation.waitForIdleSync();
        return activity;
    }

    private <T extends Activity> T next(final Activity current, final Class<T> expected) {
        Instrumentation.ActivityMonitor monitor =
                instrumentation.addMonitor(expected.getName(), null, false);
        try {
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    if (current instanceof LaborActivity) ((LaborActivity) current).onNext(null);
                    else if (current instanceof MaterialsActivity) ((MaterialsActivity) current).onNext(null);
                    else if (current instanceof PackagingActivity) ((PackagingActivity) current).onNext(null);
                    else throw new AssertionError("Unsupported navigation source");
                }
            });
            T result = expected.cast(instrumentation.waitForMonitorWithTimeout(monitor, 10000));
            assertNotNull("Expected next activity: " + expected.getSimpleName(), result);
            instrumentation.waitForIdleSync();
            return result;
        } finally {
            instrumentation.removeMonitor(monitor);
        }
    }

    @Test public void laborToMaterialsToPackagingToResults() {
        Activity labor = null, materials = null, packaging = null, results = null;
        try {
            labor = launch(LaborActivity.class);
            materials = next(labor, MaterialsActivity.class);
            packaging = next(materials, PackagingActivity.class);
            results = next(packaging, ResultActivity.class);
            assertNotNull(results.findViewById(R.id.tv_retial_price));
            assertNotNull(results.findViewById(R.id.tv_wholesale_price));
        } finally {
            final Activity[] opened = {results, packaging, materials, labor};
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    for (Activity activity : opened) if (activity != null) activity.finish();
                }
            });
            instrumentation.waitForIdleSync();
        }
    }
}
