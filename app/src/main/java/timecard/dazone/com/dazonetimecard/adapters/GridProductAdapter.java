package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.ProductDto;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class GridProductAdapter extends GridBaseAdapter<ProductDto> {

    public GridProductAdapter(Context mContext, ArrayList<ProductDto> mNavItems) {
        super(mContext, mNavItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_grid_item, null);
        }
        else {
            view = convertView;
        }
        grid_imv = (ImageView)view.findViewById(R.id.grid_imv);
        grid_imv.setImageResource(mNavItems.get(position).mIcon);
        grid_tv = (TextView)view.findViewById(R.id.grid_tv);
        grid_tv.setText(mNavItems.get(position).mTitle);
        badge_rl = (RelativeLayout)view.findViewById(R.id.badge_rl);
        badge_count = (TextView)view.findViewById(R.id.badge_count);
        if(mNavItems.get(position).gcmDto!=null && mNavItems.get(position).gcmDto.count!=0)
        {
            badge_rl.setVisibility(View.VISIBLE);
            Util.printLogs("mNavItems.get(position).gcmDto.count : "+mNavItems.get(position).gcmDto.count);
            badge_count.setText(""+mNavItems.get(position).gcmDto.count);
        }
        else
        {
            badge_rl.setVisibility(View.GONE);
        }
        return view;
    }
}
