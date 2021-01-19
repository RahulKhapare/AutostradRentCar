package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.activity.CarFilterActivity;
import com.example.fastuae.activity.ContactUsActivity;
import com.example.fastuae.databinding.ActivityCarFilterListBinding;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class LocationFilterAdapter extends RecyclerView.Adapter<LocationFilterAdapter.viewHolder> {

    private Context context;
    private List<LocationModel> locationModelList;

    public interface onClick{
        void onFilterClick(String location);
    }

    public LocationFilterAdapter(Context context, List<LocationModel> locationModelList) {
        this.context = context;
        this.locationModelList = locationModelList;
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

        holder.binding.txtFilter.setText(model.getLocation());
        holder.binding.txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((ContactUsActivity)context).onFilterClick(model.getLocation());

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
