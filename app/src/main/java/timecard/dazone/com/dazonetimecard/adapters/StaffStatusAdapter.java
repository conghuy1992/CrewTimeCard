package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.UserListActivity;
import timecard.dazone.com.dazonetimecard.dtos.AllEmployeeDto;
import timecard.dazone.com.dazonetimecard.dtos.TimeCardDto;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;
import timecard.dazone.com.dazonetimecard.viewholders.AllEmpViewHolder;
import timecard.dazone.com.dazonetimecard.viewholders.ProgressViewHolder;

public class StaffStatusAdapter extends AllEmpAdapter {

    public StaffStatusAdapter(List myDataSet, RecyclerView recyclerView) {
        super(myDataSet, recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AllEmpViewHolder) {
            final AllEmployeeDto item = mDataset.get(position);
            AllEmpViewHolder viewHolder = (AllEmpViewHolder) holder;
            viewHolder.row_ae_name.setTextColor(Color.BLACK);
            viewHolder.row_ae_name.setText(item.name);
            viewHolder.row_ae_position.setText(item.position);
            final int size = (Util.getDimenInPx(R.dimen.avatar_user_dimen));
            Util.drawCycleImage(viewHolder.row_ae_avatar, R.drawable.avatar, size);
            BaseActivity.Instance.showCycleImageFromLink(root_link + item.avatar, viewHolder.row_ae_avatar, R.dimen.avatar_user_dimen_custom);
//            addListViewItem(item.listcheck, viewHolder.my_list_checklist);
            viewHolder.row_staff_status.setVisibility(View.VISIBLE);
            setStatusText(item.listcheck, viewHolder.row_staff_status);
            viewHolder.employer_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(BaseActivity.Instance, UserListActivity.class);
                    newIntent.putExtra(Statics.KEY_USER_NO, item.userno);
                    newIntent.putExtra(Statics.KEY_USER_NAME, item.name);
                    newIntent.putExtra(Statics.KEY_TIME_REQUEST, Calendar.getInstance().getTimeInMillis());
                    BaseActivity.Instance.startActivity(newIntent);
                    BaseActivity.Instance.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    protected void setStatusText(List<TimeCardDto> listOfCheck, TextView textView) {
        if (listOfCheck != null && listOfCheck.size() != 0) {
            TimeCardDto timeCardDto = listOfCheck.get(listOfCheck.size() - 1);
            switch (timeCardDto.TypeCheckNumber) {
                case 1:
                case 4:
                    textView.setText(R.string.menu_action_working);
                    textView.setTextColor(DaZoneApplication.getInstance().getResources().getColor(R.color.working_color));
                    break;

                case 2:
                    textView.setText(R.string.menu_action_not_working);
                    textView.setTextColor(DaZoneApplication.getInstance().getResources().getColor(R.color.work_outside_color));
                    break;

                case 0:
                    textView.setText(R.string.menu_action_absent);
                    textView.setTextColor(DaZoneApplication.getInstance().getResources().getColor(R.color.absent_color));
                    break;

                case 3:
                    textView.setText(R.string.menu_action_check_out);
                    textView.setTextColor(Color.BLACK);
                    break;
            }
        } else {
            textView.setTextColor(DaZoneApplication.getInstance().getResources().getColor(R.color.red));
            textView.setText(R.string.menu_action_absent);
        }
    }
}