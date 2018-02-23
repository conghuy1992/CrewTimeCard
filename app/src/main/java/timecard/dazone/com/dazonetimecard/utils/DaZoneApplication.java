package timecard.dazone.com.dazonetimecard.utils;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DaZoneApplication extends Application {
    private static final String TAG = "EmcorApplication";

    private static DaZoneApplication _instance;
    private RequestQueue mRequestQueue;

    private static Prefs mPrefs;

    public static boolean isTurnOff = false;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        init();
    }

    public static synchronized DaZoneApplication getInstance() {
        return _instance;
    }

    private static void init() {
        mPrefs = new Prefs();
    }

    public Prefs getmPrefs() {
        return mPrefs;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(Statics.REQUEST_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void clearCache() {
        getRequestQueue().getCache().clear();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (isTurnOff) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.disable();
        }
    }
}