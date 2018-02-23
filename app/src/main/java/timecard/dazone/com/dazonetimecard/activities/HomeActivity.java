package timecard.dazone.com.dazonetimecard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timecard.dazone.com.dazonetimecard.BuildConfig;
import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.DrawerListAdapter;
import timecard.dazone.com.dazonetimecard.database.UserDBHelper;
import timecard.dazone.com.dazonetimecard.dtos.AllowDevices;
import timecard.dazone.com.dazonetimecard.dtos.ContentAllow;
import timecard.dazone.com.dazonetimecard.dtos.DataDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.MenuDto;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.dtos.UserDto;
import timecard.dazone.com.dazonetimecard.dtos.UserInfoDto;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.DataHttpCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.IGetListDepart;
import timecard.dazone.com.dazonetimecard.interfaces.OnCheckAllowDevice;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public abstract class HomeActivity extends BaseActivity implements BaseHTTPCallBack, DataHttpCallBack, View.OnClickListener, OnCheckAllowDevice {
    private String TAG = "HomeActivity";
    protected ActionBarDrawerToggle mDrawerToggle;
    ArrayList<MenuDto> mNavItems = new ArrayList<>();
    RelativeLayout mDrawerPane, main_content;
    RelativeLayout action_bar_rlr;
    protected DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    protected DrawerListAdapter adapter;
    TextView userName, companyName, tvName, tvEmail;
    ImageView avatar, mIvSetting;
    protected int isAdmin = 0;
    protected UserDto userDto;

    protected LinearLayout layoutMain;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
            case R.id.iv_setting:
                callActivity(ProfileUserActivity.class);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.activity_home, null);
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        //decor.removeView(child);
        decor.addView(drawer);*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        main_content = (RelativeLayout) findViewById(R.id.main_content);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        avatar = (ImageView) findViewById(R.id.iv_avatar);
        avatar.setOnClickListener(this);
        mIvSetting = (ImageView) findViewById(R.id.iv_setting);
        mIvSetting.setOnClickListener(this);
        layoutMain = (LinearLayout) findViewById(R.id.layout_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //enableHomeAction();



        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvName.getLayoutParams();
        params.topMargin = getStatusBarHeight();
        tvName.setLayoutParams(params);*/
        //action_bar_rlr = (RelativeLayout) findViewById(R.id.action_bar_rlr);
        //action_bar_rlr.addView(child);

        HttpRequest.getInstance().checkAllowDevice(this);
        isAdmin = mPrefs.getIsAdmin();
        Log.d(TAG, "isAdmin:" + isAdmin);


        //check disable

        setupDrawerToggle();
        setMainView();
        //action_bar_rlr = (RelativeLayout) findViewById(R.id.action_bar_rlr);
        /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        layoutParams.setMargins(0,0,0,0);
        child.setLayoutParams(layoutParams);
        //layoutMain.addView(child,0);
        //action_bar_rlr.addView(child);
        //action_bar_rlr.setBackgroundColor(ContextCompat.getColor(this,R.color.app_base_color));
        enableHomeAction();*/

        adapter = new DrawerListAdapter(this, mNavItems);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.common_open_on_phone, R.string.common_open_on_phone) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupView(savedInstanceState);
    }

    protected void setupProfile() {
        HttpRequest.getInstance().getUserInfo(this);
    }

    protected void setupView(Bundle savedInstanceState) {
        addFragment(savedInstanceState);
    }

    protected void setMainView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        DaZoneApplication.getInstance().clearCache();
        setupProfile();
    }

    protected void aboutVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.timecard_version));
        String versionName = BuildConfig.VERSION_NAME;
        String user_version = getResources().getString(R.string.user_version) + " " + versionName;
        String msg = user_version;
        builder.setMessage(msg);
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
//        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        if (b != null) {
//            b.setTextColor(ContextCompat.getColor(mContext, R.color.light_black));
//        }
    }

    protected void selectItemFromDrawer(int position) {
        if (isAdmin == 1) {
            switch (position) {
                case 0:
                    startNewActivity(TimeCardActivity.class, position);
                    break;
                case 1:
                    startNewActivity(MyListActivity.class, position);
                    break;
                case 2:
                    startNewActivity(ListEmployeesActivity.class, position);
                    break;
                case 3:
                    startNewActivity(StaffStatusActivity.class, position);
                    break;
                case 4:
                    startNewActivity(IntroActivity.class, position);
                    break;
                case 5:
                    startNewActivity(SettingActivity.class, position);
                    break;
                case 6:
                    aboutVersion();
                    break;
                case 7:
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                            .setMessage(R.string.are_you_sure_loguot)
                            .setPositiveButton(getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    HttpRequest.getInstance().logout(HomeActivity.this);
                                }
                            })
                            .setNegativeButton(getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();

                    break;
            }
        }
//        else if (isAdmin != 0) {
        else if (isAdmin == 2) {
            switch (position) {
                case 0:
                    startNewActivity(TimeCardActivity.class, position);
                    break;
                case 1:
                    startNewActivity(MyListActivity.class, position);
                    break;
                case 2:
                    startNewActivity(ListEmployeesActivity.class, position);
                    break;
                case 3:
                    startNewActivity(StaffStatusActivity.class, position);
                    break;
                case 4:
                    startNewActivity(IntroActivity.class, position);
                    break;
                case 5:
                    aboutVersion();
                    break;
                case 6:
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                            .setMessage(R.string.are_you_sure_loguot)
                            .setPositiveButton(getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    HttpRequest.getInstance().logout(HomeActivity.this);
                                }
                            })
                            .setNegativeButton(getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    startNewActivity(TimeCardActivity.class, position);
                    break;
                case 1:
                    startNewActivity(MyListActivity.class, position);
                    break;
                case 2:
                    startNewActivity(IntroActivity.class, position);
                    break;
                case 3:
                    aboutVersion();
                    break;
                case 4:
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                            .setMessage(R.string.are_you_sure_loguot)
                            .setPositiveButton(getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    HttpRequest.getInstance().logout(HomeActivity.this);
                                }
                            })
                            .setNegativeButton(getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    break;
            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(mDrawerPane);
            }
        }, 150);
    }

    protected abstract void addFragment(Bundle bundle);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    protected void setupDrawerToggle() {
        mNavItems.add(new MenuDto(R.string.side_menu_time_card, R.drawable.menuleft_ic_timecard));
        mNavItems.add(new MenuDto(R.string.side_menu_my_list, R.drawable.menuleft_ic_mylist));

//        if (isAdmin != 0) {
        if (isAdmin == 1 || isAdmin == 2) {
            mNavItems.add(new MenuDto(R.string.side_menu_all_employer, R.drawable.menuleft_ic_stafflist));
            mNavItems.add(new MenuDto(R.string.side_menu_current_status, R.drawable.menuleft_ic_status));
        }

        mNavItems.add(new MenuDto(R.string.side_menu_intro, R.drawable.mnu_left_ic_intro));

        if (isAdmin == 1) {
            mNavItems.add(new MenuDto(R.string.side_menu_setting, R.drawable.menuleft_ic_setting));
        }
        mNavItems.add(new MenuDto(R.string.side_menu_infor, R.drawable.menuleft_ic_pro_info));
        mNavItems.add(new MenuDto(R.string.side_menu_logout, R.drawable.menuleft_ic_logout));

        //mNavItems.add(new MenuDto(R.string.side_menu_produc, R.drawable.home_menu_crew_store, true));
        //mNavItems.add(new MenuDto(R.string.side_menu_product_info, R.drawable.menuleft_ic_pro_info));
    }

    @Override
    public void onHTTPSuccess() {
        mPrefs.clearLogin();
        startSingleActivity(LoginActivity.class);
    }

    @Override
    public void onHTTPFail(ErrorDto errorDto) {
//        mPrefs.clearLogin();
//        startSingleActivity(LoginActivity.class);
        Util.printLogs(errorDto.message);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerPane)) {
            mDrawerLayout.closeDrawer(mDrawerPane);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDataHTTPSuccess(DataDto dto) {
        UserInfoDto userInfoDto = (UserInfoDto) dto;
        userDto = UserDBHelper.getUser();
        userDto.FullName = userInfoDto.FullName;
        userDto.NameCompany = userInfoDto.CompanyName;
        userDto.MailAddress = userInfoDto.MailAddress;
//        new Prefs().putStringValue(Statics.PREFS_KEY_COMPANY_NAME, userInfoDto.CompanyName);
        userName = (TextView) findViewById(R.id.tv_name);
        companyName = (TextView) findViewById(R.id.companyName);
        userName.setText(userDto.FullName);
        companyName.setText(userDto.NameCompany);
        final int size = (Util.getDimenInPx(R.dimen.avatar_user_dimen_custom_45));
        Util.drawCycleImage(avatar, R.drawable.avatar, size);
        showCycleImageFromLink(mPrefs.getServerSite() + userDto.avatar, avatar, R.dimen.avatar_user_dimen_custom_45);
//        ádasldalsdasdasdasdasd
        tvEmail.setText(userDto.MailAddress);

        Log.d("lch", mPrefs.getServerSite());
    }

    @Override
    public void onDataHTTPFail(ErrorDto errorDto) {
        userDto = UserDBHelper.getUser();
        userName = (TextView) findViewById(R.id.userName);
        companyName = (TextView) findViewById(R.id.companyName);
        userName.setText(userDto.FullName);
        companyName.setText(userDto.NameCompany);
        avatar = (ImageView) findViewById(R.id.avatar);
        final int size = (Util.getDimenInPx(R.dimen.avatar_dimen));
        Util.drawCycleImage(avatar, R.drawable.avatar, size);
        showCycleImageFromLink(mPrefs.getServerSite() + userDto.avatar, avatar, R.dimen.avatar_dimen);

    }

    public boolean closeMenu() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    @Override
    public void onGetMyListSuccess(AllowDevices myListDtos) {
        if (myListDtos.getContentAllow() != null) {
            Type listType = new TypeToken<List<ContentAllow>>() {
            }.getType();
            List<ContentAllow> contentAllow = new Gson().fromJson(myListDtos.getContentAllow(), listType);

            Prefs prefs = new Prefs();
            prefs.putBooleanValue(Statics.IS_CHECK_ALLOW, contentAllow.get(1).isAllow());
        }
    }

    @Override
    public void onGetMyListFail(ErrorDto errorDto) {
    }
}