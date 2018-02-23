package timecard.dazone.com.dazonetimecard.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.dtos.CompanyDto;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;

public class CompanyDBHelper {
    public static final String TABLE_NAME = "company_infor_tbl";
    public static final String ID = "Id";
    public static final String COMPANY_ID = "company_id";
    public static final String COMPANY_ERROR_RANGE = "range";
    public static final String COMPANY_WORKING = "working";
    public static final String COMPANY_LAT = "company_lat";
    public static final String COMPANY_LNG = "company_lng";
    public static final String COMPANY_DES = "company_des";

    public static final String SQL_EXECUTE = "create table "
            + TABLE_NAME
            + " ("
            + ID + " integer primary key autoincrement, "
            + COMPANY_ID + " integer, "
            + COMPANY_WORKING + " integer, "
            + COMPANY_ERROR_RANGE + " integer, "
            + COMPANY_LAT + " text, "
            + COMPANY_LNG + " text, "
            + COMPANY_DES + " text "
            + ");";

    public static boolean addCompanyInfo(CompanyDto companyDto) {
        try {
            ContentValues values = new ContentValues();
            values.put(COMPANY_LAT, companyDto.Latitude);
            values.put(COMPANY_ERROR_RANGE, companyDto.ErrorRange);
            values.put(COMPANY_WORKING, companyDto.IsWorking);
            values.put(COMPANY_LNG, companyDto.Longitude);
            values.put(COMPANY_DES, companyDto.Description);
            values.put(COMPANY_ID, companyDto.LocationNo);

            ContentResolver resolver = DaZoneApplication.getInstance().getApplicationContext().getContentResolver();
            resolver.insert(AppContentProvider.GET_COMPANY_INFO_CONTENT_URI, values);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ArrayList<CompanyDto> getAllCompanyInfo() {
        String[] columns = new String[] { ID, COMPANY_ID, COMPANY_LAT, COMPANY_LNG, COMPANY_DES };
        ContentResolver resolver = DaZoneApplication.getInstance().getApplicationContext().getContentResolver();
        ArrayList<CompanyDto> arrayList = new ArrayList<>();
        Cursor cursor = resolver.query(AppContentProvider.GET_COMPANY_INFO_CONTENT_URI, columns, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                try {

                    while (!cursor.isLast()) {
                        cursor.moveToNext();

                        CompanyDto companyDto = new CompanyDto();
                        companyDto.LocationNo = cursor.getInt(cursor.getColumnIndex(COMPANY_ID));
                        companyDto.ErrorRange = cursor.getInt(cursor.getColumnIndex(COMPANY_ERROR_RANGE));
                        companyDto.IsWorking = cursor.getInt(cursor.getColumnIndex(COMPANY_WORKING));
                        companyDto.Latitude = cursor.getString(cursor.getColumnIndex(COMPANY_LAT));
                        companyDto.Longitude = cursor.getString(cursor.getColumnIndex(COMPANY_LNG));
                        companyDto.Description = cursor.getString(cursor.getColumnIndex(COMPANY_DES));

                        arrayList.add(companyDto);
                    }
                } finally {
                    cursor.close();
                }
            }
        }

        return arrayList;
    }

    public static boolean clearCompany() {
        try {
            ContentResolver resolver = DaZoneApplication.getInstance().getApplicationContext().getContentResolver();
            resolver.delete(AppContentProvider.GET_COMPANY_INFO_CONTENT_URI, null, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}