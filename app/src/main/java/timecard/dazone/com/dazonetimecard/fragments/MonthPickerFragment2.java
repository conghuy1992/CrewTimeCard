package timecard.dazone.com.dazonetimecard.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class MonthPickerFragment2 extends DatePickerMonthYearFragment {
    @Override
    protected void modifiedPicker() {
        if (datePicker != null) {
            Util.printLogs("Resources.getSystem() : " + Resources.getSystem());
            Util.printLogs("Resources.getSystem().getIdentifier : " + Resources.getSystem().getIdentifier("day", "id", "android"));
            View v = datePicker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android"));
            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }
    }

    public static MonthPickerFragment2 newInstance(Date date, int dialogType, String strDate) {
        Bundle arg = new Bundle();
        arg.putSerializable(extraDate, date);
        arg.putSerializable(extraType, dialogType);
        arg.putString(Statics.KEY_MONTH_PICKED, strDate);

        MonthPickerFragment2 fragment = new MonthPickerFragment2();
        fragment.setArguments(arg);
        return fragment;
    }
}
