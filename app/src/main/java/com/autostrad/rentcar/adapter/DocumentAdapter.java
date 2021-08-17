package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.CarBookingDetailsActivity;
import com.autostrad.rentcar.activity.DocumentEditActivity;
import com.autostrad.rentcar.databinding.ActivityDocumentListBinding;
import com.autostrad.rentcar.fragment.DocumentFragment;
import com.autostrad.rentcar.model.DocumentModel;
import com.autostrad.rentcar.model.FieldModel;
import com.autostrad.rentcar.util.Click;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.viewHolder> {

    private Context context;
    private List<DocumentModel> documentModelList;
    private DocumentFragment fragment;
    boolean fromActivity;
    int flagValue;

    public interface onClick{
        void downloadDocument(String name, TextView textView,TextView txtImagePath);
    }

    public DocumentAdapter(Context context, List<DocumentModel> documentModelList,DocumentFragment fragment) {
        this.context = context;
        this.documentModelList = documentModelList;
        this.fragment = fragment;
        fromActivity = false;
    }

    public DocumentAdapter(Context context, List<DocumentModel> documentModelList,int flag) {
        this.context = context;
        this.documentModelList = documentModelList;
        fromActivity = true;
        flagValue = flag;
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

        holder.binding.txtTitle.setText(model.getTitle());
        holder.binding.txtDocument.setText(context.getResources().getString(R.string.upload)+" "+model.getTitle());

        holder.binding.lnrCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.binding.checkBox.isChecked()){
                    holder.binding.checkBox.setChecked(false);
                    model.setCheckValue("0");
                }else {
                    model.setCheckValue("1");
                    holder.binding.checkBox.setChecked(true);
                }
            }
        });

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    model.setCheckValue("1");
                }else {
                    model.setCheckValue("0");
                }
            }
        });

        holder.binding.lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (fromActivity){
                    if (flagValue==1){
                        ((CarBookingDetailsActivity)context).downloadDocument(model.getTitle(),holder.binding.txtDocument,holder.binding.txtImagePath);
                    }else if (flagValue==2){
                        ((DocumentEditActivity)context).downloadDocument(model.getTitle(),holder.binding.txtDocument,holder.binding.txtImagePath);
                    }
                }else {
                    ((DocumentFragment)fragment).downloadDocument(model.getTitle(),holder.binding.txtDocument,holder.binding.txtImagePath);
                }
            }
        });

        List<FieldModel> fieldList = new ArrayList<>();
        try {
            for (int i=0; i<model.getField().length(); i++){
                String value = model.getField().getString(i);
                if (!value.equals("image")){
                    fieldList.add(new FieldModel(model.getField().getString(i),new Json()));
                }
            }
        }catch (Exception e){
        }
        model.setFieldList(fieldList);
        DocumentFieldAdapter documentFieldAdapter = new DocumentFieldAdapter(context,model.getFieldList(),new Json(),model.getSave_data(),1);
        holder.binding.recyclerExtraFields.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerExtraFields.setHasFixedSize(true);
        holder.binding.recyclerExtraFields.setNestedScrollingEnabled(true);
        holder.binding.recyclerExtraFields.setAdapter(documentFieldAdapter);

        if (position==documentModelList.size()-1){
            holder.binding.lnrView.setVisibility(View.GONE);
        }else {
            holder.binding.lnrView.setVisibility(View.VISIBLE);
        }

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
