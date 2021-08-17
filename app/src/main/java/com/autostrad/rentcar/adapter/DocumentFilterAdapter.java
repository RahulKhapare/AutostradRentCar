package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityCarFilterListBinding;
import com.autostrad.rentcar.fragment.AdditionalDriverDocumentFragment;
import com.autostrad.rentcar.model.DocumentFilterModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class DocumentFilterAdapter extends RecyclerView.Adapter<DocumentFilterAdapter.viewHolder> {

    private Context context;
    private List<DocumentFilterModel> documentFilterModelList;
    private AdditionalDriverDocumentFragment fragment;
    private boolean firstValue;
    private String filterTitle;

    public interface onClick {
        void onFilterClick(DocumentFilterModel model);
    }

    public DocumentFilterAdapter(Context context, List<DocumentFilterModel> documentFilterModelList, AdditionalDriverDocumentFragment fragment, boolean firstValue,String filterTitle) {
        this.context = context;
        this.documentFilterModelList = documentFilterModelList;
        this.fragment = fragment;
        this.firstValue = firstValue;
        this.filterTitle = filterTitle;
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

        if (!filterTitle.equals("")){
            firstValue = false;
            if (model.getTitle().equals(filterTitle)){
                ((AdditionalDriverDocumentFragment)fragment).onFilterClick(model);
            }
        }else {
            if (firstValue){
                firstValue = false;
                ((AdditionalDriverDocumentFragment)fragment).onFilterClick(model);
            }
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
