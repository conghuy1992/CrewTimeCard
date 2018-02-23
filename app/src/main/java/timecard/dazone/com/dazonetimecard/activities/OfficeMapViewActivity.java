package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.view.MenuItem;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.fragments.CompanyMapFragment;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class OfficeMapViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_map);

        enableHomeAction();
        addFragment(savedInstanceState);
    }

    protected void addFragment(Bundle bundle) {
        if (bundle == null) {
            String company = getIntent().getExtras().getString(Statics.KEY_COMPANY);
            Util.addFragmentToActivity(getSupportFragmentManager(), new CompanyMapFragment().newInstance(company), R.id.office_map_content, false);
        }
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
}