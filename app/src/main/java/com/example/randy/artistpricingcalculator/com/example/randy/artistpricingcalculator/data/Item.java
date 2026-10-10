package com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data;

import android.support.annotation.Nullable;

/**
 * Created by randy on 3/7/18.
 */

public class Item {
    public int mID;
    public String mType;
    public String mName;
    public double mValue;
    public boolean mPersist;

    public Item() {}

    public Item(String type, String name, double value) {
        //mID = 0;
        mType = type;
        mName = name;
        mValue = value;
        mPersist = false;
    }

    public Item(String type, String name, double value, boolean persist, int id) {
        mID = id;
        mType = type;
        mName = name;
        mValue = value;
        mPersist = persist;
    }

}