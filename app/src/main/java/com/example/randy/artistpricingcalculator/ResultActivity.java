package com.example.randy.artistpricingcalculator;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;

public class ResultActivity extends BaseActivity {

    private ItemDbHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        database = new ItemDbHelper(this);
        ((EditText) findViewById(R.id.et_price_multiplier)).addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { doCalc(); }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        doCalc();
    }

    @Override
    protected void onDestroy() {
        if (database != null) database.close();
        super.onDestroy();
    }

    public void doCalc() {
        if (database == null) return;
        TextView retail = (TextView) findViewById(R.id.tv_retial_price);
        TextView wholesale = (TextView) findViewById(R.id.tv_wholesale_price);
        TextView breakdown = (TextView) findViewById(R.id.tv_price_breakdown);
        TextView error = (TextView) findViewById(R.id.tv_price_error);
        try {
            List<PricingCalculator.Line> lines = new ArrayList<>();
            for (Item item : database.getAllItems()) {
                lines.add(new PricingCalculator.Line(category(item.mType), item.mName, item.mValue));
            }
            String multiplier = ((EditText) findViewById(R.id.et_price_multiplier)).getText().toString();
            PricingCalculator.Result result = PricingCalculator.calculate(lines, multiplier);
            retail.setText(result.retail.toPlainString());
            wholesale.setText(result.wholesale.toPlainString());
            breakdown.setText(getString(R.string.price_breakdown,
                    PricingCalculator.money(result.cost).toPlainString(),
                    PricingCalculator.money(result.labor).toPlainString(),
                    result.multiplier.stripTrailingZeros().toPlainString()));
            error.setText("");
            error.setVisibility(View.GONE);
        } catch (IllegalArgumentException exception) {
            retail.setText(R.string.price_unavailable);
            wholesale.setText(R.string.price_unavailable);
            breakdown.setText("");
            error.setText(exception.getMessage());
            error.setVisibility(View.VISIBLE);
        }
    }

    private PricingCalculator.Category category(String type) {
        if (ItemContract.ITEM_TYPE_OVERHEAD.equals(type)) return PricingCalculator.Category.OVERHEAD;
        if (ItemContract.ITEM_TYPE_MARKETING.equals(type)) return PricingCalculator.Category.MARKETING;
        if (ItemContract.ITEM_TYPE_LABOR.equals(type)) return PricingCalculator.Category.LABOR;
        if (ItemContract.ITEM_TYPE_MATERIALS.equals(type)) return PricingCalculator.Category.MATERIALS;
        if (ItemContract.ITEM_TYPE_PACKAGING.equals(type)) return PricingCalculator.Category.PACKAGING;
        if (ItemContract.ITEM_TYPE_MISC.equals(type)) return PricingCalculator.Category.MISC;
        throw new IllegalArgumentException("Unknown cost category: " + type);
    }
}
