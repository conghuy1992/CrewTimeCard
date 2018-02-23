package timecard.dazone.com.dazonetimecard.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.IntroPageAdapter;
import timecard.dazone.com.dazonetimecard.dtos.AllowDevices;
import timecard.dazone.com.dazonetimecard.dtos.ContentAllow;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;

public class IntroActivity extends BaseActivity {
    private String TAG = "IntroActivity";
    protected ViewPager main_vpg_main;
    protected TextView skip_intro;
    protected LinearLayout dot_lnl;
    private List<ImageView> dots;

    private IntroPageAdapter mainSelectionPagerAdapter;
    private int max = Statics.INTRO_PAGE_NUMBER;

    public static String BROADCAST_ACTION = "com.dazone.crewcloud.account.receive";
    BroadcastReceiver accountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receiverPackageName = intent.getExtras().getString("receiverPackageName");
            if (IntroActivity.this.getPackageName().equals(receiverPackageName)) {
                //String senderPackageName = intent.getExtras().getString("senderPackageName");
                String companyID = intent.getExtras().getString("companyID");
                String userID = intent.getExtras().getString("userID");
                if (!TextUtils.isEmpty(companyID) && !TextUtils.isEmpty(userID)) {
                    dot_lnl.removeAllViews();
                    if (mainSelectionPagerAdapter != null) {
                        max = 4;
                        dots = new ArrayList<>();

                        for (int i = 0; i < 4; i++) {
                            ImageView dot = new ImageView(IntroActivity.this);

                            if (i == main_vpg_main.getCurrentItem()) {
                                dot.setImageResource(R.drawable.slider_intro_ellip_ic01);
                            } else {
                                dot.setImageResource(R.drawable.slider_intro_ellip_ic02);
                            }

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            params.rightMargin = 20;
                            params.leftMargin = 20;
                            dot_lnl.addView(dot, params);

                            dots.add(dot);
                        }
                        mainSelectionPagerAdapter.update(4);
                        mainSelectionPagerAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Log.d(TAG,"onCreate");
//        HttpRequest.getInstance().checkAllowDevice(this);
        if (checkPermissions()) {
            startApplication();
        } else {
            setPermissions();
          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.set_permissions);
            builder.setPositiveButton(R.string.string_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(accountReceiver);
    }

    public void startApplication() {
        init();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(accountReceiver, intentFilter);

        /** SEND BROADCAST */
        Intent intent = new Intent();
        intent.setAction("com.dazone.crewcloud.account.get");
        intent.putExtra("senderPackageName", this.getPackageName());
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
        addDots(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return;
        }

        boolean isGranted = true;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        if (isGranted) {
            startApplication();
        } else {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void setPermissions() {
        String[] requestPermission = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.EXPAND_STATUS_BAR
        };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            requestPermission[requestPermission.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        ActivityCompat.requestPermissions(this, requestPermission, MY_PERMISSIONS_REQUEST_CODE);
    }

    private final int MY_PERMISSIONS_REQUEST_CODE = 1;

    protected void init() {
        skip_intro = (TextView) findViewById(R.id.skip_intro);

        if (mPrefs.getIntroCount() <= 0) {
            skip_intro.setVisibility(View.GONE);
        }

        main_vpg_main = (ViewPager) findViewById(R.id.main_vpg_main);
        initAdapter();

        main_vpg_main.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                addDots(position);
                if (position == max - 1) {
                    if (skip_intro.getVisibility() == View.GONE) {
                        skip_intro.setVisibility(View.VISIBLE);
                    }
                    skip_intro.setText(R.string.string_done);
                }
            }
        });

        dot_lnl = (LinearLayout) findViewById(R.id.dot_lnl);
        skip_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "skip_intro");
                finish();
            }
        });
        Log.d(TAG,"putIntroCount");
        mPrefs.putIntroCount(mPrefs.getIntroCount() + 1);
    }

    protected void initAdapter() {
        mainSelectionPagerAdapter = new IntroPageAdapter(getSupportFragmentManager());
        main_vpg_main.setAdapter(mainSelectionPagerAdapter);
    }

    private void addDots(int position) {
        if (dots == null) {
            dots = new ArrayList<>();

            for (int i = 0; i < Statics.INTRO_PAGE_NUMBER; i++) {
                ImageView dot = new ImageView(this);

                if (i == position) {
                    dot.setImageResource(R.drawable.slider_intro_ellip_ic01);
                } else {
                    dot.setImageResource(R.drawable.slider_intro_ellip_ic02);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.rightMargin = 20;
                params.leftMargin = 20;
                dot_lnl.addView(dot, params);

                dots.add(dot);
            }
        } else {
            for (int i = 0; i < max; i++) {
                if (i == position) {
                    dots.get(i).setImageResource(R.drawable.slider_intro_ellip_ic01);
                } else {
                    dots.get(i).setImageResource(R.drawable.slider_intro_ellip_ic02);
                }
            }
        }
    }

//    @Override
//    public void onGetMyListSuccess(AllowDevices myListDtos) {
//        if (myListDtos.getContentAllow() != null) {
//            Type listType = new TypeToken<List<ContentAllow>>() {
//            }.getType();
//            List<ContentAllow> contentAllow = new Gson().fromJson(myListDtos.getContentAllow(), listType);
//
//            Prefs prefs = new Prefs();
//            prefs.putBooleanValue(Statics.IS_CHECK_ALLOW, contentAllow.get(1).isAllow());
//        }
//    }
//
//    @Override
//    public void onGetMyListFail(ErrorDto errorDto) {
//
//    }
}