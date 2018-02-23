package timecard.dazone.com.dazonetimecard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Prefs {

    private SharedPreferences prefs;

    private static final String SHAREDPREFERENCES_NAME = "oathsharedpreferences";
    private static final String ACCESS_TOKEN = "accesstoken";
    private static final String ISADMIN = "isadmin";
    private static final String SERVER = "SERVER";
    private static final String SERVERSITE = "serversite";
    private static final String AESORTTYPE = "aesorttype";
    private static final String SORT_STAFF_LIST = "SORT_STAFF_LIST";
    private static final String SSORTTYPE = "sSortType";
    private static final String SSORTTYPE1 = "sSortType1";
    private static final String SSORTTYPE2 = "sSortType2";
    private static final String SSORTTYPE3 = "sSortType3";
    private static final String INTRO_COUNT = "introcount";
    private static final String TIME_OFFSET = "timeoffset";
    private static final String SERVER_TIME = "server_time";
    private static final String PASSWORD = "password";
    private static final String USER_NAME = "username";
    private static final String COMPANYNO = "companyno";
    private static final String DOMAIN = "domain";
    private static final String RELOADSTAFFSTATUS = "reloadstaffstatus";
    private static final String RELOADEMP = "reloademp";
    private static final String USERNO = "user_no";

    public Prefs() {
        prefs = DaZoneApplication.getInstance().getApplicationContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs mInstance;

    public static Prefs getInstance() {
        if (null == mInstance) {
            mInstance = new Prefs();
        }
        return mInstance;
    }

    public void putCompanyNo(int aesorttype) {
        putIntValue(COMPANYNO, aesorttype);
    }

    public void putUserNo(int userNo) {
        putIntValue(USERNO, userNo);
    }

    public int getUserNo() {
        return getIntValue(USERNO, -1);
    }

    public int getCompanyNo() {
        return getIntValue(COMPANYNO, 1);
    }

    public boolean getReloadEmp() {
        return getBooleanValue(RELOADEMP, false);
    }

    public void putReloadEmp(boolean aesorttype) {
        putBooleanValue(RELOADEMP, aesorttype);
    }

    public void putReloadStatus(boolean aesorttype) {
        putBooleanValue(RELOADSTAFFSTATUS, aesorttype);
    }

    public boolean getReloadStatus() {
        return getBooleanValue(RELOADSTAFFSTATUS, false);
    }

    public void putSortStaffList(int index) {
        putIntValue(Statics.PREFS_KEY_SORT_STAFF_LIST, index);
    }

    public int getSortStaffList() {
        return getIntValue(Statics.PREFS_KEY_SORT_STAFF_LIST, 2);
    }

    public void putssorttype(int aesorttype) {
        putIntValue(SSORTTYPE, aesorttype);
    }

    public int getssorttype() {
        return getIntValue(SSORTTYPE, 0);
    }

    public void putssorttype1(int aesorttype) {
        putIntValue(SSORTTYPE1, aesorttype);
    }

    public int getssorttype1() {
        return getIntValue(SSORTTYPE1, 1);
    }

    public void putssorttype2(int aesorttype) {
        putIntValue(SSORTTYPE2, aesorttype);
    }

    public int getssorttype2() {
        return getIntValue(SSORTTYPE2, 2);
    }

    public void putssorttype3(int aesorttype) {
        putIntValue(SSORTTYPE3, aesorttype);
    }

    public int getssorttype3() {
        return getIntValue(SSORTTYPE3, 3);
    }

    public void putaesorttype(int aesorttype) {
        putIntValue(AESORTTYPE, aesorttype);
    }

    public int getaesorttype() {
        return getIntValue(AESORTTYPE, 0);
    }

    public void putTimeOffset(long timeOffset) {
//        Log.d("putTimeOffset","timeOffset:"+timeOffset);
        putLongValue(TIME_OFFSET, timeOffset);
    }

    public long getTimeOffset() {
        return getLongValue(TIME_OFFSET, 0);
    }

    public void putServerTime(long timeOffset) {
        putLongValue(SERVER_TIME, timeOffset);
    }

    public long getServerTime() {
        return getLongValue(SERVER_TIME, 0);
    }

    public void putIntroCount(int introCount) {
        putIntValue(INTRO_COUNT, introCount);
    }

    public int getIntroCount() {
        return getIntValue(INTRO_COUNT, 0);
    }

    public void putIsAdmin(int isAdmin) {
        putIntValue(ISADMIN, isAdmin);
    }

    public int getIsAdmin() {
        return getIntValue(ISADMIN, 0);
    }

    public void putServer(String serverSite) {
        putStringValue(SERVER, serverSite);
    }

    public String getServer() {
        return getStringValue(SERVER, "");
    }

    public void putServerSite(String serverSite) {
        putStringValue(SERVERSITE, serverSite);
    }

    public String getServerSite() {
        return getStringValue(SERVERSITE, "");
    }

    public void putUserName(String username) {
        putStringValue(USER_NAME, username);
    }

    public String getUserName() {
        return getStringValue(USER_NAME, "");
    }



    public void putPassword(String username) {
        putStringValue(PASSWORD, username);
    }

    public String getPassword() {
        return getStringValue(PASSWORD, "");
    }


    public void putDomain(String domain) {
        putStringValue(DOMAIN, domain);
    }

    public String getDomain() {
        return getStringValue(DOMAIN, "");
    }

    public void putAccessToken(String accessToken) {
        putStringValue(ACCESS_TOKEN, accessToken);
    }

    public String getAccessToken() {
        return getStringValue(ACCESS_TOKEN, "");
    }

    public void putBooleanValue(String KEY, boolean value) {
        prefs.edit().putBoolean(KEY, value).apply();
    }

    public boolean getBooleanValue(String KEY, boolean defValue) {
        return prefs.getBoolean(KEY, defValue);
    }

    public void putStringValue(String KEY, String value) {
        prefs.edit().putString(KEY, value).apply();
    }

    public String getStringValue(String KEY, String defValue) {
        return prefs.getString(KEY, defValue);
    }

    private void putIntValue(String KEY, int value) {
        prefs.edit().putInt(KEY, value).apply();
    }

    private int getIntValue(String KEY, int defValue) {
        return prefs.getInt(KEY, defValue);
    }

    private void putLongValue(String KEY, long value) {
        prefs.edit().putLong(KEY, value).apply();
    }

    private long getLongValue(String KEY, long defValue) {
        return prefs.getLong(KEY, defValue);
    }

    public void clearLogin() {
        prefs.edit().remove(ACCESS_TOKEN).apply();
    }
}