package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityContactUsListBinding;
import com.example.fastuae.model.ContactModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.viewHolder> {

    private Context context;
    private List<ContactModel> contactModelList;


    public ContactAdapter(Context context, List<ContactModel> contactModelList) {
        this.context = context;
        this.contactModelList = contactModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityContactUsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_contact_us_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ContactModel model = contactModelList.get(position);

        holder.binding.txtArea.setText(model.getArea());
        holder.binding.txtNumber.setText(model.getNumber());
        holder.binding.txtEmail.setText(model.getEmail());

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityContactUsListBinding binding;
        public viewHolder(@NonNull ActivityContactUsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
