package timecard.dazone.com.dazonetimecard.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.CompanyInfoActivity;
import timecard.dazone.com.dazonetimecard.activities.LoginActivity;

/**
 * Created by Dat on 7/29/2016.
 */
public class AutoLoginFragment extends Dialog {
    /**
     * VIEW
     */
    private View rootView;
    private TextView tvCompanyID;
    private TextView tvUserID;
    private TextView btnYes;
    private TextView btnNo;

    private String companyID;
    private String userID;

    public LoginActivity c;

    public AutoLoginFragment(Context context, String companyID, String userID) {
        super(context);
        c = (LoginActivity) context;
        this.companyID = companyID;
        this.userID = userID;
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rootView = inflater.inflate(R.layout.dialog_auto_login, container, false);
        getDialog().setCancelable(false);

        initView();
        return rootView;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_auto_login);
        initView();
       /* btnYes = (TextView) findViewById(R.id.btn_yes);
        btnNo = (TextView) findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);*/

    }

    private void initView() {
        tvCompanyID = (TextView) findViewById(R.id.tv_company_id);
        tvUserID = (TextView) findViewById(R.id.tv_user_id);

        tvCompanyID.setText(companyID);
        tvUserID.setText(userID);

        btnYes = (TextView) findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ((LoginActivity) c).autoLogin(companyID, userID);
            }
        });

        btnNo = (TextView) findViewById(R.id.btn_no);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
