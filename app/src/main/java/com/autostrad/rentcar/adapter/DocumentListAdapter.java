package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.CarBookingDetailsActivity;
import com.autostrad.rentcar.activity.DocumentEditActivity;
import com.autostrad.rentcar.databinding.ActivityDocumentDetailsListBinding;
import com.autostrad.rentcar.fragment.DocumentFragment;
import com.autostrad.rentcar.model.DocumentUploadedModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.viewHolder> {

    private Context context;
    private List<DocumentUploadedModel> documentModelList;
    private DocumentFragment fragment;
    boolean fromActivity;

    public DocumentListAdapter(Context context, List<DocumentUploadedModel> documentModelList) {
        this.context = context;
        this.documentModelList = documentModelList;
        fromActivity = true;
    }

    public DocumentListAdapter(Context context, List<DocumentUploadedModel> documentModelList,DocumentFragment fragment) {
        this.context = context;
        this.documentModelList = documentModelList;
        this.fragment = fragment;
        fromActivity = false;
    }

    public interface onClick{
        void documentView(String imagePath);
    }

    public interface onClickDelete{
        void documentDelete(DocumentUploadedModel model);
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

        holder.binding.txtDocument.setText(title.trim());
        holder.binding.txtDetails.setText(details.trim());

        holder.binding.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (fromActivity){
                    ((CarBookingDetailsActivity)context).documentView(model.getImage_url());
                }else {
                    ((DocumentFragment)fragment).documentView(model.getImage_url());
                }
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

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (fromActivity){
//                    ((CarBookingDetailsActivity)context).documentDelete(model.getImage_url());
                }else {
                    ((DocumentFragment)fragment).documentDelete(model);
                }
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
