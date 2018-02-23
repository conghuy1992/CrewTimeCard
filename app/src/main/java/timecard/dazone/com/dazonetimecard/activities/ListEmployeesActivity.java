package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.AllEmployeeViewPagerAdapter;
import timecard.dazone.com.dazonetimecard.customviews.OrganizationDialog;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.fragments.DatePickerFragment;
import timecard.dazone.com.dazonetimecard.fragments.TimeCardFragment;
import timecard.dazone.com.dazonetimecard.interfaces.ChooseGroupNoCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.DatePickerDialogListener;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;

public class ListEmployeesActivity extends HomePagerViewActivity implements DatePickerDialogListener {
    private String TAG = "ListEmployeesActivity";
    private Menu menu;

    @Override
    protected void addFragment(Bundle bundle) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrefs.getReloadEmp()) {
            mPrefs.putReloadEmp(false);
            setupViewPager(mPrefs.getSortStaffList(), GroupNoTemp);
        }
    }

    @Override
    public void onFinishEditDialog(Calendar mDate) {
        currentDay = mDate;
        viewPagerMain.setCurrentItem(TimeUtils.getPositionForDay(mDate));
    }

    @Override
    protected void setupViewPager(Bundle bundle) {
        viewPagerMain = (ViewPager) findViewById(R.id.main_vpg_main);
        AllEmployeeViewPagerAdapter mainSelectionPagerAdapter = new AllEmployeeViewPagerAdapter(getSupportFragmentManager(), mPrefs.getSortStaffList(), 0);
        viewPagerMain.setAdapter(mainSelectionPagerAdapter);
        viewPagerMain.setCurrentItem(TimeUtils.getPositionForDay(currentDay));
    }

    private int GroupNoTemp = 0;
    private int sortTypeTemp = 0;

    protected void setupViewPager(int sortType, int GroupNo) {
        sortTypeTemp = sortType;
        viewPagerMain = (ViewPager) findViewById(R.id.main_vpg_main);
        int position = viewPagerMain.getCurrentItem();
        AllEmployeeViewPagerAdapter mainSelectionPagerAdapter = new AllEmployeeViewPagerAdapter(getSupportFragmentManager(), sortType, GroupNo);
        viewPagerMain.setAdapter(mainSelectionPagerAdapter);
        viewPagerMain.setCurrentItem(position);
    }

    @Override
    protected void openCalendar() {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dateDialog = DatePickerFragment.newInstance(currentDay.getTime(), 1);
        dateDialog.show(fm, DIALOG_DATE);
    }

    MenuItem action_organization;

    private void showOrganizationMenu() {
        if (action_organization != null) action_organization.setVisible(true);
    }

    private void hideOrganizationMenu() {
        if (action_organization != null) action_organization.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_allemp, menu);

        action_organization = menu.findItem(R.id.action_organization);
        if (TimeCardFragment.instance != null) {
            List<TreeUserDTO> list = TimeCardFragment.instance.getSubordinates();
            if (list != null && list.size() > 0) {
                showOrganizationMenu();
            } else {
                hideOrganizationMenu();
//                Toast.makeText(context, "Can not get list user, restart app please", Toast.LENGTH_SHORT).show();
            }
        } else {
            hideOrganizationMenu();
//            Toast.makeText(context, "Can not get list user, restart app please", Toast.LENGTH_SHORT).show();
        }


        int sortStaffList = new Prefs().getSortStaffList();
        MenuItem menuItem = null;

        switch (sortStaffList) {
            case 0:
                menuItem = menu.findItem(R.id.action_name);
                break;
            case 1:
                menuItem = menu.findItem(R.id.action_soonest);
                break;
            case 2:
                menuItem = menu.findItem(R.id.action_latest);
                break;
            case 3:
                menuItem = menu.findItem(R.id.action_working_time);
                break;

        }
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
        return true;
    }

    private void resetCheckedMenu() {
        MenuItem actionName = menu.findItem(R.id.action_name);
        actionName.setChecked(false);
        MenuItem actionSoonest = menu.findItem(R.id.action_soonest);
        actionSoonest.setChecked(false);
        MenuItem actionLatest = menu.findItem(R.id.action_latest);
        actionLatest.setChecked(false);
        MenuItem actionWorkingTime = menu.findItem(R.id.action_working_time);
        actionWorkingTime.setChecked(false);
    }

    void organization() {

        new OrganizationDialog(this, new ChooseGroupNoCallBack() {
            @Override
            public void onCompleted(TreeUserDTO treeUserDTO) {
                setTitle(treeUserDTO.getName());
                GroupNoTemp = treeUserDTO.getId();
                setupViewPager(sortTypeTemp, GroupNoTemp);
            }
        });
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
            case R.id.action_name:
                resetCheckedMenu();
                item.setChecked(true);
                setupViewPager(0, GroupNoTemp);
                return true;
            case R.id.action_soonest:
                resetCheckedMenu();
                item.setChecked(true);
                setupViewPager(1, GroupNoTemp);
                return true;
            case R.id.action_latest:
                resetCheckedMenu();
                item.setChecked(true);
                setupViewPager(2, GroupNoTemp);
                return true;
            case R.id.action_working_time:
                resetCheckedMenu();
                item.setChecked(true);
                setupViewPager(3, GroupNoTemp);
                return true;
            case R.id.action_organization:
                organization();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}