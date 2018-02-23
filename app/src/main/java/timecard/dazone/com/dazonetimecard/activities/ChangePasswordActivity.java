package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.interfaces.OnChangePasswordCallBack;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;
import timecard.dazone.com.dazonetimecard.utils.Util;

/**
 * Created by LongTran on 3/24/2017.
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    /**
     * VIEW
     */
    private TextView btnChangePassword;
    private EditText edOldPassword;
    private EditText edNewPassword;
    private EditText edConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.nav_back_ic);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initView() {
        btnChangePassword = (TextView) findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(this);

        edOldPassword = (EditText) findViewById(R.id.ed_old_password);
        edNewPassword = (EditText) findViewById(R.id.ed_new_password);
        edConfirmPassword = (EditText) findViewById(R.id.ed_confirm_password);
    }

    private boolean checkConfirmPassword(String originalPassword, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(originalPassword)) {
            Util.showShortMessage("Original Password is empty");
            return false;
        } else if (TextUtils.isEmpty(newPassword)) {
            Util.showShortMessage("New Password is empty");
            return false;
        } else if (!newPassword.equals(confirmPassword)) {
            Util.showShortMessage("Confirm Password is not matched");
            return false;
        }
        return true;
    }

    /**
     * CALL API - CHANGE PASSWORD
     */
    private void callApi(String originalPassword,final String newPassword) {
        HttpRequest.getInstance().ChangePassword(originalPassword, newPassword, new OnChangePasswordCallBack() {
            @Override
            public void onSuccess(String response) {
                DaZoneApplication.getInstance().getmPrefs().putPassword(newPassword);
                Util.showShortMessage("Change password success");
                finish();
                System.out.println("onSuccess " + response);
            }

            @Override
            public void onFail(ErrorDto errorDto) {
                Util.showShortMessage(errorDto.message);
//                System.out.println("onFail " + errorDto.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                String originalPassword = edOldPassword.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();
                boolean isChecked = checkConfirmPassword(originalPassword, newPassword, confirmPassword);
                if (isChecked) {
                    callApi(originalPassword, newPassword);
                }
                break;
        }
    }
}
