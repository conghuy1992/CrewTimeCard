package timecard.dazone.com.dazonetimecard.helper;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.interfaces.GetAddressCallback;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class LocationAddress extends AsyncTask<String, Void, String[]> {

    String latLng;
    GetAddressCallback callback;

    public LocationAddress(double lat, double lng, GetAddressCallback callback) {
        super();
        this.latLng = lat + "," + lng;
        this.callback = callback;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String response;
        Util.printLogs(" latLng 111:" + latLng);
        try {
            String url = "http://maps.google.com/maps/api/geocode/json?latlng=" + latLng + "&sensor=false&language=" + Locale.getDefault().getLanguage();
            response = getLatLongByURL(url);

            return new String[]{response};
        } catch (Exception e) {
            return new String[]{"error"};
        }
    }

    @Override
    protected void onPostExecute(String... result) {
        String address = null;
        if (BaseActivity.Instance == null) {
            return;
        }
        if (!result[0].equalsIgnoreCase("error")) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                address = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getString("formatted_address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (callback != null) {
            callback.onGetAddressCallback(address);
        }
    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}