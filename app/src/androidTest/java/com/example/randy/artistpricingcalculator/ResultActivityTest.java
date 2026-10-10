package com.example.randy.artistpricingcalculator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/** Tests the real Results screen against SQLite, restoring pre-test rows and IDs. */
@RunWith(AndroidJUnit4.class)
public class ResultActivityTest {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private ItemDbHelper helper;
    private final List<ContentValues> originalRows = new ArrayList<>();
    private Activity activity;

    @Before public void setUp() {
        helper = new ItemDbHelper(InstrumentationRegistry.getInstrumentation().getTargetContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        try (Cursor cursor = db.query(ItemContract.ItemEntry.TABLE_NAME,null,null,null,null,null,null)) {
            while (cursor.moveToNext()) {
                ContentValues row = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor,row);
                originalRows.add(row);
            }
        }
        db.delete(ItemContract.ItemEntry.TABLE_NAME,null,null);
    }
    @After public void tearDown() {
        if (activity != null) instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() { activity.finish(); }
        });
        instrumentation.waitForIdleSync();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(ItemContract.ItemEntry.TABLE_NAME,null,null);
            for (ContentValues row : originalRows) db.insertOrThrow(ItemContract.ItemEntry.TABLE_NAME,null,row);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            helper.close();
        }
    }
    private void launch(Class<? extends Activity> type) {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity = instrumentation.startActivitySync(intent);
        instrumentation.waitForIdleSync();
    }
    private String text(int id) { return ((TextView)activity.findViewById(id)).getText().toString(); }
    private void seed() {
        helper.addItem(new Item(ItemContract.ITEM_TYPE_OVERHEAD,"Studio",10));
        helper.addItem(new Item(ItemContract.ITEM_TYPE_MARKETING,"Ads",5));
        helper.addItem(new Item(ItemContract.ITEM_TYPE_LABOR,"Hourly Rate",20));
        helper.addItem(new Item(ItemContract.ITEM_TYPE_LABOR,"Hours",1.5));
        helper.addItem(new Item(ItemContract.ITEM_TYPE_MATERIALS,"Canvas",12.5));
        helper.addItem(new Item(ItemContract.ITEM_TYPE_PACKAGING,"Box",2.5));
        helper.addItem(new Item(ItemContract.ITEM_TYPE_MISC,"Fees",1));
    }
    @Test public void databaseCostsRenderAndMultiplierChangesClearInvalidResults() {
        seed();
        launch(ResultActivity.class);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                assertEquals("183.00",text(R.id.tv_retial_price));
                assertEquals("91.50",text(R.id.tv_wholesale_price));
                assertTrue(text(R.id.tv_price_breakdown).contains("61.00"));
                EditText multiplier = (EditText)activity.findViewById(R.id.et_price_multiplier);
                multiplier.setText("3.5");
                assertEquals("213.50",text(R.id.tv_retial_price));
                assertEquals("106.75",text(R.id.tv_wholesale_price));
                multiplier.setText("0.5");
                assertEquals(activity.getString(R.string.price_unavailable),text(R.id.tv_retial_price));
                assertEquals(activity.getString(R.string.price_unavailable),text(R.id.tv_wholesale_price));
                assertEquals("",text(R.id.tv_price_breakdown));
                assertEquals(View.VISIBLE,activity.findViewById(R.id.tv_price_error).getVisibility());
                multiplier.setText("4");
                assertEquals("244.00",text(R.id.tv_retial_price));
                assertEquals(View.GONE,activity.findViewById(R.id.tv_price_error).getVisibility());
            }
        });
    }
    @Test public void incompleteLaborShowsErrorInsteadOfPrice() {
        helper.addItem(new Item(ItemContract.ITEM_TYPE_LABOR,"Hours",1.5));
        launch(ResultActivity.class);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                assertEquals(activity.getString(R.string.price_unavailable),text(R.id.tv_retial_price));
                assertTrue(text(R.id.tv_price_error).contains("both"));
            }
        });
    }
    @Test public void emptyCostsDisplayZero() {
        launch(ResultActivity.class);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                assertEquals("0.00",text(R.id.tv_retial_price));
                assertEquals("0.00",text(R.id.tv_wholesale_price));
            }
        });
    }
    @Test public void customMultiplierSurvivesActivityRecreation() {
        seed();
        launch(ResultActivity.class);
        final Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(ResultActivity.class.getName(),null,false);
        try {
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    ((EditText)activity.findViewById(R.id.et_price_multiplier)).setText("1.125");
                    activity.recreate();
                }
            });
            Activity recreated = instrumentation.waitForMonitorWithTimeout(monitor,10000);
            assertNotNull("Results activity must recreate",recreated);
            activity = recreated;
            instrumentation.waitForIdleSync();
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    assertEquals("1.125",text(R.id.et_price_multiplier));
                    assertEquals("68.63",text(R.id.tv_retial_price));
                    assertEquals("34.31",text(R.id.tv_wholesale_price));
                }
            });
        } finally { instrumentation.removeMonitor(monitor); }
    }
    @Test public void pricingHelpLaunchesWithAssumptions() {
        launch(ResultsHelpActivity.class);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                assertEquals(activity.getString(R.string.price_help),text(R.id.tv_results_help_text));
                assertTrue(activity.findViewById(R.id.tv_results_help_text).isShown());
            }
        });
    }

    @Test public void addingAndUpdatingStoredMaterialsChangesVisibleResults() {
        helper.addItem(new Item(ItemContract.ITEM_TYPE_MATERIALS, "Test canvas", 10));
        launch(ResultActivity.class);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                assertEquals("30.00", text(R.id.tv_retial_price));
            }
        });
        Item item = helper.getItemsByType(ItemContract.ITEM_TYPE_MATERIALS).get(0);
        item.mValue = 20;
        helper.updateItem(item);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                ((ResultActivity) activity).doCalc();
                assertEquals("60.00", text(R.id.tv_retial_price));
            }
        });
        helper.deleteItemById(item.mID);
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() {
                ((ResultActivity) activity).doCalc();
                assertEquals("0.00", text(R.id.tv_retial_price));
            }
        });
    }
}
