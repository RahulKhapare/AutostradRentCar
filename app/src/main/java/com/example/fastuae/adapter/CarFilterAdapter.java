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
import com.example.fastuae.databinding.ActivityCarFilterListBinding;
import com.example.fastuae.databinding.ActivityLocationListBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class CarFilterAdapter extends RecyclerView.Adapter<CarFilterAdapter.viewHolder> {

    private Context context;
    private List<CarFilterModel> carFilterModelList;
    public interface onClick{
        void onFilterClick(String location);
    }

    public CarFilterAdapter(Context context, List<CarFilterModel> carFilterModelList) {
        this.context = context;
        this.carFilterModelList = carFilterModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarFilterListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_filter_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CarFilterModel model = carFilterModelList.get(position);

        holder.binding.txtFilter.setText(model.getFilterName());
        holder.binding.txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((CarFilterActivity)context).onFilterClick(model.getFilterName());

            }
        });

        if (position==carFilterModelList.size()-1){
            holder.binding.lnrLine.setVisibility(View.GONE);
        }else {
            holder.binding.lnrLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return carFilterModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarFilterListBinding binding;
        public viewHolder(@NonNull ActivityCarFilterListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
