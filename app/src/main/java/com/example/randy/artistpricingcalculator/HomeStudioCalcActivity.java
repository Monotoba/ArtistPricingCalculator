package com.example.randy.artistpricingcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;

public class HomeStudioCalcActivity extends AppCompatActivity {
    EditText mHomeSqft;
    EditText mStudioWidth;
    EditText mStudioLength;
    EditText mStudioSqft;
    EditText mStudioPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_studio_calc);
    }

    // Handle the Calculate Button
    public void onCalculate(View view) {
        // Get the values from the various fields.
        mHomeSqft = (EditText) findViewById(R.id.et_home_sqft_value);
        mStudioWidth = (EditText) findViewById(R.id.et_studio_width_value);
        mStudioLength = (EditText) findViewById(R.id.et_studio_length_value);
        mStudioSqft = (EditText) findViewById(R.id.et_studio_sqft_value);
        mStudioPercentage = (EditText) findViewById(R.id.et_percent_home_sqft_value);

        // Clear the output fields
        mStudioSqft.setText("");
        mStudioPercentage.setText("");

        // Calculate the current values
        double studio_width = Double.parseDouble(mStudioWidth.getText().toString());
        double studio_length = Double.parseDouble((mStudioLength.getText().toString()));

        // Calculate the square footage
        double studio_sqft = studio_width * studio_length;
        double home_sqft = Double.parseDouble(mHomeSqft.getText().toString());
        double studio_percentage = (studio_sqft / home_sqft) * 100.00;

        // Display the new values
        mStudioSqft.setText(String.format(Locale.US,"%.2f", studio_sqft));
        mStudioPercentage.setText(String.format(Locale.US, "%.2f", studio_percentage) + "%");

    }

    // Handle the Clear Button
    public void onClear(View view) {
        mHomeSqft = (EditText) findViewById(R.id.et_home_sqft_value);
        mStudioWidth = (EditText) findViewById(R.id.et_studio_width_value);
        mStudioLength = (EditText) findViewById(R.id.et_studio_length_value);
        mStudioSqft = (EditText) findViewById(R.id.et_studio_sqft_value);
        mStudioPercentage = (EditText) findViewById(R.id.et_percent_home_sqft_value);

        mHomeSqft.setText("");
        mStudioWidth.setText("");
        mStudioLength.setText("");
        mStudioSqft.setText("");
        mStudioPercentage.setText("");

    }
}
