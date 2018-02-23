package timecard.dazone.com.dazonetimecard.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;


import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.CompanyInfoActivity;
import timecard.dazone.com.dazonetimecard.activities.OfficeMapViewActivity;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.UserSettingDto;
import timecard.dazone.com.dazonetimecard.interfaces.BaseHTTPCallBack;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetUserInfoResponse;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class SettingFragment extends BaseFragment implements OnGetUserInfoResponse, BaseHTTPCallBack {
    Prefs prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = BaseActivity.Instance.mPrefs;

    }

    private TextView setting_name;
    private TextView setting_position;
    private TextView setting_server_site;
    private TextView setting_company_name;

    Spinner setting_spinner, setting_spinner_status, setting_spinner_status1, setting_spinner_status2, setting_spinner_status3;
    LinearLayout setting_office_lnl, lnl_staff_status, rl_sort_type;

    TextView setting_checkin_time, setting_checkout_time, setting_lunch_time_start, setting_lunch_time_end, setting_add_btn;
    CheckBox cb_lunch_time_in_working;

    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        //setting_name = (TextView) v.findViewById(R.id.setting_name);
        setting_office_lnl = (LinearLayout) rootView.findViewById(R.id.setting_office_lnl);
        lnl_staff_status = (LinearLayout) rootView.findViewById(R.id.lnl_staff_status);
        //setting_position = (TextView) v.findViewById(R.id.setting_position);
        setting_add_btn = (TextView) rootView.findViewById(R.id.setting_add_btn);
        //setting_server_site = (TextView) v.findViewById(R.id.setting_server_site);
        //setting_company_name = (TextView) v.findViewById(R.id.setting_company_name);
        rl_sort_type = (LinearLayout) rootView.findViewById(R.id.rl_sort_type);


        setting_checkin_time = (TextView) rootView.findViewById(R.id.setting_checkin_time);
        setting_checkout_time = (TextView) rootView.findViewById(R.id.setting_checkout_time);
        setting_lunch_time_start = (TextView) rootView.findViewById(R.id.setting_lunch_time_start);
        setting_lunch_time_end = (TextView) rootView.findViewById(R.id.setting_lunch_time_end);

        cb_lunch_time_in_working = (CheckBox) rootView.findViewById(R.id.cb_lunch_time_in_working);

        //setting_server_site.setText(prefs.getServerSite());
        //setting_company_name.setText(UserDBHelper.getUser().NameCompany);
        if (prefs.getIsAdmin() == 1) {
            String[] spinnerArray = {getString(R.string.menu_action_name), getString(R.string.menu_action_soonest),
                    getString(R.string.menu_action_latest), getString(R.string.menu_action_workingtime)};
            setting_spinner = (Spinner) rootView.findViewById(R.id.setting_spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview_single_bl, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setting_spinner.setAdapter(spinnerArrayAdapter);
            setting_spinner.setSelection(prefs.getSortStaffList());

            String[] statusArray = {getString(R.string.menu_action_working), getString(R.string.menu_action_not_working),
                    getString(R.string.menu_action_absent), getString(R.string.menu_action_check_out)};
            setting_spinner_status = (Spinner) rootView.findViewById(R.id.setting_spinner_status);
            ArrayAdapter<String> spinnerStatusAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview_single_bl, statusArray);
            spinnerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setting_spinner_status.setAdapter(spinnerStatusAdapter);
            setting_spinner_status.setSelection(prefs.getssorttype());

            setting_spinner_status1 = (Spinner) rootView.findViewById(R.id.setting_spinner_status1);
            ArrayAdapter<String> spinnerStatusAdapter1 = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview_single_bl, statusArray);
            spinnerStatusAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setting_spinner_status1.setAdapter(spinnerStatusAdapter1);
            setting_spinner_status1.setSelection(prefs.getssorttype1());

            setting_spinner_status2 = (Spinner) rootView.findViewById(R.id.setting_spinner_status2);
            ArrayAdapter<String> spinnerStatusAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview_single_bl, statusArray);
            spinnerStatusAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setting_spinner_status2.setAdapter(spinnerStatusAdapter2);
            setting_spinner_status2.setSelection(prefs.getssorttype2());

            setting_spinner_status3 = (Spinner) rootView.findViewById(R.id.setting_spinner_status3);
            ArrayAdapter<String> spinnerStatusAdapter3 = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview_single_bl, statusArray);
            spinnerStatusAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setting_spinner_status3.setAdapter(spinnerStatusAdapter3);
            setting_spinner_status3.setSelection(prefs.getssorttype3());
            setting_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OfficeMapViewActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Statics.KEY_COMPANY, "");
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            setUpSortType();
        } else {
            setting_add_btn.setVisibility(View.GONE);
            lnl_staff_status.setVisibility(View.GONE);
            rl_sort_type.setVisibility(View.GONE);
        }
        showProgressDialog();
        HttpRequest.getInstance().getSettingUser(this);
        return rootView;
    }

    private void setUpSortType() {
        if (setting_spinner != null) {
            setting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    prefs.putSortStaffList(position);
                    prefs.putReloadEmp(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        if (setting_spinner_status != null && setting_spinner_status1 != null && setting_spinner_status2 != null && setting_spinner_status3 != null) {
            setting_spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == setting_spinner_status1.getSelectedItemPosition()) {
                        setting_spinner_status1.setSelection(prefs.getssorttype());
                        prefs.putssorttype1(setting_spinner_status1.getSelectedItemPosition());
                    }
                    if (position == setting_spinner_status2.getSelectedItemPosition()) {
                        setting_spinner_status2.setSelection(prefs.getssorttype());
                        prefs.putssorttype2(setting_spinner_status2.getSelectedItemPosition());
                    }
                    if (position == setting_spinner_status3.getSelectedItemPosition()) {
                        setting_spinner_status3.setSelection(prefs.getssorttype());
                        prefs.putssorttype3(setting_spinner_status3.getSelectedItemPosition());
                    }
                    prefs.putssorttype(position);
                    prefs.putReloadStatus(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            setting_spinner_status1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == setting_spinner_status.getSelectedItemPosition()) {
                        setting_spinner_status.setSelection(prefs.getssorttype1());
                        prefs.putssorttype(setting_spinner_status.getSelectedItemPosition());
                    }
                    if (position == setting_spinner_status2.getSelectedItemPosition()) {
                        setting_spinner_status2.setSelection(prefs.getssorttype1());
                        prefs.putssorttype2(setting_spinner_status2.getSelectedItemPosition());
                    }
                    if (position == setting_spinner_status3.getSelectedItemPosition()) {
                        setting_spinner_status3.setSelection(prefs.getssorttype1());
                        prefs.putssorttype3(setting_spinner_status3.getSelectedItemPosition());
                    }

                    prefs.putssorttype1(position);
                    prefs.putReloadStatus(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        setting_spinner_status2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == setting_spinner_status.getSelectedItemPosition()) {
                    setting_spinner_status.setSelection(prefs.getssorttype2());
                    prefs.putssorttype(setting_spinner_status.getSelectedItemPosition());
                }

                if (position == setting_spinner_status1.getSelectedItemPosition()) {
                    setting_spinner_status1.setSelection(prefs.getssorttype2());
                    prefs.putssorttype1(setting_spinner_status1.getSelectedItemPosition());
                }

                if (position == setting_spinner_status3.getSelectedItemPosition()) {
                    setting_spinner_status3.setSelection(prefs.getssorttype2());
                    prefs.putssorttype3(setting_spinner_status3.getSelectedItemPosition());
                }

                prefs.putssorttype2(position);
                prefs.putReloadStatus(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setting_spinner_status3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == setting_spinner_status.getSelectedItemPosition()) {
                    setting_spinner_status.setSelection(prefs.getssorttype3());
                    prefs.putssorttype(setting_spinner_status.getSelectedItemPosition());
                }
                if (position == setting_spinner_status1.getSelectedItemPosition()) {
                    setting_spinner_status1.setSelection(prefs.getssorttype3());
                    prefs.putssorttype1(setting_spinner_status1.getSelectedItemPosition());
                }
                if (position == setting_spinner_status2.getSelectedItemPosition()) {
                    setting_spinner_status2.setSelection(prefs.getssorttype3());
                    prefs.putssorttype2(setting_spinner_status2.getSelectedItemPosition());
                }
                prefs.putssorttype3(position);
                prefs.putReloadStatus(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onGetUserInfoResponseSuccess(UserSettingDto user) {
        dismissProgressDialog();
        if (Util.isPhoneLanguageEN()) {
            //setting_name.setText(user.user.NameEN);
            //setting_position.setText(user.user.PositionNameEN);
        } else {
            //setting_name.setText(user.user.Name);
            //setting_position.setText(user.user.PositionName);
        }
        settingOffice(user);

        settingTime(user);
    }

    private void settingTime(UserSettingDto user) {
        setting_checkin_time.setText(insertCharacter(user.CheckInTime));
        setting_checkout_time.setText(insertCharacter(user.CheckOutTime));
        setting_lunch_time_start.setText(insertCharacter(user.StartLunchTime));
        setting_lunch_time_end.setText(insertCharacter(user.EndLunchTime));
        cb_lunch_time_in_working.setChecked(user.CheckLunch == 1);
        cb_lunch_time_in_working.setEnabled(false);
    }

    private String insertCharacter(String string) {
        return string.substring(0, 2) + ":" + string.substring(2);
    }

    int item = -1;

    private void settingOffice(UserSettingDto user) {
        if (getActivity() == null)
            return;
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView setting_companyName, company_address, company_zero_btn, company_delete_btn;
        setting_office_lnl.removeAllViews();
        if (user.offices != null && user.offices.size() != 0) {
            for (final UserSettingDto.CompanyInfo item : user.offices) {
                View view = inflater.inflate(R.layout.company_info_row, null);
                setting_companyName = (TextView) view.findViewById(R.id.setting_companyName);
                company_zero_btn = (TextView) view.findViewById(R.id.company_zero_btn);
                company_delete_btn = (TextView) view.findViewById(R.id.company_delete_btn);
                String strCompanyName = item.nameoffice;
                setting_companyName.setText(strCompanyName);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CompanyInfoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(Statics.KEY_COMPANY, new Gson().toJson(item));
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                if (prefs.getIsAdmin() == 1) {
                    company_zero_btn.setVisibility(View.VISIBLE);
                    company_delete_btn.setVisibility(View.VISIBLE);
                    company_delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = getString(R.string.txt_confirm_delete_office);
                            BaseActivity.Instance.displayAddAlertDialog(getString(R.string.app_name), text, getString(R.string.string_ok), getString(R.string.string_cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HttpRequest.getInstance().deleteSettingOffice(SettingFragment.this, item);
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                        }
                    });
                    company_zero_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), OfficeMapViewActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Statics.KEY_COMPANY, new Gson().toJson(item));
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                } else {
                    company_zero_btn.setVisibility(View.GONE);
                    company_delete_btn.setVisibility(View.GONE);
                }
                company_address = (TextView) view.findViewById(R.id.company_address);
                company_address.setText(item.description);
                setting_office_lnl.addView(view);
            }
        }
    }

    @Override
    public void onGetUserInfoResponseError(ErrorDto errorDto) {
        dismissProgressDialog();
        Util.showMessage(errorDto.message);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getBooleanValue(Statics.PREFS_KEY_RELOAD_SETTING, false)) {
            new Prefs().putBooleanValue(Statics.PREFS_KEY_RELOAD_SETTING, false);
            showProgressDialog();
            HttpRequest.getInstance().getSettingUser(this);
        }
    }

    @Override
    public void onHTTPSuccess() {
        new Prefs().putBooleanValue(Statics.PREFS_KEY_RELOAD_TIMECARD, true);
        HttpRequest.getInstance().checkLogin(null);
        HttpRequest.getInstance().getSettingUser(this);
    }

    @Override
    public void onHTTPFail(ErrorDto errorDto) {
        Util.showMessage(errorDto.message);

    }
}