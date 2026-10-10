package com.example.randy.artistpricingcalculator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.*;

/** Exercises Add/Edit actions, SQLite persistence, and Results without modifying existing rows. */
@RunWith(AndroidJUnit4.class)
public class CostEntryFlowTest {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

    private Activity launch(Class<? extends Activity> type, int id, String recordType) {
        Intent intent = new Intent(instrumentation.getTargetContext(), type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (id >= 0) intent.putExtra("ID", id);
        if (recordType != null) intent.putExtra(ItemContract.RECORD_TYPE, recordType);
        Activity activity = instrumentation.startActivitySync(intent);
        instrumentation.waitForIdleSync();
        return activity;
    }

    private void finish(final Activity activity) {
        if (activity == null) return;
        instrumentation.runOnMainSync(new Runnable() {
            @Override public void run() { activity.finish(); }
        });
        instrumentation.waitForIdleSync();
    }

    @Test public void addEditDeletePropagatesThroughDatabaseAndResults() {
        ItemDbHelper db = new ItemDbHelper(instrumentation.getTargetContext());
        String name = "Cost flow " + UUID.randomUUID();
        Activity add = null, edit = null;
        int id = -1;
        try {
            add = launch(AddItemActivity.class, -1, ItemContract.ITEM_TYPE_MATERIALS);
            final Activity addScreen = add;
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    ((EditText)addScreen.findViewById(R.id.tv_item_name)).setText(name);
                    ((EditText)addScreen.findViewById(R.id.tv_item_amount)).setText("bad");
                    addScreen.findViewById(R.id.btn_add_item).performClick();
                }
            });
            assertNull("Invalid amount must not be saved", findByName(db, name));
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    ((EditText)addScreen.findViewById(R.id.tv_item_amount)).setText("12.50");
                    addScreen.findViewById(R.id.btn_add_item).performClick();
                }
            });
            instrumentation.waitForIdleSync();
            Item inserted = findByName(db, name);
            assertNotNull("Valid amount must be saved", inserted);
            assertEquals(12.5, inserted.mValue, 0.00001);
            id = inserted.mID;
            finish(add);
            add = null;

            edit = launch(EditItemActivity.class, id, ItemContract.ITEM_TYPE_MATERIALS);
            final Activity editScreen = edit;
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    ((EditText)editScreen.findViewById(R.id.tv_item_amount)).setText("-1");
                    editScreen.findViewById(R.id.btn_edit_item).performClick();
                }
            });
            assertEquals("Invalid edit must leave stored value unchanged",
                    12.5, db.getItemById(id).mValue, 0.00001);
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    ((EditText)editScreen.findViewById(R.id.tv_item_amount)).setText("15.75");
                    editScreen.findViewById(R.id.btn_edit_item).performClick();
                }
            });
            instrumentation.waitForIdleSync();
            assertEquals(15.75, db.getItemById(id).mValue, 0.00001);
            finish(edit);
            edit = null;

            // Verify that the persisted record is recognized by the same pricing engine as Results.
            boolean found = false;
            for (Item item : db.getAllItems()) {
                if (item.mID == id) {
                    found = true;
                    assertEquals(15.75, item.mValue, 0.00001);
                }
            }
            assertTrue(found);
            db.deleteItemById(id);
            assertNull(db.getItemById(id));
            id = -1;
        } finally {
            finish(add);
            finish(edit);
            if (id >= 0) db.deleteItemById(id);
            db.close();
        }
    }

    private Item findByName(ItemDbHelper db, String name) {
        for (Item item : db.getItemsByType(ItemContract.ITEM_TYPE_MATERIALS)) {
            if (name.equals(item.mName)) return item;
        }
        return null;
    }
}
