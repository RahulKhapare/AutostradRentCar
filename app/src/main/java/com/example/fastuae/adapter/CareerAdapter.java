package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCareerListBinding;
import com.example.fastuae.model.CareerModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

import java.util.List;

public class CareerAdapter extends RecyclerView.Adapter<CareerAdapter.viewHolder> {

    private Context context;
    private List<CareerModel> careerModelList;
    private Session session;

    public CareerAdapter(Context context, List<CareerModel> careerModelList) {
        this.context = context;
        this.careerModelList = careerModelList;
        session = new Session(context);
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCareerListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_career_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CareerModel model = careerModelList.get(position);

        holder.binding.txtLocation.setText(model.getLocation());
        holder.binding.txtExperence.setText(model.getExperience());
        holder.binding.txtDetails.setText(model.getDetails());

        if (session.getString(P.languageFlag).equals(Config.ARABIC)){

        }

    }

    @Override
    public int getItemCount() {
        return careerModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCareerListBinding binding;
        public viewHolder(@NonNull ActivityCareerListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setClipboard(Context context, String text) {
        try {
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }
        }catch (Exception e){
        }
    }
}
