package timecard.dazone.com.dazonetimecard.activities;

import android.content.Context;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.database.CompanyDBHelper;
import timecard.dazone.com.dazonetimecard.dtos.CompanyDto;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class MapActivity extends BaseActivity {
    RelativeLayout map_rl;
    LinearLayout info_lnl;
    double lat, lng, latCompany, lonCompany;
    LatLng latLng, companyLatlng;
    String distance;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lat = bundle.getDouble(Statics.KEY_LATITUDE);
            lng = bundle.getDouble(Statics.KEY_LONGITUDE);
            latCompany = bundle.getDouble(Statics.KEY_LATITUDE_COMPANY);
            lonCompany = bundle.getDouble(Statics.KEY_LONGITUDE_COMPANY);
            distance = bundle.getString(Statics.KEY_DISTANCE);
            latLng = new LatLng(lat, lng);
            companyLatlng = new LatLng(latCompany, lonCompany);
        }

        setContentView(R.layout.activity_map);
        map_rl = (RelativeLayout) findViewById(R.id.map_rl);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            addmap();
        } else {
            addMapLow(savedInstanceState);
        }


        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    MapView mapView;

    private void addMapLow(Bundle savedInstanceState) {
        View v = LayoutInflater.from(map_rl.getContext()).inflate(R.layout.map_layout_low, null);
        MapsInitializer.initialize(this);
        info_lnl = (LinearLayout) v.findViewById(R.id.info_lnl);
        info_lnl.setVisibility(View.GONE);
        mapview_distance = (TextView) v.findViewById(R.id.mapview_distance);
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            case ConnectionResult.SUCCESS:
                Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) v.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                if (mapView != null) {
                    mapView.setOnClickListener(null);
                    myMap.setOnMapClickListener(null);

                    myMap = mapView.getMap();
                    myMap.getUiSettings().setMyLocationButtonEnabled(false);
                    myMap.setMyLocationEnabled(true);
                    myMap.getUiSettings().setMapToolbarEnabled(false);

                    myMap.addMarker(new MarkerOptions().position(latLng).title(getAddress())).showInfoWindow();

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17.0f).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    myMap.moveCamera(cameraUpdate);

                    map_rl.addView(v);
                }
                break;

            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(this, "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(this, "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, GooglePlayServicesUtil.isGooglePlayServicesAvailable(this), Toast.LENGTH_SHORT).show();
        }
    }

    CameraUpdate cameraUpdate = null;

    public void addmap() {
        List<CompanyDto> companyDtos = CompanyDBHelper.getAllCompanyInfo();
        int range = 0;
        if (companyDtos != null && companyDtos.size() != 0) {
            range = companyDtos.get(0).ErrorRange;
        }
        LayoutInflater inflater = (LayoutInflater) map_rl.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (map == null) {
            map = inflater.inflate(R.layout.map_layout, null);
        } else {
            ViewGroup view1 = (ViewGroup) map.getParent();
            if (view1 != null)
                view1.removeView(map);
        }

        mapview_distance = (TextView) map.findViewById(R.id.mapview_distance);
        info_lnl = (LinearLayout) map.findViewById(R.id.info_lnl);
        info_lnl.setVisibility(View.GONE);
        setUpMapIfNeeded();
        myMap.getUiSettings().setMapToolbarEnabled(false);
        myMap.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng);
        int padding = Util.getDimenInPx(R.dimen.map_scale);
        myMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(distance)).showInfoWindow();

        if (latCompany != 0 && lonCompany != 0) {
            builder.include(companyLatlng);
            myMap.addMarker(new MarkerOptions()
                    .position(companyLatlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_build_icon)));
            if (range != 0) {
                myMap.addCircle(new CircleOptions()
                        .center(companyLatlng)
                        .radius(range)
                        .strokeColor(getResources().getColor(R.color.app_base_color)).strokeWidth(3)
                        .fillColor(Color.argb(77, 39, 123, 205)));
            }
            LatLngBounds bounds = builder.build();
            if (getDistanceInMeters(latLng) > 700) {
                cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            } else {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            }
        } else {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        }

        setUpMapIfNeeded();
        map_rl.addView(map);
        if (myMap != null) {
            if (latLng != null && cameraUpdate != null)
                map_rl.post(new Runnable() {
                    @Override
                    public void run() {
                        myMap.moveCamera(cameraUpdate);
                    }
                });
        }
    }

    private void setUpMapIfNeeded() {
        if (myMap == null) {
            myMap = ((MapFragment) BaseActivity.Instance.getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            /*myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return true;
                }
            });
            myMap.setOnMapClickListener(null);
            myMap.getUiSettings().setAllGesturesEnabled(false);*/
            final ImageView maptype = (ImageView) map.findViewById(R.id.btn_switch_map_type);
            maptype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ISHYBRID) {
                        maptype.setImageResource(R.drawable.map_aeryal_icon);
                        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        ISHYBRID = false;
                    } else {
                        maptype.setImageResource(R.drawable.map_hybrid_icon);
                        myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        ISHYBRID = true;
                    }
                }
            });
        }
    }

    //add for map
    public GoogleMap myMap;
    View map;
    boolean ISHYBRID = false;
    TextView mapview_distance;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getAddress() {
        String yourAddress = "";
        //Geocoder geocoder;
        //List<Address> yourAddresses = new ArrayList<>();
        //geocoder = new Geocoder(this, Locale.getDefault());

        //try {
        //    yourAddresses = geocoder.getFromLocation(lat, lng, 1);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        return yourAddress;
    }

    @Override
    protected void onPause() {
        super.onPause();
        myMap = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    protected long getDistanceInMeters(LatLng latLng) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad, lat2Rad, deltaLonRad, dist;
        long nDist;
        lat2Rad = Math.toRadians(latLng.latitude);
        lat1Rad = Math.toRadians(companyLatlng.latitude);
        deltaLonRad = Math.toRadians(latLng.longitude - companyLatlng.longitude);
        dist = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
        nDist = Math.round(dist * 1000);

        return nDist;
    }
}