package timecard.dazone.com.dazonetimecard.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.SignUpActivity;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;

public class IntroFragment extends Fragment {
    protected static final String ARG_PARAM1 = "param1";

    protected int mParam1;

    public static IntroFragment newInstance(int param1) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        switch (mParam1)
        {
            case 0:
                 v = inflater.inflate(R.layout.fragment_intro, null);
                break;
            case 1:
                 v = inflater.inflate(R.layout.fragment_intro2, null);
                break;
            case 2:
                v = inflater.inflate(R.layout.fragment_intro3,null);
                break;
            case 3:
                v = inflater.inflate(R.layout.fragment_intro4, null);
                break;
            case 4:
                v = inflater.inflate(R.layout.fragment_intro5, null);
                RelativeLayout company_link = (RelativeLayout)v.findViewById(R.id.login_btn_sign_up);
                company_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DaZoneApplication.getInstance(),SignUpActivity.class);
                        startActivity(i);
                    }
                });
                /*final String url = getString(R.string.intro5_text3);
                SpannableString ss = new SpannableString(url);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        *//*String urlTemp;
                        if(url.startsWith("http://")) {
                            urlTemp = url;
                        }
                        else
                        {
                            urlTemp = "http://" + url;
                        }*//*
                        Intent i = new Intent(DaZoneApplication.getInstance(),SignUpActivity.class);
                        startActivity(i);

                    }
                };
                ss.setSpan(clickableSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                company_link.setText(ss);
                company_link.setMovementMethod(LinkMovementMethod.getInstance());*/
                break;
            default:
                v = inflater.inflate(R.layout.fragment_intro, null);
                break;
        }
        return v;
    }

}
