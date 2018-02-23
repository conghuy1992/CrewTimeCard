package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import timecard.dazone.com.dazonetimecard.adapters.AllEmpAdapter;
import timecard.dazone.com.dazonetimecard.adapters.MyListAdapter;
import timecard.dazone.com.dazonetimecard.dtos.AllEmployeeDto;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetAllEmpHTTPCallBack;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class AllEmployeeFragment extends ListFragment<AllEmployeeDto> implements OnGetAllEmpHTTPCallBack {
    int sortType,GroupNo=0;
    protected boolean isLoading = false;
    protected boolean isLoadMore = true;
    String TAG = "AllEmployeeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        millis = getArguments().getLong(KEY_POSITION);
        sortType = getArguments().getInt(Statics.KEY_SORT_TYPE, 0);
        GroupNo = getArguments().getInt(Statics.KEY_GROUP_NO, 0);
    }

    public static AllEmployeeFragment newInstance(long position) {
        AllEmployeeFragment fragmentFirst = new AllEmployeeFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_POSITION, position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public static AllEmployeeFragment newInstance(long position, int sortType,int GroupNo) {
        AllEmployeeFragment fragmentFirst = new AllEmployeeFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_POSITION, position);
        args.putInt(Statics.KEY_SORT_TYPE, sortType);
        args.putInt(Statics.KEY_GROUP_NO, GroupNo);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    protected void initRecycleView() {
        rvJobsList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvJobsList.setLayoutManager(layoutManager);
        adapterList = new AllEmpAdapter(current_Task, rvJobsList, millis);
        rvJobsList.setAdapter(adapterList);
        adapterList.setOnLoadMoreListener(new MyListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                reloadContentPage();
            }
        });
    }

    @Override
    protected void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isVisible() && !isLoading) {
                    isLoadMore = false;
                    start = 0;
                    userNo = "0";
                    current_Task.clear();
                    adapterList.updateRecyclerView(current_Task);
                    adapterList.setLoaded();
                    //onRefresh();
                    reloadContentPage();
                }
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onRefresh() {
        mHttpRequest.getAllEmployeesSort(this, millis, limit, userNo, sortType,GroupNo);
    }

    @Override
    protected void reloadContentPage() {
        if (!isLoading) {
            isLoading = true;
//            swipeRefreshLayout.setRefreshing(true);
            Log.d(TAG,"reloadContentPage sortType:"+sortType+" GroupNo:"+GroupNo);
            mHttpRequest.getAllEmployeesSort(this, millis, limit, userNo, sortType,GroupNo);
        }
    }

    @Override
    public void onGetAllEmpSuccess(List<AllEmployeeDto> myListDtos) {

//        for (AllEmployeeDto obj : myListDtos) {
//            Log.d(TAG, new Gson().toJson(obj));
//        }

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

            if (current_Task.size() > 0 && current_Task.get(current_Task.size() - 1) == null) {
                current_Task.remove(current_Task.size() - 1);
                adapterList.notifyItemRemoved(current_Task.size());

            }

            current_Task.addAll(myListDtos);
            start = start + limit;

            if (current_Task != null && current_Task.size() > 0) {
                userNo = (current_Task.get(current_Task.size() - 1)).userno;
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
}
