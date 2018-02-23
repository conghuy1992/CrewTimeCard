package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.MenuDto;

/**
 * Created by dazone on 3/13/2017.
 */

public class SpinnerDisableAdapter  extends ArrayAdapter<MenuDto> {

    public SpinnerDisableAdapter(Context context, int resource, List<MenuDto> objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isHide;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_disable_permission, parent, false);
        View mView = v.findViewById(R.id.textview_spinner);
        TextView mTextView = (TextView) mView;

        if (!isEnabled(position)) {
            mTextView.setTextColor(Color.GRAY);
        } else {
            mTextView.setTextColor(Color.BLACK);
        }

        mTextView.setText(getItem(position).mTitle);

        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = super.getView(position, convertView, parent);

        TextView mTextView = (TextView) mView;
        /*mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTextView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.absent_color));
        mTextView.setdra
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.WHITE);*/
        mTextView.setText(getItem(position).mTitle);

        return mView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
