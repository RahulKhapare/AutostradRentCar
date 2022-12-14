package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.EditPickupDropofftActivity;
import com.autostrad.rentcar.databinding.ActivityLocationListBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.HomeLocationModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.viewHolder> {

    private Context context;
    private List<HomeLocationModel> locationModelList;
    private HomeFragment homeFragment;
    private int flag = 0;
    private boolean fromActivity = false;
    private boolean value = false;


    public interface onClick{
        void onLocationClick(String location,int flag,HomeLocationModel model);
    }

    public LocationAdapter(Context context, List<HomeLocationModel> locationModelList) {
        this.context = context;
        this.locationModelList = locationModelList;
        fromActivity = true;
        value = false;
    }

    public LocationAdapter(Context context, List<HomeLocationModel> locationModelList,HomeFragment fragment,int flag) {
        this.context = context;
        this.locationModelList = locationModelList;
        this.homeFragment = fragment;
        this.flag = flag;
        fromActivity = false;
        value = false;
    }

    public LocationAdapter(Context context, List<HomeLocationModel> locationModelList,int flag,boolean value2) {
        this.context = context;
        this.locationModelList = locationModelList;
        this.flag = flag;
        fromActivity = true;
        value = value2;
    }

    public LocationAdapter(Context context, List<HomeLocationModel> locationModelList,int flag) {
        this.context = context;
        this.locationModelList = locationModelList;
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
        HomeLocationModel model = locationModelList.get(position);

        holder.binding.txtLocation.setText(model.getLocation_name());
        holder.binding.txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
//                if (fromActivity){
//                    ((CarBookingDetailsActivity)context).onLocationClick(model.getLocation_name(),flag,model);
//                }else {
//                    ((HomeFragment)homeFragment).onLocationClick(model.getLocation_name(),flag,model);
//                }
                if (fromActivity){
                    if (value){
                        ((EditPickupDropofftActivity)context).onLocationClick(model.getLocation_name(),flag,model);
                    }else {
//                        ((LocationActivity)context).onLocationClick(model.getLocation_name(),flag,model);
                    }
                }else {
                    ((HomeFragment)homeFragment).onLocationClick(model.getLocation_name(),flag,model);
                }


            }
        });

        if (position==locationModelList.size()-1){
            holder.binding.lnrLine.setVisibility(View.GONE);
        }else {
            holder.binding.lnrLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return locationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityLocationListBinding binding;
        public viewHolder(@NonNull ActivityLocationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
