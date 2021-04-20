package com.example.fastuae.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityAddOnsListBinding;
import com.example.fastuae.databinding.ActivityImageListBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.model.CarImageModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CarImageAdapter extends RecyclerView.Adapter<CarImageAdapter.viewHolder> {

    private Context context;
    private List<CarImageModel> carImageModelList;
    private Session session;
    private ImageView imgCar;

    public interface onClick {
        void imageClick(String imagePath);
    }

    public CarImageAdapter(Context context, List<CarImageModel> carImageModelList, ImageView imgCar) {
        this.context = context;
        this.carImageModelList = carImageModelList;
        this.imgCar = imgCar;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityImageListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_image_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CarImageModel model = carImageModelList.get(position);
        if (!TextUtils.isEmpty(model.getImage())){
            Picasso.get().load(model.getImage()).into(holder.binding.img);
        }
        holder.binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(model.getImage())){
                    Picasso.get().load(model.getImage()).into(imgCar);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return carImageModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityImageListBinding binding;

        public viewHolder(@NonNull ActivityImageListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
