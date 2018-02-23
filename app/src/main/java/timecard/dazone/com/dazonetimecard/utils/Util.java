package timecard.dazone.com.dazonetimecard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Util {
    private static Toast toast;

    public static void setTypeFacee(Context context, TextView textView) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf");
        textView.setTypeface(tf);
    }

    public static void setTypeFaceMedium(Context context, TextView textView) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Medium.ttf");
        textView.setTypeface(tf);
    }

    public static void showShortMessage(int text) {
        showMessage(getString(text));
    }

    public static void showShortMessage(String text) {
        if (toast == null) {
            toast = Toast.makeText(DaZoneApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static String getFormatNumber(Double x) {
        DecimalFormat df = new DecimalFormat("#.######");
        String temp = df.format(x);
        Double abc = Double.parseDouble(temp);
        return String.valueOf(abc);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) DaZoneApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public static boolean isWifiEnable() {
        WifiManager wifi = (WifiManager) DaZoneApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) DaZoneApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isWifiEnabled() && mWifi.isConnected();
    }

    public static boolean isMobileEnable() {
        ConnectivityManager connMgr = (ConnectivityManager) DaZoneApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile.isAvailable() && mobile.isConnected()) {
            return true;
        }
        return false;
    }


    public static void printLogs(String logs) {
        if (Statics.ENABLE_DEBUG) {
            if (logs == null)
                return;
            int maxLogSize = 1000;
            if (logs.length() > maxLogSize) {
                for (int i = 0; i <= logs.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > logs.length() ? logs.length() : end;
                    Log.d(Statics.TAG, logs.substring(start, end));
                }
            } else {
                Log.d(Statics.TAG, logs);
            }
        }
    }

    public static void displaySimpleAlert(Context context, int icon, String title, String message, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (icon != -1) {
            builder.setIcon(icon);
        }
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(positiveTitle)) {
            builder.setPositiveButton(positiveTitle, onPositiveClick);
        }
        if (!TextUtils.isEmpty(negativeTitle)) {
            builder.setNegativeButton(negativeTitle, onNegativeClick);
        }
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public static Dialog displaySimpleAlertNotCancelable(Context context, int icon, String title, String message, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (icon != -1) {
            builder.setIcon(icon);
        }
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(positiveTitle)) {
            builder.setPositiveButton(positiveTitle, onPositiveClick);
        }
        if (!TextUtils.isEmpty(negativeTitle)) {
            builder.setNegativeButton(negativeTitle, onNegativeClick);
        }
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    private static AlertDialog dialog;

    public static Dialog displaySimpleAlertNotCancelableAvoidDouble(Context context, int icon, String title, String message, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (icon != -1) {
            builder.setIcon(icon);
        }
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(positiveTitle)) {
            builder.setPositiveButton(positiveTitle, onPositiveClick);
        }
        if (!TextUtils.isEmpty(negativeTitle)) {
            builder.setNegativeButton(negativeTitle, onNegativeClick);
        }
        if (dialog != null && dialog.isShowing()) {
            return null;
        } else {
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
        return dialog;
    }

    public static void showMessage(String message) {
        Toast.makeText(DaZoneApplication.getInstance().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(int string_id) {
        Toast.makeText(DaZoneApplication.getInstance().getApplicationContext(), getString(string_id), Toast.LENGTH_LONG).show();
    }

    public static String getString(int stringID) {
        return DaZoneApplication.getInstance().getApplicationContext().getResources().getString(stringID);
    }

    public static Resources getResources() {
        return DaZoneApplication.getInstance().getApplicationContext().getResources();
    }

    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkStringValue(String... params) {
        for (String param : params) {
            if (param != null) {
                if (TextUtils.isEmpty(param.trim())) {
                    return false;
                }

                if (param.contains("\n") && TextUtils.isEmpty(param.replace("\n", ""))) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameLayout, boolean isSaveStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout, fragment);

        if (isSaveStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int frameLayout, boolean isSaveStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameLayout, fragment);

        if (isSaveStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public static String setDateInfo(long milis, String format) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(format, Locale.getDefault());

        if (milis >= 0) {
            //cal.setTimeZone();
            return timeFormat.format(new Date(milis + DaZoneApplication.getInstance().getmPrefs().getTimeOffset()));
        }

        return "N/A";
    }

    public static String setDateInfoList(long milis, String format) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(format, Locale.getDefault());

        if (milis >= 0) {
            return timeFormat.format(new Date(milis));
        }

        return "N/A";
    }

    public static String setDateInfoListNoTimeZone(long milis, String format) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(format, Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (milis >= 0) {
            return timeFormat.format(new Date(milis));
        }

        return "N/A";
    }

    public static void drawCycleImage(ImageView profilePic, int imId, int size) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(DaZoneApplication.getInstance().getResources(), imId);
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, size, size, false);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(DaZoneApplication.getInstance().getResources(), imageBitmap);
        roundedBitmapDrawable.setCornerRadius(size / 2);
        roundedBitmapDrawable.setAntiAlias(true);
        profilePic.setImageDrawable(roundedBitmapDrawable);
    }

    public static int getDimenInPx(int id) {
        return (int) DaZoneApplication.getInstance().getApplicationContext().getResources().getDimension(id);
    }

    public static long getTimeOffsetInHour() {
        return TimeUnit.HOURS.convert(getTimeOffsetInMilis(), TimeUnit.MILLISECONDS);
    }

    public static long getTimeOffsetInMinute() {
        return TimeUnit.MINUTES.convert(getTimeOffsetInMilis(), TimeUnit.MILLISECONDS);
    }

    public static long getTimeOffsetInMilis() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();

        return mTimeZone.getRawOffset();
    }

    public static String getTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());

        return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

    public static long getDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad, lat2Rad, deltaLonRad, dist;
        //List<Long> value = new ArrayList<>();

        lat2Rad = Math.toRadians(lat2);
        lat1Rad = Math.toRadians(lat1);
        deltaLonRad = Math.toRadians(lon2 - lon1);
        dist = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
        return Math.round(dist * 1000);
    }

    public static String covertDistance(long distance) {
        String dist = "";
        if (distance < 0) {
            return dist;
        }

        if (distance >= 10000) {
            double temp = distance / 1000.0000;

            if (distance < 100000) {
                dist = new DecimalFormat("###,###.00").format(temp) + " km";
            } else if (distance < 1000000) {
                dist = new DecimalFormat("###,###.0").format(temp) + " km";
            } else {
                dist = new DecimalFormat("###,###").format(temp) + " km";
            }
        } else {
            dist = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.US)).format(distance) + " m";
//            dist = new DecimalFormat("###,###").format(distance) + " m";
        }
        Log.d("dist","dist:"+dist);
        return dist;
    }

    public static String getPhoneLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static boolean isPhoneLanguageEN() {
        return Locale.getDefault().getLanguage().equalsIgnoreCase("EN");
    }

    public static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constant.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat(Constant.DATE_FORMAT_PICTURE,
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Constant.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + Constant.IMAGE_JPG);
        } else if (type == Constant.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + Constant.VIDEO_MP4);
        } else {
            return null;
        }
        return mediaFile;
    }
}