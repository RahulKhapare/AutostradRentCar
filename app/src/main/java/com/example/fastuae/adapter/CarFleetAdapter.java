package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarFleetListBinding;
import com.example.fastuae.model.CarFleetModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class CarFleetAdapter extends RecyclerView.Adapter<CarFleetAdapter.viewHolder> {

    private Context context;
    private List<CarFleetModel> carFleetModelList;

    public CarFleetAdapter(Context context, List<CarFleetModel> carFleetModelList) {
        this.context = context;
        this.carFleetModelList = carFleetModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarFleetListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_fleet_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CarFleetModel model = carFleetModelList.get(position);

        holder.binding.imgCar.setImageResource(model.getImage());
        holder.binding.txtCarName.setText(model.getCarName());
        holder.binding.txtCarGroup.setText(model.getGroupName());

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        holder.binding.imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.imgLike.setVisibility(View.VISIBLE);
                holder.binding.imgDisLike.setVisibility(View.GONE);
            }
        });

        holder.binding.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.imgLike.setVisibility(View.GONE);
                holder.binding.imgDisLike.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carFleetModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarFleetListBinding binding;
        public viewHolder(@NonNull ActivityCarFleetListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
