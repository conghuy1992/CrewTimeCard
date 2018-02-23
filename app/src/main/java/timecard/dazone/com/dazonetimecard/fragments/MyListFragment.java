package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import timecard.dazone.com.dazonetimecard.adapters.MyListAdapter;
import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.MyListDto;
import timecard.dazone.com.dazonetimecard.interfaces.OnGetMyListHTTPCallBack;
import timecard.dazone.com.dazonetimecard.utils.Prefs;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class MyListFragment extends ListFragment implements OnGetMyListHTTPCallBack {
    String TAG = "MyListFragment";
    RecyclerView.LayoutManager layoutManager;

    public static MyListFragment newInstance(long position) {
        MyListFragment fragmentFirst = new MyListFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_POSITION, position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    protected void initRecycleView() {
        rvJobsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvJobsList.setLayoutManager(layoutManager);
        adapterList = new MyListAdapter(current_Task, rvJobsList);
        rvJobsList.setAdapter(adapterList);
    }

    @Override
    protected void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                current_Task.clear();
                adapterList.updateRecyclerView(current_Task);
                reloadContentPage();
            }
        });
    }

    @Override
    protected void reloadContentPage() {
        //current_Task.add(null);
        //adapterList.notifyItemInserted(current_Task.size() - 1);
        mHttpRequest.getMyList(this, millis);
    }

    @Override
    protected void onRefresh() {

    }

    @Override
    public void onGetMyListSuccess(List<MyListDto> myListDtos) {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        //current_Task.remove(current_Task.size() - 1);
        //adapterList.notifyItemRemoved(current_Task.size());
//        Log.d(TAG,"start-------------------");
//        for (MyListDto obj : myListDtos) {
//            Log.d(TAG, new Gson().toJson(obj));
//        }
//        Log.d(TAG,"end-------------------");

        current_Task.addAll(myListDtos);
        adapterList.updateRecyclerView(current_Task);

        if (layoutManager != null) {
            //long temp;
            Calendar cal = Calendar.getInstance();
            /*if (new Prefs().getServerTime() != 0) {
                temp = new Prefs().getServerTime() + Util.getTimeOffsetInMilis();
                cal.setTimeInMillis(temp);
            } else {
                temp = cal.getTimeInMillis() + Util.getTimeOffsetInMilis();
            }*/
            //cal.setTimeInMillis(temp);
            try {
                layoutManager.scrollToPosition(cal.get(Calendar.DATE) - 1);
            } catch (Exception e) {
                layoutManager.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onGetMyListFail(ErrorDto errorDto) {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        //current_Task.remove(current_Task.size() - 1);
        //adapterList.updateRecyclerView(current_Task);
        //Util.showMessage(errorDto.message);
    }
}
