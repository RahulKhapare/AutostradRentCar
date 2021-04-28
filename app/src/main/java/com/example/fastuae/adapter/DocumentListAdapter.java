package com.example.fastuae.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.example.fastuae.model.DocumentUploadedModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.P;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.viewHolder> {

    private Context context;
    private List<DocumentUploadedModel> documentModelList;

    public DocumentListAdapter(Context context, List<DocumentUploadedModel> documentModelList) {
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
        DocumentUploadedModel model = documentModelList.get(position);

        String details = "";

        if (!TextUtils.isEmpty(model.getId_number()) && !model.getId_number().equals("null")){
            details = context.getResources().getString(R.string.number)+ " - " + model.getId_number();
        }

        if (!TextUtils.isEmpty(model.getEntry_stamp()) && !model.getEntry_stamp().equals("null")){
            details = details  + "\n" + context.getResources().getString(R.string.entryStamp)+ " - " + model.getEntry_stamp();
        }

        if (!TextUtils.isEmpty(model.getIssue_date()) && !model.getIssue_date().equals("null")){
            details = details  + "\n" + context.getResources().getString(R.string.issuedate) + " - " + model.getIssue_date().replace("-","/");
        }

        if (!TextUtils.isEmpty(model.getExpiry()) && !model.getExpiry().equals("null")){
            details = details  + "\n" + context.getResources().getString(R.string.expiryDate) + " - " + model.getExpiry().replace("-","/");
        }

        String title = model.getDocument_key();
        title = title.replace("_"," ");
        title = capitalize(title);

        holder.binding.txtDocument.setText(title);
        holder.binding.txtDetails.setText(details);

        holder.binding.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((CarBookingDetailsActivity)context).documentView(model.getImage_url());
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

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
