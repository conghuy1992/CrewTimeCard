package timecard.dazone.com.dazonetimecard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.ListLoadMoreAdapter;
import timecard.dazone.com.dazonetimecard.utils.HttpRequest;

public abstract class ListFragment<T> extends Fragment {
    protected ListLoadMoreAdapter adapterList;
    protected List<T> current_Task;
    protected HttpRequest mHttpRequest;
    protected RecyclerView rvJobsList;
    protected LinearLayout recycler_header;
    protected TextView no_item_found;
    protected SwipeRefreshLayout swipeRefreshLayout;
    public static final String KEY_POSITION = "position";
    protected long millis = 0;
    protected int start = 0;
    protected int limit = 20;
    protected String userNo = "0";
    protected RelativeLayout progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHttpRequest = HttpRequest.getInstance();
        current_Task = new ArrayList<>();
        try {
            millis = getArguments().getLong(KEY_POSITION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        rvJobsList = (RecyclerView) v.findViewById(R.id.mytask_rv_main);
        recycler_header = (LinearLayout) v.findViewById(R.id.recycler_header);
        no_item_found = (TextView) v.findViewById(R.id.no_item_found);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        progressBar = (RelativeLayout) v.findViewById(R.id.progressBar);
        initRecycleView();
        initSwipeRefresh();
        initList();

        return v;
    }

    protected void initList() {
        progressBar.setVisibility(View.VISIBLE);
        reloadContentPage();
    }

    protected abstract void initRecycleView();

    protected abstract void initSwipeRefresh();

    protected abstract void reloadContentPage();

    protected abstract void onRefresh();
}