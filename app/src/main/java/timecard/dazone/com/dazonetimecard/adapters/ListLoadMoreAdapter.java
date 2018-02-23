package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.BaseActivity;
import timecard.dazone.com.dazonetimecard.activities.MapActivity;
import timecard.dazone.com.dazonetimecard.dtos.TimeCardDto;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.TimeUtils;
import timecard.dazone.com.dazonetimecard.utils.Util;

public abstract class ListLoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final int VIEW_ITEM = 1;
    protected final int VIEW_PROG = 0;
    protected List<T> mDataset;
    protected static String root_link;

    protected int visibleThreshold = 2;
    protected int lastVisibleItem, totalItemCount;
    protected boolean loading;
    protected OnLoadMoreListener onLoadMoreListener;

    public ListLoadMoreAdapter(List<T> myDataSet, RecyclerView recyclerView) {
        mDataset = myDataSet;

        root_link = DaZoneApplication.getInstance().getmPrefs().getServerSite();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    public void setLoaded() {
        loading = false;
    }

    public void setUnLoad() {
        loading = true;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void updateRecyclerView(List<T> jobDetailDtos) {
        mDataset = jobDetailDtos;
        notifyDataSetChanged();
    }

    protected void addListViewItem(List<TimeCardDto> listCheck, ViewGroup v) {
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v.removeAllViews();
        v.setVisibility(View.VISIBLE);
        View view;
        for (final TimeCardDto timeCardDto : listCheck) {
            view = inflater.inflate(R.layout.time_card_row, null);

            TextView tc_row_type_check = (TextView) view.findViewById(R.id.tc_row_type_check);
            TextView tc_row_date = (TextView) view.findViewById(R.id.tc_row_date);
            TextView tc_row_check_type = (TextView) view.findViewById(R.id.tc_row_check_type);
            TextView tc_row_distance_ip = (TextView) view.findViewById(R.id.tc_row_distance_ip);
            View tc_row_distance_ip2_empty = view.findViewById(R.id.tc_row_distance_ip2_empty);
            TextView tc_row_distance_ip2 = (TextView) view.findViewById(R.id.tc_row_distance_ip2);
            TextView tc_row_offset = (TextView) view.findViewById(R.id.tc_row_offset);
            TextView tc_row_note = (TextView) view.findViewById(R.id.tc_row_note);
            ImageView tc_row_check_type_imv = (ImageView) view.findViewById(R.id.tc_row_check_type_imv);
            RelativeLayout navigate_rtl = (RelativeLayout) view.findViewById(R.id.navigate_rtl);

            if (timeCardDto.TimeOffset != Util.getTimeOffsetInMilis()) {
                tc_row_offset.setVisibility(View.VISIBLE);

                if (timeCardDto.TimeOffset >= 0) {
                    String datePattern = "+hh:mm";
                    tc_row_offset.setText("( " + TimeUtils.getFormattedTimeWithoutTimeZone(timeCardDto.TimeOffset, datePattern) + " )");
                } else {
                    String datePattern = "-hh:mm";
                    tc_row_offset.setText("( " + TimeUtils.getFormattedTimeWithoutTimeZone(-timeCardDto.TimeOffset, datePattern) + " )");
                }
            }

            tc_row_type_check.setText(timeCardDto.TypeCheck);

            boolean isKorean = Locale.getDefault().getDisplayLanguage().equals(Locale.KOREA.getDisplayLanguage());

            if (timeCardDto.TimeOffset == Util.getTimeOffsetInMilis()) {
                if (isKorean) {
                    tc_row_date.setText(Util.setDateInfoList(timeCardDto.longMilliseconds, Statics.FORMAT_HOUR_MINUTE_SECOND_AMPM_KOREAN).toLowerCase());
                } else {
                    tc_row_date.setText(Util.setDateInfoList(timeCardDto.longMilliseconds, Statics.FORMAT_HOUR_MINUTE_SECOND_AMPM).toLowerCase());
                }
            } else {
                if (isKorean) {
                    tc_row_date.setText(Util.setDateInfoListNoTimeZone(timeCardDto.longMilliseconds + timeCardDto.TimeOffset, Statics.FORMAT_HOUR_MINUTE_SECOND_AMPM_KOREAN).toLowerCase());
                } else {
                    tc_row_date.setText(Util.setDateInfoListNoTimeZone(timeCardDto.longMilliseconds + timeCardDto.TimeOffset, Statics.FORMAT_HOUR_MINUTE_SECOND_AMPM).toLowerCase());
                }
            }

            if (!TextUtils.isEmpty(timeCardDto.Remart)) {
                tc_row_note.setVisibility(View.VISIBLE);
                tc_row_note.setText(timeCardDto.Remart);
            } else {
                tc_row_note.setVisibility(View.GONE);
                tc_row_note.setText("");
            }

            if (TextUtils.isEmpty(timeCardDto.NameCompany)) {
                tc_row_distance_ip.setText(timeCardDto.DistanceOrIp);
                tc_row_distance_ip2_empty.setVisibility(View.GONE);
                tc_row_distance_ip2.setVisibility(View.GONE);
            } else {
                tc_row_distance_ip.setText(timeCardDto.NameCompany);
                tc_row_distance_ip2.setText(timeCardDto.DistanceOrIp);
                tc_row_distance_ip2_empty.setVisibility(View.VISIBLE);
                tc_row_distance_ip2.setVisibility(View.VISIBLE);

                if (tc_row_note.getVisibility() == View.GONE) {
                    tc_row_note.setVisibility(View.VISIBLE);
                }
            }

            if (Integer.parseInt(timeCardDto.Type) == 2) {
                if (!TextUtils.isEmpty(timeCardDto.BeaconInfo)) {
                    tc_row_check_type_imv.setImageResource(R.drawable.icon);

                    try {
                        String strCompanyName = timeCardDto.BeaconInfo.split(",")[1];
                        tc_row_distance_ip.setText(strCompanyName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                tc_row_check_type_imv.setVisibility(View.VISIBLE);

                navigate_rtl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToGoogleMap(timeCardDto);
                    }
                });

                tc_row_distance_ip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToGoogleMap(timeCardDto);
                    }
                });
            } else {
                if (Integer.parseInt(timeCardDto.Type) == 3) {
                    tc_row_check_type.setVisibility(View.VISIBLE);
                    tc_row_check_type.setText(R.string.time_car_row_separate);

                    tc_row_distance_ip2.setVisibility(View.GONE);
                }

                tc_row_check_type.setVisibility(View.VISIBLE);
            }

            v.addView(view);
        }
    }

    private void navigateToGoogleMap(TimeCardDto timeCardDto) {
        Intent newIntent = new Intent(DaZoneApplication.getInstance().getBaseContext(), MapActivity.class);
        newIntent.putExtra(Statics.KEY_LATITUDE, timeCardDto.Latitude);
        newIntent.putExtra(Statics.KEY_LONGITUDE, timeCardDto.Longitude);
        newIntent.putExtra(Statics.KEY_LATITUDE_COMPANY, timeCardDto.LatCompany);
        newIntent.putExtra(Statics.KEY_LONGITUDE_COMPANY, timeCardDto.LngCompany);
        newIntent.putExtra(Statics.KEY_DISTANCE, timeCardDto.DistanceOrIp);
        BaseActivity.Instance.startActivity(newIntent);
    }
}