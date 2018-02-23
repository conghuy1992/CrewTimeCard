package timecard.dazone.com.dazonetimecard.utils;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

public class LocationUtil {
    public static int getLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = 0;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = 3;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                locationMode = 1;
            } else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = 2;
            }
        }

        return locationMode;
    }
}