package com.example.fastuae.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.AddOnsActivity;
import com.example.fastuae.databinding.ActivityChooseExtraListBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.LoadImage;
import com.example.fastuae.util.RemoveHtml;

import java.util.ArrayList;
import java.util.List;

public class ChooseExtrasAdapter extends RecyclerView.Adapter<ChooseExtrasAdapter.viewHolder> {

    private Context context;
    private List<ChooseExtrasModel> chooseExtrasModelList;
    private Session session;

    public interface onClick {
        void cancelBooking(String quantity,BookingModel model);
    }

    public ChooseExtrasAdapter(Context context, List<ChooseExtrasModel> chooseExtrasModelList) {
        this.context = context;
        this.chooseExtrasModelList = chooseExtrasModelList;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityChooseExtraListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_choose_extra_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ChooseExtrasModel model = chooseExtrasModelList.get(position);
        int target = 0;

        if (!TextUtils.isEmpty(model.getMax_quantity()) && !model.getMax_quantity().equals("null")) {
            target = Integer.parseInt(model.getMax_quantity());
        }

        int finalTarget = target;
        holder.binding.txtName.setText(model.getTitle());
        holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
        holder.binding.txtPrice.setText(context.getResources().getString(R.string.aed) + " " + model.getPrice());

        if (model.getDescription().length()>100){
            holder.binding.txtDesc.setMaxLines(2);
            holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
            holder.binding.txtMore.setVisibility(View.VISIBLE);
            holder.binding.txtLess.setVisibility(View.GONE);
        }else {
            holder.binding.txtMore.setVisibility(View.GONE);
            holder.binding.txtLess.setVisibility(View.GONE);
            holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
        }

        if (model.getTitle().contains("Baby Seater")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_baby_seat));
        }else if (model.getTitle().contains("GPS")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_gps));
        }else if (model.getTitle().contains("Additional Driver")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_driver));
        }else {
            holder.binding.imgExtra.setVisibility(View.GONE);
        }

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (finalTarget >1){
                        holder.binding.lnrQuantity.setVisibility(View.VISIBLE);
                    }else {
                        holder.binding.lnrQuantity.setVisibility(View.GONE);
                    }
                    AddOnsActivity.addOnsList.add(model);
                }else {
                    holder.binding.lnrQuantity.setVisibility(View.GONE);
                    for (int i=0; i<AddOnsActivity.addOnsList.size(); i++){
                        String title = AddOnsActivity.addOnsList.get(i).getTitle();
                        if (title.equals(model.getTitle())){
                            AddOnsActivity.addOnsList.remove(i);
                            holder.binding.txtCount.setText("1");
                        }
                    }
                }

            }
        });

        model.setQuantity("1");
        holder.binding.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                int countValue = H.getInt(holder.binding.txtCount.getText().toString());
                if(countValue< finalTarget){
                    int itemCount = countValue + 1;
                    holder.binding.txtCount.setText(itemCount + "");
                    model.setQuantity(itemCount + "");
                }else {
                    H.showMessage(context,context.getResources().getString(R.string.noMoreQty));
                }
            }
        });

        holder.binding.imhMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                int countValue = H.getInt(holder.binding.txtCount.getText().toString());
                if (countValue > 1) {
                    int itemCount = countValue - 1;
                    holder.binding.txtCount.setText(itemCount + "");
                    model.setQuantity(itemCount + "");
                }
            }
        });

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.txtMore.setVisibility(View.GONE);
                holder.binding.txtLess.setVisibility(View.VISIBLE);
                holder.binding.txtDesc.setMaxLines(5000);
                holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
            }
        });

        holder.binding.txtLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.txtMore.setVisibility(View.VISIBLE);
                holder.binding.txtLess.setVisibility(View.GONE);
                holder.binding.txtDesc.setMaxLines(2);
                holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return chooseExtrasModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityChooseExtraListBinding binding;

        public viewHolder(@NonNull ActivityChooseExtraListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
