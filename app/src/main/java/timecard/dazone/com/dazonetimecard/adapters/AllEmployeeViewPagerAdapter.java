package timecard.dazone.com.dazonetimecard.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

import timecard.dazone.com.dazonetimecard.fragments.AllEmployeeFragment;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;

public class AllEmployeeViewPagerAdapter extends FragmentStatePagerAdapter {
    int sortType = 0;
    int GroupNo = 0;

    public AllEmployeeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public AllEmployeeViewPagerAdapter(FragmentManager fm, int sortType, int GroupNo) {
        super(fm);
        this.sortType = sortType;
        this.GroupNo = GroupNo;
    }

    @Override
    public Fragment getItem(int position) {
        long timeForPosition = TimeUtils.getDayForPosition(position).getTimeInMillis();
        return AllEmployeeFragment.newInstance(timeForPosition, sortType, GroupNo);
    }

    @Override
    public int getCount() {
        return TimeUtils.DAYS_OF_TIME;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar cal = TimeUtils.getDayForPosition(position);
        return TimeUtils.getFormattedDate(cal.getTimeInMillis());
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}