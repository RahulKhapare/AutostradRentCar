package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivitySelectionBgBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ViewHolder> {

    private Context context;
    private List<CarFilterModel> carFilterModelList;
    int value;

    public interface click {
        void removeLocation(int position);
    }

    public SelectionAdapter(Context context, List<CarFilterModel> carFilterModelList) {
        this.context = context;
        this.carFilterModelList = carFilterModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivitySelectionBgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_selection_bg, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarFilterModel model = carFilterModelList.get(position);

        holder.binding.txtName.setText(model.getName());

        holder.binding.lnrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                carFilterModelList.remove(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return carFilterModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivitySelectionBgBinding binding;

        public ViewHolder(@NonNull ActivitySelectionBgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
