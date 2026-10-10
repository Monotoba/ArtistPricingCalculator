package com.example.randy.artistpricingcalculator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** Exercises the actual Android SQLite implementation using isolated test data. */
@RunWith(AndroidJUnit4.class)
public class ItemDbHelperTest {
    private Context context;
    private ItemDbHelper helper;

    @Before
    public void setUp() {
        // Prefix all database names in the writable app context to isolate test data.
        context = new RenamingDelegatingContext(
                InstrumentationRegistry.getTargetContext(), "regression_");
        context.deleteDatabase(ItemDbHelper.DATABASE_NAME);
        helper = new ItemDbHelper(context);
        helper.getWritableDatabase().delete(ItemContract.ItemEntry.TABLE_NAME, null, null);
    }

    @After
    public void tearDown() {
        if (helper != null) helper.close();
        context.deleteDatabase(ItemDbHelper.DATABASE_NAME);
    }

    private Item sample(String name, boolean persist) {
        return new Item(ItemContract.ITEM_TYPE_MATERIALS, name, 12.75, persist, 0);
    }

    @Test
    public void emptyDatabaseHasZeroCount() {
        assertEquals(0, helper.getItemCount());
        assertTrue(helper.getAllItems().isEmpty());
    }

    @Test
    public void insertedItemRoundTripsUnicodeQuotesAndPersistence() {
        helper.addItem(sample("Artist's café 材料", true));
        Item actual = helper.getAllItems().get(0);
        assertEquals("Artist's café 材料", actual.mName);
        assertEquals(12.75, actual.mValue, 0.000001);
        assertTrue(actual.mPersist);
        assertTrue(helper.getItemById(actual.mID).mPersist);
        assertTrue(helper.getItemsByType(actual.mType).get(0).mPersist);
    }

    @Test
    public void nonPersistentItemRemainsNonPersistent() {
        helper.addItem(sample("Canvas", false));
        assertFalse(helper.getAllItems().get(0).mPersist);
    }

    @Test
    public void bulkInsertionHandlesQuotesAndMultipleRows() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(sample("Artist's paint", true));
        items.add(sample("帆布", false));
        helper.addItems(items);
        assertEquals(2, helper.getAllItems().size());
        assertEquals(2, helper.getItemByTypeCount(ItemContract.ITEM_TYPE_MATERIALS));
    }

    @Test
    public void emptyBulkInsertionIsANoOp() {
        helper.addItems(new ArrayList<Item>());
        assertTrue(helper.getAllItems().isEmpty());
    }

    @Test
    public void updatingAndDeletingOnlyAffectsTheSelectedRow() {
        helper.addItem(sample("First", false));
        helper.addItem(sample("Second", false));
        Item first = helper.getAllItems().get(0);
        first.mName = "Updated";
        first.mValue = 22.50;
        first.mPersist = true;
        assertEquals(1, helper.updateItem(first));
        Item updated = helper.getItemById(first.mID);
        assertEquals("Updated", updated.mName);
        assertEquals(22.50, updated.mValue, 0.000001);
        assertTrue(updated.mPersist);
        helper.deleteItem(first);
        assertEquals(1, helper.getAllItems().size());
        assertEquals("Second", helper.getAllItems().get(0).mName);
    }

    @Test
    public void missingIdReturnsNull() {
        assertNull(helper.getItemById(Integer.MAX_VALUE));
    }

    @Test
    public void repeatedReadsAndHelperReopenPreserveData() {
        helper.addItem(sample("Canvas", true));
        for (int i = 0; i < 100; i++) {
            assertEquals(1, helper.getAllItems().size());
            assertEquals(1, helper.getItemByTypeCount(ItemContract.ITEM_TYPE_MATERIALS));
        }
        helper.close();
        helper = new ItemDbHelper(context);
        assertEquals(1, helper.getItemCount());
        assertTrue(helper.getAllItems().get(0).mPersist);
    }
}
