package com.example.randy.artistpricingcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;

public class AddItemActivity extends BaseActivity {
    public static final String TAG = "AddItemActivity";
    public static final String RECORD_TYPE = ItemContract.RECORD_TYPE;
    public static final String OVERHEAD_RECORD_TYPE = ItemContract.ITEM_TYPE_OVERHEAD;
    public static final String MARKETING_RECORD_TYPE = ItemContract.ITEM_TYPE_MARKETING;
    private final int REQUEST_CODE = 336699;

    private String mRecordType;
    private ItemDbHelper mDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mRecordType = getIntent().getStringExtra(ItemContract.RECORD_TYPE);
        Log.d(TAG, "The Data passed in was: " + mRecordType);

        mDbHelper = new ItemDbHelper(this);
    }


    public void onCancel(View view) {
        finish();
    }

    public void onAdd(View view) {
        // Insert record into database
        TextView etName = (TextView) findViewById(R.id.tv_item_name);
        TextView etAmount = (TextView) findViewById(R.id.tv_item_amount);
        String name = etName.getText().toString();
        String amount = etAmount.getText().toString();
        Item item = new Item(mRecordType, name, Double.valueOf(amount));
        mDbHelper.addItem(item);
        Log.d(TAG, "The RecordType passed in was: " + mRecordType);
        Toast.makeText(this, "The Data passed in was: " + mRecordType, Toast.LENGTH_SHORT).show();
        finish();
    }
}
