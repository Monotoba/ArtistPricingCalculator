package com.example.randy.artistpricingcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        doCalc();
    }



    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onHelp() {
        Intent helpIntent = new Intent(this, ResultsHelpActivity.class);
        startActivity(helpIntent);
    }

    // Get the values from the database and total them.
    // Note that labor costs must be multiplied....
    // (Overhead + Marketing + (Labor * Hours) + Materials + Packaging) * 3 or 4 = Retail
    // Retail / 2 = Wholesale
    public void doCalc() {

    }
}
