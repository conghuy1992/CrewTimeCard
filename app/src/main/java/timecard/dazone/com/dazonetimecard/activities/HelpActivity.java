package timecard.dazone.com.dazonetimecard.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import timecard.dazone.com.dazonetimecard.R;

public class HelpActivity extends AppCompatActivity {
    LinearLayout back_lnl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        back_lnl = (LinearLayout) findViewById(R.id.back_lnl);
        back_lnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}