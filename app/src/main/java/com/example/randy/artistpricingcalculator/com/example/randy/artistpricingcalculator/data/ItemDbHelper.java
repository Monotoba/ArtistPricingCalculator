package com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randy on 3/7/18.
 */

public class ItemDbHelper extends SQLiteOpenHelper {
    String TAG = getClass().getSimpleName();

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
        db.execSQL(ItemContract.SQL_DELETE_ITEMS);
        onCreate(db);
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
        String query = "SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        // Return count
        return cursor.getCount();
    }

    // Returns the count of items matching type.
    public int getItemByTypeCount(String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Did we get a valid item type?
        // If not, return null.
        if(!isItemType(type)) return Integer.parseInt(null);

        ArrayList<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME;
        String whereCluase = " WHERE " + ItemContract.ItemEntry.COLUMN_ITEM_TYPE + " = \""
                + type + "\"";

        Cursor cursor = db.query(ItemContract.ItemEntry.TABLE_NAME,
                new String[] {
                        ItemContract.ItemEntry._ID,
                        ItemContract.ItemEntry.COLUMN_ITEM_TYPE,
                        ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                        ItemContract.ItemEntry.COLUMN_ITEM_VALUE,
                        ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS},
                ItemContract.ItemEntry.COLUMN_ITEM_TYPE + "=?",
                new String[] {type},
                null,
                null,
                ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                null
        );

        return cursor.getCount();
    }

    // Utility method used to determine if the string
    // passed in is a valid item type.
    private boolean isItemType(String type) {
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

    // Insert a single item into the database
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a container for our record values
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, item.mType);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, item.mName);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_VALUE, item.mValue);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS, item.mPersist);

        // Now insert the row
        db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
        db.close();

    }

    // Insert a list of items into the database
    public void addItems(ArrayList<Item> items) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query;

        query = "INSERT INTO " + ItemContract.ItemEntry.TABLE_NAME +"( "
                + ItemContract.ItemEntry.COLUMN_ITEM_TYPE + ", "
                + ItemContract.ItemEntry.COLUMN_ITEM_NAME + ", "
                + ItemContract.ItemEntry.COLUMN_ITEM_VALUE + ", "
                + ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS + ") "
                + "VALUES ";

        // Loop over sample data adding each item to the query
        for(Item item : items) {
            query += " ("
                    +item.mType +", "
                    + item.mName + ", "
                    + String.valueOf(item.mValue)
                    + String.valueOf(item.mPersist)
                    + "),";
        }

        Log.d(TAG, "addItems Query: " + query);
        // Trim the trailing comma and append the closing semicolon.
        query = query.substring(0,-1);
        query += ";";

        // Finally, we run the query.
        db.execSQL(query);
        db.close();

    }

    /**********************************
     *          Get Methods           *
     **********************************/

    // Read the row matching id from the database
    // and return the data as an object of type Item.
    public Item getItemById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create and execute the query
        Cursor cursor = db.query(ItemContract.ItemEntry.TABLE_NAME,
                new String[] {
                        ItemContract.ItemEntry._ID,
                        ItemContract.ItemEntry.COLUMN_ITEM_TYPE,
                        ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                        ItemContract.ItemEntry.COLUMN_ITEM_VALUE,
                        ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS},
                ItemContract.ItemEntry._ID + "=?",
                new String[] {String.valueOf(id)},
                ItemContract.ItemEntry.COLUMN_ITEM_TYPE,
                null,
                null,
                null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        Item item = ItemContract.makeItem(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                Boolean.parseBoolean(cursor.getString(4)),
                Integer.parseInt(cursor.getString(0))
        );

        return item;
    }


    // Get Items by Type
    // Note that this is not safe. Any string can be passed in type...
    // TODO DONE  Fix so this is safe
    public ArrayList<Item> getItemsByType(String type){
        SQLiteDatabase db = this.getReadableDatabase();

        // Did we get a valid item type?
        // If not, return null.
        if(!isItemType(type)) return null;

        ArrayList<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME;
        String whereCluase = " WHERE " + ItemContract.ItemEntry.COLUMN_ITEM_TYPE + " = \""
                + type + "\"";

        Cursor cursor = db.query(ItemContract.ItemEntry.TABLE_NAME,
                new String[] {
                        ItemContract.ItemEntry._ID,
                        ItemContract.ItemEntry.COLUMN_ITEM_TYPE,
                        ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                        ItemContract.ItemEntry.COLUMN_ITEM_VALUE,
                        ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS},
                ItemContract.ItemEntry.COLUMN_ITEM_TYPE + "=?",
                new String[] {type},
                null,
                null,
                ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                null
                );

        if(cursor.moveToFirst()) {
            do {

                // Create a Item object
                Item item = ItemContract.makeItem(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        Boolean.parseBoolean(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(0))
                );

                // Add the new item to the items list
                items.add(item);
            } while (cursor.moveToNext());

        }

        // return our list of items
        return items;

    }

    // Get a list of all items from the database
    public ArrayList<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Item> items = new ArrayList<Item>();

        // Select All Query
        String query = "SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        // Loop through all returned records
        // and add them to the Item list.
        if(cursor.moveToFirst()) {
            do {
                // Create a Item object
                Item item = ItemContract.makeItem(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        Boolean.parseBoolean(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(0))
                );

                // Add the new item to the items list
                items.add(item);
            } while (cursor.moveToNext());

        }

        // return our list of items
        return items;

    }

    /**********************************
     *         Update Methods         *
     **********************************/

    // Update a record
    public int updateItem(Item item) {
        int updateCount;

        SQLiteDatabase db = this.getReadableDatabase();

        // Did we get a valid item type?
        // If not, return null.
        if(!isItemType(item.mType)) return Integer.parseInt(null);

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, item.mType);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, item.mName);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_VALUE, item.mValue);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PERSISTS, item.mPersist);

        // Update the row
         updateCount = db.update(ItemContract.ItemEntry.TABLE_NAME, values,
                ItemContract.ItemEntry._ID + " = ?",
                new String[] { String.valueOf(item.mID) });
        db.close();

        return updateCount;

    }


    /**********************************
     *         Delete Methods         *
     **********************************/

    // Remove the record matching id from the database
    public void deleteItem(Item item) {
        deleteItemById(item.mID);
    }

    public void deleteItemById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ItemContract.ItemEntry.TABLE_NAME, ItemContract.ItemEntry._ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}
