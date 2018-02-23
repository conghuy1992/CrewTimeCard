package timecard.dazone.com.dazonetimecard.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static boolean firstDisconnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (Util.isNetworkAvailable()) {
            firstDisconnect = true;
        } else {
            if (firstDisconnect) {
                firstDisconnect = false;
                if (BaseActivity.Instance != null) {
                    try {
                        BaseActivity.Instance.showNetworkDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
    }
}