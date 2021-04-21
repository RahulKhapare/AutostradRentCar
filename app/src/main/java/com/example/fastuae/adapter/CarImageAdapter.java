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
import com.example.fastuae.databinding.ActivityImageListBinding;
import com.example.fastuae.model.CarImageModel;
import com.example.fastuae.util.LoadImage;

import java.util.List;

public class CarImageAdapter extends RecyclerView.Adapter<CarImageAdapter.viewHolder> {

    private Context context;
    private List<CarImageModel> carImageModelList;
    private Session session;
    private ImageView imgCar;
    private int flag;

    public interface onClick {
        void imageClick(String imagePath);
    }

    public CarImageAdapter(Context context, List<CarImageModel> carImageModelList, ImageView imgCar,int flag) {
        this.context = context;
        this.carImageModelList = carImageModelList;
        this.imgCar = imgCar;
        this.flag = flag;
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

        if (flag==1){
            holder.binding.lnr1.setVisibility(View.VISIBLE);
            holder.binding.lnr2.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(model.getImage())){
                LoadImage.glideString(context,holder.binding.img1,model.getImage());
            }
            holder.binding.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(model.getImage())){
                        LoadImage.glideString(context,imgCar,model.getImage());
                    }
                }
            });
        }else if (flag==2){
            holder.binding.lnr2.setVisibility(View.VISIBLE);
            holder.binding.lnr1.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(model.getImage())){
                LoadImage.glideString(context,holder.binding.img2,model.getImage());
            }
            holder.binding.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(model.getImage())){
                        LoadImage.glideString(context,imgCar,model.getImage());
                    }
                }
            });
        }

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
