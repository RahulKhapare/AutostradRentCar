package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.ContactUsActivity;
import com.autostrad.rentcar.activity.LocationActivity;
import com.autostrad.rentcar.databinding.ActivityCarFilterListBinding;
import com.autostrad.rentcar.model.LocationModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class LocationFilterAdapter extends RecyclerView.Adapter<LocationFilterAdapter.viewHolder> {

    private Context context;
    private List<LocationModel> locationModelList;
    private int from;

    public interface onClick{
        void onFilterClick(LocationModel model);
    }

    public LocationFilterAdapter(Context context, List<LocationModel> locationModelList,int from) {
        this.context = context;
        this.locationModelList = locationModelList;
        this.from = from;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarFilterListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_filter_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LocationModel model = locationModelList.get(position);

        holder.binding.txtFilter.setText(model.getEmirate_name());
        holder.binding.txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (from==1){
                    ((ContactUsActivity)context).onFilterClick(model);
                }else if (from==2){
                    ((LocationActivity)context).onFilterClick(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarFilterListBinding binding;
        public viewHolder(@NonNull ActivityCarFilterListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
