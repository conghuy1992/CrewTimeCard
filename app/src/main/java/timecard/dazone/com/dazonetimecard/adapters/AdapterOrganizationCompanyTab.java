package timecard.dazone.com.dazonetimecard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.customviews.OrganizationDialog;
import timecard.dazone.com.dazonetimecard.dtos.TreeUserDTO;
import timecard.dazone.com.dazonetimecard.interfaces.ChooseGroupNoCallBack;
import timecard.dazone.com.dazonetimecard.utils.Constant;
import timecard.dazone.com.dazonetimecard.utils.Util;

/**
 * Created by maidinh on 29/12/2016.
 */

public class AdapterOrganizationCompanyTab extends RecyclerView.Adapter<AdapterOrganizationCompanyTab.MyViewHolder> {
    private String TAG = "OrganizationChart";
    private List<TreeUserDTO> list = new ArrayList<>();
    private List<TreeUserDTO> listTemp = new ArrayList<>();
    private List<TreeUserDTO> listTemp_2 = new ArrayList<>();
    private List<TreeUserDTO> listTemp_3 = new ArrayList<>();
    private int isSearch = 0; // 0 -> normal : 1 -> search
    private Context context;
    private OrganizationDialog instance;
    private int mg = 0;
    private ChooseGroupNoCallBack callBack;

    public List<TreeUserDTO> getList() {
        return listTemp;
    }

    public AdapterOrganizationCompanyTab(Context context, List<TreeUserDTO> list, OrganizationDialog instance, ChooseGroupNoCallBack callBack) {
        this.context = context;
        this.list = list;
        this.instance = instance;
        this.mg = Util.getDimenInPx(R.dimen.dimen_20_40);
        this.callBack = callBack;
    }

    public List<TreeUserDTO> getCurrentList() {
        return list;
    }

    public void updateIsSearch(int a) {
        isSearch = a;
    }

    boolean isAddSearch(List<TreeUserDTO> lst, int id) {
        for (TreeUserDTO obj : lst) {
            if (obj.getId() == id)
                return false;
        }
        return true;
    }


    public void actionSearch(String key) {
        List<TreeUserDTO> lst = new ArrayList<>();
        for (TreeUserDTO obj : listTemp_3) {
            if (obj.getType() == 2) {
                if ((obj.getName().toUpperCase().contains(key.toUpperCase()))
                        || (obj.getPhoneNumber() != null && obj.getPhoneNumber().toUpperCase().contains(key.toUpperCase()))
                        || (obj.getPhoneNumber() != null && obj.getPhoneNumber().toUpperCase().replace("-", "").contains(key.toUpperCase()))
                        || (obj.getCompanyNumber() != null && obj.getCompanyNumber().toUpperCase().contains(key.toUpperCase()))
                        || (obj.getCompanyNumber() != null && obj.getCompanyNumber().toUpperCase().replace("-", "").contains(key.toUpperCase()))) {
                    if (isAddSearch(lst, obj.getId())) {
                        lst.add(obj);
                    }
                }
            }
        }
        this.list = lst;
        this.notifyDataSetChanged();
    }

    public void updateListSearch(List<TreeUserDTO> lst) {
        Log.d(TAG, "updateListSearch");
        this.list = lst;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout iconWrapper, item_org_wrapper, layout_one, layout_two;
        public ImageView avatar;
        public ImageView folderIcon;

        public TextView name, position, nameTwo, positionTwo, name_department;


        public MyViewHolder(View view) {
            super(view);

            layout_one = (LinearLayout) view.findViewById(R.id.layout_one);
            layout_two = (LinearLayout) view.findViewById(R.id.layout_two);

            item_org_wrapper = (LinearLayout) view.findViewById(R.id.item_org_wrapper);

            avatar = (ImageView) view.findViewById(R.id.avatar);
            folderIcon = (ImageView) view.findViewById(R.id.ic_folder);

            iconWrapper = (LinearLayout) view.findViewById(R.id.icon_wrapper);

            name = (TextView) view.findViewById(R.id.name);
            position = (TextView) view.findViewById(R.id.position);

            name_department = (TextView) view.findViewById(R.id.name_department);
            nameTwo = (TextView) view.findViewById(R.id.nameTwo);
            positionTwo = (TextView) view.findViewById(R.id.positionTwo);
        }

        public void handler(final TreeUserDTO treeUserDTO, final int index) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = treeUserDTO.getMargin();
            if (isSearch == 0) {
                margin = treeUserDTO.getMargin();
                layout_one.setVisibility(View.VISIBLE);
                layout_two.setVisibility(View.GONE);
            } else {
                margin = mg;
                layout_one.setVisibility(View.GONE);
                layout_two.setVisibility(View.VISIBLE);
            }
            params.setMargins(margin, 0, 0, 0);
            item_org_wrapper.setLayoutParams(params);

            folderIcon.setImageResource(treeUserDTO.isFlag() ? R.drawable.home_folder_open_ic : R.drawable.home_folder_close_ic);
            String nameString = treeUserDTO.getName();
            String namePosition = "";
            try {
                namePosition = treeUserDTO.getPosition();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (treeUserDTO.getType() != 2) {
                position.setVisibility(View.GONE);

                positionTwo.setVisibility(View.GONE);


                folderIcon.setVisibility(View.VISIBLE);
            }
            name_department.setText("" + treeUserDTO.getName_parent());
            name.setText(nameString);
            nameTwo.setText(nameString);
            item_org_wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "getId:" + treeUserDTO.getId());
                    if (callBack != null) callBack.onCompleted(treeUserDTO);
                    if (instance != null) instance.dissmiss();
                }
            });
            folderIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (treeUserDTO.getType() != 2) {
                        if (treeUserDTO.isFlag()) {
                            Log.d(TAG, "collapse");
                            collapse(index, treeUserDTO);
                            treeUserDTO.setFlag(false);
                        } else {
                            Log.d(TAG, "expand");
                            boolean flag = false;
                            if (index == list.size() - 1) {
                                flag = true;
                            }
                            expand(index, treeUserDTO, flag);
                            treeUserDTO.setFlag(true);
                        }
                    }
                }
            });

        }
    }

    boolean isAdd(List<TreeUserDTO> lst, TreeUserDTO treeUserDTO) {
//        for (TreeUserDTO obj : lst) {
//            if (treeUserDTO.getId() == obj.getId()
//                    && treeUserDTO.getDBId() == obj.getDBId()
//                    && treeUserDTO.getParent() == obj.getParent()
//                    && treeUserDTO.getType() == obj.getType()
//                    && treeUserDTO.getPositionSortNo() == obj.getPositionSortNo()
//                    && treeUserDTO.getmSortNo() == obj.getmSortNo()
//                    && treeUserDTO.getName().equals(obj.getName()))
//                return false;
//        }
        return true;
    }


    void addList(TreeUserDTO obj, int margin, int level) {

        margin += Util.getDimenInPx(R.dimen.dimen_20_40);
        obj.setMargin(margin);

        level += 1;
        obj.setLevel(level);

        obj.setName_parent(Constant.get_department_name(obj, listTemp_3));
        this.listTemp.add(obj);

        boolean flag = true;
        obj.setFlag(flag);
//        Log.d(TAG,new Gson().toJson(obj));
        this.listTemp_2.add(obj);
        this.listTemp_3.add(obj);
        if (obj.getSubordinates() != null) {
            if (obj.getSubordinates().size() > 0) {
                boolean hasType2 = false;
                boolean hasType0 = false;

                for (TreeUserDTO dto : obj.getSubordinates()) {
                    if (dto.getType() == 2) {
                        hasType2 = true;
                    }
                    if (dto.getType() == 0) {
                        hasType0 = true;
                    }
                }
                if (hasType2 && hasType0) {
                    Collections.sort(obj.getSubordinates(), new Comparator<TreeUserDTO>() {
                        @Override
                        public int compare(TreeUserDTO r1, TreeUserDTO r2) {
                            return r1.getmSortNo() - r2.getmSortNo();
                        }
                    });
                }
                for (TreeUserDTO dto1 : obj.getSubordinates()) {
                    addList(dto1, margin, level);
                }
            }
        }
    }

    public void updateList(List<TreeUserDTO> list) {
        if (list != null && list.size() > 0) {
            Log.d(TAG, "start updateList");
            this.list.clear();
            final int tempMargin = Util.getDimenInPx(R.dimen.dimen_20_40) * -1;
            for (TreeUserDTO obj : list) {
                addList(obj, tempMargin, -1);
            }
            Log.d(TAG, "finish addListTemp");
            List<TreeUserDTO> lst = this.listTemp_2;
//            for (int i = 0; i < lst.size(); i++) {
//                TreeUserDTO obj = lst.get(i);
//                int level = obj.getLevel();
//                boolean flag = obj.isFlag();
//                if (!flag) {
//                    if (i + 1 < lst.size()) {
//                        for (int j = i + 1; j < lst.size(); j++) {
//                            TreeUserDTO nextObj = lst.get(j);
//                            if (level < nextObj.getLevel()) {
//                                lst.remove(j);
//                                j--;
//                            } else {
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
            this.list = lst;

            this.notifyDataSetChanged();
            int k = 0;

            if (instance != null)
                instance.scrollToEndList(k);
            Log.d(TAG, "notifyDataSetChanged");

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_organization_company_tab, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TreeUserDTO treeUserDTO = list.get(position);
        holder.handler(treeUserDTO, position);

    }

    void collapse(final int position, final TreeUserDTO treeUserDTO) {
        int levelCur = list.get(position).getLevel();
        Log.d(TAG, "levelCur:" + levelCur);
        int a = position + 1;
        if (a < list.size()) {
            for (int i = a; i < list.size(); i++) {
                TreeUserDTO obj = list.get(i);
                int level = obj.getLevel();
                if (levelCur < level) {
                    Log.d(TAG, "remove: " + obj.getName());
                    list.remove(i);
                    i--;
                } else {
                    break;
                }
            }
            notifyDataSetChanged();
        }


    }

    private void expand(int position, TreeUserDTO treeUserDTO, boolean flag) {
        int levelCur = treeUserDTO.getLevel();
        int index = position + 1;
        // get index of list
        int indexListTemp = 0;
        for (int i = 0; i < listTemp.size(); i++) {
            TreeUserDTO obj = listTemp.get(i);
            if (obj.getType() != 2) {
                if (treeUserDTO.getId() == obj.getId()
                        && treeUserDTO.getDBId() == obj.getDBId()
                        && treeUserDTO.getParent() == obj.getParent()
                        && treeUserDTO.getType() == obj.getType()
                        && treeUserDTO.getPositionSortNo() == obj.getPositionSortNo()
                        && treeUserDTO.getmSortNo() == obj.getmSortNo()
                        && treeUserDTO.getName().equals(obj.getName())) {
                    indexListTemp = i;
                    break;
                }
            }
        }

        int a = indexListTemp + 1;
        if (a < listTemp.size()) {
            for (int i = a; i < listTemp.size(); i++) {
                TreeUserDTO object = listTemp.get(i);
                if (levelCur < object.getLevel()) {
                    object.setFlag(true);
                    if (isAdd(this.list, object)) {
                        list.add(index, object);
                        index++;
                    }
                } else {
                    break;
                }
            }
        }

        notifyDataSetChanged();
        if (flag) {
            Log.d(TAG, "scrollToEndList");
            if (instance != null)
                instance.scrollToEndList(position + 1);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}