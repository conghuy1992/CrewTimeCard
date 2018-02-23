package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.Calendar;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.MyListViewPagerAdapter;
import timecard.dazone.com.dazonetimecard.fragments.MonthPickerFragment2;
import timecard.dazone.com.dazonetimecard.interfaces.DatePickerDialogListener;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class MyListActivity extends HomePagerViewActivity implements DatePickerDialogListener {

    @Override
    protected void openCalendar() {
        FragmentManager fm = getSupportFragmentManager();
        String strDate = (String) viewPagerMain.getAdapter().getPageTitle(viewPagerMain.getCurrentItem());
        MonthPickerFragment2 dateDialog = MonthPickerFragment2.newInstance(currentDay.getTime(), 1, strDate);
        dateDialog.show(fm, DIALOG_DATE);
    }

    @Override
    protected void addFragment(Bundle bundle) {
    }

    @Override
    protected void setupViewPager(Bundle bundle) {
        viewPagerMain = (ViewPager) findViewById(R.id.main_vpg_main);
        MyListViewPagerAdapter mainSelectionPagerAdapter = new MyListViewPagerAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(mainSelectionPagerAdapter);
        viewPagerMain.setCurrentItem(TimeUtils.getPositionForMonth(currentDay));
    }

    @Override
    public void onFinishEditDialog(Calendar mDate) {
        currentDay = mDate;
        viewPagerMain.setCurrentItem(TimeUtils.getPositionForMonth(mDate));
    }
}