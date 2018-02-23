package timecard.dazone.com.dazonetimecard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.activities.CompanyInfoActivity;
import timecard.dazone.com.dazonetimecard.adapters.BeaconAdapter;
import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;

/**
 * Created by Dat on 7/26/2016.
 */
public class DialogRecyclerViewFragment extends DialogFragment implements View.OnClickListener {
    /**
     * VIEW
     */
    private View rootView;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private TextView btnOk;
    private TextView btnCancel;

    private Context context;

    private BeaconAdapter adapter;
    private ArrayList<BeaconDTO> data = new ArrayList<>();

    public int selectedBeacon;


    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //inflate layout with recycler view
        rootView = inflater.inflate(R.layout.dialog_recycler_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        btnOk = (TextView) rootView.findViewById(R.id.btn_ok);
        btnOk.setVisibility(View.GONE);
        btnOk.setOnClickListener(this);
        btnCancel = (TextView) rootView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        //setadapter
        adapter = new BeaconAdapter(this, data);
        mRecyclerView.setAdapter(adapter);
        //get your recycler view and populate it.

        /*BeaconDTO beaconDTO = new BeaconDTO();
        beaconDTO.setBeaconUUID("11111111111111111");
        beaconDTO.setBeaconMajor(Integer.parseInt("11"));
        beaconDTO.setBeaconMinor(Integer.parseInt("11111"));
        data.add(beaconDTO);
        int index = data.indexOf(beaconDTO);
        if (index != -1) {
            adapter.notifyItemInserted(index);
        }*/

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void update(String uuid, String major, String minor) {
        if (!isContains(uuid)) {
            BeaconDTO beaconDTO = new BeaconDTO();
            beaconDTO.setBeaconUUID(uuid);
            beaconDTO.setBeaconMajor(Integer.parseInt(major));
            beaconDTO.setBeaconMinor(Integer.parseInt(minor));
            data.add(beaconDTO);
            int index = data.indexOf(beaconDTO);
            if (index != -1) {
                adapter.notifyItemInserted(index);
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean isContains(String uuid) {
        for (BeaconDTO beaconDTO : data) {
            if (beaconDTO.getBeaconUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((CompanyInfoActivity) getActivity()).dialogClose();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                ((CompanyInfoActivity) getActivity()).closeDialog();
                this.dismiss();
                break;
            case R.id.btn_ok:
                BeaconDTO beaconDTO = data.get(adapter.mSelectedItem);
                ((CompanyInfoActivity) getActivity()).insertBeacon(beaconDTO.getBeaconUUID(), beaconDTO.getBeaconMajor(), beaconDTO.getBeaconMinor());
                this.dismiss();
                break;
        }
    }

    public void showBtnOK() {
        btnOk.setVisibility(View.VISIBLE);
    }

}
