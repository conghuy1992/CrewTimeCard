package timecard.dazone.com.dazonetimecard.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.utils.Statics;

/**
 * Created by Dat on 7/26/2016.
 */
public class ImageViewActivity extends AppCompatActivity {
    /**
     * VIEW
     */
    private ImageView ivAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        initView();
        receiveData();
    }

    private void initView() {
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
    }

    private void receiveData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String url = bundle.getString(Statics.KEY_URL, "");
            if (!TextUtils.isEmpty(url) && ivAvatar != null) {
                ImageLoader.getInstance().displayImage(url, ivAvatar, Statics.optionsProfileAvatar);
            }
        }
    }
}
