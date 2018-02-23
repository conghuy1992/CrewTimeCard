package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.Calendar;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.UserListViewPagerAdapter;
import timecard.dazone.com.dazonetimecard.interfaces.DatePickerDialogListener;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;

public class UserListActivity extends MyListActivity implements DatePickerDialogListener {
    String userNo = "";
    String userName = "";
    long milis = 0;
    @Override
    protected void setupViewPager(Bundle bundles) {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null&&bundle.getString(Statics.KEY_USER_NO)!=null) {
            userNo = bundle.getString(Statics.KEY_USER_NO);
            userName = bundle.getString(Statics.KEY_USER_NAME);
            milis = bundle.getLong(Statics.KEY_TIME_REQUEST);
            mActionBar.setTitle( userName+ " " + getString(R.string.time_list));
        }
        viewPagerMain = (ViewPager) findViewById(R.id.main_vpg_main);
        UserListViewPagerAdapter mainSelectionPagerAdapter = new UserListViewPagerAdapter(getSupportFragmentManager(),userNo,milis);
        viewPagerMain.setAdapter(mainSelectionPagerAdapter);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        viewPagerMain.setCurrentItem(TimeUtils.getPositionForMonth(calendar));

    }
}
