package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarFilterListBinding;
import com.example.fastuae.fragment.AdditionalDriverDocumentFragment;
import com.example.fastuae.model.DocumentFilterModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class DocumentFilterAdapter extends RecyclerView.Adapter<DocumentFilterAdapter.viewHolder> {

    private Context context;
    private List<DocumentFilterModel> documentFilterModelList;
    private AdditionalDriverDocumentFragment fragment;
    private boolean firstValue;

    public interface onClick {
        void onFilterClick(DocumentFilterModel model);
    }

    public DocumentFilterAdapter(Context context, List<DocumentFilterModel> documentFilterModelList, AdditionalDriverDocumentFragment fragment, boolean firstValue) {
        this.context = context;
        this.documentFilterModelList = documentFilterModelList;
        this.fragment = fragment;
        this.firstValue = firstValue;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarFilterListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_filter_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DocumentFilterModel model = documentFilterModelList.get(position);

        holder.binding.txtFilter.setText(model.getTitle());
        holder.binding.txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverDocumentFragment)fragment).onFilterClick(model);
            }
        });

        if (firstValue){
            firstValue = false;
            ((AdditionalDriverDocumentFragment)fragment).onFilterClick(model);
        }
    }

    @Override
    public int getItemCount() {
        return documentFilterModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarFilterListBinding binding;

        public viewHolder(@NonNull ActivityCarFilterListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
