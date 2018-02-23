package timecard.dazone.com.dazonetimecard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import timecard.dazone.com.dazonetimecard.fragments.IntroFragment;
import timecard.dazone.com.dazonetimecard.utils.Statics;

public class IntroPageAdapter extends AllEmployeeViewPagerAdapter {
    private int max = Statics.INTRO_PAGE_NUMBER;

    public IntroPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return IntroFragment.newInstance(position);
    }

    public void update(int max) {
        this.max = max;
    }

    @Override
    public int getCount() {
        return max;
    }
}