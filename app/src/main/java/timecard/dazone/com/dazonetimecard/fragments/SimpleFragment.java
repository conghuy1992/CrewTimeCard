package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.MyListActivity;
import timecard.dazone.com.dazonetimecard.database.CompanyDBHelper;
import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.dtos.CompanyDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.interfaces.GetServerTimeCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetStatusResponse;
import timecard.dazone.com.dazonetimecard.interfaces.OnInsertTimeCardForBeaconCallBack;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

/**
 * Created by Dat on 7/27/2016.
 */
public class SimpleFragment extends BaseFragment implements View.OnClickListener {
    /**
     * VIEW
     */
    private String TAG = "SimpleFragment";
    private View rootView;
    private TextView tvCompany;
    private TextView tvBeacon;
    private TextView tvTime;
    private TextView tvAmPm;
    private TextView tvDate;
    private TextView btnCheckIn;
    private TextView btnCheckOut;
    private TextView btnWorkOutside;
    private TextView btnReturn;
    private ProgressBar progressBar;
    private EditText edNote;

    /**
     * PARAM
     */
    //private double latitude = 0;
    //private double longitude = 0;
    private LatLng latLng = null;
    private List<CompanyDto> companyDtos;
    private int timeType = 0;
    private long serverTime = 0;

    /**
     * BEACON
     */
    public BeaconDTO beaconDTO;
    private BeaconManager mBeaconManager;
    private final List<Beacon> mListOfBeacons = new ArrayList<>();

    /**
     * PARAM
     */
    private boolean isMatched = false;
    private boolean isVisible = false;
    private boolean isSearchingBlueTooth = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconDTO = getArguments().getParcelable(Statics.KEY_BEACON);
        timeType = getArguments().getInt(Statics.KEY_TIME_TYPE);
        Log.d(TAG, "onCreate timeType:" + timeType);
//        serverTime = DaZoneApplication.getInstance().getmPrefs().getServerTime();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_simple, container, false);
        /*tvUUID = (TextView) rootView.findViewById(R.id.tv_uuid);
        tvUUID.setText(beaconDTO.getBeaconUUID());*/
//        initView();
//        initLocation();
//        initButton();
        //updateTime();
        return rootView;
    }

    public void initView() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tvTime = (TextView) rootView.findViewById(R.id.day_time);
        tvAmPm = (TextView) rootView.findViewById(R.id.am_pm);
//        tvTime.setText(Util.setDateInfo(serverTime, Statics.FORMAT_HOUR_MINUTE_SECOND));
//        tvAmPm.setText(Util.setDateInfo(serverTime, Statics.FORMAT_AM_PM));

        if (Util.getPhoneLanguage().equalsIgnoreCase("KO")) {
            ViewGroup view = (ViewGroup) tvTime.getParent();
            view.removeView(tvTime);
            view.addView(tvTime);
        }

        edNote = (EditText) rootView.findViewById(R.id.ed_note);
        /*edNote.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int lineCount = edNote.getLineCount();
                if (lineCount <= 3) {
                } else {
                    String strText = edNote.getText().toString();
                    strText = strText.substring(0, strText.length() - 2);
                    edNote.setText(strText);
                }
            }
        });*/
        /*edNote.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                int lineCount = edNote.getLineCount();
                if (lineCount> 2) {
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        edNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                int lineCount = edNote.getLineCount();
//                if (lineCount > 3) {
//                    String strText = edNote.getText().toString();
//                    strText = strText.substring(0, strText.length() - 1);
//                    edNote.setText(strText);
//                    int pos = edNote.getText().length();
//                    edNote.setSelection(pos);
//                }
            }
        });


        tvDate = (TextView) rootView.findViewById(R.id.day_of_year);
//        String strDate = Util.setDateInfo(serverTime, Statics.FORMAT_DATE_MONTH_YEAR) + " " + Util.setDateInfo(serverTime, Statics.FORMAT_DATE_OF_WEEK);
//        tvDate.setText(strDate);

        tvCompany = (TextView) rootView.findViewById(R.id.tv_company_name);
        //String strCompanyName = new Prefs().getStringValue(Statics.PREFS_KEY_COMPANY_NAME, "");
        String strCompanyName = beaconDTO.getOfficeName();
        tvCompany.setText(strCompanyName);

        tvBeacon = (TextView) rootView.findViewById(R.id.tv_beacon);
        String strBeacon = beaconDTO.getBeaconMajor() + "/" + beaconDTO.getBeaconMinor() + "";
        tvBeacon.setText(strBeacon);

        btnCheckIn = (TextView) rootView.findViewById(R.id.btn_check_in);
        btnCheckIn.setOnClickListener(this);
        btnCheckOut = (TextView) rootView.findViewById(R.id.btn_check_out);
        btnCheckOut.setOnClickListener(this);
        btnWorkOutside = (TextView) rootView.findViewById(R.id.btn_work_outside);
        btnWorkOutside.setOnClickListener(this);
        btnReturn = (TextView) rootView.findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(this);


        HttpRequest.getInstance().getServerTime(new GetServerTimeCallBack() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                isSave = true;
                serverTime = DaZoneApplication.getInstance().getmPrefs().getServerTime();
                Log.d(TAG, "onSuccess serverTime:" + serverTime);
                tvTime.setText(Util.setDateInfo(serverTime, Statics.FORMAT_HOUR_MINUTE_SECOND));
                tvAmPm.setText(Util.setDateInfo(serverTime, Statics.FORMAT_AM_PM));
                String dateYear = Util.setDateInfo(serverTime, Statics.FORMAT_DATE_MONTH_YEAR);
                String dateOfWeek = Util.setDateInfo(serverTime, Statics.FORMAT_DATE_OF_WEEK);
                tvDate.setText(dateYear + " " + dateOfWeek);
//                updateTime();
            }

            @Override
            public void onFail() {
                showNotify();
            }
        });
    }

    private void showNotify() {
        Toast.makeText(getActivity(), "Can not get Server time, try again", Toast.LENGTH_LONG).show();
    }

    private boolean isSave = false;

    public void initLocation() {
        /** USER LOCATION */
        /*Location lastKnownLocation = LocationTracker.getInstance().getLocation();
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        }*/

        /** COMPANY LOCATION */
        companyDtos = CompanyDBHelper.getAllCompanyInfo();
        for (CompanyDto companyDto : companyDtos) {
            if (companyDto.LocationNo == beaconDTO.getLocationNo()) {
                latLng = new LatLng(companyDto.getLat(), companyDto.getLng());
                break;
            }
        }
    }

    private void resetAllButton() {
        btnCheckIn.setBackgroundResource(R.drawable.bg_button_orange);
        btnCheckOut.setBackgroundResource(R.drawable.bg_button_orange);
        btnWorkOutside.setBackgroundResource(R.drawable.bg_button_orange);
        btnReturn.setBackgroundResource(R.drawable.bg_button_orange);

    }

    /**
     * CHECK IN = 1
     * WORK OUTSIDE = 2
     * CHECK OUT = 3
     * RETURN = 4
     */
    public void initButton() {


        resetAllButton();
        LinearLayout linearLayoutRow1 = (LinearLayout) btnCheckIn.getParent();
        LinearLayout linearLayoutRow2 = (LinearLayout) btnWorkOutside.getParent();
        LinearLayout.LayoutParams paramsCheckIn = (LinearLayout.LayoutParams) btnCheckIn.getLayoutParams();
        LinearLayout.LayoutParams paramsCheckOut = (LinearLayout.LayoutParams) btnCheckOut.getLayoutParams();
        LinearLayout.LayoutParams paramsWorkOutside = (LinearLayout.LayoutParams) btnWorkOutside.getLayoutParams();
        LinearLayout.LayoutParams paramsReturn = (LinearLayout.LayoutParams) btnReturn.getLayoutParams();

        Log.d(TAG, "initButton timeType:" + timeType);


        switch (timeType) {
            case 1:
                btnCheckIn.setBackgroundResource(R.drawable.bg_button_blue);
                break;
            case 2:
                btnWorkOutside.setLayoutParams(paramsCheckIn);
                btnCheckIn.setLayoutParams(paramsCheckOut);
                btnCheckOut.setLayoutParams(paramsWorkOutside);
                btnReturn.setLayoutParams(paramsReturn);
                linearLayoutRow2.removeView(btnWorkOutside);
                linearLayoutRow1.addView(btnWorkOutside, 0);
                linearLayoutRow1.removeView(btnCheckOut);
                linearLayoutRow2.addView(btnCheckOut, 0);
                btnWorkOutside.setBackgroundResource(R.drawable.bg_button_blue);
                break;
            case 3:
                btnCheckIn.setLayoutParams(paramsCheckOut);
                btnCheckOut.setLayoutParams(paramsCheckIn);
                linearLayoutRow1.removeView(btnCheckIn);
                linearLayoutRow1.addView(btnCheckIn);
                btnCheckOut.setBackgroundResource(R.drawable.bg_button_blue);
                break;
            case 4:
                btnReturn.setLayoutParams(paramsCheckIn);
                btnCheckIn.setLayoutParams(paramsCheckOut);
                btnCheckOut.setLayoutParams(paramsWorkOutside);
                btnWorkOutside.setLayoutParams(paramsReturn);

                linearLayoutRow2.removeView(btnReturn);
                if (linearLayoutRow1.findViewById(R.id.btn_return) != null) {
                    linearLayoutRow1.removeView(btnReturn);
                }
                linearLayoutRow1.addView(btnReturn, 0);

                linearLayoutRow1.removeView(btnCheckOut);
                if (linearLayoutRow2.findViewById(R.id.btn_check_out) != null) {
                    linearLayoutRow2.removeView(btnCheckOut);
                }
                linearLayoutRow2.addView(btnCheckOut, 0);

                btnReturn.setBackgroundResource(R.drawable.bg_button_blue);
                break;
        }
    }

    public static SimpleFragment newInstance(BeaconDTO beaconDTO, int timeType) {
        Bundle arg = new Bundle();
        arg.putParcelable(Statics.KEY_BEACON, beaconDTO);
        arg.putInt(Statics.KEY_TIME_TYPE, timeType);
        SimpleFragment fragment = new SimpleFragment();
        fragment.setArguments(arg);
        return fragment;
    }

//    private void updateTime() {
//        Log.d(TAG, "updateTime");
//        Thread t = new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(1000);
//                        if (getActivity() != null) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    serverTime = serverTime + 1000;
////                                    Log.d(TAG, "time:" + serverTime);
//                                    tvTime.setText(Util.setDateInfo(serverTime, Statics.FORMAT_HOUR_MINUTE_SECOND));
//                                    tvAmPm.setText(Util.setDateInfo(serverTime, Statics.FORMAT_AM_PM));
//                                    String dateYear = Util.setDateInfo(serverTime, Statics.FORMAT_DATE_MONTH_YEAR);
//                                    String dateOfWeek = Util.setDateInfo(serverTime, Statics.FORMAT_DATE_OF_WEEK);
//                                    tvDate.setText(dateYear + " " + dateOfWeek);
//                                }
//                            });
//                        }
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        t.start();
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_check_out:
                if (isSave)
                    insertTimeCard(3);
                else showNotify();
                break;
            case R.id.btn_check_in:
                if (isSave)
                    insertTimeCard(1);
                else showNotify();
                break;
            case R.id.btn_return:
                insertTimeCard(4);
                break;
            case R.id.btn_work_outside:
                insertTimeCard(2);
                break;
        }
    }

    /**
     * INSERT TIME CARD
     * CHECK IN type 1
     * WORK OUTSIDE type 2
     * CHECK OUT type 3
     * RETURN type 4
     */
    private void insertTimeCard(int type) {
        BaseActivity.Instance.showProgressDialog();
        //if (latitude != 0 || longitude != 0) {
        if (companyDtos != null && companyDtos.size() != 0) {
            String strNote = edNote.getText().toString();
            Calendar cal = Calendar.getInstance();
            Log.d(TAG, "insertTimeCard serverTime:" + serverTime);
            cal.setTimeInMillis(serverTime);
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            long time = cal.getTimeInMillis();
            Log.d(TAG, "insertTimeCard time:" + time);
            String beaconInfo = beaconDTO.getBeaconMajor() + "/" + beaconDTO.getBeaconMinor() + "," + beaconDTO.getOfficeName();
            double companyLat = latLng.latitude;
            double companyLong = latLng.longitude;

            HttpRequest.getInstance().InsertTimeCardsForBeacon(
                    companyLat,
                    companyLong,
                    strNote,
                    type,
                    "0m",
                    time,
                    companyLat,
                    companyLong,
                    beaconInfo,
                    new OnInsertTimeCardForBeaconCallBack() {
                        @Override
                        public void OnInsertTimeCardForBeaconSuccess(String response) {
                            BaseActivity.Instance.dismissProgressDialog();
                            BaseActivity.Instance.startNewActivity(MyListActivity.class, 1);
                            getActivity().finish();
                        }

                        @Override
                        public void OnInsertTimeCardForBeaconFail(ErrorDto dto) {
                            String message = dto.message;
                            if (TextUtils.isEmpty(message)) {
                                message = Util.getString(R.string.no_network_error);
                            }
                            Util.showShortMessage(message);
                        }
                    });
        } else {
            Util.showMessage(R.string.string_no_office);
        }
        /*} else {
            Util.showMessage(R.string.waiting_map_api);
        }*/
    }
}
