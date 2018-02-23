package timecard.dazone.com.dazonetimecard.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import timecard.dazone.com.dazonetimecard.R;

public abstract class HomePagerViewActivity extends HomeActivity {
    protected static final String DIALOG_DATE = "date";
    protected ViewPager viewPagerMain;
    protected Calendar currentDay = Calendar.getInstance();

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setupViewPager(savedInstanceState);
    }

    protected abstract void setupViewPager(Bundle bundle);

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void setMainView() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pager_view_layout, null);
        main_content.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_pager_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_calendar:
                openCalendar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void openCalendar() {
    }
}