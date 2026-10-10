package com.example.randy.artistpricingcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;

import java.util.ArrayList;

public class PackagingActivity extends BaseActivity {
    String TAG = getClass().getSimpleName();

    public static final String RECORD_TYPE = ItemContract.RECORD_TYPE;
    //public static final String PACKAGING_RECORD_TYPE = ItemContract.ITEM_TYPE_PACKAGING;
    private final int REQUEST_CODE = 336694;

    private ItemDbHelper mDB;

    private ArrayList<Item> itemList;

    private Boolean mConfirmed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packaging);

        // Create the database helper object
        mDB = new ItemDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    public void onAdd(View view) {
        Intent addIntent = new Intent(this, AddItemActivity.class)
                .putExtra(ItemContract.RECORD_TYPE, ItemContract.ITEM_TYPE_PACKAGING);

        startActivity(addIntent);//, REQUEST_CODE);
        Toast.makeText(this, "onAdd was fired in PackagingActivity.class", Toast.LENGTH_SHORT).show();
    }

    public void onDelete(View view) {
        int id = (int) view.getTag();
        Item item = mDB.getItemById(id);
        showConfirm(item);
    }

    public void doDeleteById(int id) {
        mDB.deleteItemById(id);
    }

    public boolean showConfirm(final Item item) {
        mConfirmed = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PackagingActivity.this);
        alertDialog.setMessage("Do you want to delete the " + item.mName + " item.");
        alertDialog.setTitle("Delete");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doDeleteById(item.mID);
                updateUI();
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        alertDialog.show();
        return mConfirmed;
    }

    public void setConfirmed (Boolean state) {
        mConfirmed = state;
    }

    public void onEdit(View view) {
        int id = (int) view.getTag();
        Intent editIntent = new Intent(this, EditItemActivity.class)
                .putExtra(ItemContract.RECORD_TYPE, ItemContract.ITEM_TYPE_PACKAGING)
                .putExtra("ID", id);

        startActivity(editIntent);

        Toast.makeText(this, "onEdit was fired with ID: " + String.valueOf(id), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        String dataPassedBack = data.getExtras().get(ItemContract.RECORD_TYPE).toString();
        Log.d(TAG, "The data passed back was: " + dataPassedBack);
        // Check which request we are responding to
        if(requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            if(resultCode == RESULT_OK) {
                // The user entered a new item
                Toast.makeText(this, "The data passed back was: " + dataPassedBack, Toast.LENGTH_SHORT).show();
                // Do something with the data entered, if any...
            }
        }
    }

    public void onPackagingBack(View view) {
        finish();
    }

    public void onNext(View view) {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        startActivity(resultIntent);
    }

    // Update the UI from the database as they may have changed
    // do to editing or deletion.
    public void updateUI() {
        // Create the database helper object
        //mDB = new ItemDbHelper(this);

        // Get all items of the Packaging type
        itemList = mDB.getItemsByType(ItemContract.ITEM_TYPE_PACKAGING);
        // Get the scrollview from the packaging layout
        LinearLayout containerView = (LinearLayout) findViewById(R.id.ll_packaging_container);

        // Remove the current items in the listView
        containerView.removeAllViews();

        // Make sure we got some items back.
        // and add them to the view
        if(itemList.size() > 0) {
            for (Item item : itemList) {
                CardView cardLayout = (CardView) getLayoutInflater().inflate(R.layout.list_item_view, containerView, false);
                //Log.d(TAG, findViewById(R.id.list_item_view).toString());
                if(cardLayout == null) {
                    // If null the layout needs inflated.
                    Log.d(TAG, "Card was null!");
                }else {
                    TextView tvName = (TextView) cardLayout.findViewById(R.id.tv_item_name);
                    TextView tvCost = (TextView) cardLayout.findViewById(R.id.tv_item_cost);

                    // Get the icons and add the db record id to their tags.
                    View delIcon = (View) cardLayout.findViewById(R.id.icon_delete);
                    //View editIcon = (View) cardLayout.findViewById(R.id.icon_edit);
                    delIcon.setTag(item.mID);
                    //editIcon.setTag(item.mID);

                    // Set the text view values
                    tvName.setText(item.mName);
                    tvName.setTag(item.mID);
                    tvCost.setText(String.valueOf(item.mValue));
                    tvCost.setTag(item.mID);

                    // Add the item to the container view
                    containerView.addView(cardLayout);
                }
            }
        }
    }

    public void onHelp() {
        Intent helpIntent = new Intent(this, PackagingHelpActivity.class);
        startActivity(helpIntent);
    }

}
