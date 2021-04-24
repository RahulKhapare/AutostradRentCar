package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityDocumentDetailsListBinding;
import com.example.fastuae.databinding.ActivityDocumentListBinding;
import com.example.fastuae.fragment.DocumentFragment;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.viewHolder> {

    private Context context;
    private List<DocumentModel> documentModelList;

    public DocumentListAdapter(Context context, List<DocumentModel> documentModelList) {
        this.context = context;
        this.documentModelList = documentModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityDocumentDetailsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_document_details_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DocumentModel model = documentModelList.get(position);

        holder.binding.txtDocument.setText(model.getTitle());
        holder.binding.txtDetails.setText(model.getKey());

        holder.binding.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });


    }

    @Override
    public int getItemCount() {
        return documentModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityDocumentDetailsListBinding binding;
        public viewHolder(@NonNull ActivityDocumentDetailsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
