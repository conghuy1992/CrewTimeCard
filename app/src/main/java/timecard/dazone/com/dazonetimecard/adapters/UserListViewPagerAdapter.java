package timecard.dazone.com.dazonetimecard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Calendar;

import timecard.dazone.com.dazonetimecard.fragments.UserListFragment;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;

public class UserListViewPagerAdapter extends MyListViewPagerAdapter {
    private String userNo = "";
    private long timeRequest;

    public UserListViewPagerAdapter(FragmentManager fm, String userNo, long timeRequest) {
        super(fm);
        this.userNo = userNo;
        this.timeRequest = timeRequest;
    }

    @Override
    public Fragment getItem(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeRequest);
        long timeForPosition;

        if (TimeUtils.getPositionForMonth(calendar) == position) {
            timeForPosition = timeRequest;
        } else {
            timeForPosition = TimeUtils.getMonthForPosition(position).getTimeInMillis();
        }

        return UserListFragment.newInstance(timeForPosition, userNo);
    }
}