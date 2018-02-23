package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.MenuDto;

public class DrawerListAdapter extends BaseAdapter {
    protected Context mContext;
    protected List<MenuDto> mMenuItems;

    public DrawerListAdapter(Context context, List<MenuDto> navItems) {
        mContext = context;
        mMenuItems = navItems;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mMenuItems.get(position).isHide) {
            view = inflater.inflate(R.layout.row_null, null);
        } else {
            view = inflater.inflate(R.layout.drawer_item, null);
            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText(mMenuItems.get(position).mTitle);
            iconView.setImageResource(mMenuItems.get(position).mIcon);
        }

        return view;
    }
}