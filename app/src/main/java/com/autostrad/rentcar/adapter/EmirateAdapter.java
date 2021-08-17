package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.CarBookingDetailsActivity;
import com.autostrad.rentcar.activity.EditPickupDropofftActivity;
import com.autostrad.rentcar.databinding.ActivityLocationListBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.EmirateModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class EmirateAdapter extends RecyclerView.Adapter<EmirateAdapter.viewHolder> {

    private Context context;
    private List<EmirateModel> emirateModelList;
    private HomeFragment homeFragment;
    private int flag = 0;
    private boolean fromActivity = false;
    private boolean value = false;


    public interface onClick{
        void onEmirateClick(int flag,EmirateModel model);
    }

    public EmirateAdapter(Context context, List<EmirateModel> emirateModelList, HomeFragment fragment, int flag) {
        this.context = context;
        this.emirateModelList = emirateModelList;
        this.homeFragment = fragment;
        this.flag = flag;
        fromActivity = false;
        value = false;
    }

    public EmirateAdapter(Context context, List<EmirateModel> emirateModelList, int flag,boolean value1) {
        this.context = context;
        this.emirateModelList = emirateModelList;
        this.flag = flag;
        fromActivity = true;
        value = value1;
    }

    public EmirateAdapter(Context context, List<EmirateModel> emirateModelList,int flag) {
        this.context = context;
        this.emirateModelList = emirateModelList;
        this.flag = flag;
        fromActivity = true;
        value = false;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityLocationListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_location_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        EmirateModel model = emirateModelList.get(position);

        holder.binding.txtLocation.setText(model.getEmirate_name());
        holder.binding.txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (fromActivity){
                    if (value){
                        ((EditPickupDropofftActivity)context).onEmirateClick(flag,model);
                    }else {
                        ((CarBookingDetailsActivity)context).onEmirateClick(flag,model);
                    }
                }else {
                    ((HomeFragment)homeFragment).onEmirateClick(flag,model);
                }

            }
        });

        if (position==emirateModelList.size()-1){
            holder.binding.lnrLine.setVisibility(View.GONE);
        }else {
            holder.binding.lnrLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return emirateModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityLocationListBinding binding;
        public viewHolder(@NonNull ActivityLocationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
