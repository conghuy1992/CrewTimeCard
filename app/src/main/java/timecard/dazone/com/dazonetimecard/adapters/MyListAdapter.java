package timecard.dazone.com.dazonetimecard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.MyListDto;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;
import timecard.dazone.com.dazonetimecard.viewholders.MyListViewHolder;
import timecard.dazone.com.dazonetimecard.viewholders.ProgressViewHolder;

public class MyListAdapter extends ListLoadMoreAdapter<MyListDto> {

    public MyListAdapter(List myDataSet, RecyclerView recyclerView) {
        super(myDataSet, recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_list, parent, false);
            vh = new MyListViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyListViewHolder) {
            final MyListDto item = mDataset.get(position);
            MyListViewHolder viewHolder = (MyListViewHolder) holder;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.longMilliseconds);
            int date = calendar.get(Calendar.DAY_OF_WEEK);

            switch (date) {
                case 1:
                    viewHolder.row_my_list_date.setTextColor(Util.getResources().getColor(R.color.red));
                    break;
                case 7:
                    viewHolder.row_my_list_date.setTextColor(Util.getResources().getColor(R.color.orange));
                    break;
                default:
                    viewHolder.row_my_list_date.setTextColor(Util.getResources().getColor(R.color.app_base_color));
                    break;
            }

            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English")) {
                viewHolder.row_my_list_date.setText(Util.setDateInfo(item.longMilliseconds, Statics.FORMAT_DATE_MY_LIST));
            } else {
                viewHolder.row_my_list_date.setText(Util.setDateInfo(item.longMilliseconds, Statics.FORMAT_DATE_MY_LIST_KR));
            }

            addListViewItem(item.listcheck, viewHolder.my_list_checklist);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
}