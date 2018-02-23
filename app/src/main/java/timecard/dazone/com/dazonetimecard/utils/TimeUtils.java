package timecard.dazone.com.dazonetimecard.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    public static final Calendar FIRST_DAY_OF_TIME;
    public static final Calendar LAST_DAY_OF_TIME;
    public static final int DAYS_OF_TIME;
    public static final int MONTHS_OF_TIME;

    static {
        FIRST_DAY_OF_TIME = Calendar.getInstance();
        FIRST_DAY_OF_TIME.set(Calendar.getInstance().get(Calendar.YEAR) - 100, Calendar.JANUARY, 1);
        LAST_DAY_OF_TIME = Calendar.getInstance();
//        if(new Prefs().getServerTime()!=0)
//        {
//            long temp = new Prefs().getServerTime()+ Util.getTimeOffsetInMilis();
//            LAST_DAY_OF_TIME.setTimeInMillis(temp);
//        }
//        else
//        {
//            long temp = LAST_DAY_OF_TIME.getTimeInMillis()+ Util.getTimeOffsetInMilis();
//            LAST_DAY_OF_TIME.setTimeInMillis(temp);
//        }
        LAST_DAY_OF_TIME.set(Calendar.getInstance().get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31);
//        LAST_DAY_OF_TIME.add(Calendar.DATE,1);
        DAYS_OF_TIME = (int) Math.ceil((LAST_DAY_OF_TIME.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis() + Util.getTimeOffsetInMilis()) / (24 * 60 * 60 * 1000));
        MONTHS_OF_TIME = (LAST_DAY_OF_TIME.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR)) * 12 + LAST_DAY_OF_TIME.get(Calendar.MONTH) - FIRST_DAY_OF_TIME.get(Calendar.MONTH) + 1;
    }

    public static int getPositionForMonth(Calendar month) {
        if (month != null) {
            int diffYear = month.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR);
            return diffYear * 12 + month.get(Calendar.MONTH) - FIRST_DAY_OF_TIME.get(Calendar.MONTH);
        }
        return 0;
    }

    public static Calendar getMonthForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.YEAR, position / 12);
        cal.add(Calendar.MONTH, position % 12);
        return cal;
    }

    public static Calendar getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, position);
        return cal;
    }

    public static int getPositionForDay(Calendar day) {
        if (day != null) {
            return (int) ((day.getTimeInMillis() + Util.getTimeOffsetInMilis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / 86400000  //(24 * 60 * 60 * 1000)
            );
        }
        return 0;
    }

    public static String getFormattedDate(long date) {
        final String defaultPattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static String getFormattedMonth(long date) {
        final String defaultPattern = "yyyy-MM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static String getFormattedTime(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static String getFormattedTimeWithoutTimeZone(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date(date));
    }

    public static String getTimezoneOffsetInMinutes() {
        TimeZone tz = TimeZone.getDefault();
        int offsetMinutes = tz.getRawOffset() / 60000;
        String sign = "";
        if (offsetMinutes < 0) {
            sign = "-";
            offsetMinutes = -offsetMinutes;
        }
        return sign + "" + offsetMinutes;
    }
}