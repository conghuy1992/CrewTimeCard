package timecard.dazone.com.dazonetimecard.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import timecard.dazone.com.dazonetimecard.locationutil.LocationTracker;
import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.CompanyInfoActivity;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto;
import timecard.dazone.com.dazonetimecard.interfaces.LocationChangeCallback;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class CompanyInfoFragment extends BaseFragment implements LocationChangeCallback{

    public UserSettingDto.CompanyInfo companyInfo;
    private TextView office_address,office_latitude,office_longitude,office_range,current_distance;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String company = getArguments().getString(Statics.KEY_COMPANY);
        companyInfo = new Gson().fromJson(company,UserSettingDto.CompanyInfo.class);
    }

    public static CompanyInfoFragment newInstance(String company)
    {
        Bundle arg = new Bundle();
        arg.putString(Statics.KEY_COMPANY, company);
        CompanyInfoFragment fragment = new CompanyInfoFragment();
        fragment.setArguments(arg);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_company_info, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        office_address = (TextView)v.findViewById(R.id.office_address);
        office_latitude = (TextView)v.findViewById(R.id.office_latitude);
        office_longitude = (TextView)v.findViewById(R.id.office_longitude);
        office_range = (TextView)v.findViewById(R.id.office_range);
        current_distance = (TextView)v.findViewById(R.id.current_distance);
        binData();
        return v;
    }
    Location mCurrentLocation = null;
    private void binData()
    {
        office_address.setText(companyInfo.description);
        office_latitude.setText(Util.getFormatNumber(companyInfo.lat));
        office_longitude.setText(Util.getFormatNumber(companyInfo.lng ));
        office_range.setText(companyInfo.PermissibleRange + "");
        mCurrentLocation = LocationTracker.getInstance().getLocation();
        if(mCurrentLocation!=null) {
            current_distance.setText(Util.covertDistance(Util.getDistanceInMeters(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),
                    companyInfo.lat,companyInfo.lng)));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //((CompanyInfoActivity)getActivity()).mActionBar.setTitle(companyInfo.nameoffice);
        //LocationTracker.getInstance().registerLocationService(getActivity(), this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationTracker.getInstance().stopLocationUpdates();
    }

    @Override
    public void onLocationChangeCallBack(Location location) {
        mCurrentLocation = location;
        if(mCurrentLocation!=null)
        {
            current_distance.setText(Util.covertDistance(Util.getDistanceInMeters(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),
                    companyInfo.lat,companyInfo.lng)));
        }
    }
}
