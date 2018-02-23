package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.fragments.ProductInfoFragment;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class ProductInfoActivity extends HomeActivity {
    @Override
    protected void addFragment(Bundle bundle) {
        if (bundle == null) {
            ProductInfoFragment newFragment = new ProductInfoFragment();
            Util.addFragmentToActivity(getSupportFragmentManager(), newFragment, R.id.main_content, false);
        }
    }
}
