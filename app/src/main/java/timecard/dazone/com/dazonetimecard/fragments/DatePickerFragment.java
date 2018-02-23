package timecard.dazone.com.dazonetimecard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.interfaces.DatePickerDialogListener;

public class DatePickerFragment extends DialogFragment {
    public static final String extraDate = "extraDate";
    public static final String extraType = "extraType";
    private Date mDate;
    private int mType;
    protected DatePicker datePicker;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar cal = Calendar.getInstance();
        mDate = (Date) getArguments().getSerializable(extraDate);
        mType = (int) getArguments().getSerializable(extraType);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                mDate = calendar.getTime();
                getArguments().putSerializable(extraDate, mDate);

            }
        });
        modifiedPicker();
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        if (mType != 0) {
                            DatePickerDialogListener activity = (DatePickerDialogListener) getActivity();
                            if (activity != null)
                                activity.onFinishEditDialog(cal);
                        } else {
                            mDate = cal.getTime();
                            sendResult(Activity.RESULT_OK);
                        }
                    }
                })
                .create();
    }

    protected void modifiedPicker() {

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(extraDate, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle arg = new Bundle();
        arg.putSerializable(extraDate, date);
        arg.putSerializable(extraType, 0);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    public static DatePickerFragment newInstance(Date date, int dialogType) {
        Bundle arg = new Bundle();
        arg.putSerializable(extraDate, date);
        arg.putSerializable(extraType, dialogType);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(arg);
        return fragment;
    }
}
