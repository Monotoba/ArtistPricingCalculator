package com.example.randy.artistpricingcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StudioTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_type);
    }


    public void onNext(View view) {
        Intent overheadIntent = new Intent(this, OverheadActivity.class);
        startActivity(overheadIntent);
    }


    public void onBack(View view) {
        finish();
    }
}
