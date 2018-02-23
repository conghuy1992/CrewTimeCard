package timecard.dazone.com.dazonetimecard.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.adapters.AdapterOrganizationCompanyTab;
import timecard.dazone.com.dazonetimecard.dtos.BelongDepartmentDTO;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTOTemp;
import timecard.dazone.com.dazonetimecard.fragments.TimeCardFragment;
import timecard.dazone.com.dazonetimecard.interfaces.ChooseGroupNoCallBack;
import timecard.dazone.com.dazonetimecard.utils.Constant;

/**
 * Created by maidinh on 11-Oct-17.
 */

public class OrganizationDialog extends Dialog {
    private String TAG = "OrganizationDialog";
    private RecyclerView listCompany;
    private ProgressBar progressBar;
    private ArrayList<TreeUserDTO> mPersonList = new ArrayList<>();
    private ArrayList<TreeUserDTO> temp = new ArrayList<>();
    private ArrayList<TreeUserDTOTemp> listTemp = new ArrayList<>();
    private ArrayList<TreeUserDTO> mDepartmentList = new ArrayList<>();
    private List<TreeUserDTO> list = new ArrayList<>();
    private AdapterOrganizationCompanyTab mAdapter;

    public void dissmiss() {
        dismiss();
    }

    public OrganizationDialog(@NonNull Context context, ChooseGroupNoCallBack callBack) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.organization_dialog_layout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listCompany = (RecyclerView) findViewById(R.id.listCompany);
        listCompany.setLayoutManager(new LinearLayoutManager(context));
        OrganizationDialog in = this;
        mAdapter = new AdapterOrganizationCompanyTab(context, list, in, callBack);
        listCompany.setAdapter(mAdapter);

        if (TimeCardFragment.instance != null) {
            list = TimeCardFragment.instance.getSubordinates();
            if (list != null && list.size() > 0) {
                mAdapter.updateList(list);
            } else {
                Toast.makeText(context, "Can not get list user, restart app please", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Can not get list user, restart app please", Toast.LENGTH_SHORT).show();
        }

        hideLoading();

//        buildTree(treeUserDTOs, true);

        Window window = getWindow();
//        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        show();
    }


    public void scrollToEndList(int size) {
        listCompany.smoothScrollToPosition(size);
    }

    private void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }


}
