package com.example.fastuae.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLocationDetailsListBinding;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

import java.util.List;

public class LocationDetailAdapter extends RecyclerView.Adapter<LocationDetailAdapter.viewHolder> {

    private Context context;
    private List<LocationModel> locationModelList;
    private Session session;

    public LocationDetailAdapter(Context context, List<LocationModel> locationModelList) {
        this.context = context;
        this.locationModelList = locationModelList;
        session = new Session(context);
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityLocationDetailsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_location_details_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LocationModel model = locationModelList.get(position);

        holder.binding.txtBranchCode.setText(model.getEmirate_id());
        holder.binding.txtBranchName.setText(model.getLocation_name());
        holder.binding.txtDetails.setText(model.getAddress()+
                "\n" + context.getResources().getString(R.string.tel)+ ": "+ model.getContact_number()
                +"\n"+ context.getResources().getString(R.string.email) + ": "  + model.getContact_email());
        holder.binding.txtNormalOperation.setText(model.getLocation_timing());

        if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            holder.binding.txtBranchName.setGravity(Gravity.RIGHT);
            holder.binding.txtBranchCode.setGravity(Gravity.RIGHT);
        }
    }

    @Override
    public int getItemCount() {
        return locationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityLocationDetailsListBinding binding;
        public viewHolder(@NonNull ActivityLocationDetailsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
