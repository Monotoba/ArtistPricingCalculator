package com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data;

import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by randy on 3/7/18.
 */

public final class ItemContract extends Item{
    public static final String TAG = "ItemContract";

    // Hold out sample data used to populate a new table
    private ArrayList<Item> mSampleData;

    // Item Types
    public static final String ITEM_TYPE_OVERHEAD = "Overhead";
    public static final String ITEM_TYPE_MARKETING = "Marketing";
    public static final String ITEM_TYPE_MATERIALS = "Materials";
    public static final String ITEM_TYPE_LABOR = "Labor";
    public static final String ITEM_TYPE_PACKAGING = "Packaging";
    public static final String ITEM_TYPE_MISC = "Misc";
    public static final String RECORD_TYPE = "RECORD_TYPE";

    // To prevent someone from accidentally instantiating the contract class,
    // make sure the constructor is private.
    private ItemContract() {}

    /* Inner class that defines the table contents */
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_ITEM_TYPE = "type";
        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_ITEM_VALUE = "value";
        public static final String COLUMN_ITEM_PERSISTS = "persist";
    }

    public static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY, "
                    + ItemEntry.COLUMN_ITEM_TYPE + " TEXT,"
                    + ItemEntry.COLUMN_ITEM_NAME + " TEXT,"
                    + ItemEntry.COLUMN_ITEM_VALUE + " TEXT,"
                    + ItemEntry.COLUMN_ITEM_PERSISTS + " INTEGER)";

    public static final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

    //public static final String SQL_CREATE_DEFUALT_RECORDS = getDefaultRecords();





    // Builds the query string to insert sample data.
    public static String getDefaultRecords(ArrayList<Item> sampleData) {
        String query;

        // Build the query to insert multiple rows.
        query = "INSERT INTO " + ItemEntry.TABLE_NAME +"( "
                + ItemEntry.COLUMN_ITEM_TYPE + ", "
                + ItemEntry.COLUMN_ITEM_NAME + ", "
                + ItemEntry.COLUMN_ITEM_VALUE + ", "
                + ItemEntry.COLUMN_ITEM_PERSISTS + ") "
                + "VALUES ";

        // Loop over sample data adding each item to the query
        for(Item item :sampleData) {
            query += "('"
                    +item.mType +"', "
                    + "'" + item.mName + "', "
                    + String.valueOf(item.mValue) + ", "
                    + String.valueOf((item.mPersist ? 1 : 0))
                    + "), ";
        }

        // Trim the trailing comma and append the closing semicolon.
        query = query.replaceAll("\\s*,\\s*$", "");
        Log.d(TAG, "Query: " + query);
        query += ";";

        // Finally, we return the query string.
        return query;
    }

//    // Builds up the sample data.
//    private ArrayList<Item> getSampleDate() {
//        mSampleData.add(new Item(ITEM_TYPE_OVERHEAD, "Rent", 578.00));
//        mSampleData.add(new Item(ITEM_TYPE_OVERHEAD, "Electric", 98.50));
//        mSampleData.add(new Item(ITEM_TYPE_OVERHEAD, "Water", 65.28));
//        mSampleData.add(new Item(ITEM_TYPE_OVERHEAD, "Gas", 34.46));
//
//        mSampleData.add(new Item(ITEM_TYPE_MARKETING, "AdSense", 100.00));
//        mSampleData.add(new Item(ITEM_TYPE_MARKETING, "AdWords", 100.00));
//        mSampleData.add(new Item(ITEM_TYPE_MARKETING,"Amazon", 50.00));
//        mSampleData.add(new Item(ITEM_TYPE_MARKETING, "Art World", 75.00));
//
//        mSampleData.add(new Item(ITEM_TYPE_LABOR, "Hourly Rate", 25.00));
//        mSampleData.add(new Item(ITEM_TYPE_LABOR, "Hours", 25.00));
//
//        mSampleData.add(new Item(ITEM_TYPE_MATERIALS, "Canvas", 55.00));
//        mSampleData.add(new Item(ITEM_TYPE_MATERIALS, "Acrylics", 110.00));
//        mSampleData.add(new Item(ITEM_TYPE_MATERIALS, "Framing", 235.00));
//
//        mSampleData.add(new Item(ITEM_TYPE_PACKAGING, "Bag", 8.00));
//        mSampleData.add(new Item(ITEM_TYPE_PACKAGING, "Padding", 12.00));
//        mSampleData.add(new Item(ITEM_TYPE_PACKAGING, "Wrapping Paper", 6.00));
//        mSampleData.add(new Item(ITEM_TYPE_PACKAGING, "Box", 14.00));
//
//        return mSampleData;
//    }


    public static Item makeItem(String type, String name, double value){
        return new Item(type, name, value);
    }

    public static Item makeItem(String type, String name, double value, boolean persists) {
        return new Item(type, name, value, persists, Integer.parseInt(null));
    }

    public static Item makeItem(String type, String name, double value, boolean persists, int id) {
        return new Item(type, name, value, persists, id);
    }
}
