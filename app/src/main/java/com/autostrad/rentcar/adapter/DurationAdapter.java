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
import com.autostrad.rentcar.model.DurationModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class DurationAdapter extends RecyclerView.Adapter<DurationAdapter.viewHolder> {

    private Context context;
    private List<DurationModel> durationModelList;
    private HomeFragment homeFragment;
    private boolean value = false;

    public interface onClick{
        void onDurationClick(DurationModel model);
    }

    public DurationAdapter(Context context, List<DurationModel> durationModelList, HomeFragment fragment) {
        this.context = context;
        this.durationModelList = durationModelList;
        this.homeFragment = fragment;
        value = false;
    }

    public DurationAdapter(Context context, List<DurationModel> durationModelList,boolean value1) {
        this.context = context;
        this.durationModelList = durationModelList;
        value = value1;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityLocationListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_location_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DurationModel model = durationModelList.get(position);

        holder.binding.txtLocation.setText(model.getDuration());
        holder.binding.txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (value){
                    ((EditPickupDropofftActivity)context).onDurationClick(model);
                }else {
                    ((HomeFragment)homeFragment).onDurationClick(model);
                }
            }
        });

        if (position==durationModelList.size()-1){
            holder.binding.lnrLine.setVisibility(View.GONE);
        }else {
            holder.binding.lnrLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return durationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityLocationListBinding binding;
        public viewHolder(@NonNull ActivityLocationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
