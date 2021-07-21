package com.example.fastuae.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.CarDetailOneActivity;
import com.example.fastuae.databinding.ActivityAddOnsListBinding;
import com.example.fastuae.databinding.ActivityChooseExtraListBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.RemoveHtml;

import java.util.ArrayList;
import java.util.List;

public class AddOnsAdapter extends RecyclerView.Adapter<AddOnsAdapter.viewHolder> {

    private Context context;
    private List<ChooseExtrasModel> chooseExtrasModelList;
    private Session session;

    public interface onClick {
        void aedDetailsClick(ChooseExtrasModel model);
    }

    public interface onCalculation {
        void aedCalculation(ChooseExtrasModel model,int pos);
    }

    public AddOnsAdapter(Context context, List<ChooseExtrasModel> chooseExtrasModelList) {
        this.context = context;
        this.chooseExtrasModelList = chooseExtrasModelList;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityAddOnsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_add_ons_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ChooseExtrasModel model = chooseExtrasModelList.get(position);
        holder.binding.txtExtraTitle.setText(model.getTitle());
        holder.binding.txtExtraValue.setText("AED "+model.getPrice());

        ((CarDetailOneActivity)context).aedCalculation(model,position);

        holder.binding.txtAEDDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CarDetailOneActivity)context).aedDetailsClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chooseExtrasModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityAddOnsListBinding binding;

        public viewHolder(@NonNull ActivityAddOnsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
