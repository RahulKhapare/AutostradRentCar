package com.example.fastuae.adapter;

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
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityPendingDocumentListBinding;
import com.example.fastuae.fragment.AdditionalDriverDocumentFragment;
import com.example.fastuae.model.FieldModel;
import com.example.fastuae.model.PendingDocumentModel;
import com.example.fastuae.util.Click;

import java.util.ArrayList;
import java.util.List;

public class PendingDocumentAdapter extends RecyclerView.Adapter<PendingDocumentAdapter.viewHolder> {

    private Context context;
    private List<PendingDocumentModel> documentModelList;
    private AdditionalDriverDocumentFragment fragment;


    public interface onClick{
        void downloadDocument(String name, TextView textView,TextView txtImagePath);
    }

    public PendingDocumentAdapter(Context context, List<PendingDocumentModel> documentModelList, AdditionalDriverDocumentFragment fragment) {
        this.context = context;
        this.documentModelList = documentModelList;
        this.fragment = fragment;

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityPendingDocumentListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_pending_document_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PendingDocumentModel model = documentModelList.get(position);

        holder.binding.txtTitle.setText(model.getTitle());
//        holder.binding.txtDocument.setText(context.getResources().getString(R.string.upload)+" "+model.getTitle());

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
                ((AdditionalDriverDocumentFragment)fragment).downloadDocument(model.getTitle(),holder.binding.txtDocument,holder.binding.txtImagePath);
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
        DocumentFieldAdapter documentFieldAdapter = new DocumentFieldAdapter(context,model.getFieldList(),new Json(),model.getSave_data(),2);
        holder.binding.recyclerExtraFields.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerExtraFields.setHasFixedSize(true);
        holder.binding.recyclerExtraFields.setNestedScrollingEnabled(true);
        holder.binding.recyclerExtraFields.setAdapter(documentFieldAdapter);

    }

    @Override
    public int getItemCount() {
        return documentModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityPendingDocumentListBinding binding;
        public viewHolder(@NonNull ActivityPendingDocumentListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
