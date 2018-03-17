package com.example.randy.artistpricingcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    public boolean mFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Is this the first time the app has been ran?
        prefs = getSharedPreferences("com.example.randy.artistpricingcalculator", MODE_PRIVATE);
        mFirstRun = prefs.getBoolean("firstrun", true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(prefs.getBoolean("firstrun", true)){
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.mnu_action_configuration:
//                Intent overheadIntent = new Intent(this, OverheadActivity.class);
//                startActivity(overheadIntent);
                Intent studioIntent = new Intent(this, StudioTypeActivity.class);
                startActivity(studioIntent);
                break;

            case R.id.mnu_action_help:
                onHelp();
                break;

            case R.id.mnu_studio_calc:
                onStudioCalc();
                break;

            case R.id.mnu_action_settings:
                onSettings();

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    // Open the Home Studio Calculator
    protected void onStudioCalc() {
        Intent calcIntent = new Intent(this, HomeStudioCalcActivity.class);
        startActivity(calcIntent);
    }


    // Note: This method must be overridden in the subclass.
    // I wish Java allowed an abstract method here...
    // TODO: Override in subclass
    protected void onHelp() {
        // Override in subclass
    }

    // Simply closes Activity
    public void onDone(View view) {
        finish();
    }
}
