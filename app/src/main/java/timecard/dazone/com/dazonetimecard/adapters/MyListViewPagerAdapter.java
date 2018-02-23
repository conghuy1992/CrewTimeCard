package timecard.dazone.com.dazonetimecard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

import timecard.dazone.com.dazonetimecard.fragments.MyListFragment;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;

public class MyListViewPagerAdapter extends FragmentStatePagerAdapter {

    public MyListViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        long timeForPosition = TimeUtils.getMonthForPosition(position).getTimeInMillis();
        return MyListFragment.newInstance(timeForPosition);
    }

    @Override
    public int getCount() {
        return TimeUtils.MONTHS_OF_TIME;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar cal = TimeUtils.getMonthForPosition(position);
        return TimeUtils.getFormattedMonth(cal.getTimeInMillis());
    }
}