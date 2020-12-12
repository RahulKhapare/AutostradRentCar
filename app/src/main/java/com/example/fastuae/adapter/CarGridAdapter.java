package com.example.fastuae.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarGreedListBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.Click;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarGridAdapter extends RecyclerView.Adapter<CarGridAdapter.viewHolder> {

    private Context context;
    private List<CarModel> carModelList;

    public CarGridAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = carModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarGreedListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_greed_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CarModel model = carModelList.get(position);

//        Picasso.get().load(R.drawable.ic_car_four).into(holder.binding.imgCard);
        holder.binding.txtCarName.setText(model.getName());
        holder.binding.txtModel.setText(model.getModel());
        holder.binding.txtCarGroup.setText(model.getGroup());
        holder.binding.txtType.setText(model.getType());
        holder.binding.txtSeat.setText(model.getSeat());
        holder.binding.txtEngine.setText(model.getEngine());
        holder.binding.txtDoor.setText(model.getDore());
        holder.binding.txtPayNow.setText(context.getResources().getString(R.string.payNow) +  " - " + model.getAedNow());
        holder.binding.txtPayLatter.setText(context.getResources().getString(R.string.payLater) +  " - " + model.getAedLater());

        String more = context.getResources().getString(R.string.more);
        SpannableString content = new SpannableString(more);
        content.setSpan(new UnderlineSpan(), 0, more.length(), 0);
        holder.binding.txtMore.setText(content);

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        holder.binding.txtPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        holder.binding.txtPayLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarGreedListBinding binding;
        public viewHolder(@NonNull ActivityCarGreedListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
