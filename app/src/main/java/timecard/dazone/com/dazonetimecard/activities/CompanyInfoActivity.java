package timecard.dazone.com.dazonetimecard.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto;
import timecard.dazone.com.dazonetimecard.interfaces.OnDeleteBeaconCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetBeaconByLocationCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnInsertBeaconCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnUpdateBeaconCallBack;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.DialogUtil;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;
import timecard.dazone.com.dazonetimecard.fragments.DialogRecyclerViewFragment;

public class CompanyInfoActivity extends AppCompatActivity implements View.OnClickListener, BeaconConsumer {
    /**
     * VIEW
     */
    private ImageView btnBack;
    private TextView tvName;
    //private TextView tvDescription;
    private TextView tvAddress;
    private TextView tvPermissibleRange;
    private TextView tvBeacon;
    private TextView btnAddBeacon;
    private TextView btnDeleteBeacon;
    private ProgressBar progressBarBeacon;


    /**
     * PARAM
     */
    private UserSettingDto.CompanyInfo companyInfo;
    private boolean isUpdateBeacon = false;
    private boolean isReCall = true;

    /**
     * BLUETOOTH
     */
    private BluetoothAdapter mBluetoothAdapter;
    private DialogRecyclerViewFragment dialogRecyclerViewFragment;

    /**
     * BEACON
     */
    private BeaconDTO beaconDTO;
    private BeaconManager mBeaconManager;
    private final List<Beacon> mListOfBeacons = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        initView();
        receiveData();
        fillData();
        getData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // finish to search beacons
        if (mBeaconManager != null) {
            mBeaconManager.unbind(this);
        }
        // turn off bluetooth
        if (mBluetoothAdapter != null && DaZoneApplication.isTurnOff) {
            mBluetoothAdapter.disable();
        }

        isReCall = false;
    }

    private void initView() {
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.tv_name);
        //tvDescription = (TextView) findViewById(R.id.tv_description);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvPermissibleRange = (TextView) findViewById(R.id.tv_permissible_range);

        tvBeacon = (TextView) findViewById(R.id.tv_beacon);
        progressBarBeacon = (ProgressBar) findViewById(R.id.progressBar_beacon);
        btnAddBeacon = (TextView) findViewById(R.id.btn_add_beacon);
        btnAddBeacon.setOnClickListener(this);
        btnDeleteBeacon = (TextView) findViewById(R.id.btn_delete_beacon);
        btnDeleteBeacon.setOnClickListener(this);
    }

    /**
     * DATA FROM SERVER
     */
    private void getData() {
        HttpRequest.getInstance().GetBeaconPointByLocation(companyInfo.LocationNo, new OnGetBeaconByLocationCallBack() {
            @Override
            public void OnGetBeaconByLocationCallBackSuccess(BeaconDTO beaconDTO) {
                progressBarBeacon.setVisibility(View.GONE);
                if (beaconDTO != null) {
                    if (new Prefs().getIsAdmin() == 1) {
                        btnAddBeacon.setVisibility(View.VISIBLE);
                        btnDeleteBeacon.setVisibility(View.VISIBLE);
                    }
                    CompanyInfoActivity.this.beaconDTO = beaconDTO;
                    isUpdateBeacon = true;
                    btnAddBeacon.setText(Util.getString(R.string.company_info_change_beacon));
                    tvBeacon.setText(beaconDTO.getBeaconMajor() + "/" + beaconDTO.getBeaconMinor());
                } else {
                    if (new Prefs().getIsAdmin() == 1) {
                        btnAddBeacon.setVisibility(View.VISIBLE);
                    }
                    CompanyInfoActivity.this.beaconDTO = new BeaconDTO();
                    isUpdateBeacon = false;
                    tvBeacon.setText("None");
                }
            }

            @Override
            public void OnGetBeaconByLocationCallBackFail(ErrorDto dto) {
                progressBarBeacon.setVisibility(View.GONE);
                String message = dto.message;
                if (TextUtils.isEmpty(message)) {
                    message = Util.getString(R.string.no_network_error);
                }
                Util.showShortMessage(message);
            }
        });
    }

    /**
     * DATA FROM INTENT
     */
    private void receiveData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String company = bundle.getString(Statics.KEY_COMPANY);
            companyInfo = new Gson().fromJson(company, UserSettingDto.CompanyInfo.class);
        }
    }

    private void fillData() {
        if (companyInfo != null) {
            tvName.setText(companyInfo.nameoffice);
            //tvDescription.setText(companyInfo.description);
            String strAddress = companyInfo.lat + " , " + companyInfo.lng + "\n" + companyInfo.description;
            tvAddress.setText(strAddress);
            tvPermissibleRange.setText(companyInfo.PermissibleRange + "m");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_beacon:
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, Statics.REQUEST_CODE_BLUE_TOOTH);
                } else {
                    showBeaconDialog();
                }
                break;
            case R.id.btn_delete_beacon:
                String title = "";
                String message = Util.getString(R.string.company_info_dialog_delete_beacon_message);
                String strYes = Util.getString(R.string.auto_login_button_yes);
                String strNo = Util.getString(R.string.auto_login_button_no);


                DialogUtil.showYesNoDialog(
                        this,
                        title,
                        message,
                        strYes,
                        strNo,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                callDeleteBeaconApi();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }
                );
                break;
        }
    }

    private void callDeleteBeaconApi() {
        HttpRequest.getInstance().DeleteBeacon(beaconDTO.getPointNo(), new OnDeleteBeaconCallBack() {
            @Override
            public void OnDeleteBeaconSuccess(String response) {
                if (tvBeacon != null) {
                    isUpdateBeacon = false;
                    tvBeacon.setText("None");
                    btnDeleteBeacon.setVisibility(View.GONE);
                    if (new Prefs().getIsAdmin() == 1) {
                        btnAddBeacon.setVisibility(View.VISIBLE);
                    }
                    btnAddBeacon.setText(Util.getString(R.string.company_info_add_beacon));
                }
                try {
                    for (BeaconDTO beaconDTO1 : TimeCardActivity.beaconDTOsTemp) {
                        if (beaconDTO.getBeaconUUID().equals(beaconDTO1.getBeaconUUID())) {
                            TimeCardActivity.beaconDTOsTemp.remove(beaconDTO1);
                            TimeCardActivity.updateIcon();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnDeleteBeaconFail(ErrorDto dto) {
                Util.showShortMessage(R.string.company_info_delete_beacon_fail);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Statics.REQUEST_CODE_BLUE_TOOTH:
                if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                    DaZoneApplication.isTurnOff = true;
                    showBeaconDialog();
                }
                break;
        }
    }

    public void closeDialog() {
        isReCall = false;
    }

    public void insertBeacon(String uuid, final int major, final int minor) {
        isReCall = false;
        beaconDTO.setBeaconUUID(uuid);
        beaconDTO.setBeaconMajor(major);
        beaconDTO.setBeaconMinor(minor);
        beaconDTO.setLocationNo(companyInfo.LocationNo);
        if (isUpdateBeacon) {
            /** UPDATE */
            HttpRequest.getInstance().UpdateBeacon(beaconDTO, new OnUpdateBeaconCallBack() {
                @Override
                public void OnUpdateBeaconSuccess(String response) {
                    if (tvBeacon != null) {
                        tvBeacon.setText(major + "/" + minor);
                    }
                }

                @Override
                public void OnUpdateBeaconFail(ErrorDto dto) {
                    String message = dto.message;
                    if (TextUtils.isEmpty(message)) {
                        message = Util.getString(R.string.no_network_error);
                    }
                    Util.showShortMessage(message);
                }
            });
        } else {
            /** ADD */
            HttpRequest.getInstance().InsertBeacon(companyInfo, uuid, major, minor, new OnInsertBeaconCallBack() {
                @Override
                public void OnInsertBeaconSuccess(String response) {
                    beaconDTO.setPointNo(Integer.parseInt(response));
                    try {
                        TimeCardActivity.beaconDTOsTemp.add(beaconDTO);
                        TimeCardActivity.updateIcon();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (tvBeacon != null) {
                        isUpdateBeacon = true;
                        tvBeacon.setText(major + "/" + minor);
                        if (new Prefs().getIsAdmin() == 1) {
                            btnDeleteBeacon.setVisibility(View.VISIBLE);
                        }
                        btnAddBeacon.setText(Util.getString(R.string.company_info_change_beacon));
                    }
                }

                @Override
                public void OnInsertBeaconFail(ErrorDto dto) {
                    String message = dto.message;
                    if (TextUtils.isEmpty(message)) {
                        message = Util.getString(R.string.no_network_error);
                    }
                    Util.showShortMessage(message);
                }
            });
        }
    }

    private void showBeaconDialog() {
        startScan();
        FragmentManager fm = getSupportFragmentManager();
        dialogRecyclerViewFragment = new DialogRecyclerViewFragment();
        dialogRecyclerViewFragment.show(fm, "TAG");
    }

    public void dialogClose() {
        mBeaconManager.unbind(this);
    }

    private void startScan() {
        isReCall = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(0);
                mBeaconManager.bind(CompanyInfoActivity.this);
            }
        }, 500);
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                synchronized (mListOfBeacons) {
                    mListOfBeacons.clear();
                    for (Beacon beacon : beacons) {
                        mListOfBeacons.add(beacon);
                    }
                }
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            //mTextView.setText("");

            // getId1 => UUID
            // getId2 => Major
            // getId3 => Minor

            synchronized (mListOfBeacons) {
                for (Beacon beacon : mListOfBeacons) {
                    dialogRecyclerViewFragment.update(beacon.getId1() + "", beacon.getId2() + "", beacon.getId3() + "");
                }
            }
            if (isReCall) {
                mhandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };


    /*@Override
    protected void addFragment(Bundle bundle) {
        coppy_right.setVisibility(View.GONE);
        if (bundle == null) {
            String company = getIntent().getExtras().getString(Statics.KEY_COMPANY, "");
            CompanyInfoFragment fm = CompanyInfoFragment.newInstance(company);
            Util.addFragmentToActivity(getSupportFragmentManager(), fm, R.id.forgot_pass_content, false);
        }
    }*/
}