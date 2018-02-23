package timecard.dazone.com.dazonetimecard.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

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
import timecard.dazone.com.dazonetimecard.fragments.SimpleFragment;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetBeaconsCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetStatusResponse;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;


/**
 * Created by Dat on 7/27/2016.
 */
public class SimpleActivity extends HomeActivity implements BeaconConsumer {
    /**
     * BLUETOOTH
     */
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG = "SimpleActivity";
    /**
     * BEACON
     */
    public static ArrayList<BeaconDTO> beaconDTOsTemp;
    private BeaconDTO beaconDTO;
    private BeaconManager mBeaconManager;
    private final List<Beacon> mListOfBeacons = new ArrayList<>();

    /**
     * PARAM
     */
    private boolean isMatched = false;
    private boolean isVisible = false;
    private boolean isCheck;
    private boolean isSearchingBlueTooth = false;
    private SimpleFragment newFragment;
    private AlertDialog dialog;


    @Override
    protected void addFragment(Bundle bundle) {
        if (bundle == null) {
//            BeaconDTO beaconDTO = getIntent().getExtras().getParcelable(Statics.KEY_BEACON);
            int timeType = getIntent().getExtras().getInt(Statics.KEY_TIME_TYPE);

            beaconDTOsTemp = getIntent().getExtras().getParcelableArrayList(Statics.KEY_LIST_BEACON);
            Log.d(TAG, "addFragment timeType:" + timeType);
            newFragment = SimpleFragment.newInstance(beaconDTO, timeType);
            Util.addFragmentToActivity(getSupportFragmentManager(), newFragment, R.id.main_content, false);

            displayDialog();
            checkBeacon();
//            if (!isSearchingBlueTooth) {
//                getBeacon();
//            }
        }
    }

    private void displayDialog() {

        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(Util.getString(R.string.app_name_2))
                .setMessage(Util.getString(R.string.beacon_please_wait))
                .setPositiveButton(Util.getString(R.string.beacon_button_use_gps), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

//        dialog = DialogUtil.displayDialogSearchBeacon(this,
//                Util.getString(R.string.beacon_please_wait),
//                Util.getString(R.string.beacon_button_use_gps),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
    }

    public void binBeacon() {
        Log.d(TAG, "checkBeacon");
        if (mBeaconManager == null) {
            mBeaconManager = BeaconManager.getInstanceForApplication(SimpleActivity.this);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        }

        mBeaconManager.bind(SimpleActivity.this);
    }

    private void checkBeacon() {

        if (beaconDTOsTemp != null && beaconDTOsTemp.size() > 0) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            isCheck = new Prefs().getBooleanValue(Statics.IS_CHECK_ALLOW, true);

            if (isCheck) {
                if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                    Log.d(TAG, "BluetoothAdapter");
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, Statics.REQUEST_CODE_BLUE_TOOTH);
                } else {
                    binBeacon();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setMessage(R.string.not_permission)
                        .setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
            }
        } else {
            finish();
        }

        isSearchingBlueTooth = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Statics.REQUEST_CODE_BLUE_TOOTH:
                Log.d(TAG, "resultCode:" + resultCode);
                if (resultCode == RESULT_OK) {
                    if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                        DaZoneApplication.isTurnOff = true;
                        binBeacon();
                    }
                } else {
//                    RESULT_CANCELED
                    finish();
                }
                break;
        }
    }
//    private void getBeacon() {
//        isSearchingBlueTooth = true;
//        HttpRequest.getInstance().GetBeacons(new OnGetBeaconsCallBack() {
//            @Override
//            public void OnGetBeaconsSuccess(ArrayList<BeaconDTO> beaconDTOs) {
//                if (beaconDTOs.size() > 0) {
//                    beaconDTOsTemp = beaconDTOs;
//                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    isCheck = new Prefs().getBooleanValue(Statics.IS_CHECK_ALLOW, true);
//
//                    if (isCheck) {
//                        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
//                            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                            startActivityForResult(turnOn, Statics.REQUEST_CODE_BLUE_TOOTH);
//                        } else {
//                            if (mBeaconManager == null) {
//                                mBeaconManager = BeaconManager.getInstanceForApplication(SimpleActivity.this);
//                                mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//                            }
//
//                            mBeaconManager.bind(SimpleActivity.this);
//                        }
//                    } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
//                                .setMessage(R.string.not_permission)
//                                .setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                        builder.show();
//                    }
//                } else {
//                    beaconDTOsTemp = new ArrayList<>();
//                }
//
//                isSearchingBlueTooth = false;
//            }
//
//            @Override
//            public void OnGetBeaconsFail(ErrorDto dto) {
//                String message = dto.message;
//                if (TextUtils.isEmpty(message)) {
//                    message = Util.getString(R.string.no_network_error);
//                }
//
//                Util.showShortMessage(message);
//                isSearchingBlueTooth = false;
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBeaconManager != null) {
            mBeaconManager.unbind(this);
        }
    }


    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                Log.d(TAG,"didRangeBeaconsInRegion");
                for (Beacon beacon : beacons) {
                    for (BeaconDTO beaconDTO : beaconDTOsTemp) {
                        if (beacon.getId1().toString().equals(beaconDTO.getBeaconUUID()) &&
                                Integer.parseInt(beacon.getId2().toString()) == beaconDTO.getBeaconMajor() &&
                                Integer.parseInt(beacon.getId3().toString()) == beaconDTO.getBeaconMinor()) {
                            matchedBeacon(beaconDTO);
                            isMatched = true;
                        }
                    }
                }
            }
        });

        try {
            if (!isMatched) {
                mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            } else {
                mBeaconManager.stopRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void matchedBeacon(final BeaconDTO beaconDTO) {
        if (!isMatched) {
            if (mBeaconManager != null) {
                mBeaconManager.unbind(this);
            }

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                newFragment.beaconDTO = beaconDTO;
                newFragment.initView();
                newFragment.initLocation();
                newFragment.initButton();


            }
        });

    }
}
