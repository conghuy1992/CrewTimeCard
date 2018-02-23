package timecard.dazone.com.dazonetimecard.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import timecard.dazone.com.dazonetimecard.BuildConfig;

public class AppContentProvider extends ContentProvider {
    AppDatabaseHelper mDatabaseHelper;

    private static final int GET_USER_KEY = 1;
    private static final int GET_USER_ROW_KEY = 2;
    private static final int GET_COMPANY_INFO_KEY = 3;
    private static final int GET_COMPANY_INFO_ROW_KEY = 4;

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String GET_USER_PATH = "request";
    private static final String GET_COMPANY_INFO_PATH = "request_company_info";

    public static final Uri GET_USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + GET_USER_PATH);
    public static final Uri GET_COMPANY_INFO_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + GET_COMPANY_INFO_PATH);

    static {
        sUriMatcher.addURI(AUTHORITY, GET_USER_PATH, GET_USER_KEY);
        sUriMatcher.addURI(AUTHORITY, GET_USER_PATH + "/#", GET_USER_ROW_KEY);
        sUriMatcher.addURI(AUTHORITY, GET_COMPANY_INFO_PATH, GET_COMPANY_INFO_KEY);
        sUriMatcher.addURI(AUTHORITY, GET_COMPANY_INFO_PATH + "/#", GET_COMPANY_INFO_ROW_KEY);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new AppDatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int row_deleted;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int uriKey = sUriMatcher.match(uri);

        switch (uriKey) {
            case GET_USER_ROW_KEY:
                row_deleted = db.delete(UserDBHelper.TABLE_NAME, UserDBHelper.ID + " = " + uri.getLastPathSegment() + " and " + selection, selectionArgs);
                break;
            case GET_USER_KEY:
                row_deleted = db.delete(UserDBHelper.TABLE_NAME, selection, selectionArgs);
                break;

            case GET_COMPANY_INFO_ROW_KEY:
                row_deleted = db.delete(CompanyDBHelper.TABLE_NAME, CompanyDBHelper.ID + " = " + uri.getLastPathSegment() + " and " + selection, selectionArgs);
                break;
            case GET_COMPANY_INFO_KEY:
                row_deleted = db.delete(CompanyDBHelper.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return row_deleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri result_uri;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        int uriKey = sUriMatcher.match(uri);
        long id;

        switch (uriKey) {
            case GET_USER_KEY:
                id = db.insertWithOnConflict(UserDBHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result_uri = Uri.parse(GET_USER_CONTENT_URI + "/" + Long.toString(id));
                break;

            case GET_COMPANY_INFO_KEY:
                id = db.insertWithOnConflict(CompanyDBHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result_uri = Uri.parse(GET_COMPANY_INFO_CONTENT_URI + "/" + Long.toString(id));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return result_uri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriKey = sUriMatcher.match(uri);

        switch (uriKey) {
            case GET_USER_ROW_KEY:
                queryBuilder.appendWhere(UserDBHelper.ID + " = " + uri.getLastPathSegment());
            case GET_USER_KEY:
                queryBuilder.setTables(UserDBHelper.TABLE_NAME);
                cursor = queryBuilder.query(db, null, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case GET_COMPANY_INFO_ROW_KEY:
                queryBuilder.appendWhere(CompanyDBHelper.ID + " = " + uri.getLastPathSegment());
            case GET_COMPANY_INFO_KEY:
                queryBuilder.setTables(CompanyDBHelper.TABLE_NAME);
                cursor = queryBuilder.query(db, null, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int row_update;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int uriKey = sUriMatcher.match(uri);

        switch (uriKey) {
            case GET_USER_ROW_KEY:
                row_update = db.update(UserDBHelper.TABLE_NAME, values, UserDBHelper.ID + " = " + uri.getLastPathSegment() + " and " + selection, selectionArgs);
                break;

            case GET_USER_KEY:
                row_update = db.update(UserDBHelper.TABLE_NAME, values, selection, selectionArgs);
                break;

            case GET_COMPANY_INFO_ROW_KEY:
                row_update = db.update(CompanyDBHelper.TABLE_NAME, values, CompanyDBHelper.ID + " = " + uri.getLastPathSegment() + " and " + selection, selectionArgs);
                break;

            case GET_COMPANY_INFO_KEY:
                row_update = db.update(CompanyDBHelper.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return row_update;
    }
}