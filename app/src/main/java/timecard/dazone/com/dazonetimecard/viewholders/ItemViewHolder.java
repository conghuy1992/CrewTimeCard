package timecard.dazone.com.dazonetimecard.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {
    public ItemViewHolder(View v) {
        super(v);
        setup(v);
    }

    protected abstract void setup(View v);
}