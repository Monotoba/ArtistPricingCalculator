package com.example.randy.artistpricingcalculator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.Item;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemContract;
import com.example.randy.artistpricingcalculator.com.example.randy.artistpricingcalculator.data.ItemDbHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.UUID;
import static org.junit.Assert.*;

/** Verifies migrated layouts, cards, and Material add controls on real cost screens. */
@RunWith(AndroidJUnit4.class)
public class CostScreenTest {
    private void verify(Class<? extends Activity> type, final int containerId,
                        final int addId, String category) {
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        ItemDbHelper helper = new ItemDbHelper(instrumentation.getTargetContext());
        final String name = "AndroidX UI test " + UUID.randomUUID();
        Activity launched = null;
        try {
            helper.addItem(new Item(category,name,12.75));
            Intent intent = new Intent(instrumentation.getTargetContext(),type);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launched = instrumentation.startActivitySync(intent);
            final Activity activity = launched;
            instrumentation.waitForIdleSync();
            instrumentation.runOnMainSync(new Runnable() {
                @Override public void run() {
                    LinearLayout container = (LinearLayout)activity.findViewById(containerId);
                    assertTrue(container.getChildCount() > 0);
                    boolean found = false;
                    for (int i = 0; i < container.getChildCount(); i++) {
                        View row = container.getChildAt(i);
                        assertTrue(row instanceof CardView);
                        if (name.equals(((TextView)row.findViewById(R.id.tv_item_name)).getText().toString())) {
                            assertEquals("12.75",((TextView)row.findViewById(R.id.tv_item_cost)).getText().toString());
                            found = true;
                        }
                    }
                    assertTrue("Inserted cost must appear in the migrated card list",found);
                    View add = activity.findViewById(addId);
                    assertTrue(add instanceof FloatingActionButton);
                    assertTrue(add.isShown());
                    assertTrue(add.isClickable());
                }
            });
        } finally {
            if (launched != null) {
                final Activity activity = launched;
                instrumentation.runOnMainSync(new Runnable() {
                    @Override public void run() { activity.finish(); }
                });
                instrumentation.waitForIdleSync();
            }
            helper.getWritableDatabase().delete(ItemContract.ItemEntry.TABLE_NAME,
                    ItemContract.ItemEntry.COLUMN_ITEM_NAME + "=? AND " + ItemContract.ItemEntry.COLUMN_ITEM_TYPE + "=?",
                    new String[]{name,category});
            helper.close();
        }
    }
    @Test public void overheadScreenRendersAndroidXCardsAndMaterialAddControl() {
        verify(OverheadActivity.class,R.id.ll_overhead_container,R.id.fab_overhead_add_action,ItemContract.ITEM_TYPE_OVERHEAD);
    }
    @Test public void marketingScreenRendersAndroidXCardsAndMaterialAddControl() {
        verify(MarketingActivity.class,R.id.ll_marketing_container,R.id.floatingActionButton,ItemContract.ITEM_TYPE_MARKETING);
    }
    @Test public void laborScreenRendersAndroidXCardsAndMaterialAddControl() {
        verify(LaborActivity.class,R.id.ll_labor_container,R.id.floatingActionButton,ItemContract.ITEM_TYPE_LABOR);
    }
    @Test public void materialsScreenRendersAndroidXCardsAndMaterialAddControl() {
        verify(MaterialsActivity.class,R.id.ll_materials_container,R.id.floatingActionButton,ItemContract.ITEM_TYPE_MATERIALS);
    }
    @Test public void packagingScreenRendersAndroidXCardsAndMaterialAddControl() {
        verify(PackagingActivity.class,R.id.ll_packaging_container,R.id.floatingActionButton,ItemContract.ITEM_TYPE_PACKAGING);
    }
}
