package com.example.randy.artistpricingcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;

public class MainActivity extends BaseActivity {
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the database
        Log.d(TAG, "Create Database " + ItemDbHelper.DATABASE_NAME);
        ItemDbHelper db = new ItemDbHelper(this);

    }


    public void onStart(View view) {

        if(mFirstRun) {
            Intent configIntent = new Intent(this, OverheadActivity.class);
            startActivity(configIntent);
        } else {
//            Intent materialIntent = new Intent(this, MaterialsActivity.class);
//            startActivity(materialIntent);

            // TODO Clean up comments
            Intent laborIntent = new Intent(this, LaborActivity.class);
            startActivity(laborIntent);
        }
    }

    // This method overrides a method in the BaseActivity
    public void onHelp() {
        Intent helpIntent = new Intent(this, MainHelpActivity.class);
        startActivity(helpIntent);
    }

}
