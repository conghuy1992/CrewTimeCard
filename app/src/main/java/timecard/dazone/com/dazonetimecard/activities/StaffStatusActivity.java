package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.customviews.OrganizationDialog;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.fragments.StaffStatusFragment;
import timecard.dazone.com.dazonetimecard.fragments.TimeCardFragment;
import timecard.dazone.com.dazonetimecard.interfaces.ChooseGroupNoCallBack;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class StaffStatusActivity extends HomeActivity {
    //    Bundle mBundle;
//    private MenuItem menuRouteItem;
    private String TAG = "StaffStatusActivity";
    private Menu menu;

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrefs.getReloadStatus()) {
            mPrefs.putReloadStatus(false);
            addFragment(0, 0);
        }
    }

    @Override
    protected void addFragment(Bundle bundle) {
        if (bundle == null) {
            long timeForPosition = Calendar.getInstance().getTimeInMillis();
            Util.addFragmentToActivity(getSupportFragmentManager(), StaffStatusFragment.newInstance(timeForPosition, 0, 0), R.id.main_content, false);
        }
    }

    private int sortTypeTemp = 0;
    private int groupNoTemp = 0;

    protected void addFragment(int sortType, int groupNo) {
        sortTypeTemp = sortType;
        long timeForPosition = Calendar.getInstance().getTimeInMillis();
        Util.addFragmentToActivity(getSupportFragmentManager(), StaffStatusFragment.newInstance(timeForPosition, sortType, groupNo), R.id.main_content, false);
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
        getMenuInflater().inflate(R.menu.menu_staff_status, menu);

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

        MenuItem working = menu.findItem(R.id.action_working);
        working.setTitle(Html.fromHtml("<font color='" + getResources().getColor(R.color.working_color) + "'>" + getString(R.string.menu_action_working) + " </font>"));
        MenuItem not_working = menu.findItem(R.id.action_not_working);
        not_working.setTitle(Html.fromHtml("<font color='" + getResources().getColor(R.color.work_outside_color) + "'>" + getString(R.string.menu_action_not_working) + " </font>"));
        MenuItem absent = menu.findItem(R.id.action_absent);
        absent.setTitle(Html.fromHtml("<font color='" + getResources().getColor(R.color.absent_color) + "'>" + getString(R.string.menu_action_absent) + " </font>"));
        return true;
    }

    private void resetCheckedMenu() {
        MenuItem all = menu.findItem(R.id.action_all);
        all.setChecked(false);
        MenuItem working = menu.findItem(R.id.action_working);
        working.setChecked(false);
        MenuItem not_working = menu.findItem(R.id.action_not_working);
        not_working.setChecked(false);
        MenuItem absent = menu.findItem(R.id.action_absent);
        absent.setChecked(false);
        MenuItem checkOut = menu.findItem(R.id.action_check_out);
        checkOut.setChecked(false);
    }

    void organization() {
        new OrganizationDialog(this, new ChooseGroupNoCallBack() {
            @Override
            public void onCompleted(TreeUserDTO treeUserDTO ) {
                setTitle(treeUserDTO.getName());
                groupNoTemp = treeUserDTO.getId();
                addFragment(sortTypeTemp, groupNoTemp);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        switch (item.getItemId()) {
            case R.id.action_all:
                resetCheckedMenu();
                item.setChecked(true);
                addFragment(0, groupNoTemp);
                return true;
            case R.id.action_working:
                resetCheckedMenu();
                item.setChecked(true);
                addFragment(1, groupNoTemp);
                return true;
            case R.id.action_not_working:
                resetCheckedMenu();
                item.setChecked(true);
                addFragment(2, groupNoTemp);
                return true;
            case R.id.action_absent:
                resetCheckedMenu();
                item.setChecked(true);
                addFragment(3, groupNoTemp);
                return true;
            case R.id.action_check_out:
                resetCheckedMenu();
                item.setChecked(true);
                addFragment(4, groupNoTemp);
                return true;

            case R.id.action_organization:
                organization();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
