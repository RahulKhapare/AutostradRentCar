package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityDocumentListBinding;
import com.example.fastuae.fragment.DocumentFragment;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.viewHolder> {

    private Context context;
    private List<DocumentModel> documentModelList;
    private DocumentFragment fragment;

    public interface onClick{
        void downloadDocument(String path);
    }

    public DocumentAdapter(Context context, List<DocumentModel> documentModelList,DocumentFragment fragment) {
        this.context = context;
        this.documentModelList = documentModelList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityDocumentListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_document_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DocumentModel model = documentModelList.get(position);

        holder.binding.txtDocument.setText(model.getDocumentName());

        holder.binding.lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((DocumentFragment)fragment).downloadDocument(model.getPath());
            }
        });

    }

    @Override
    public int getItemCount() {
        return documentModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityDocumentListBinding binding;
        public viewHolder(@NonNull ActivityDocumentListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
