package com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by randy on 3/7/18.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ArtistPricing.db";

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ItemContract.SQL_CREATE_ITEMS);
        //db.execSQL(ItemContract.SQL_CREATE_DEFUALT_RECORDS);
        db.execSQL((ItemContract.getDefaultRecords(getSampleDate())));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("No data-preserving migration from version "
                + oldVersion + " to " + newVersion + " is implemented.");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    /**********************************
     *        Utility Methods         *
     **********************************/

    // Builds up the sample data.
    private ArrayList<Item> getSampleDate() {
        ArrayList<Item> sampleData = new ArrayList<Item>();

        sampleData.add(new Item(ItemContract.ITEM_TYPE_OVERHEAD, "Rent", 578.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_OVERHEAD, "Electric", 98.50));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_OVERHEAD, "Water", 65.28));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_OVERHEAD, "Gas", 34.46));

        sampleData.add(new Item(ItemContract.ITEM_TYPE_MARKETING, "AdSense", 100.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_MARKETING, "AdWords", 100.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_MARKETING,"Amazon", 50.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_MARKETING, "Art World", 75.00));

        sampleData.add(new Item(ItemContract.ITEM_TYPE_LABOR, "Hourly Rate", 25.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_LABOR, "Hours", 25.00));

        sampleData.add(new Item(ItemContract.ITEM_TYPE_MATERIALS, "Canvas", 55.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_MATERIALS, "Acrylics", 110.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_MATERIALS, "Framing", 235.00));

        sampleData.add(new Item(ItemContract.ITEM_TYPE_PACKAGING, "Bag", 8.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_PACKAGING, "Padding", 12.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_PACKAGING, "Wrapping Paper", 6.00));
        sampleData.add(new Item(ItemContract.ITEM_TYPE_PACKAGING, "Box", 14.00));

        return sampleData;
    }

    // Get a count of all items in the db
    public int getItemCount() {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + ItemContract.ItemEntry.TABLE_NAME, null)) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

    // Returns the count of items matching type.
    public int getItemByTypeCount(String type) {
        if (!isItemType(type)) {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + ItemContract.ItemEntry.TABLE_NAME
                        + " WHERE " + ItemContract.ItemEntry.COLUMN_ITEM_TYPE + "=?",
                new String[] {type})) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

    // Utility method used to determine if the string
    // passed in is a valid item type.
    private boolean isItemType(String type) {
        if (type == null) return false;
        switch(type) {
            case ItemContract.ITEM_TYPE_LABOR: return true;
            case ItemContract.ITEM_TYPE_MARKETING: return true;
            case ItemContract.ITEM_TYPE_MATERIALS: return true;
            case ItemContract.ITEM_TYPE_MISC: return true;
            case ItemContract.ITEM_TYPE_OVERHEAD: return true;
            case ItemContract.ITEM_TYPE_PACKAGING: return true;

            default:
                return false;
        }
    }

    /**********************************
     *         Insert Methods         *
     **********************************/

    private static final String[] ITEM_COLUMNS = {
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.COLUMN_ITEM_TYPE,
            ItemContract.ItemEntry.COLUMN_ITEM_NAME,
            ItemContract.ItemEntry.COLUMN_ITEM_VALUE,
            ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS
    };

    private static Item readItem(Cursor cursor) {
        return ItemContract.makeItem(cursor.getString(1), cursor.getString(2),
                cursor.getDouble(3), cursor.getInt(4) != 0, cursor.getInt(0));
    }

    private ContentValues itemValues(Item item) {
        if (item == null || !isItemType(item.mType)) {
            throw new IllegalArgumentException("Item must have a supported type");
        }
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, item.mType);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, item.mName);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_VALUE, item.mValue);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS, item.mPersist ? 1 : 0);
        return values;
    }

    // Insert a single item into the database
    public void addItem(Item item) {
        getWritableDatabase().insertOrThrow(
                ItemContract.ItemEntry.TABLE_NAME, null, itemValues(item));
    }

    // Insert a list of items into the database
    public void addItems(ArrayList<Item> items) {
        if (items == null) throw new IllegalArgumentException("Items must not be null");
        if (items.isEmpty()) return;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Item item : items) {
                db.insertOrThrow(ItemContract.ItemEntry.TABLE_NAME, null, itemValues(item));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**********************************
     *          Get Methods           *
     **********************************/

    // Read the row matching id from the database
    // and return the data as an object of type Item.
    public Item getItemById(int id) {
        try (Cursor cursor = getReadableDatabase().query(
                ItemContract.ItemEntry.TABLE_NAME, ITEM_COLUMNS,
                ItemContract.ItemEntry._ID + "=?", new String[] {String.valueOf(id)},
                null, null, null)) {
            return cursor.moveToFirst() ? readItem(cursor) : null;
        }
    }


    // Get Items by Type
    // Note that this is not safe. Any string can be passed in type...
    // TODO DONE  Fix so this is safe
    public ArrayList<Item> getItemsByType(String type){
        // Preserve the existing null result for an unsupported category.
        if (!isItemType(type)) return null;
        ArrayList<Item> items = new ArrayList<Item>();
        try (Cursor cursor = getReadableDatabase().query(
                ItemContract.ItemEntry.TABLE_NAME, ITEM_COLUMNS,
                ItemContract.ItemEntry.COLUMN_ITEM_TYPE + "=?", new String[] {type},
                null, null, ItemContract.ItemEntry.COLUMN_ITEM_NAME)) {
            while (cursor.moveToNext()) items.add(readItem(cursor));
        }
        return items;
    }

    // Get a list of all items from the database
    public ArrayList<Item> getAllItems(){
        ArrayList<Item> items = new ArrayList<Item>();
        try (Cursor cursor = getReadableDatabase().query(
                ItemContract.ItemEntry.TABLE_NAME, ITEM_COLUMNS,
                null, null, null, null, null)) {
            while (cursor.moveToNext()) items.add(readItem(cursor));
        }
        return items;
    }

    /**********************************
     *         Update Methods         *
     **********************************/

    // Update a record
    public int updateItem(Item item) {
        return getWritableDatabase().update(
                ItemContract.ItemEntry.TABLE_NAME, itemValues(item),
                ItemContract.ItemEntry._ID + "=?", new String[] {String.valueOf(item.mID)});
    }


    /**********************************
     *         Delete Methods         *
     **********************************/

    // Remove the record matching id from the database
    public void deleteItem(Item item) {
        deleteItemById(item.mID);
    }

    public void deleteItemById(int id) {
        getWritableDatabase().delete(ItemContract.ItemEntry.TABLE_NAME,
                ItemContract.ItemEntry._ID + "=?", new String[] {String.valueOf(id)});
    }
}
