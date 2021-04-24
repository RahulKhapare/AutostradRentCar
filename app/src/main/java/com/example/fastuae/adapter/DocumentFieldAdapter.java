package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityAddOnsListBinding;
import com.example.fastuae.databinding.ActivityFiledListBinding;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.model.FieldModel;

import java.util.List;

public class DocumentFieldAdapter extends RecyclerView.Adapter<DocumentFieldAdapter.viewHolder> {

    private Context context;
    private List<FieldModel> filedList;


    public DocumentFieldAdapter(Context context, List<FieldModel> filedList) {
        this.context = context;
        this.filedList = filedList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityFiledListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_filed_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        FieldModel model = filedList.get(position);

    }

    @Override
    public int getItemCount() {
        return filedList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityFiledListBinding binding;

        public viewHolder(@NonNull ActivityFiledListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
