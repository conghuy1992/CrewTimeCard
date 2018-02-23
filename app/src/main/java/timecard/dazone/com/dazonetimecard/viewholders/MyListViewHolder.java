package timecard.dazone.com.dazonetimecard.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;

public class MyListViewHolder extends ItemViewHolder {

    public MyListViewHolder(View itemView) {
        super(itemView);
    }

    public TextView row_my_list_date;
    public LinearLayout my_list_checklist;

    @Override
    protected void setup(View v) {
        row_my_list_date = (TextView) v.findViewById(R.id.row_my_list_date);
        my_list_checklist = (LinearLayout) v.findViewById(R.id.my_list_checklist);
    }
}