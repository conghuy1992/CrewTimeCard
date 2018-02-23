package timecard.dazone.com.dazonetimecard.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.interfaces.OnDialogWithListChoiceCallBack;

public class DialogUtil {

    public static void showGPSModDialog() {
        if (BaseActivity.Instance != null) {
            String contentStr = "";
            switch (LocationUtil.getLocationMode(BaseActivity.Instance)) {
                case 0:
                    contentStr = Util.getString(R.string.location_disable);
                    break;
                case 1:
                    contentStr = Util.getString(R.string.gps_mode);
                    break;
                case 2:
                    if (!Util.isNetworkAvailable()) {
                        contentStr = Util.getString(R.string.gps_disable);
                    } else {
                        contentStr = Util.getString(R.string.gps_mode);
                    }
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            if (!TextUtils.isEmpty(contentStr)) {
                Util.displaySimpleAlertNotCancelableAvoidDouble(BaseActivity.Instance, -1, Util.getString(R.string.app_name), contentStr, Util.getString(R.string.go_gps_enable),
                        Util.getString(R.string.string_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                BaseActivity.Instance.startActivity(callGPSSettingIntent);
                            }
                        }, null);
            }
        }
    }

    public static void showUpdateDialog() {
        if (BaseActivity.Instance != null) {
            Util.displaySimpleAlertNotCancelable(BaseActivity.Instance, -1, Util.getString(R.string.app_name),
                    Util.getString(R.string.string_update_content), Util.getString(R.string.string_update),
                    null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            BaseActivity.Instance.startActivity(callGPSSettingIntent);
                        }
                    }, null);
        }
    }

    public static void showYesNoDialog(final Context context, String title, String message, String strYes, String strNo, DialogInterface.OnClickListener clickListenerYes, DialogInterface.OnClickListener clickListenerNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(strYes)) {
            builder.setPositiveButton(strYes, clickListenerYes);
        }

        if (!TextUtils.isEmpty(strNo)) {
            builder.setNegativeButton(strNo, clickListenerNo);
        }

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.myColor_PrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.myColor_PrimaryDark));
            }
        });

        alertDialog.show();

    }

    /**
     * LIST CHOICE
     */
    public static void displayDialogWithListChoice(final Context context, ArrayList<String> itemList, final OnDialogWithListChoiceCallBack callBack) {
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, itemList);
        adb.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int n) {
                callBack.onClickOK(n);
                d.dismiss();
            }
        });
        adb.show();
    }

    /**
     * DISPLAY DIALOG ONE OPTION
     */
    public static AlertDialog displayDialogSearchBeacon(final Context context, String message, String button, View.OnClickListener onClickListener) {
        View customView = LayoutInflater.from(context).inflate(R.layout.dialog_beacon_search, null);

        /** INIT VIEW */
        TextView btnOK = (TextView) customView.findViewById(R.id.btn_ok);
        TextView tvMessage = (TextView) customView.findViewById(R.id.tv_message);
        tvMessage.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customView);

        final AlertDialog customDialog = builder.create();
        btnOK.setText(button);
        btnOK.setOnClickListener(onClickListener);
        customDialog.show();
        return customDialog;
    }
}