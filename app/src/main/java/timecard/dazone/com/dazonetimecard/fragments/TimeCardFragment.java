package timecard.dazone.com.dazonetimecard.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import timecard.dazone.com.dazonetimecard.BuildConfig;
import timecard.dazone.com.dazonetimecard.activities.HomeActivity;
import timecard.dazone.com.dazonetimecard.activities.TimeCardActivity;
import timecard.dazone.com.dazonetimecard.adapters.SpinnerDisableAdapter;
import timecard.dazone.com.dazonetimecard.dtos.AllowDevices;
import timecard.dazone.com.dazonetimecard.dtos.BelongDepartmentDTO;
import timecard.dazone.com.dazonetimecard.dtos.ContentAllow;
import timecard.dazone.com.dazonetimecard.dtos.MyListDto;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTOTemp;
import timecard.dazone.com.dazonetimecard.interfaces.GetServerTimeCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.IGetListDepart;
import timecard.dazone.com.dazonetimecard.interfaces.OnCheckAllowDevice;
import timecard.dazone.com.dazonetimecard.locationutil.LocationTracker;
import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.MyListActivity;
import timecard.dazone.com.dazonetimecard.activities.OfficeMapViewActivity;
import timecard.dazone.com.dazonetimecard.adapters.SpinnerAdapter;
import timecard.dazone.com.dazonetimecard.database.CompanyDBHelper;
import timecard.dazone.com.dazonetimecard.dtos.CompanyDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.MenuDto;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.LocationChangeCallback;
import timecard.dazone.com.dazonetimecard.interfaces.OnCallGetMessageValue;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetStatusResponse;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetUserInfoCallBack;
import timecard.dazone.com.dazonetimecard.utils.Constant;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.DialogUtil;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class TimeCardFragment extends Fragment implements BaseHTTPCallBack, OnGetUserInfoCallBack, OnGetStatusResponse, OnCallGetMessageValue, LocationChangeCallback {
    private String TAG = "TimeCardFragment";
    public static TimeCardFragment instance = null;
    private LinearLayout lnl_map;
    private RelativeLayout note_lnl, rlStatus;
    private TextView day_time, day_of_year, hello_tv, am_pm, note_dismiss, note_content;
    private ImageView home_note;
    private TextView home_save_btn, tvStatus;
    private Spinner type_spinner;
    private Boolean isCheck;
    private List<CompanyDto> companyDtos;
    private ProgressBar progressBar;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_card, container, false);


        instance = this;
        GetListDepart();

        isCheck = new Prefs().getBooleanValue(Statics.IS_CHECK_ALLOW, true);

        companyDtos = CompanyDBHelper.getAllCompanyInfo();
        lnl_map = (LinearLayout) v.findViewById(R.id.lnl_map);
        note_lnl = (RelativeLayout) v.findViewById(R.id.note_lnl);
        note_content = (TextView) v.findViewById(R.id.note_content);
        home_save_btn = (TextView) v.findViewById(R.id.home_save_btn);
        day_time = (TextView) v.findViewById(R.id.day_time);
        tvStatus = (TextView) v.findViewById(R.id.status);
        am_pm = (TextView) v.findViewById(R.id.am_pm);
        note_dismiss = (TextView) v.findViewById(R.id.note_dismiss);
        day_of_year = (TextView) v.findViewById(R.id.day_of_year);
        hello_tv = (TextView) v.findViewById(R.id.hello_tv);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        note_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_lnl.setVisibility(View.GONE);
                note_content.setText(null);
                note = "";
            }
        });

        note_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddCommentDialog();
            }
        });

        if (Util.getPhoneLanguage().equalsIgnoreCase("KO")) {
            ViewGroup view = (ViewGroup) day_time.getParent();
            view.removeView(day_time);
            view.addView(day_time);
        }

//        updateTime();

        hello_tv.setSelected(true);
        hello_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.displaySimpleAlert(BaseActivity.Instance, -1, getString(R.string.app_name),
                        hello_tv.getText().toString(), BaseActivity.Instance.getString(R.string.string_ok), null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }, null);
            }
        });

        long serverTime = DaZoneApplication.getInstance().getmPrefs().getServerTime();
        day_of_year.setText(Util.setDateInfo(serverTime, Statics.FORMAT_DATE_MONTH_YEAR) + " " + Util.setDateInfo(serverTime, Statics.FORMAT_DATE_OF_WEEK));
        type_spinner = (Spinner) v.findViewById(R.id.type_spinner);
        rlStatus = (RelativeLayout) v.findViewById(R.id.rl_status);
        tempArray = DaZoneApplication.getInstance().getResources().getStringArray(R.array.list_home_status);
        Log.d(TAG, "setTimeType: 1");
        setTimeType(0);

        setupDrawerToggle();
        if (isCheck) {
            home_save_btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_btn));
            rlStatus.setVisibility(View.GONE);
            type_spinner.setVisibility(View.VISIBLE);


        } else {
            home_save_btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_spinner));
            type_spinner.setVisibility(View.GONE);
            rlStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(mNavItems.get(getTimeType()).mTitle);

        }

        rlStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.not_permission)
                        .setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();


                            }
                        });
                builder.show();
            }
        });

        home_note = (ImageView) v.findViewById(R.id.home_note);
        home_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customDialog == null || !customDialog.isShowing()) {
                    displayAddCommentDialog();
                }
            }
        });

        home_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "isSave:" + isSave);
//                Log.d(TAG, "isMockSettingsON:" + isMockSettingsON(getActivity()));

//                Toast.makeText(getActivity(), "isMockSettingsON:" + isMockSettingsON(getActivity()), Toast.LENGTH_SHORT).show();
                if (isMock || isMockSettingsON(getActivity())) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.please_change_your_mock), Toast.LENGTH_SHORT).show();
                    devOption();
                } else {
                    if (isSave) {
                        isCheck = new Prefs().getBooleanValue(Statics.IS_CHECK_ALLOW, true);
                        if (isCheck) {
                            if (latitude != 0 || longitude != 0) {
                                if (companyDtos != null && companyDtos.size() != 0) {
                                    BaseActivity.Instance.showProgressDialog();
                                    HttpRequest.getInstance().insertTimeCards(TimeCardFragment.this, latitude, longitude, note, getTimeType(), dist, latLng.latitude, latLng.longitude, currentLocationNo);
                                } else {
                                    Util.showMessage(R.string.string_no_office);
                                }
                            } else {
                                Util.showMessage(R.string.waiting_map_api);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.not_permission)
                                    .setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Can not get time server, try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        addMap();

        return v;
    }

    private AlertDialog dialog;

    private void devOption() {
        if (dialog != null && dialog.isShowing()) {
            Log.d(TAG,"devOption dont show");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.app_name_icon));
            builder.setMessage(getResources().getString(R.string.please_change_your_mock));
            builder.setCancelable(false);
            builder.setPositiveButton(getResources().getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }

    private void GetListDepart() {
        int isAdmin = DaZoneApplication.getInstance().getmPrefs().getIsAdmin();
        if (isAdmin == 1 || isAdmin == 2) {
            Log.d(TAG, "GetListDepart");
            HttpRequest.getInstance().GetListDepart(new IGetListDepart() {
                @Override
                public void onGetListDepartSuccess(final ArrayList<TreeUserDTO> treeUserDTOs) {
                    if (treeUserDTOs != null && treeUserDTOs.size() > 0) {

                        for (TreeUserDTO obj : treeUserDTOs) {
                            obj.setParent(0);
                        }

                        Log.d(TAG, "onGetListDepartSuccess");
                        buildTree(treeUserDTOs, true);
                    }
                }

                @Override
                public void onGetListDepartFail(ErrorDto dto) {
                    Log.d(TAG, "onGetListDepartFail");

                }
            });
        } else {
            Log.d(TAG, "Staff dont need call api");
        }
    }

    private ArrayList<TreeUserDTO> mPersonList = new ArrayList<>();
    private ArrayList<TreeUserDTO> mDepartmentList = new ArrayList<>();
    private ArrayList<TreeUserDTO> temp = new ArrayList<>();
    private ArrayList<TreeUserDTOTemp> listTemp = new ArrayList<>();

    public void convertData(List<TreeUserDTO> treeUserDTOs) {
        if (treeUserDTOs != null && treeUserDTOs.size() != 0) {
            for (TreeUserDTO dto : treeUserDTOs) {
                if (dto.getSubordinates() != null && dto.getSubordinates().size() > 0) {
                    temp.add(dto);
                    convertData(dto.getSubordinates());
                } else {
                    temp.add(dto);
                }
            }
        }
    }

    private void buildTree(final ArrayList<TreeUserDTO> treeUserDTOs, final boolean isFromServer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (treeUserDTOs != null) {
                    if (isFromServer) {
                        convertData(treeUserDTOs);
                        mDepartmentList.clear();
                        mDepartmentList.addAll(temp);

                    } else {
                        temp.clear();
                        temp.addAll(treeUserDTOs);
                    }
                    for (TreeUserDTO treeUserDTO : temp) {
                        if (treeUserDTO.getSubordinates() != null && treeUserDTO.getSubordinates().size() > 0) {
                            treeUserDTO.setSubordinates(null);
                        }
                    }
                    // sort data by order
                    Collections.sort(temp, new Comparator<TreeUserDTO>() {
                        @Override
                        public int compare(TreeUserDTO r1, TreeUserDTO r2) {
                            if (r1.getmSortNo() > r2.getmSortNo()) {
                                return 1;
                            } else if (r1.getmSortNo() == r2.getmSortNo()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    });


                    for (TreeUserDTOTemp treeUserDTOTemp : listTemp) {
                        if (treeUserDTOTemp.getBelongs() != null) {
                            for (BelongDepartmentDTO belong : treeUserDTOTemp.getBelongs()) {
                                TreeUserDTO treeUserDTO = new TreeUserDTO(
                                        treeUserDTOTemp.getName(),
                                        treeUserDTOTemp.getNameEN(),
                                        treeUserDTOTemp.getCellPhone(),
                                        treeUserDTOTemp.getAvatarUrl(),
                                        belong.getPositionName(),
                                        treeUserDTOTemp.getType(),
                                        treeUserDTOTemp.getStatus(),
                                        treeUserDTOTemp.getUserNo(),
                                        belong.getDepartNo(),
                                        treeUserDTOTemp.getUserStatusString(),
                                        belong.getPositionSortNo()
                                );
                                treeUserDTO.setCompanyNumber(treeUserDTOTemp.getCompanyPhone());
                                temp.add(treeUserDTO);
                            }
                        }
                    }
                    mPersonList = new ArrayList<>(temp);
                    Log.d(TAG, "mPersonList:" + mPersonList.size());
                    TreeUserDTO dto = null;
                    try {
                        dto = Constant.buildTree(mPersonList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.obtainMessage(CREATE_TREE, dto).sendToTarget();
                }
            }
        }).start();
    }

    public List<TreeUserDTO> getSubordinates() {
        if (list == null) list = new ArrayList<>();
        return list;
    }

    private List<TreeUserDTO> list = new ArrayList<>();
    private int CREATE_TREE = 102;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == CREATE_TREE) {
                Log.d(TAG, "CREATE_TREE");
                TreeUserDTO dto = (TreeUserDTO) msg.obj;
                if (dto != null) {
                    list = dto.getSubordinates();
                    if (list == null) list = new ArrayList<>();
//                    Log.d(TAG,"list.size:"+new Gson().toJson(list));
                }

            }
        }
    };
    MapView mapView;

    private void addMapLow(LayoutInflater inflater, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_layout_low, null);
        MapsInitializer.initialize(getActivity());
        mapView_distance = (TextView) v.findViewById(R.id.mapview_distance);
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) v.findViewById(R.id.map);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mapView != null) {
                    mapView.onCreate(savedInstanceState);
                    myMap = mapView.getMap();
                    myMap.getUiSettings().setMyLocationButtonEnabled(false);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Util.showMessage(R.string.permission_denied);
                        return;
                    }
                    myMap.setMyLocationEnabled(false);
                    myMap.getUiSettings().setMapToolbarEnabled(false);
                    for (CompanyDto companyDto : companyDtos) {
                        latLng = new LatLng(companyDto.getLat(), companyDto.getLng());
                        myMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(companyDto.Description)).showInfoWindow();
                    }
                    myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if (currentMaker != null) {
                                currentMaker.showInfoWindow();
                            }
                        }
                    });

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10.0f).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    myMap.moveCamera(cameraUpdate);
                    lnl_map.addView(v);
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHTTPSuccess() {
        BaseActivity.Instance.dismissProgressDialog();
        note = "";
        note_lnl.setVisibility(View.GONE);
        Log.d(TAG, "setTimeType 2:" + getTimeType());
        setTimeType(getTimeType());
        BaseActivity.Instance.startNewActivity(MyListActivity.class, 1);
    }

    @Override
    public void onHTTPFail(ErrorDto errorDto) {
        BaseActivity.Instance.dismissProgressDialog();
        String message = errorDto.message;

        if (TextUtils.isEmpty(message)) {
            message = Util.getString(R.string.no_network_error);
        }

        Util.displaySimpleAlert(getActivity(), -1, getString(R.string.app_name), message, "OK", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, null);
    }

    public int getTimeType() {
        switch (type_spinner.getSelectedItemPosition()) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    private void setTimeType(int type) {
        Activity activity = getActivity();
        if (activity != null) {
            switch (type) {
                case 0:
                case 3:
                    setupDrawerToggle();
                    initSpinnerValue();
                    type_spinner.setSelection(0);
                    break;
                case 1:
                case 4:
                    setupDrawerToggle();
                    initSpinnerValue();
                    type_spinner.setSelection(3);
                    break;
                case 2:
                    setupDrawerToggle();
                    initSpinnerValue();
                    type_spinner.setSelection(2);
                    break;
            }
        }
    }

    private boolean isUpdate = false;
    private boolean isSave = false;

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        setUpMapIfNeeded();
        DialogUtil.showGPSModDialog();
        updateCurrentPosition(null);
        updateUserInfo();
        //HttpRequest.getInstance().getUpdateUserInfo(this);
        HttpRequest.getInstance().getServerTime(new GetServerTimeCallBack() {
            @Override
            public void onSuccess() {
                isSave = true;
                Log.d(TAG, "getServerTime onSuccess");
                progressBar.setVisibility(View.GONE);
                if (!isUpdate) {
                    Log.d(TAG, "start update");
                    isUpdate = true;
                    updateTime();
                } else {
                    Log.d(TAG, "dont update because already running");
                }
            }

            @Override
            public void onFail() {
                if (!isUpdate) {
                    if (day_of_year != null) day_of_year.setText("");
                }
                Toast.makeText(getActivity(), "Can not get Server time, try again", Toast.LENGTH_LONG).show();
            }
        });
        HttpRequest.getInstance().checkStatus(this);
        LocationTracker.getInstance().registerLocationService(getActivity(), this);

        if (new Prefs().getBooleanValue(Statics.PREFS_KEY_RELOAD_TIMECARD, false)) {
            new Prefs().putBooleanValue(Statics.PREFS_KEY_RELOAD_TIMECARD, false);
            if (mCurrentLocation != null && map != null) {
                companyDtos = CompanyDBHelper.getAllCompanyInfo();
                myMap.clear();
                addCompanyPosition();
                setUpMap(mCurrentLocation);
            }
        }
        if (isMockSettingsON(getActivity())) {
            devOption();
        }
    }

    private void updateUserInfo() {
        companyDtos = CompanyDBHelper.getAllCompanyInfo();
        if (companyDtos != null && companyDtos.size() != 0)
            for (int i = 0; i < companyDtos.size(); i++) {
                try {
                    mCircles.get(i).setCenter(companyDtos.get(i).getLatLng());
                    mCircles.get(i).setRadius(companyDtos.get(i).ErrorRange);
                    mMarkers.get(i).setPosition(companyDtos.get(i).getLatLng());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    public double latitude = 0;
    public double longitude = 0;
    protected LatLng latLng = null;
    protected int currentLocationNo = 0;

    private void initSpinnerValue() {
        final SpinnerAdapter spinnerArrayAdapter;
        Activity activity = getActivity();
        if (activity != null) {
            spinnerArrayAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_textview_single, mNavItems);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_spinner.setAdapter(spinnerArrayAdapter);
        }

    }

    private AlertDialog customDialog = null;
    private String note = null;

    private void displayAddCommentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_comment, null);
        builder.setView(customView);

        Button btnCancel = (Button) customView.findViewById(R.id.add_btn_cancel);
        Button btnAdd = (Button) customView.findViewById(R.id.add_btn_log_time);

        final EditText edtDescription = (EditText) customView.findViewById(R.id.description);

        edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                int lineCount = edtDescription.getLineCount();
//                if (lineCount > 3) {
//                    String strText = edtDescription.getText().toString();
//                    strText = strText.substring(0, strText.length() - 1);
//                    edtDescription.setText(strText);
//                    int pos = edtDescription.getText().length();
//                    edtDescription.setSelection(pos);
//                }
            }
        });

        edtDescription.setText(note);
        edtDescription.setSelection(edtDescription.getText().length());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                note = null;
                Util.hideKeyboard(getActivity());
                customDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = edtDescription.getText().toString().trim();
                if (!TextUtils.isEmpty(note)) {
                    note_content.setText(note);
                    note_lnl.setVisibility(View.VISIBLE);
                } else {
                    note_lnl.setVisibility(View.GONE);
                }
                Util.hideKeyboard(getActivity());
                customDialog.dismiss();
            }
        });

        customDialog = builder.create();
        customDialog.show();
        Util.showKeyboard(getActivity());
    }

    protected long getDistanceInMeters(double lat2, double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad, lat2Rad, deltaLonRad, dist;
        long nDist;

        if (companyDtos != null && companyDtos.size() != 0) {
            List<Long> value = new ArrayList<>();

            lat2Rad = Math.toRadians(lat2);
            if (workingCompany != null && workingCompany.size() != 0) {
                for (int i = 0; i < workingCompany.size(); i++) {
                    lat1Rad = Math.toRadians(workingCompany.get(i).getLat());

                    deltaLonRad = Math.toRadians(lon2 - workingCompany.get(i).getLng());

                    dist = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;

                    value.add(Math.round(dist * 1000));
                }

                nDist = Collections.min(value);

                int count = 0;
                for (int i = 0; i < value.size(); i++) {
                    if (nDist == value.get(i)) {
                        count = i;
                    }
                }

                CompanyDto company = workingCompany.get(count);
                latLng = company.getLatLng();
                currentLocationNo = company.LocationNo;
            } else {
                for (int i = 0; i < companyDtos.size(); i++) {
                    lat1Rad = Math.toRadians(companyDtos.get(i).getLat());
                    deltaLonRad = Math.toRadians(lon2 - companyDtos.get(i).getLng());
                    dist = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
                    value.add(Math.round(dist * 1000));
                }

                nDist = Collections.min(value);
                int count = 0;

                for (int i = 0; i < value.size(); i++) {
                    if (nDist == value.get(i)) {
                        count = i;
                    }
                }

                CompanyDto company = companyDtos.get(count);
                latLng = company.getLatLng();
                currentLocationNo = company.LocationNo;
            }

            return nDist;
        }

        return 0;
    }

    List<CompanyDto> workingCompany = null;

    public void addMap() {
        LayoutInflater inflater = (LayoutInflater) lnl_map.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (map == null) {
            map = inflater.inflate(R.layout.map_layout, null);
        } else {
            ViewGroup view1 = (ViewGroup) map.getParent();
            if (view1 != null)
                view1.removeView(map);
        }
        if (map == null) {
            return;
        }
        mapView_distance = (TextView) map.findViewById(R.id.mapview_distance);
        setUpMapIfNeeded();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Util.showMessage(R.string.permission_denied);
            return;
        }
        myMap.setMyLocationEnabled(false);
        myMap.getUiSettings().setMapToolbarEnabled(false);
        myMap.clear();
        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMaker != null) {
                    currentMaker.showInfoWindow();
                }
            }
        });
        if (companyDtos != null && companyDtos.size() != 0) {
            addCompanyPosition();
        } else {
            String text = getString(R.string.string_no_office);
            BaseActivity.Instance.displayAddAlertDialog(getString(R.string.app_name), text, getString(R.string.string_ok), getString(R.string.string_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), OfficeMapViewActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Statics.KEY_COMPANY, "");
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
        CameraPosition cameraPosition = null;
        Location lastKnownLocation = LocationTracker.getInstance().getLocation();
        if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0.0) {
            updateCurrentPosition(lastKnownLocation);
        } else {
            if (workingCompany != null && workingCompany.size() != 0) {
                cameraPosition = new CameraPosition.Builder().target(workingCompany.get(workingCompany.size() - 1).getLatLng()).zoom(17).build();
            } else {
                if (latLng != null)
                    cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            }
            if (cameraPosition != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                myMap.moveCamera(cameraUpdate);
            }
        }
        final ImageView maptype = (ImageView) map.findViewById(R.id.btn_switch_map_type);
        maptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsHybrid) {
                    maptype.setImageResource(R.drawable.map_aeryal_icon);
                    myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mIsHybrid = false;
                } else {
                    maptype.setImageResource(R.drawable.map_hybrid_icon);
                    myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    mIsHybrid = true;
                }
            }
        });
        lnl_map.addView(map);
    }

    private List<Circle> mCircles;
    private List<Marker> mMarkers;

    private Marker addMarker(LatLng lng, String title, boolean isFlat, int iconID) {
        Marker marker = null;
        if (myMap != null) {
            marker = myMap.addMarker(new MarkerOptions()
                    .flat(isFlat)
                    .position(lng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(iconID))
                    .title(title));
            marker.showInfoWindow();
        }
        return marker;
    }

    private void setUpMapIfNeeded() {
        if (myMap == null) {
            myMap = ((MapFragment) BaseActivity.Instance.getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (myMap == null) {
                return;
            }
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mIsHybrid = false;
        }
    }

    private void addCompanyPosition() {
        if (companyDtos != null && companyDtos.size() != 0) {
            mCircles = new ArrayList<>();
            mMarkers = new ArrayList<>();
            for (CompanyDto companyDto : companyDtos) {
//                if(companyDto.IsWorking ==1)
//                {
//                    if(workingCompany==null)
//                    {
//                        workingCompany = new ArrayList<>();
//                    }
//                    workingCompany.add(companyDto);
//
//                }
//                else {
                latLng = new LatLng(companyDto.getLat(), companyDto.getLng());
//                }
                Circle circle = myMap.addCircle(new CircleOptions()
                        .center(companyDto.getLatLng())
                        .radius(companyDto.ErrorRange)
                        .strokeColor(getResources().getColor(R.color.app_base_color)).strokeWidth(3)
                        .fillColor(Color.argb(77, 39, 123, 205)));
                mCircles.add(circle);
                Marker marker = addMarker(companyDto.getLatLng(), "", true, R.drawable.map_location_build_icon);
                mMarkers.add(marker);
            }
            if (workingCompany != null && workingCompany.size() != 0) {
                latLng = workingCompany.get(workingCompany.size() - 1).getLatLng();
            }
        }
    }

    public GoogleMap myMap;
    View map;
    boolean mIsHybrid = false;
    TextView mapView_distance;
    String dist = "";
    Marker currentMaker = null;

    private void updateCurrentPosition(Location location) {
        if (location == null) {
            location = LocationTracker.getInstance().getLocation();
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng1);
            CameraUpdate cameraUpdate;

            if (companyDtos != null && companyDtos.size() != 0) {
                long distance = getDistanceInMeters(location.getLatitude(),
                        location.getLongitude());

                dist = Util.covertDistance(distance);
                if (currentMaker != null) {
                    currentMaker.remove();
                    currentMaker = addMarker(latLng1, dist, true, R.drawable.map_location_icon_tt);
                } else {
                    currentMaker = addMarker(latLng1, dist, true, R.drawable.map_location_icon_tt);
                }
                builder.include(latLng);
                LatLngBounds bounds = builder.build();
                int padding = Util.getDimenInPx(R.dimen.map_scale);
                if (getDistanceInMeters(location.getLatitude(),
                        location.getLongitude()) > 250) {
                    cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                } else {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                }
                setUpMapIfNeeded();
                if (myMap != null) {
                    try {
                        myMap.moveCamera(cameraUpdate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                setUpMapIfNeeded();
                if (myMap != null) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng1, 17);
                    myMap.moveCamera(cameraUpdate);
                }
            }
        }
    }

    private boolean isFirst = true;
    private boolean isGo = false;

    @Override
    public void onGetStatusResponseSuccess(int type) {
        Log.d(TAG, "setTimeType 3:" + type);
        setTimeType(type);

        if (TimeCardActivity.instance != null && isFirst) {
            isFirst = false;
//            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
//                Log.d(TAG, "BluetoothAdapter off");
//            } else {
//                Log.d(TAG, "BluetoothAdapter on");
////                TimeCardActivity.instance.goToSimpleActivity();
//            }


            if (isGotoBluetoothPage && !isMockSettingsON(getActivity())) {
                isGo = false;
                Log.d(TAG, "goToSimpleActivity 2");
                TimeCardActivity.instance.goToSimpleActivity();
            } else {
                isGo = true;
            }
        }

        HttpRequest.getInstance().getStatusMessage(this, type);
    }

    @Override
    public void onGetStatusResponseError(ErrorDto errorDto) {
        isFirst = false;
        Util.printLogs(errorDto.message);
    }


    private void updateTime() {
        Log.d(TAG, "updateTime");
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    long time = DaZoneApplication.getInstance().getmPrefs().getServerTime();
                                    DaZoneApplication.getInstance().getmPrefs().putServerTime(time + 1000);
                                    time = time + 1000;

//                                    Log.d(TAG, "time:" + time + " - " + Util.setDateInfo(time, Statics.FORMAT_HOUR_MINUTE_SECOND)
//                                            + " - " + DaZoneApplication.getInstance().getmPrefs().getTimeOffset());

                                    day_time.setText(Util.setDateInfo(time, Statics.FORMAT_HOUR_MINUTE_SECOND));
                                    am_pm.setText(Util.setDateInfo(time, Statics.FORMAT_AM_PM));
                                    String dateYear = Util.setDateInfo(time, Statics.FORMAT_DATE_MONTH_YEAR);
                                    String dateOfWeek = Util.setDateInfo(time, Statics.FORMAT_DATE_OF_WEEK);
                                    day_of_year.setText(dateYear + " " + dateOfWeek);

                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }

    @Override
    public void onResponseMessageSuccess(String message) {
        hello_tv.setText(message);
    }

    @Override
    public void onResponseMessageError(ErrorDto errorDto) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        instance = null;
    }

    @Override
    public void onPause() {
        super.onPause();

        LocationTracker.getInstance().stopLocationUpdates();
    }

    ArrayList<MenuDto> mNavItems = new ArrayList<>();
    String[] tempArray = null;

    protected void setupDrawerToggle() {
        mNavItems.clear();

        for (String temp : tempArray) {
            mNavItems.add(new MenuDto(temp, R.drawable.home_menu_time, false));
        }
    }

    @Override
    public void onSuccess() {
        companyDtos = CompanyDBHelper.getAllCompanyInfo();
        if (companyDtos != null && companyDtos.size() != 0)
            for (int i = 0; i < companyDtos.size(); i++) {
                try {
                    mCircles.get(i).setCenter(companyDtos.get(i).getLatLng());
                    mCircles.get(i).setRadius(companyDtos.get(i).ErrorRange);
                    mMarkers.get(i).setPosition(companyDtos.get(i).getLatLng());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onFail(ErrorDto errorDto) {
    }

    Location mCurrentLocation = null;

    boolean isMock = false;


    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }


    @Override
    public void onLocationChangeCallBack(Location location) {

        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            isMock = !Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        }
        if (!isMock) {
//            continue check
        }
        //if marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            boolean checkForAllowMockLocationsApps = Constant.checkForAllowMockLocationsApps(getActivity());
//            boolean isAllowMockLocationsOn = Constant.isLocationFromMockProvider(getActivity(), location);
//
//            Toast.makeText(getActivity(), "isMockLocation:" + isAllowMockLocationsOn, Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "isMock:" + isMock);
        setUpMap(location);
    }

    private boolean isGotoBluetoothPage = false;

    private void setUpMap(Location location) {
        if (location != null) {
            Log.d(TAG, "location != null");
            mCurrentLocation = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng1);
            CameraUpdate cameraUpdate;

            if (companyDtos != null && companyDtos.size() != 0) {
                long distance = getDistanceInMeters(location.getLatitude(),
                        location.getLongitude());
                long ErrorRange = (long) companyDtos.get(0).ErrorRange;
                Log.d(TAG, "distance:" + distance + "ErrorRange:" + ErrorRange);
                if (distance <= ErrorRange && !isGotoBluetoothPage) {
                    isGotoBluetoothPage = true;
                    Log.d(TAG, "isGotoBluetoothPage");
                    if (isGo && !isMockSettingsON(getActivity())) {
                        Log.d(TAG, "goToSimpleActivity 1");
                        TimeCardActivity.instance.goToSimpleActivity();
                    }
                }

                dist = Util.covertDistance(distance);
                new Prefs().putStringValue(Statics.KEY_PREF_DISTANCE, dist);
                if (currentMaker != null) {
                    currentMaker.remove();
                    currentMaker = addMarker(latLng1, dist, true, R.drawable.map_location_icon_tt);
                } else {
                    currentMaker = addMarker(latLng1, dist, true, R.drawable.map_location_icon_tt);
                }

                builder.include(latLng);
                LatLngBounds bounds = builder.build();

                int padding = Util.getDimenInPx(R.dimen.map_scale);
                if (getDistanceInMeters(location.getLatitude(), location.getLongitude()) > 250) {
                    cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                } else {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                }

                setUpMapIfNeeded();

                if (myMap != null) {
                    try {
                        myMap.moveCamera(cameraUpdate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Log.d(TAG, "else distance");
                setUpMapIfNeeded();
                if (currentMaker != null) {
                    currentMaker.remove();
                    currentMaker = addMarker(latLng1, "", true, R.drawable.map_location_icon_tt);
                } else {
                    currentMaker = addMarker(latLng1, "", true, R.drawable.map_location_icon_tt);
                }

                if (myMap != null) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng1, 17);
                    myMap.moveCamera(cameraUpdate);
                }
            }
        } else {
            Log.d(TAG, "location null");
        }
    }
}