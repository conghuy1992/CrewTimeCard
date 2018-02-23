package timecard.dazone.com.dazonetimecard.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timecard.dazone.com.dazonetimecard.utils.Statics;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = Statics.DATABASE_NAME;
    public static final int DB_VERSION = Statics.DATABASE_VERSION;

    public static final String[] TABLE_NAMES = new String[]{
            UserDBHelper.TABLE_NAME, CompanyDBHelper.TABLE_NAME
    };

    public static final String[] SQL_EXECUTE = new String[]{
            UserDBHelper.SQL_EXECUTE, CompanyDBHelper.SQL_EXECUTE
    };

    public AppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();

        try {
            execMultipleSQL(db, SQL_EXECUTE);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            dropMultipleSQL(db, TABLE_NAMES);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.onCreate(db);
    }

    private void execMultipleSQL(SQLiteDatabase db, String[] sql) throws android.database.SQLException {
        for (String s : sql) {
            if (s.trim().length() > 0) {
                try {
                    db.execSQL(s);
                } catch (android.database.SQLException e) {
                    throw new android.database.SQLException();
                }
            }
        }
    }

    private void dropMultipleSQL(SQLiteDatabase db, String[] tableNames) throws android.database.SQLException {
        for (String s : tableNames) {
            if (s.trim().length() > 0) {
                try {
                    db.execSQL("DROP TABLE IF EXISTS " + s + ";");
                } catch (android.database.SQLException e) {
                    throw new android.database.SQLException();
                }
            }
        }
    }
}