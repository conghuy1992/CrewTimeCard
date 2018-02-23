package timecard.dazone.com.dazonetimecard.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;

public class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (null == mProgressDialog || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(BaseActivity.Instance);
            mProgressDialog.setTitle(getString(R.string.loading_title));
            mProgressDialog.setMessage(getString(R.string.loading_content));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }
    public void dismissProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
