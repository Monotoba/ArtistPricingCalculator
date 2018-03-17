package com.example.randy.artistpricingcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class OverheadHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overhead_help);

        TextView tv = (TextView)findViewById(R.id.tv_overhead_help_text);
        tv.setMovementMethod(new ScrollingMovementMethod());
    }

    public void onDone(View view) {
        finish();
    }


}
