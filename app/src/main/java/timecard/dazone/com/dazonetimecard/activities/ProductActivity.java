package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.GridProductAdapter;
import timecard.dazone.com.dazonetimecard.dtos.ProductDto;

public class ProductActivity extends BaseActivity {
    GridView product_grid;
    GridProductAdapter adapter;
    protected int activityNumber = 0;
    ArrayList<ProductDto> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getInt("count_id") != 0) {
            activityNumber = bundle.getInt("count_id");
        }

        setContentView(R.layout.activity_product);

        product_grid = (GridView) findViewById(R.id.product_grid);
        if (activityNumber != 0) {
            enableHomeAction();
        }

        initArray();
        adapter = new GridProductAdapter(this, mNavItems);
        product_grid.setAdapter(adapter);
        product_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startNewActivity(TimeCardActivity.class, position);
            }
        });
    }

    private void initArray() {
        mNavItems.clear();
        mNavItems.add(new ProductDto(R.string.menu_action_time_card, R.drawable.crew_store_ic_timecard));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initArray();
        adapter.notifyDataSetChanged();
    }
}