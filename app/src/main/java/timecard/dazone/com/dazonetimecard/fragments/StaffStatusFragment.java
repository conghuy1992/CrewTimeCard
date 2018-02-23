package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.MyListAdapter;
import timecard.dazone.com.dazonetimecard.adapters.StaffStatusAdapter;
import timecard.dazone.com.dazonetimecard.dtos.AllEmployeeDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.TimeCardDto;
import timecard.dazone.com.dazonetimecard.utils.DaZoneApplication;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class StaffStatusFragment extends AllEmployeeFragment {
    private String TAG="StaffStatusFragment";
    int sortType;
    int groupNo;
    Prefs prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = DaZoneApplication.getInstance().getmPrefs();
        millis = getArguments().getLong(KEY_POSITION);
        sortType = getArguments().getInt(Statics.KEY_SORT_TYPE, 0);
        groupNo = getArguments().getInt(Statics.KEY_GROUP_NO, 0);
        title = getResources().getStringArray(R.array.list_staff_status);
    }

    public static StaffStatusFragment newInstance(long position, int sortType,int groupNo) {
        StaffStatusFragment fragmentFirst = new StaffStatusFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_POSITION, position);
        args.putInt(Statics.KEY_SORT_TYPE, sortType);
        args.putInt(Statics.KEY_GROUP_NO, groupNo);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    protected void initRecycleView() {
        rvJobsList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvJobsList.setLayoutManager(layoutManager);
        adapterList = new StaffStatusAdapter(current_Task, rvJobsList);
        rvJobsList.setAdapter(adapterList);
        adapterList.setOnLoadMoreListener(new MyListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    isLoadMore = false;
                    reloadContentPage();
                }
                //loadMore();
            }
        });
    }

    String[] title;

    @Override
    public void onGetAllEmpSuccess(List<AllEmployeeDto> myListDtos) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (isVisible()) {
            if (myListDtos.size() >= limit) {
                isLoadMore = true;
            }
            else
            {
                isLoadMore = false;
            }
            if (current_Task.size() > 0 && current_Task.get(current_Task.size() - 1) == null) {
                current_Task.remove(current_Task.size() - 1);
                adapterList.notifyItemRemoved(current_Task.size());
            }
            current_Task.addAll(myListDtos);
            start = start + limit;
            if (current_Task != null && current_Task.size() > 0) {
                userNo = (current_Task.get(current_Task.size() - 1)).userno;
            }
            if (!isExistType()) {
                no_item_found.setVisibility(View.VISIBLE);
            } else {
                no_item_found.setVisibility(View.GONE);
            }
            adapterList.updateRecyclerView(current_Task);
            if (start <= current_Task.size()) {
                adapterList.setLoaded();
            }
        }
        isLoading = false;
    }

    @Override
    public void onGetAllEmpFail(ErrorDto errorDto) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        isLoadMore = false;
        isLoading = false;
        Util.showMessage(errorDto.message);
    }

    @Override
    protected void reloadContentPage() {

        if (!isLoading) {
            isLoading = true;
//            swipeRefreshLayout.setRefreshing(true);
            /*if (current_Task.size() > 0) {
                if (current_Task.get(current_Task.size() - 1) != null) {
                    addProgressBar();
                }
            } else {
                addProgressBar();
            }*/

            Log.d(TAG,"GroupNo:"+GroupNo);
            if (sortType == 0) {
                mHttpRequest.getAllEmployeesStatusNew(this, millis, limit, userNo, prefs.getssorttype() + 1,
                        prefs.getssorttype1() + 1, prefs.getssorttype2() + 1, prefs.getssorttype3() + 1,GroupNo);
            } else {
                mHttpRequest.getAllEmployeesStatusNew(this, millis, limit, userNo, sortType, -1, -1, -1,GroupNo);
            }
        }
    }

    /**
     * ADD PROGRESS BAR
     */
    private void addProgressBar() {
        current_Task.add(null);
        adapterList.notifyItemInserted(current_Task.size() - 1);
    }

    private boolean isExistType() {
        boolean isExist = false;
        if (current_Task != null && current_Task.size() > 0) {
            isExist = true;
        }

        Util.printLogs("Current item size = " + current_Task.size());

        for (AllEmployeeDto dto : current_Task) {
            if (dto != null) {
                if (dto.listcheck != null && dto.listcheck.size() != 0) {
                    if (sortType == getStatus(dto.listcheck)) {
                        isExist = true;
                    }
                } else {
                    if (sortType == 0) {
                        isExist = true;
                    }
                }
            }
        }
        return isExist;
    }

    protected int getStatus(List<TimeCardDto> listcheck) {
        int type = -1;
        TimeCardDto timeCardDto = listcheck.get(listcheck.size() - 1);
        switch (timeCardDto.TypeCheckNumber) {
            case 1:
            case 4:
                return 1;
            case 2:
                return 2;
            case 0:
                return 3;
            case 3:
                return 4;
        }
        return type;
    }

    @Override
    protected void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isVisible() && !isLoading) {
                    isLoadMore = false;

                    reloadContentPage();
                }
            }
        });
    }
}
