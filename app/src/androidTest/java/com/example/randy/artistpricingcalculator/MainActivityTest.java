package com.example.randy.artistpricingcalculator;

import android.app.Instrumentation;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Checks that the legacy activity and support libraries can render the home screen. */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Test
    public void homeScreenLaunchesWithTitleAndStartControl() {
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final MainActivity activity = (MainActivity) instrumentation.startActivitySync(intent);
        try {
            instrumentation.waitForIdleSync();
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    TextView title = (TextView) activity.findViewById(R.id.tv_app_title);
                    TextView start = (TextView) activity.findViewById(R.id.tvStart);
                    assertEquals(activity.getString(R.string.tv_app_label), title.getText().toString());
                    assertEquals(activity.getString(R.string.btn_start_label), start.getText().toString());
                    assertTrue(title.isShown());
                    assertTrue(start.isShown());
                    assertTrue(start.isClickable());
                }
            });
        } finally {
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    activity.finish();
                }
            });
        }
    }
}
