package timecard.dazone.com.dazonetimecard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Calendar;
import java.util.Date;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.interfaces.DatePickerDialogListener;
import timecard.dazone.com.dazonetimecard.utils.Statics;

public class DatePickerMonthYearFragment extends DialogFragment implements View.OnClickListener {
    public static final String extraDate = "extraDate";
    public static final String extraType = "extraType";
    private String strDate = "";
    private Date mDate;
    private int mType;
    protected DatePicker datePicker;

    private View rootView;
    private TextSwitcher tvYear;

    int mYear = Calendar.getInstance().get(Calendar.YEAR);

    /**
     * VIEW
     */
    private LinearLayout btnMonth1;
    private LinearLayout btnMonth2;
    private LinearLayout btnMonth3;
    private LinearLayout btnMonth4;
    private LinearLayout btnMonth5;
    private LinearLayout btnMonth6;
    private LinearLayout btnMonth7;
    private LinearLayout btnMonth8;
    private LinearLayout btnMonth9;
    private LinearLayout btnMonth10;
    private LinearLayout btnMonth11;
    private LinearLayout btnMonth12;


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final Calendar cal = Calendar.getInstance();
        mDate = (Date) getArguments().getSerializable(extraDate);
        mType = (int) getArguments().getSerializable(extraType);
        strDate = getArguments().getString(Statics.KEY_MONTH_PICKED, "");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        //int year = calendar.get(Calendar.YEAR);
        //final int month = calendar.get(Calendar.MONTH);
        //final int day = calendar.get(Calendar.DAY_OF_MONTH);
        rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_date_month_year, null);
        initView();
        setMonthPicked();
        /*datePicker = (DatePicker) rootView.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                mDate = calendar.getTime();
                getArguments().putSerializable(extraDate, mDate);

            }
        });*/
        modifiedPicker();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light));
        //dialogBuilder.setNegativeButton(null,null);
        //dialogBuilder.setPositiveButton(null,null);

        //dialogBuilder.create();


        AlertDialog dialog = dialogBuilder.create();
        dialog.setView(rootView, 0, 0, 0, 0);
        //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        //dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);

        return dialog;
       /* return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                *//*.setTitle(R.string.date_picker_title)
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
                })*//*
                .create();*/


    }

    /**
     * SET MONTH PICKED
     */
    private void setMonthPicked() {
        if (!TextUtils.isEmpty(strDate)) {

            int mMonth = 0;
            try {
                mYear = Integer.parseInt(strDate.split("-")[0]);
                mMonth = Integer.parseInt(strDate.split("-")[strDate.split("-").length - 1]);
            } catch (Exception e) {
                mYear = Calendar.getInstance().get(Calendar.YEAR);
                e.printStackTrace();
            }
            tvYear.setText(mYear + "");
            switch (mMonth) {
                case 1:
                    btnMonth1.setPressed(true);
                    break;
                case 2:
                    btnMonth2.setPressed(true);
                    break;
                case 3:
                    btnMonth3.setPressed(true);
                    break;
                case 4:
                    btnMonth4.setPressed(true);
                    break;
                case 5:
                    btnMonth5.setPressed(true);
                    break;
                case 6:
                    btnMonth6.setPressed(true);
                    break;
                case 7:
                    btnMonth7.setPressed(true);
                    break;
                case 8:
                    btnMonth8.setPressed(true);
                    break;
                case 9:
                    btnMonth9.setPressed(true);
                    break;
                case 10:
                    btnMonth10.setPressed(true);
                    break;
                case 11:
                    btnMonth11.setPressed(true);
                    break;
                case 12:
                    btnMonth12.setPressed(true);
                    break;
            }
        }
    }

    private void initView() {
        tvYear = (TextSwitcher) rootView.findViewById(R.id.tv_year);

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        tvYear.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(getActivity());
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextColor(Color.BLACK);
                myText.setTypeface(Typeface.DEFAULT_BOLD);
                myText.setText(String.valueOf(mYear));
                return myText;
            }
        });

        ImageView btnPreviousYear = (ImageView) rootView.findViewById(R.id.btn_previous_year);
        ImageView btnNextYear = (ImageView) rootView.findViewById(R.id.btn_next_year);

        btnPreviousYear.setOnClickListener(this);
        btnNextYear.setOnClickListener(this);

        /** LAYOUT MONTH */
        btnMonth1 = (LinearLayout) rootView.findViewById(R.id.btn_month_1);
        btnMonth1.setTag(1);
        btnMonth1.setOnClickListener(this);

        btnMonth2 = (LinearLayout) rootView.findViewById(R.id.btn_month_2);
        btnMonth2.setTag(2);
        btnMonth2.setOnClickListener(this);

        btnMonth3 = (LinearLayout) rootView.findViewById(R.id.btn_month_3);
        btnMonth3.setTag(3);
        btnMonth3.setOnClickListener(this);

        btnMonth4 = (LinearLayout) rootView.findViewById(R.id.btn_month_4);
        btnMonth4.setTag(4);
        btnMonth4.setOnClickListener(this);

        btnMonth5 = (LinearLayout) rootView.findViewById(R.id.btn_month_5);
        btnMonth5.setTag(5);
        btnMonth5.setOnClickListener(this);

        btnMonth6 = (LinearLayout) rootView.findViewById(R.id.btn_month_6);
        btnMonth6.setTag(6);
        btnMonth6.setOnClickListener(this);

        btnMonth7 = (LinearLayout) rootView.findViewById(R.id.btn_month_7);
        btnMonth7.setTag(7);
        btnMonth7.setOnClickListener(this);

        btnMonth8 = (LinearLayout) rootView.findViewById(R.id.btn_month_8);
        btnMonth8.setTag(8);
        btnMonth8.setOnClickListener(this);

        btnMonth9 = (LinearLayout) rootView.findViewById(R.id.btn_month_9);
        btnMonth9.setTag(9);
        btnMonth9.setOnClickListener(this);

        btnMonth10 = (LinearLayout) rootView.findViewById(R.id.btn_month_10);
        btnMonth10.setTag(10);
        btnMonth10.setOnClickListener(this);

        btnMonth11 = (LinearLayout) rootView.findViewById(R.id.btn_month_11);
        btnMonth11.setTag(11);
        btnMonth11.setOnClickListener(this);

        btnMonth12 = (LinearLayout) rootView.findViewById(R.id.btn_month_12);
        btnMonth12.setTag(12);
        btnMonth12.setOnClickListener(this);
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

    public static DatePickerMonthYearFragment newInstance(Date date) {
        Bundle arg = new Bundle();
        arg.putSerializable(extraDate, date);
        arg.putSerializable(extraType, 0);
        DatePickerMonthYearFragment fragment = new DatePickerMonthYearFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    public static DatePickerMonthYearFragment newInstance(Date date, int dialogType) {
        Bundle arg = new Bundle();
        arg.putSerializable(extraDate, date);
        arg.putSerializable(extraType, dialogType);
        DatePickerMonthYearFragment fragment = new DatePickerMonthYearFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        Animation in;
        Animation out;
        switch (v.getId()) {
            case R.id.btn_previous_year:
                mYear--;
                in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
                out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
                tvYear.setInAnimation(in);
                tvYear.setOutAnimation(out);
                tvYear.setText(mYear + "");
                break;
            case R.id.btn_next_year:
                mYear++;
                in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right_dialog);
                out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left_dialog);
                tvYear.setInAnimation(in);
                tvYear.setOutAnimation(out);
                tvYear.setText(mYear + "");
                break;
            case R.id.btn_month_1:
            case R.id.btn_month_2:
            case R.id.btn_month_3:
            case R.id.btn_month_4:
            case R.id.btn_month_5:
            case R.id.btn_month_6:
            case R.id.btn_month_7:
            case R.id.btn_month_8:
            case R.id.btn_month_9:
            case R.id.btn_month_10:
            case R.id.btn_month_11:
            case R.id.btn_month_12:
                int mMonth = (int) v.getTag();
                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, 0);
                if (mType != 0) {
                    DatePickerDialogListener activity = (DatePickerDialogListener) getActivity();
                    if (activity != null)
                        activity.onFinishEditDialog(cal);
                } else {
                    mDate = cal.getTime();
                    sendResult(Activity.RESULT_OK);
                }
                this.dismiss();
                break;
        }
    }
}