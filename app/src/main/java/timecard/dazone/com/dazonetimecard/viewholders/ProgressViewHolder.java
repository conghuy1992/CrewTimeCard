package timecard.dazone.com.dazonetimecard.viewholders;

import android.view.View;
import android.widget.ProgressBar;

import timecard.dazone.com.dazonetimecard.R;

public class ProgressViewHolder extends ItemViewHolder {
    public ProgressBar progressBar;
    public ProgressViewHolder(View v) {
        super(v);
        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
    }

    @Override
    protected void setup(View v) {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
