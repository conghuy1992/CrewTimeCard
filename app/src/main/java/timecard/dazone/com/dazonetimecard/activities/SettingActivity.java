package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.fragments.SettingFragment;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class SettingActivity extends HomeActivity {
    @Override
    protected void addFragment(Bundle bundle) {
        if (bundle == null) {
            SettingFragment newFragment = new SettingFragment();
            Util.addFragmentToActivity(getSupportFragmentManager(), newFragment, R.id.main_content, false);
        }
    }
}