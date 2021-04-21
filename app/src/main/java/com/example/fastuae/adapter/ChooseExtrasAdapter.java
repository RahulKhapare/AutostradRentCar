package com.example.fastuae.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.AddOnsActivity;
import com.example.fastuae.databinding.ActivityChooseExtraListBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.model.CountryCodeModel;
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

        holder.binding.txtName.setText(model.getTitle());
        holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
        holder.binding.txtPrice.setText("AED " + model.getPrice());


        if (model.getTitle().contains("Baby seater")){
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
                    AddOnsActivity.addOnsList.add(model);
                }else {
                    for (int i=0; i<AddOnsActivity.addOnsList.size(); i++){
                        String title = AddOnsActivity.addOnsList.get(i).getTitle();
                        if (title.equals(model.getTitle())){
                            AddOnsActivity.addOnsList.remove(i);
                        }
                    }
                }
            }
        });

        List<CountryCodeModel> countryCodeModelList = new ArrayList<>();

        if (!TextUtils.isEmpty(model.getMax_quantity()) && !model.getMax_quantity().equals("null")){
            int target = Integer.parseInt(model.getMax_quantity());
            for (int i=0; i<target; i++){
                int value = i+1;
                countryCodeModelList.add(new CountryCodeModel(value+""));
            }
        }

        CountryCodeAdapter adapterLogin = new CountryCodeAdapter(context, countryCodeModelList,3);
        holder.binding.spinnerQuantity.setAdapter(adapterLogin);

        holder.binding.spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel modelData = countryCodeModelList.get(position);
                String quantitySelected = modelData.getPhone_code();
                model.setQuantity(quantitySelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
