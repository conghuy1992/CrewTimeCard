package timecard.dazone.com.dazonetimecard.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import timecard.dazone.com.dazonetimecard.BuildConfig;
import timecard.dazone.com.dazonetimecard.R;

public class Statics {

    public static final String TAG = ">>>>>> TimeCard";
    public static final boolean ENABLE_DEBUG = BuildConfig.ENABLE_DEBUG;
    public static final String FORMAT_DATE_MONTH_YEAR = "yyyy-MM-dd";
    public static final String FORMAT_AM_PM = "aa";
    public static final String FORMAT_HOUR_MINUTE_SECOND = "hh:mm:ss";
    public static final String FORMAT_HOUR_MINUTE_SECOND_AMPM = "hh:mm:ss a";
    public static final String FORMAT_HOUR_MINUTE_SECOND_AMPM_KOREAN = "a hh:mm:ss";

    public static final String FORMAT_HOUR_MINUTE_ALL_EMP = "HH:mm";
    public static final String FORMAT_DATE_OF_WEEK = "EEEEEEE";
    public static final String FORMAT_DATE_MY_LIST = "EEEEEEE, MMM dd";
    public static final String FORMAT_DATE_MY_LIST_KR = "MMM dd, EEEEEEE";
    public static final String FORMAT_DATE_SESSION_EXPIRE = "EEEEEEE, dd MMM yyyy HH:mm:ss";
    public static final int REQUEST_TIMEOUT_MS = 15000;

    public static final int INTRO_PAGE_NUMBER = 5;

    public static final String KEY_URL = "KEY_URL";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE_COMPANY = "latitude_company";
    public static final String KEY_LONGITUDE_COMPANY = "longitude_company";
    public static final String KEY_DISTANCE = "distance";

    public static final String KEY_SORT_TYPE = "sortType";
    public static final String KEY_GROUP_NO = "KEY_GROUP_NO";

    public static final String KEY_COMPANY = "company";
    public static final String KEY_USER_NO = "userNo";
    public static final String KEY_TIME_REQUEST = "timerequest";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_PREF_DISTANCE = "pref_distance";

    public static final String KEY_MONTH_PICKED = "KEY_MONTH_PICKED";

    public static final String KEY_BEACON = "KEY_BEACON";
    public static final String KEY_TIME_TYPE = "KEY_TIME_TYPE";
    public static final String KEY_LIST_BEACON = "KEY_LIST_BEACON";
    public static final String IS_CHECK_ALLOW = "IS_CHECK_ALLOW";


    public static final  int USER_LOGOUT = 0;

    public static final boolean WRITEHTTPREQUEST = true;

    //for database
    public static final int DATABASE_VERSION = 14;
    public static final String DATABASE_NAME = "emcor.db";

    public static final String FILE = "drawable://";

    public static final DisplayImageOptions optionsProfileAvatarRounded = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.loading)
            .showImageOnFail(R.drawable.loading)
            .cacheOnDisk(true).cacheInMemory(true)
            .imageScaleType(ImageScaleType.NONE_SAFE)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(false)
            .displayer(new RoundedBitmapDisplayer(180))
            .showImageOnLoading(R.drawable.loading)
            .build();

    public static final DisplayImageOptions optionsProfileAvatar = new DisplayImageOptions.Builder()
            .cacheOnDisk(true).cacheInMemory(true)
            .imageScaleType(ImageScaleType.NONE_SAFE)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(false)
            .build();


    /** PREFS */
    public static final String PREFS_KEY_RELOAD_SETTING = "reload_setting";
    public static final String PREFS_KEY_RELOAD_TIMECARD = "reload_timecard";
    public static final String PREFS_KEY_SESSION_ERROR = "session_error";
    public static final String PREFS_KEY_SORT_STAFF_LIST = "PREFS_KEY_SORT_STAFF_LIST";
    public static final String PREFS_KEY_COMPANY_NAME = "PREFS_KEY_COMPANY_NAME";
    public static final String PREFS_KEY_COMPANY_DOMAIN = "PREFS_KEY_COMPANY_DOMAIN";
    public static final String PREFS_KEY_USER_ID = "PREFS_KEY_USER_ID";

    /** KEY ACTIVITY RESULT */
    public static final int REQUEST_CODE_BLUE_TOOTH = 15000;

}