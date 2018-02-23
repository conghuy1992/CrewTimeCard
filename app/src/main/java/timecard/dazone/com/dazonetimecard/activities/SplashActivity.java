package timecard.dazone.com.dazonetimecard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import timecard.dazone.com.dazonetimecard.R;

/**
 * Created by Dat on 7/19/2016.
 */
public class SplashActivity extends AppCompatActivity {

    /*protected Handler mHandler = new Handler();
    private static long FADE_OUT_DURATION = 150;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // else if not login then show LoginActivity
                Intent intent = new Intent(SplashActivity.this.getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, FADE_OUT_DURATION);
    }*/
}
