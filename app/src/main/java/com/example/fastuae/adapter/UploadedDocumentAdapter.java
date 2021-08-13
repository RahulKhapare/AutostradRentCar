package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityUploadedDocumentListBinding;
import com.example.fastuae.fragment.AdditionalDriverDocumentFragment;
import com.example.fastuae.model.DocumentUploadedModel;
import com.example.fastuae.model.UploadedDocumentModel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadedDocumentAdapter extends RecyclerView.Adapter<UploadedDocumentAdapter.viewHolder> {

    private Context context;
    private List<UploadedDocumentModel> documentModelList;
    private AdditionalDriverDocumentFragment fragment;
    boolean fromActivity;


    public UploadedDocumentAdapter(Context context, List<UploadedDocumentModel> documentModelList, AdditionalDriverDocumentFragment fragment) {
        this.context = context;
        this.documentModelList = documentModelList;
        this.fragment = fragment;

    }

    public interface onClick {
        void documentView(String imagePath);
    }

    public interface onClickDelete {
        void documentDelete(DocumentUploadedModel model);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityUploadedDocumentListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_uploaded_document_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        UploadedDocumentModel model = documentModelList.get(position);

    }

    @Override
    public int getItemCount() {
        return documentModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityUploadedDocumentListBinding binding;

        public viewHolder(@NonNull ActivityUploadedDocumentListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
