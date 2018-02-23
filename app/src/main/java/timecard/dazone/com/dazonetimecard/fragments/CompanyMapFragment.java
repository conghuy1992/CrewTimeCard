package timecard.dazone.com.dazonetimecard.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timecard.dazone.com.dazonetimecard.locationutil.LocationTracker;
import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.OfficeMapViewActivity;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto.CompanyInfo;
import timecard.dazone.com.dazonetimecard.interfaces.GetAddressCallback;
import timecard.dazone.com.dazonetimecard.helper.LocationAddress;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.LocationChangeCallback;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.DialogUtil;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class CompanyMapFragment extends Fragment implements LocationChangeCallback, BaseHTTPCallBack, GetAddressCallback {

    LinearLayout lnl_map;
    public GoogleMap myMap;
    View map;
    TextView mapView_distance;
    String dist = "";
    Marker currentMaker = null;

    private Circle circle;
    boolean mIsHybrid = false;

    private Marker marker;
    public double latitude = 0;
    public double longitude = 0;
    protected LatLng latLng = null;

    public static CompanyMapFragment newInstance(String company) {
        CompanyMapFragment fragmentFirst = new CompanyMapFragment();
        Bundle args = new Bundle();

        args.putString(Statics.KEY_COMPANY, company);
        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOnCreate();
    }

    UserSettingDto.CompanyInfo companyInfo;

    protected void initOnCreate() {

        try {
            companyInfo = new Gson().fromJson((String) getArguments().get(Statics.KEY_COMPANY), UserSettingDto.CompanyInfo.class);
            mCurrentLocation = LocationTracker.getInstance().getLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView office_name, office_address, office_latitude, office_longitude, office_check_range, office_distance, set_zero_btn, setting_save_btn;
    private LinearLayout office_info, lnl_permissible_range;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_company_map, container, false);
        lnl_map = (LinearLayout) v.findViewById(R.id.lnl_map);
        office_name = (TextView) v.findViewById(R.id.office_name);
        office_address = (TextView) v.findViewById(R.id.office_address);
        office_latitude = (TextView) v.findViewById(R.id.office_latitude);
        office_longitude = (TextView) v.findViewById(R.id.office_longitude);
        office_check_range = (TextView) v.findViewById(R.id.office_check_range);
        set_zero_btn = (TextView) v.findViewById(R.id.set_zero_btn);
        setting_save_btn = (TextView) v.findViewById(R.id.setting_save_btn);
        lnl_permissible_range = (LinearLayout) v.findViewById(R.id.lnl_permissible_range);
        office_distance = (TextView) v.findViewById(R.id.office_distance);
        office_info = (LinearLayout) v.findViewById(R.id.office_info);

        if (companyInfo == null) {
            ((OfficeMapViewActivity) getActivity()).mActionBar.setTitle(getString(R.string.setting_new_workplace));
        } else {
            ((OfficeMapViewActivity) getActivity()).mActionBar.setTitle(getString(R.string.setting_zero_adjust));
        }

        addMap();
        bindData();

        return v;
    }

    private void updateCamera() {
        if (companyInfo != null && (companyInfo.lat != 0.0 || companyInfo.lng != 0.0)) {
            latLng = new LatLng(companyInfo.lat, companyInfo.lng);
            if (circle != null) {
                circle.remove();
            }
            circle = myMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(companyInfo.PermissibleRange)
                    .strokeColor(getResources().getColor(R.color.app_base_color)).strokeWidth(3)
                    .fillColor(Color.argb(77, 39, 123, 205)));
            if (marker != null) {
                marker.remove();
            }
            marker = addMarker(latLng, "", true, R.drawable.map_location_build_icon);
        }
        CameraPosition cameraPosition = null;
        Location lastKnownLocation = LocationTracker.getInstance().getLocation();
        if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0.0) {
            updateCurrentPosition(lastKnownLocation);
        } else {
            if (latLng != null) {
                cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            }
            if (cameraPosition != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                myMap.moveCamera(cameraUpdate);
            }
        }
    }

    private void bindData() {
        if (companyInfo != null) {
            office_name.setText(companyInfo.nameoffice);
            office_address.setText(companyInfo.description);
            office_latitude.setText(Util.getFormatNumber(companyInfo.lat));
            office_longitude.setText(Util.getFormatNumber(companyInfo.lng));
            office_check_range.setText(companyInfo.PermissibleRange + " m");
            if (mCurrentLocation != null) {
                dist = Util.covertDistance(Util.getDistanceInMeters(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
                        companyInfo.lat, companyInfo.lng));
            }
            if (!TextUtils.isEmpty(dist)) {
                office_distance.setText(dist);
            } else {
                office_distance.setText("0 m");
            }
            lnl_permissible_range.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show();
                }
            });
            set_zero_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setZero();
                }
            });
            updateCamera();
        } else {
            displayWorkplaceDialog();
        }
        office_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWorkplaceDialog();
            }
        });
        setting_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyInfo != null) {
                    HttpRequest.getInstance().saveSettingOffice(CompanyMapFragment.this, companyInfo);
                }
            }
        });
    }

    private void setZero() {
        if (mCurrentLocation != null) {
            companyInfo.lat = mCurrentLocation.getLatitude();
            companyInfo.lng = mCurrentLocation.getLongitude();
            latLng = new LatLng(companyInfo.lat, companyInfo.lng);

            if (TextUtils.isEmpty(companyInfo.description)) {
                new LocationAddress(companyInfo.lat, companyInfo.lng, this).execute();
            }

            bindData();
        }
    }

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

        mapView_distance = (TextView) map.findViewById(R.id.mapview_distance);

        setUpMapIfNeeded();

        try {
            myMap.setMyLocationEnabled(false);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        myMap.getUiSettings().setMapToolbarEnabled(false);
        myMap.clear();

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
            if (companyInfo != null && (companyInfo.lat != 0.0 || companyInfo.lng != 0.0)) {
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

    protected long getDistanceInMeters(double lat2, double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad, lat2Rad, deltaLonRad, dist;
        long nDist;
        if (latLng != null) {
            lat2Rad = Math.toRadians(lat2);
            lat1Rad = Math.toRadians(companyInfo.lat);
            deltaLonRad = Math.toRadians(lon2 - companyInfo.lng);
            dist = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
            nDist = Math.round(dist * 1000);
            return nDist;
        }

        return 0;
    }

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
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mIsHybrid = false;
        }
    }

    private Location mCurrentLocation = null;

    @Override
    public void onLocationChangeCallBack(Location location) {
        if (location == null) {
            return;
        }
        mCurrentLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng1);
        CameraUpdate cameraUpdate;
        if (companyInfo != null && (companyInfo.lat != 0.0 || companyInfo.lng != 0.0)) {
            long distance = getDistanceInMeters(location.getLatitude(),
                    location.getLongitude());

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
            office_distance.setText(dist);
        } else {
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
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationTracker.getInstance().stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        DialogUtil.showGPSModDialog();
        LocationTracker.getInstance().registerLocationService(getActivity(), this);
        coverList(99);
    }

    public void show() {
        final Dialog d = new Dialog(getActivity());
        d.setTitle(R.string.string_settig_office_check_range);
        d.setContentView(R.layout.number_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        coverList(companyInfo.PermissibleRange);

        np.setMaxValue(list.size() - 1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(listShow());
        np.setValue(positionInList(companyInfo.PermissibleRange));
        np.setOnValueChangedListener(new MyListener());
        np.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newRange != 0) {
                    setupRange(newRange);
                }
                d.dismiss();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRange(list.get(np.getValue()));
                d.dismiss();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    private void setupRange(int range) {
        companyInfo.PermissibleRange = range;
        office_check_range.setText(range + " m");
        if (circle != null) {
            circle.remove();
        }
        circle = myMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(companyInfo.PermissibleRange)
                .strokeColor(getResources().getColor(R.color.app_base_color)).strokeWidth(3)
                .fillColor(Color.argb(77, 39, 123, 205)));

    }

    public AlertDialog workplaceDialog = null;

    public void displayWorkplaceDialog() {
        if (workplaceDialog != null && workplaceDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_set_office, null);
        builder.setView(customView);

        Button btnCancel = (Button) customView.findViewById(R.id.add_btn_cancel);
        Button btnAdd = (Button) customView.findViewById(R.id.add_btn_log_time);
        final EditText edt_workplace, edt_workplace_address, edt_workplace_lat, edt_workplace_lng, edt_workplace_range;
        edt_workplace = (EditText) customView.findViewById(R.id.edt_workplace);
        edt_workplace_address = (EditText) customView.findViewById(R.id.edt_workplace_address);
        edt_workplace_lat = (EditText) customView.findViewById(R.id.edt_workplace_lat);
        edt_workplace_lng = (EditText) customView.findViewById(R.id.edt_workplace_lng);
        edt_workplace_range = (EditText) customView.findViewById(R.id.edt_workplace_range);
        if (companyInfo != null) {
            edt_workplace.setText(companyInfo.nameoffice);
            edt_workplace_address.setText(companyInfo.description);
            edt_workplace_lat.setText(Util.getFormatNumber(companyInfo.lat));
            edt_workplace_lng.setText(Util.getFormatNumber(companyInfo.lng));
            edt_workplace_range.setText(companyInfo.PermissibleRange + "");
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workplaceDialog.dismiss();
                }
            });
        } else {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workplaceDialog.dismiss();
                    getActivity().finish();
                }
            });
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if (TextUtils.isEmpty(edt_workplace.getText().toString().trim())) {
                                              edt_workplace.setError(getString(R.string.login_empty_input));
                                          } else {
                                              edt_workplace.setError(null);
                                              if (companyInfo == null) {
                                                  companyInfo = new CompanyInfo();
                                              }
                                              companyInfo.nameoffice = edt_workplace.getText().toString().trim();
                                              companyInfo.description = edt_workplace_address.getText().toString().trim();
                                              try {
                                                  companyInfo.PermissibleRange = Integer.parseInt(edt_workplace_range.getText().toString().trim());
                                              } catch (Exception e) {
                                                  companyInfo.PermissibleRange = 50;
                                              }
                                              if (companyInfo.PermissibleRange >= 50) {
                                                  edt_workplace_range.setError(null);
                                                  try {
                                                      companyInfo.lat = Double.parseDouble(edt_workplace_lat.getText().toString().trim());
                                                  } catch (Exception e) {
                                                      companyInfo.lat = 0;
                                                  }
                                                  try {
                                                      companyInfo.lng = Double.parseDouble(edt_workplace_lng.getText().toString().trim());
                                                  } catch (Exception e) {
                                                      companyInfo.lng = 0;
                                                  }
                                                  handleUpdate();
                                                  workplaceDialog.dismiss();
                                              } else {
                                                  edt_workplace_range.setError(getString(R.string.range_error));
                                              }
                                          }
                                      }
                                  }

        );

        workplaceDialog = builder.create();
        workplaceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (companyInfo == null) {
                    getActivity().finish();
                }
            }
        });

        workplaceDialog.show();
    }

    private void handleUpdate() {
        LatLng latLngAddress;

        if (companyInfo.lat == 0 && companyInfo.lng == 0) {
            if (!TextUtils.isEmpty(companyInfo.description)) {
                latLngAddress = getLocationFromAddress(companyInfo.description);
                if (latLngAddress != null) {
                    companyInfo.lat = latLngAddress.latitude;
                    companyInfo.lng = latLngAddress.longitude;
                }
            } else {
                setZero();
            }
        }

        bindData();
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(DaZoneApplication.getInstance().getApplicationContext());
        List<Address> address;
        LatLng latLng;
        try {
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null || address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

            return latLng;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean checkLoginCall = false;

    @Override
    public void onHTTPSuccess() {
        new Prefs().putBooleanValue(Statics.PREFS_KEY_RELOAD_SETTING, true);
        new Prefs().putBooleanValue(Statics.PREFS_KEY_RELOAD_TIMECARD, true);
        if (!checkLoginCall) {
            checkLoginCall = true;
            HttpRequest.getInstance().checkLogin(this);
        } else {
            checkLoginCall = false;
            getActivity().finish();
        }
    }

    @Override
    public void onHTTPFail(ErrorDto errorDto) {
        Util.showMessage(errorDto.message);
    }

    @Override
    public void onGetAddressCallback(String address) {
        companyInfo.description = address;
        office_address.setText(companyInfo.description);
    }

    int[] pickerValue = {50, 60, 70, 80, 90, 100, 120, 150, 180, 200, 250, 300, 350, 400, 450, 500, 600, 700, 800, 900, 1000};
    List<Integer> list = new ArrayList<>();

    private void coverList(int a) {
        list.clear();
        for (int i : pickerValue) {
            list.add(i);
        }
        if (a != 0 && a >= 50 && !checkHas(a)) {
            list.add(a);
        }
        Collections.sort(list, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    private int positionInList(int a) {
        if (a != 0 && a >= 50) {
            for (int i = 0; i < list.size(); i++) {
                if (a == list.get(i)) {
                    return i;
                }
            }
        }

        return 0;
    }

    private boolean checkHas(int a) {
        if (a != 0 && a >= 50) {
            for (int i = 0; i < list.size(); i++) {
                if (a == list.get(i)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String[] listShow() {
        String[] listShow = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            listShow[i] = list.get(i) + "";
        }

        return listShow;
    }

    int newRange = 0;

    private class MyListener implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            newRange = list.get(newVal);
        }
    }
}