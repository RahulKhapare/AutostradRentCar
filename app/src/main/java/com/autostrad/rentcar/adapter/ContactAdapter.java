package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityContactUsListBinding;
import com.autostrad.rentcar.model.LocationModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.viewHolder> {

    private Context context;
    private List<LocationModel> locationModelList;


    public ContactAdapter(Context context, List<LocationModel> locationModelList) {
        this.context = context;
        this.locationModelList = locationModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityContactUsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_contact_us_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LocationModel model = locationModelList.get(position);

        holder.binding.txtArea.setText(model.getLocation_name());
        holder.binding.txtNumber.setText(model.getContact_number());
        holder.binding.txtEmail.setText(model.getContact_email());

    }

    @Override
    public int getItemCount() {
        return locationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityContactUsListBinding binding;
        public viewHolder(@NonNull ActivityContactUsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
