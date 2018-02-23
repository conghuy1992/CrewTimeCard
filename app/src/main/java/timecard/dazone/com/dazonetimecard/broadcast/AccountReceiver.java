package timecard.dazone.com.dazonetimecard.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class AccountReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String senderPackageName = intent.getExtras().getString("senderPackageName");
        if (!senderPackageName.equals(context.getPackageName())) {
            Intent intentReceive = new Intent();
            intentReceive.setAction("com.dazone.crewcloud.account.receive");
            intentReceive.putExtra("senderPackageName", context.getPackageName());
            intentReceive.putExtra("receiverPackageName", senderPackageName);
            Prefs prefs = DaZoneApplication.getInstance().getmPrefs().getInstance();
            String userID = prefs.getUserName();
            String companyID = rootLinkToDomain(prefs.getServerSite());
            intentReceive.putExtra("companyID", companyID);
            intentReceive.putExtra("userID", userID);
            intentReceive.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.sendBroadcast(intentReceive);
        }
    }

    private String rootLinkToDomain(String server_site) {
        String result = server_site;
        result = result.replace("http://", "");
        result = result.replace("www.", "");
        if (result.contains(".bizsw.co.kr")) {
            result = result.split(":")[0];
        } else {

            result = result.replace(".crewcloud.net", "");
        }
        return result;
    }
}