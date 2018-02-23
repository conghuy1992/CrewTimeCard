package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class GridBaseAdapter<T> extends BaseAdapter {
    protected ImageView grid_imv;
    protected TextView grid_tv, badge_count;
    protected RelativeLayout badge_rl;

    protected List<T> mNavItems;
    protected Context mContext;

    public GridBaseAdapter(Context mContext, List<T> mNavItems) {
        this.mContext = mContext;
        this.mNavItems = mNavItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}