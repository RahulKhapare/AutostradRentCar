package com.example.fastuae.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityUploadedDocumentListBinding;
import com.example.fastuae.fragment.AdditionalDriverDocumentFragment;
import com.example.fastuae.model.UploadedDocumentModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.LoadImage;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadedDocumentAdapter extends RecyclerView.Adapter<UploadedDocumentAdapter.viewHolder> {

    private Context context;
    private List<UploadedDocumentModel> documentModelList;
    private AdditionalDriverDocumentFragment fragment;


    public UploadedDocumentAdapter(Context context, List<UploadedDocumentModel> documentModelList, AdditionalDriverDocumentFragment fragment) {
        this.context = context;
        this.documentModelList = documentModelList;
        this.fragment = fragment;

    }

    public interface onClick {
        void documentUploadedEdit(UploadedDocumentModel model,int position);
        void documentUploadedDelete(UploadedDocumentModel model);
        void documentUploadedView(UploadedDocumentModel model);
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

        LoadImage.glideString(context,holder.binding.imgDocument,model.getImage_url());
        holder.binding.txtTitle.setText(checkString(model.getTitle()));
        holder.binding.txtNumber.setText(checkString(model.getId_number()));
        holder.binding.txtExpDate.setText(checkString(getFormatDate(model.getExpiry())));

        holder.binding.imgDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverDocumentFragment)fragment).documentUploadedView(model);
            }
        });

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverDocumentFragment)fragment).documentUploadedEdit(model,position);
            }
        });

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((AdditionalDriverDocumentFragment)fragment).documentUploadedDelete(model);
            }
        });
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

    private String checkString(String string){
        String value = "";
        if (string!=null && !string.equals("null") && !string.equals("")){
            value = string;
        }
        return value;
    }

    private String getFormatDate(String actualDate){
        String app_date = "";
        try {
            app_date = actualDate;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter.parse(app_date);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
            String finalString = newFormat.format(date);
            app_date = finalString;
        }catch (Exception e){
            app_date = actualDate;
        }

        return app_date;
    }

}
