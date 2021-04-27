package com.example.fastuae.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.activity.CarBookingDetailsActivity;
import com.example.fastuae.activity.DocumentEditActivity;
import com.example.fastuae.databinding.ActivityDocumentDetailsListBinding;
import com.example.fastuae.databinding.ActivityDocumentListBinding;
import com.example.fastuae.fragment.DocumentFragment;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.P;

import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.viewHolder> {

    private Context context;
    private List<DocumentModel> documentModelList;
    String documentPath = "https://media.itpro.co.uk/image/upload/v1570817631/itpro/2019/09/document_shutterstock_1416401996.jpg";

    public DocumentListAdapter(Context context, List<DocumentModel> documentModelList) {
        this.context = context;
        this.documentModelList = documentModelList;
    }

    public interface onClick{
        void documentView(String imagePath);
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
                ((CarBookingDetailsActivity)context).documentView(documentPath);
            }
        });

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, DocumentEditActivity.class);
                context.startActivity(intent);
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
