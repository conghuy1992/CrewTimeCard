package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.utils.Util;

public abstract class BaseAccountActivity extends BaseActivity {
    RelativeLayout main_content;
    TextView coppy_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_base);
        enableHomeAction();
        main_content = (RelativeLayout) findViewById(R.id.forgot_pass_content);
        coppy_right = (TextView) findViewById(R.id.coppy_right);
        addFragment(savedInstanceState);
    }

    protected abstract void addFragment(Bundle bundle);
    protected void replaceFragment(Fragment fragment) {
        Util.replaceFragment(getSupportFragmentManager(), fragment, R.id.forgot_pass_content, true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
