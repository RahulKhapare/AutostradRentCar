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
import com.example.fastuae.databinding.ActivitySpecialOffersListBinding;
import com.example.fastuae.model.SpecialOffersModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.viewHolder> {

    private Context context;
    private List<SpecialOffersModel> specialOffersModelList;
    private Session session;

    public SpecialOffersAdapter(Context context, List<SpecialOffersModel> specialOffersModelList) {
        this.context = context;
        this.specialOffersModelList = specialOffersModelList;
        session = new Session(context);
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivitySpecialOffersListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_special_offers_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SpecialOffersModel model = specialOffersModelList.get(position);

        holder.binding.txtDescription.setText(model.getDescription());
        holder.binding.txtValid.setText(model.getValidUpTo());
        holder.binding.txtCode.setText(model.getCode());

        if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            holder.binding.txtValid.setGravity(Gravity.RIGHT);
        }

    }

    @Override
    public int getItemCount() {
        return specialOffersModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivitySpecialOffersListBinding binding;
        public viewHolder(@NonNull ActivitySpecialOffersListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
