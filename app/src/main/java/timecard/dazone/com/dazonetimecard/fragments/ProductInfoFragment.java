package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.BuildConfig;
import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.utils.Prefs;

public class ProductInfoFragment extends BaseFragment {
    Prefs prefs;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = BaseActivity.Instance.mPrefs;

    }
    TextView product_version,product_user_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_info, container, false);
        product_version = (TextView)v.findViewById(R.id.product_version);
        product_user_id = (TextView)v.findViewById(R.id.product_user_id);
        product_version.setText(BuildConfig.VERSION_NAME);
        product_user_id.setText(prefs.getUserName());
        return v;
    }
}
