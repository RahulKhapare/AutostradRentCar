package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityAdditionalDriverListBinding;
import com.autostrad.rentcar.fragment.AdditionalDriverFragment;
import com.autostrad.rentcar.model.AdditionalDriverModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class AdditionalDriverListAdapter extends RecyclerView.Adapter<AdditionalDriverListAdapter.viewHolder> {

    private Context context;
    private List<AdditionalDriverModel> additionalDriverModelList;
    private Session session;
    private AdditionalDriverFragment fragment;

    public interface onClick {
        void editClick(AdditionalDriverModel model);
        void deleteClick(AdditionalDriverModel model);
        void uploadClick(AdditionalDriverModel model);
    }


    public AdditionalDriverListAdapter(Context context, List<AdditionalDriverModel> additionalDriverModelList, AdditionalDriverFragment fragment) {
        this.context = context;
        this.additionalDriverModelList = additionalDriverModelList;
        this.fragment = fragment;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityAdditionalDriverListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_additional_driver_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        AdditionalDriverModel model = additionalDriverModelList.get(position);

        holder.binding.txtName.setText(model.getDriver_name());
        holder.binding.txtEmail.setText(model.getDriver_email());
        holder.binding.txtMobile.setText( "+"+model.getDriver_mobile());

        holder.binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverFragment)fragment).editClick(model);
            }
        });

        holder.binding.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverFragment)fragment).deleteClick(model);
            }
        });

        holder.binding.txtDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverFragment)fragment).uploadClick(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return additionalDriverModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityAdditionalDriverListBinding binding;

        public viewHolder(@NonNull ActivityAdditionalDriverListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
