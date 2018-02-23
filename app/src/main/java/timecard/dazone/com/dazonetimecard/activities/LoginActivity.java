package timecard.dazone.com.dazonetimecard.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.Until;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.customviews.SoftKeyboardDetectorView;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnAutoLoginCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnHasAppCallBack;
import timecard.dazone.com.dazonetimecard.utils.Constant;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class LoginActivity extends BaseActivity implements BaseHTTPCallBack, OnHasAppCallBack {
    private Context context;
    private String TAG = "LoginActivity";
    private RelativeLayout include_logo;
    private ImageView img_login_logo;
    private TextView tv_login_logo_text;
    private EditText login_edt_server, login_edt_username, login_edt_password;
    private RelativeLayout login_btn_login;
    private LinearLayout ll_login_sign_up;

    private boolean mFirstLogin = true;
    private boolean mHasIntro = false;
    private String mInputUsername, mInputPassword;

    protected int mActivityNumber = 0;
    private boolean mFirstStart = false;

    private boolean isAutoLoginShow = false;
    private FrameLayout iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate");
        context = this;
        include_logo = (RelativeLayout) findViewById(R.id.include_logo);
        include_logo.setVisibility(View.VISIBLE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(mAccountReceiver, intentFilter);

        final SoftKeyboardDetectorView softKeyboardDetectorView = new SoftKeyboardDetectorView(this);
        addContentView(softKeyboardDetectorView, new FrameLayout.LayoutParams(-1, -1));

        softKeyboardDetectorView.setOnShownKeyboard(new SoftKeyboardDetectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() {
                if (img_login_logo != null) {
                    img_login_logo.setVisibility(View.GONE);
                    ll_login_sign_up.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_login_logo_text.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    tv_login_logo_text.setLayoutParams(params);
                    tv_login_logo_text.setText(Util.getString(R.string.app_name_2));
                }
            }
        });

        softKeyboardDetectorView.setOnHiddenKeyboard(new SoftKeyboardDetectorView.OnHiddenKeyboardListener() {
            @Override
            public void onHiddenSoftKeyboard() {
                if (img_login_logo != null) {

                    img_login_logo.setVisibility(View.VISIBLE);
                    ll_login_sign_up.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_login_logo_text.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    tv_login_logo_text.setLayoutParams(params);
                    tv_login_logo_text.setText(Util.getString(R.string.app_name));

                }
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.myColor_PrimaryDark));
        }

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getInt("count_id") != 0) {
            mActivityNumber = bundle.getInt("count_id");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAccountReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume:"+mIsSettingPermission);
        if (!mIsSettingPermission) {
            if (mPrefs.getIntroCount() < 1) {
                mFirstStart = true;
                mPrefs.putaesorttype(2);
            }

            if (checkPermissions()) {
                initAtStart();
            } else {
//                mIsSettingPermission = true;
                setPermissions();
            }
        }
    }

    // ----------------------------------------------------------------------------------------------

    private boolean mIsSettingPermission = false;

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
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }


        return true;
    }

    private final int MY_PERMISSIONS_REQUEST_CODE = 1;

    private void setPermissions() {
        String[] requestPermission = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.EXPAND_STATUS_BAR

        };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            requestPermission[requestPermission.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        ActivityCompat.requestPermissions(this, requestPermission, MY_PERMISSIONS_REQUEST_CODE);
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
            initAtStart();
        } else {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // ----------------------------------------------------------------------------------------------

    public void initAtStart() {
        Log.d(TAG,"mPrefs.getIntroCount():"+mPrefs.getIntroCount());
        if (mPrefs.getIntroCount() < 2 && !mHasIntro && mActivityNumber == 0) {
            callActivity(IntroActivity.class);
            mHasIntro = true;
            Log.d(TAG,"initAtStart 1");
        } else {
            firstChecking();
            Log.d(TAG,"initAtStart 2");
        }
    }

    private void firstChecking() {
        if (mFirstLogin) {
            include_logo = (RelativeLayout) findViewById(R.id.include_logo);

            if (Util.isNetworkAvailable()) {
                Log.d(TAG, "firstChecking isNetworkAvailable");
                if (mFirstStart) {
                    doLogin();
                    mFirstStart = false;
                } else {
                    doLogin();
                }
            } else {
                showNetworkDialog();
            }
        }
    }

    private void doLogin() {
        if (Util.checkStringValue(mPrefs.getAccessToken()) && !mPrefs.getBooleanValue(Statics.PREFS_KEY_SESSION_ERROR, false)) {
            HttpRequest.getInstance().checkLogin(this);
        } else {
            mPrefs.putBooleanValue(Statics.PREFS_KEY_SESSION_ERROR, false);
            include_logo.setVisibility(View.GONE);
            mFirstLogin = false;
            init();
        }
    }

    @Override
    public void showNetworkDialog() {
        Log.d(TAG, "showNetworkDialog");

        if (Util.isMobileEnable()) {
            Log.d(TAG, "isMobileEnable but not connect internet");
            displayAddAlertDialog(getString(R.string.app_name), getString(R.string.no_connection_error), getString(R.string.string_ok), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }, null);
        } else {
            Log.d(TAG, "isMobileEnable = false");
            if (Util.isWifiEnable()) {
                displayAddAlertDialog(getString(R.string.app_name), getString(R.string.no_connection_error), getString(R.string.string_ok), null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }, null);
            } else {
                displayAddAlertDialog(getString(R.string.app_name), getString(R.string.no_wifi_error), getString(R.string.turn_wifi_on), getString(R.string.string_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent wireLess = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(wireLess);
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
            }
        }
    }

    public static String BROADCAST_ACTION = "com.dazone.crewcloud.account.receive";

    private BroadcastReceiver mAccountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receiverPackageName = intent.getExtras().getString("receiverPackageName");
            if (LoginActivity.this.getPackageName().equals(receiverPackageName)) {
                //String senderPackageName = intent.getExtras().getString("senderPackageName");
                String companyID = intent.getExtras().getString("companyID");
                String userID = intent.getExtras().getString("userID");
                if (!TextUtils.isEmpty(companyID) && !TextUtils.isEmpty(userID) && !isAutoLoginShow) {
                    isAutoLoginShow = true;
                    showPopupAutoLogin(companyID, userID);
                }
            }
        }
    };

    private void showPopupAutoLogin(final String companyID, final String userID) {
        /*AutoLoginFragment cdd = new AutoLoginFragment(this, companyID, userID);
        cdd.setTitle(Util.getString(R.string.auto_login_title));
        cdd.show();*/

        String alert1 = Util.getString(R.string.auto_login_company_ID) + companyID;
        String alert2 = Util.getString(R.string.auto_login_user_ID) + userID;
        String alert3 = Util.getString(R.string.auto_login_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(Util.getString(R.string.auto_login_title))
                .setMessage(alert1 + "\n" + alert2 + "\n\n" + alert3)
                .setPositiveButton(Util.getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        autoLogin(companyID, userID);
                    }
                })
                .setNegativeButton(Util.getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //builder.show();

        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        if (textView != null) {
            //textView.setTextSize(18);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        /*FragmentManager fm = getSupportFragmentManager();
        AutoLoginFragment autoLoginFragment = new AutoLoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("companyID", companyID);
        bundle.putString("userID", userID);
        autoLoginFragment.setArguments(bundle);
        autoLoginFragment.show(fm, "AutoLogin");*/
    }

    public void autoLogin(String companyID, String userID) {
        Log.d(TAG, "autoLogin");
        mInputUsername = userID;
        server_site = companyID;

        strServer = companyID;
        server_site = getServerSite(server_site);
        String company_domain = server_site;
        String temp_server_site = server_site;

        if (!company_domain.startsWith("http")) {
            server_site = "http://" + server_site;
        }

        if (temp_server_site.contains(".bizsw.co.kr")) {
            temp_server_site = "http://www.bizsw.co.kr:8080";
        } else {
            if (temp_server_site.contains("crewcloud")) {
                temp_server_site = "http://www.crewcloud.net";
            }
        }
        showProgressDialog();
        HttpRequest.getInstance().AutoLogin(company_domain, mInputUsername, temp_server_site, new OnAutoLoginCallBack() {
            @Override
            public void OnAutoLoginSuccess(String response) {
                if (!TextUtils.isEmpty(server_site)) {
                    DaZoneApplication.getInstance().getmPrefs().putUserName(mInputUsername);
                    DaZoneApplication.getInstance().getmPrefs().putServerSite(server_site);
                    DaZoneApplication.getInstance().getmPrefs().putServer(strServer);
                }

                loginSuccess();
            }

            @Override
            public void OnAutoLoginFail(ErrorDto dto) {
                if (mFirstLogin) {
                    dismissProgressDialog();

                    mFirstLogin = false;
                    include_logo.setVisibility(View.GONE);
                    init();
                } else {
                    dismissProgressDialog();
                    String error_msg = dto.message;

                    if (TextUtils.isEmpty(error_msg)) {
                        error_msg = getString(R.string.connection_falsed);
                    }

                    showSaveDialog(error_msg);
                }
            }
        });
    }

    boolean isDisplayPass = true;

    void displayPass() {
        if (isDisplayPass) {
            login_edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isDisplayPass = false;
        } else {
            login_edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isDisplayPass = true;
        }
    }

    private void init() {

        /** SEND BROADCAST */
        Intent intent = new Intent();
        intent.setAction("com.dazone.crewcloud.account.get");
        intent.putExtra("senderPackageName", this.getPackageName());
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);

        img_login_logo = (ImageView) findViewById(R.id.img_login_logo);
        tv_login_logo_text = (TextView) findViewById(R.id.tv_login_logo_text);
        login_edt_username = (EditText) findViewById(R.id.login_edt_username);
        login_edt_password = (EditText) findViewById(R.id.login_edt_password);
        login_edt_server = (EditText) findViewById(R.id.login_edt_server);
        ll_login_sign_up = (LinearLayout) findViewById(R.id.ll_login_sign_up);

        iv = (FrameLayout) findViewById(R.id.iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPass();
            }
        });

        login_edt_username.setPrivateImeOptions("defaultInputmode=english;");
        login_edt_server.setPrivateImeOptions("defaultInputmode=english;");

        Prefs prefs = DaZoneApplication.getInstance().getmPrefs();
        login_edt_username.setText(prefs.getUserName());
        login_edt_server.setText(prefs.getServer());

        // new
        login_edt_password.setText(prefs.getPassword());

        login_edt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    login_edt_username.setText(result);
                    login_edt_username.setSelection(result.length());
                }
            }
        });

        login_edt_server.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");

                if (!s.toString().equals(result)) {
                    login_edt_server.setText(result);
                    login_edt_server.setSelection(result.length());
                }
            }
        });

        login_edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login_btn_login.callOnClick();
                }

                return false;
            }
        });

        login_btn_login = (RelativeLayout) findViewById(R.id.login_btn_login);
        RelativeLayout login_btn_sign_up = (RelativeLayout) findViewById(R.id.login_btn_sign_up);

        if (login_btn_sign_up != null) {
            login_btn_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        if (login_btn_login != null) {
            login_btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "getIMEI:" + Constant.getIMEI(context));

                    mInputUsername = login_edt_username.getText().toString();
                    mInputPassword = login_edt_password.getText().toString();
                    server_site = login_edt_server.getText().toString();

                    if (TextUtils.isEmpty(checkStringValue(server_site, mInputUsername, mInputPassword))) {
                        strServer = login_edt_server.getText().toString();
                        server_site = getServerSite(server_site);
                        String company_domain = server_site;

                        if (!company_domain.startsWith("http")) {
                            server_site = "http://" + server_site;
                        }

                        String temp_server_site = server_site;

                        if (temp_server_site.contains(".bizsw.co.kr")) {
                            temp_server_site = "http://www.bizsw.co.kr:8080";
                        } else {
                            if (temp_server_site.contains("crewcloud")) {
                                temp_server_site = "http://www.crewcloud.net";
                            }
                        }

                        showProgressDialog();
                        HttpRequest.getInstance().login(LoginActivity.this, mInputUsername, mInputPassword, company_domain, temp_server_site, server_site);
                    } else {
                        displayAddAlertDialog(getString(R.string.app_name), checkStringValue(server_site, mInputUsername, mInputPassword), getString(R.string.string_ok), null,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }, null);
                    }
                }
            });
        }
    }


    private String checkStringValue(String server_site, String username, String password) {
        String result = "";

        if (TextUtils.isEmpty(server_site)) {
            result += getString(R.string.string_server_site);
        }

        if (TextUtils.isEmpty(username)) {
            if (TextUtils.isEmpty(result)) {
                result += getString(R.string.login_username);
            } else {
                result += ", " + getString(R.string.login_username);
            }
        }

        if (TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(result)) {
                result += getString(R.string.login_password);
            } else {
                result += ", " + getString(R.string.login_password);
            }
        }

        if (TextUtils.isEmpty(result)) {
            return result;
        } else {
            return result + " " + getString(R.string.login_empty_input);
        }
    }

    private String getServerSite(String server_site) {
        String[] domains = server_site.split("[.]");

        if (server_site.contains(".bizsw.co.kr") && !server_site.contains("8080")) {
            return server_site.replace(".bizsw.co.kr", ".bizsw.co.kr:8080");
        }

        if (domains.length <= 1 || server_site.contains("crewcloud")) {
            return domains[0] + ".crewcloud.net";
        } else {
            return server_site;
        }
    }

    @Override
    public void onHTTPSuccess() {
        Log.d(TAG, "onHTTPSuccess");
        if (!TextUtils.isEmpty(server_site)) {
            DaZoneApplication.getInstance().getmPrefs().putServer(strServer);
            DaZoneApplication.getInstance().getmPrefs().putServerSite(server_site);
        }

        loginSuccess();


    }

    private void loginSuccess() {
        dismissProgressDialog();

        callActivity(TimeCardActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onHTTPFail(ErrorDto errorDto) {
        if (mFirstLogin) {
            dismissProgressDialog();

            mFirstLogin = false;
            include_logo.setVisibility(View.GONE);
            init();
        } else {
            dismissProgressDialog();
            String error_msg = errorDto.message;

            if (TextUtils.isEmpty(error_msg)) {
                error_msg = getString(R.string.connection_falsed);
            }

            showSaveDialog(error_msg);
        }
    }

    @Override
    public void hasApp() {
        loginSuccess();
    }

    @Override
    public void noHas(ErrorDto errorDto) {
        if (mFirstLogin) {
            mFirstLogin = false;
            include_logo.setVisibility(View.GONE);
            init();
        } else {
            dismissProgressDialog();
            showSaveDialog(errorDto.message);
        }
    }

    private void showSaveDialog(String message) {
        displayAddAlertDialog(getString(R.string.app_name), message, getString(R.string.string_ok), null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }, null);
    }


}