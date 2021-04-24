package com.example.fastuae.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityDocumentListBinding;
import com.example.fastuae.fragment.DocumentFragment;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.model.FieldModel;
import com.example.fastuae.util.Click;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.viewHolder> {

    private Context context;
    private List<DocumentModel> documentModelList;
    private DocumentFragment fragment;

    public interface onClick{
        void downloadDocument(String name);
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


        holder.binding.txtTitle.setText(model.getTitle());
        holder.binding.txtDocument.setText(context.getResources().getString(R.string.upload)+" "+model.getTitle());

        holder.binding.lnrCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.binding.checkBox.isChecked()){
                    holder.binding.checkBox.setChecked(false);

                }else {
                    holder.binding.checkBox.setChecked(true);
                }

            }
        });
        holder.binding.lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
//                ((DocumentFragment)fragment).downloadDocument(model.getTitle());
            }
        });


        List<FieldModel> fieldList = new ArrayList<>();
        try {
            for (int i=0; i<model.getField().length(); i++){
                String value = model.getField().getString(i);
                if (!value.equals("image")){
                    fieldList.add(new FieldModel(model.getField().getString(i)));
                }
            }
        }catch (Exception e){
        }

        DocumentFieldAdapter documentFieldAdapter = new DocumentFieldAdapter(context,fieldList);
        holder.binding.recyclerExtraFields.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerExtraFields.setHasFixedSize(true);
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
