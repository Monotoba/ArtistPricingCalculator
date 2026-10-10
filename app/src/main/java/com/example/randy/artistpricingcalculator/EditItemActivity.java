package com.example.randy.artistpricingcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;

public class EditItemActivity extends BaseActivity {
    public static final String TAG = "EditItemActivity";
    public static final String RECORD_TYPE = "OVERHEAD_COSTS";
    public static final String OVERHEAD_RECORD_TYPE = "OVERHEAD_COST_RECORD";
    private final int REQUEST_CODE = 336698;

    private String mRecordType;
    private ItemDbHelper mDbHelper;
    private Item mEditItem;
    private int mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        mDbHelper = new ItemDbHelper(this);

        mRecordType = getIntent().getStringExtra(RECORD_TYPE);
        mID = getIntent().getIntExtra("ID", -1);
        mEditItem = mDbHelper.getItemById(mID);

        // Populate the EditText Views
        EditText etName = (EditText) findViewById(R.id.tv_item_name);
        EditText etAmount = (EditText) findViewById(R.id.tv_item_amount);

        // Set the original values
        etName.setText(mEditItem.mName);
        etAmount.setText(String.valueOf(mEditItem.mValue));

        Log.d(TAG, "The Data passed in was: " + mRecordType);
    }

    public void onSave(View view) {
        // Populate the EditText Views
        EditText etName = (EditText) findViewById(R.id.tv_item_name);
        EditText etAmount = (EditText) findViewById(R.id.tv_item_amount);
        mEditItem.mName = etName.getText().toString();
        mEditItem.mValue = Double.valueOf(etAmount.getText().toString());

        mDbHelper.updateItem(mEditItem);

        finish();
    }

    public void onCancel(View view) {
        // User canceled. So just return.
        finish();
    }
}
