package timecard.dazone.com.dazonetimecard.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.AllowDevices;
import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.dtos.ContentAllow;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.fragments.DialogRecyclerViewFragment;
import timecard.dazone.com.dazonetimecard.fragments.TimeCardFragment;
import timecard.dazone.com.dazonetimecard.interfaces.OnCheckAllowDevice;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetBeaconsCallBack;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class TimeCardActivity extends HomeActivity implements OnCheckAllowDevice {
    private String TAG = "TimeCardActivity";
    public static ArrayList<BeaconDTO> beaconDTOsTemp;
    /**
     * BLUETOOTH
     */
    private BluetoothAdapter mBluetoothAdapter;
    private DialogRecyclerViewFragment dialogRecyclerViewFragment;

    /**
     * PARAM
     */
    private boolean isMatched = false;
    private boolean isVisible = false;
    private boolean isCheck = false;
    private boolean isSearchingBlueTooth = false;

    private TimeCardFragment newTaskFragment;
    public static MenuItem actionSearchBeacon;
    public static TimeCardActivity instance = null;

    @Override
    protected void addFragment(Bundle bundle) {
        instance = this;
        if (bundle == null) {
            newTaskFragment = new TimeCardFragment();
            Util.addFragmentToActivity(getSupportFragmentManager(), newTaskFragment, R.id.main_content, false);
            Log.d(TAG, "isSearchingBlueTooth:" + isSearchingBlueTooth);
            if (!isSearchingBlueTooth) {
                getBeacon(0);
            }
        }
    }

    private boolean flag = true;


    public boolean isDialogTurnOnBluetooth = false;

    public void showDialogTurnOnBluetooth() {
        if (isDialogTurnOnBluetooth) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, Statics.REQUEST_CODE_BLUE_TOOTH);
        }
    }


    private void getBeacon(final int type) {
        isSearchingBlueTooth = true;
        HttpRequest.getInstance().GetBeacons(new OnGetBeaconsCallBack() {
            @Override
            public void OnGetBeaconsSuccess(ArrayList<BeaconDTO> beaconDTOs) {
                if (beaconDTOs.size() > 0) {
                    beaconDTOsTemp = beaconDTOs;
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    isCheck = new Prefs().getBooleanValue(Statics.IS_CHECK_ALLOW, true);

                    if (isCheck) {
                        /** TURN ON BLUETOOTH */
                        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                            Log.d(TAG, "show dialog");
                            isDialogTurnOnBluetooth = true;
                            if (type != 0) {
                                showDialogTurnOnBluetooth();
                            }
                        } else {
                            if (type != 0) {
                                goToSimpleActivity();
                            }
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
                    beaconDTOsTemp = new ArrayList<>();
                }

                if (beaconDTOsTemp != null && actionSearchBeacon != null) {
                    actionSearchBeacon.setVisible(beaconDTOsTemp.size() > 0);
                }

                isSearchingBlueTooth = false;
            }

            @Override
            public void OnGetBeaconsFail(ErrorDto dto) {
                String message = dto.message;
                if (TextUtils.isEmpty(message)) {
                    message = Util.getString(R.string.no_network_error);
                }

                Util.showShortMessage(message);
                isSearchingBlueTooth = false;
            }
        });
    }

    public void goToSimpleActivity() {
        if (isCheck) {
            int type = newTaskFragment.getTimeType();
            Intent intent = new Intent(TimeCardActivity.this, SimpleActivity.class);
            intent.putExtra(Statics.KEY_TIME_TYPE, type);
            intent.putExtra(Statics.KEY_LIST_BEACON, beaconDTOsTemp);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Statics.REQUEST_CODE_BLUE_TOOTH:
                if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                    DaZoneApplication.isTurnOff = true;
                    if (newTaskFragment != null) {
                        goToSimpleActivity();
                    }


//                    if (mBeaconManager == null) {
//                        mBeaconManager = BeaconManager.getInstanceForApplication(TimeCardActivity.this);
//                        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//                    }
//                    mBeaconManager.bind(TimeCardActivity.this);
                }
                break;
        }
    }

    private boolean mIsExit;

    @Override
    public void onBackPressed() {
        if (closeMenu()) {
            return;
        }

        if (mIsExit) {
            super.onBackPressed();
        } else {
            this.mIsExit = true;
            Toast.makeText(this, R.string.quit_confirm, Toast.LENGTH_SHORT).show();
            myHandler.postDelayed(myRunnable, 2000);
        }
    }

    private Handler myHandler = new Handler();

    private Runnable myRunnable = new Runnable() {
        public void run() {
            mIsExit = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // turn off bluetooth
        if (mBluetoothAdapter != null && DaZoneApplication.isTurnOff) {
            mBluetoothAdapter.disable();
        }

        //isReCall = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        isCheck = new Prefs().getBooleanValue(Statics.IS_CHECK_ALLOW, true);

        if (isCheck) {
            getMenuInflater().inflate(R.menu.menu_time_card_map, menu);
            actionSearchBeacon = menu.findItem(R.id.action_scan_beacon);

            if (beaconDTOsTemp != null) {
                actionSearchBeacon.setVisible(beaconDTOsTemp.size() > 0);
            }
        } else {
            getMenuInflater().inflate(R.menu.menu_time_card_map_disable, menu);
            actionSearchBeacon = menu.findItem(R.id.action_scan_beacon);

            if (beaconDTOsTemp != null) {
                actionSearchBeacon.setVisible(beaconDTOsTemp.size() > 0);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan_beacon:
                Log.d(TAG,"action_scan_beacon");
                if (isCheck) {
                    if (!isSearchingBlueTooth) {
                        isMatched = false;
                        getBeacon(1);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage(R.string.not_permission)
                            .setPositiveButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builder.show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void updateIcon() {
        if (beaconDTOsTemp != null && beaconDTOsTemp.size() > 0 && actionSearchBeacon != null) {
            actionSearchBeacon.setVisible(true);
        } else {
            actionSearchBeacon.setVisible(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpRequest.getInstance().checkAllowDevice(this);
        isVisible = true;
    }

    @Override
    public void onGetMyListSuccess(AllowDevices myListDtos) {
        if (myListDtos.getContentAllow() != null) {
            Type listType = new TypeToken<List<ContentAllow>>() {
            }.getType();
            List<ContentAllow> contentAllow = new Gson().fromJson(myListDtos.getContentAllow(), listType);

            isCheck = contentAllow.get(1).isAllow();

            Prefs prefs = new Prefs();
            prefs.putBooleanValue(Statics.IS_CHECK_ALLOW, isCheck);
        }
    }

    @Override
    public void onGetMyListFail(ErrorDto errorDto) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }
}