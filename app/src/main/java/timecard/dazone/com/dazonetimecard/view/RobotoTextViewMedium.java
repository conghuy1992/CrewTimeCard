package timecard.dazone.com.dazonetimecard.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;

/**
 * Created by trannguyenbaolong on 1/19/2016.
 */
public class RobotoTextViewMedium extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RobotoTextViewMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public RobotoTextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public RobotoTextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public RobotoTextViewMedium(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);

            try {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
                setTypeface(myTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }

            a.recycle();
        }
    }
}