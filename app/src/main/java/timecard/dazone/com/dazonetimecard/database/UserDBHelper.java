package timecard.dazone.com.dazonetimecard.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import timecard.dazone.com.dazonetimecard.dtos.CompanyDto;
import timecard.dazone.com.dazonetimecard.dtos.UserDto;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;

public class UserDBHelper {
    public static final String TABLE_NAME = "UserTbl";
    public static final String ID = "Id";
    public static final String USER_ID = "user_id";
    public static final String USER_SESSION = "user_session";
    public static final String USER_NAME = "user_name";
    public static final String USER_COMPANY = "user_company";
    public static final String USER_FULLNAME = "user_fullname";
    public static final String USER_AVATAR = "user_avartar";

    public static final String SQL_EXECUTE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " integer primary key autoincrement not null," + USER_ID + " integer," + USER_SESSION
            + " text, " + USER_NAME + " text, " + USER_COMPANY + " text, "
            + USER_FULLNAME + " text, " + USER_AVATAR + " text );";

    public static UserDto getUser() {
        String[] columns = new String[]{"*"};
        ContentResolver resolver = DaZoneApplication.getInstance().getApplicationContext().getContentResolver();
        Cursor cursor = resolver.query(AppContentProvider.GET_USER_CONTENT_URI, columns, null, null, null);
        UserDto userDto = new UserDto();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                try {
                    if (cursor.moveToFirst()) {
                        userDto.Id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(USER_ID)));
                        userDto.session = cursor.getString(cursor.getColumnIndex(USER_SESSION));
                        userDto.userID = cursor.getString(cursor.getColumnIndex(USER_NAME));
                        userDto.NameCompany = cursor.getString(cursor.getColumnIndex(USER_COMPANY));
                        userDto.FullName = cursor.getString(cursor.getColumnIndex(USER_FULLNAME));
                        userDto.avatar = cursor.getString(cursor.getColumnIndex(USER_AVATAR));
                    }
                } finally {
                    cursor.close();
                }
            }
        }

        return userDto;
    }

    public static boolean addUser(UserDto userDto) {
        clearUser();
        CompanyDBHelper.clearCompany();

        try {
            ContentValues values = new ContentValues();
            values.put(USER_ID, userDto.Id);
            values.put(USER_SESSION, userDto.session);
            values.put(USER_NAME, userDto.userID);
            values.put(USER_COMPANY, userDto.NameCompany);
            values.put(USER_FULLNAME, userDto.FullName);
            values.put(USER_AVATAR, userDto.avatar);

            for (CompanyDto companyDto : userDto.informationcompany) {
                CompanyDBHelper.addCompanyInfo(companyDto);
            }

            ContentResolver resolver = DaZoneApplication.getInstance().getApplicationContext().getContentResolver();
            resolver.insert(AppContentProvider.GET_USER_CONTENT_URI, values);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean clearUser() {
        try {
            ContentResolver resolver = DaZoneApplication.getInstance().getApplicationContext().getContentResolver();
            resolver.delete(AppContentProvider.GET_USER_CONTENT_URI, null, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}