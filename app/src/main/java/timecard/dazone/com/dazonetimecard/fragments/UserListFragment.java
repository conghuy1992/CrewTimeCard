package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import timecard.dazone.com.dazonetimecard.dtos.ErrorDto;
import timecard.dazone.com.dazonetimecard.dtos.MyListDto;
import timecard.dazone.com.dazonetimecard.utils.Statics;
import timecard.dazone.com.dazonetimecard.utils.Util;

public class UserListFragment extends MyListFragment {
    String userNo = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        millis = getArguments().getLong(KEY_POSITION);
        userNo = getArguments().getString(Statics.KEY_USER_NO, "");
    }

    public static UserListFragment newInstance(long position, String userNo) {
        UserListFragment fragmentFirst = new UserListFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_POSITION, position);
        args.putString(Statics.KEY_USER_NO, userNo);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    protected void reloadContentPage() {
        mHttpRequest.getUserList(this, millis, userNo);
    }

    @Override
    public void onGetMyListSuccess(List<MyListDto> myListDtos) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        current_Task.addAll(myListDtos);
        adapterList.updateRecyclerView(current_Task);

        if (layoutManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            layoutManager.scrollToPosition(calendar.get(Calendar.DATE) - 1);
        }
    }

    @Override
    public void onGetMyListFail(ErrorDto errorDto) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        Util.showMessage(errorDto.message);
    }
}