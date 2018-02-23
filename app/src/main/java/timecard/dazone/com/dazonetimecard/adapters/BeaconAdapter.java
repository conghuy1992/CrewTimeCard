package timecard.dazone.com.dazonetimecard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import timecard.dazone.com.dazonetimecard.R;
import timecard.dazone.com.dazonetimecard.dtos.BeaconDTO;
import timecard.dazone.com.dazonetimecard.fragments.DialogRecyclerViewFragment;

/**
 * Created by Dat on 7/26/2016.
 */
public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.ViewHolder> {
    private ArrayList<BeaconDTO> data;
    private DialogRecyclerViewFragment context;
    private boolean isChecked = false;
    private RadioButton lastCheckedRB = null;

    public int mSelectedItem = -1;


    public BeaconAdapter(DialogRecyclerViewFragment context, ArrayList<BeaconDTO> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beacon, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /** GET ITEM */
        BeaconDTO item = data.get(position);

        holder.tvBeacon.setText(item.getBeaconMajor() + "/" + item.getBeaconMinor());

        holder.radioButton.setChecked(position == mSelectedItem);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layout;
        private TextView tvBeacon;
        private RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout_beacon);
            tvBeacon = (TextView) itemView.findViewById(R.id.item_tv_beacon);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_button);
            radioButton.setClickable(false);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    if(mSelectedItem!=-1)
                    {
                        context.showBtnOK();
                    }
                    notifyItemRangeChanged(0, data.size());
                }
            };
            layout.setOnClickListener(clickListener);
        }
    }
}
