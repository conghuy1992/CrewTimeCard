package timecard.dazone.com.dazonetimecard.utils;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.LoginActivity;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;

public class WebServiceManager<T> {
    String TAG="WebServiceManager";
    public void doJsonObjectRequest(int requestMethod, final String url, final JSONObject bodyParam, final RequestListener<String> listener) {
        if (Statics.WRITEHTTPREQUEST) {
            Util.printLogs("url : " + url);
            Util.printLogs("bodyParam : " + bodyParam.toString());
        }
//        Log.d(TAG,"url : " + url);
//        Log.d(TAG,"bodyParam : " + new Gson().toJson(bodyParam));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, bodyParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG,"url : " + url);
//                Log.d(TAG,"bodyParam : " + new Gson().toJson(bodyParam));
//                Log.d(TAG,"onResponse " + response);
//                Log.d(TAG,"----------------");
                if (Statics.WRITEHTTPREQUEST) {
                    Util.printLogs("response.toString() : " + response.toString());
                }
                try {
                    JSONObject json = new JSONObject(response.getString("d"));

                    if (url.contains(Urls.URL_HAS_APPLICATION)) {
                        listener.onSuccess(json.toString());
                    } else if (url.contains(Urls.URL_CHANGE_PASSWORD)) {
                        JSONObject jsonData = json.getJSONObject("data");
                        boolean isSuccess = jsonData.getBoolean("success");
                        if (isSuccess) {
                            listener.onSuccess(jsonData.toString());
                        } else {
                            ErrorDto errorDto = new Gson().fromJson(json.getString("error"), ErrorDto.class);
                            listener.onFailure(errorDto);
                        }
                    } else {
                        int isSuccess = json.getInt("success");
                        if (isSuccess == 1) {
                            listener.onSuccess(json.getString("data"));
                        } else {
                            ErrorDto errorDto = new Gson().fromJson(json.getString("error"), ErrorDto.class);
                            if (errorDto == null) {

                                errorDto = new ErrorDto();
                                errorDto.message = Util.getString(R.string.no_network_error);
                            } else {
                                if (errorDto.code == 0 && !url.contains(Urls.URL_CHECK_SESSION) && !url.contains(Urls.URL_REG_GCM_ID) && !url.contains(Urls.URL_GET_USER_INFO)) {
                                    new Prefs().putBooleanValue(Statics.PREFS_KEY_SESSION_ERROR, true);
                                    DaZoneApplication.getInstance().getmPrefs().clearLogin();
                                    BaseActivity.Instance.startSingleActivity(LoginActivity.class);
                                } else {
                                    Util.printLogs("Form is invalid");
                                }
                            }

                            listener.onFailure(errorDto);
                        }
                    }

                } catch (JSONException e) {

                    ErrorDto errorDto = new ErrorDto();
                    errorDto.message = Util.getString(R.string.no_network_error);
                    errorDto.message = e.toString();
                    listener.onFailure(errorDto);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"onErrorResponse " + error.toString());

                ErrorDto errorDto = new ErrorDto();
                errorDto.serverError=true;
                if (null != error) {
                    listener.onFailure(errorDto);
                } else {
                    listener.onFailure(errorDto);
                }
            }
        });
        DaZoneApplication.getInstance().addToRequestQueue(jsonObjectRequest, url);
    }

    public interface RequestListener<T> {
        void onSuccess(T response);

        void onFailure(ErrorDto error);
    }
}