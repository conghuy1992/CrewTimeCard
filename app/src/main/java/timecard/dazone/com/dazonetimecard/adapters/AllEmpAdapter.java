package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.UserListActivity;
import timecard.dazone.com.dazonetimecard.dtos.AllEmployeeDto;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;
import timecard.dazone.com.dazonetimecard.viewholders.AllEmpViewHolder;
import timecard.dazone.com.dazonetimecard.viewholders.ProgressViewHolder;

public class AllEmpAdapter extends ListLoadMoreAdapter<AllEmployeeDto> {
    protected long milis = 0;

    public AllEmpAdapter(List<AllEmployeeDto> myDataSet, RecyclerView recyclerView) {
        super(myDataSet, recyclerView);
    }

    public AllEmpAdapter(List<AllEmployeeDto> myDataSet, RecyclerView recyclerView, long milis) {
        super(myDataSet, recyclerView);
        this.milis = milis;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_employee, parent, false);
            vh = new AllEmpViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AllEmpViewHolder) {
            final AllEmployeeDto item = mDataset.get(position);
            AllEmpViewHolder viewHolder = (AllEmpViewHolder) holder;
            viewHolder.row_ae_name.setText(item.name);
            viewHolder.row_ae_position.setText(item.position);

            if (item.longthoigianlamviec != 0) {
                viewHolder.lnl_time.setVisibility(View.VISIBLE);
                viewHolder.row_ae_time.setText(Util.setDateInfoListNoTimeZone(item.longthoigianlamviec, Statics.FORMAT_HOUR_MINUTE_ALL_EMP) + " " + BaseActivity.Instance.getString(R.string.string_min) + " ".toLowerCase());
            } else {
                viewHolder.lnl_time.setVisibility(View.GONE);
            }

            final int size = (Util.getDimenInPx(R.dimen.avatar_user_dimen));
            Util.drawCycleImage(viewHolder.row_ae_avatar, R.drawable.avatar, size);
            BaseActivity.Instance.showCycleImageFromLink(root_link + item.avatar, viewHolder.row_ae_avatar, R.dimen.avatar_user_dimen);
            addListViewItem(item.listcheck, viewHolder.my_list_checklist);
            viewHolder.employer_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(BaseActivity.Instance, UserListActivity.class);
                    newIntent.putExtra(Statics.KEY_USER_NO, item.userno);
                    newIntent.putExtra(Statics.KEY_USER_NAME, item.name);
                    newIntent.putExtra(Statics.KEY_TIME_REQUEST, milis);
                    BaseActivity.Instance.startActivity(newIntent);
                    BaseActivity.Instance.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
}