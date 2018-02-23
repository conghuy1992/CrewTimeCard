package timecard.dazone.com.dazonetimecard.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import timecard.dazone.com.dazonetimecard.R;

public class AllEmpViewHolder extends ItemViewHolder {
    public AllEmpViewHolder(View itemView) {
        super(itemView);
    }
    public TextView row_ae_name,row_ae_position,row_staff_status,row_ae_time;
    public LinearLayout my_list_checklist,employer_info,lnl_time;
    public ImageView row_ae_avatar;

    @Override
    protected void setup(View v) {
        row_ae_name = (TextView)v.findViewById(R.id.row_ae_name);
        row_ae_position = (TextView)v.findViewById(R.id.row_ae_position);
        row_staff_status = (TextView)v.findViewById(R.id.row_staff_status);
        row_ae_time = (TextView)v.findViewById(R.id.row_ae_time);
        row_ae_avatar = (ImageView)v.findViewById(R.id.row_ae_avatar);
        my_list_checklist = (LinearLayout)v.findViewById(R.id.my_list_checklist);
        lnl_time = (LinearLayout)v.findViewById(R.id.lnl_time);
        employer_info = (LinearLayout)v.findViewById(R.id.employer_info);
    }
}
