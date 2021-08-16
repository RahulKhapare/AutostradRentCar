package com.example.fastuae.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        void documentUploadedEdit(UploadedDocumentModel model);
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

        try {
            String value = model.getField().getString(0);
            String hint = value.replace("_"," ");
            String upperString = hint.substring(0, 1).toUpperCase() + hint.substring(1).toLowerCase();
            holder.binding.txtNumTitle.setText(wordCapitalize(upperString) +":");
            if (model.getJsonAllData().has(value)){
                if (upperString.contains("date") || upperString.contains("Date") || upperString.contains("DATE")){
                    holder.binding.txtNumber.setText(checkString(getFormatDate(model.getJsonAllData().getString(value)),holder.binding.lnrNumber));
                }else {
                    holder.binding.txtNumber.setText(checkString(model.getJsonAllData().getString(value),holder.binding.lnrNumber));
                }
            }else {
                holder.binding.txtNumber.setText(checkString("",holder.binding.lnrNumber));
            }
        }catch (Exception e){
            holder.binding.txtNumber.setText(checkString("",holder.binding.lnrNumber));
        }

        holder.binding.txtExpDate.setText(checkString(getFormatDate(model.getExpiry()),holder.binding.lnrExpire));

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
                ((AdditionalDriverDocumentFragment)fragment).documentUploadedEdit(model);
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

    private String checkString(String string, LinearLayout lnr){
        String value = "";
        if (string!=null && !string.equals("null") && !string.equals("")){
            value = string;
        }else {
            lnr.setVisibility(View.GONE);
        }
        return value;
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

    public String wordCapitalize(String words)
    {

        String str = "";
        boolean isCap = false;

        for(int i = 0; i < words.length(); i++){

            if(isCap){
                str +=  words.toUpperCase().charAt(i);
            }else{
                if(i==0){
                    str +=  words.toUpperCase().charAt(i);
                }else {
                    str += words.toLowerCase().charAt(i);
                }
            }

            if(words.charAt(i)==' '){
                isCap = true;
            }else{
                isCap = false;
            }
        }
        return str;
    }
}
