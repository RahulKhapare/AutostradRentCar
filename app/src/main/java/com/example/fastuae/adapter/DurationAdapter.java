package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLocationListBinding;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.model.DurationModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class DurationAdapter extends RecyclerView.Adapter<DurationAdapter.viewHolder> {

    private Context context;
    private List<DurationModel> durationModelList;
    private HomeFragment homeFragment;

    public interface onClick{
        void onDurationClick(DurationModel model);
    }

    public DurationAdapter(Context context, List<DurationModel> durationModelList, HomeFragment fragment) {
        this.context = context;
        this.durationModelList = durationModelList;
        this.homeFragment = fragment;
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
                ((HomeFragment)homeFragment).onDurationClick(model);
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
