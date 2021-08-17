package com.autostrad.rentcar.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.CarFleetDetailsActivity;
import com.autostrad.rentcar.databinding.ActivityCarFleetListBinding;
import com.autostrad.rentcar.model.CarFleetModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;

import java.util.List;

public class CarFleetAdapter extends RecyclerView.Adapter<CarFleetAdapter.viewHolder> {

    private Context context;
    private List<CarFleetModel> carFleetModelList;
    private Session session;

    public CarFleetAdapter(Context context, List<CarFleetModel> carFleetModelList) {
        this.context = context;
        this.carFleetModelList = carFleetModelList;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarFleetListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_fleet_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CarFleetModel model = carFleetModelList.get(position);

        LoadImage.glideString(context,holder.binding.imgCar,model.getCar_image());
        holder.binding.txtCarName.setText(model.getCar_name());

        holder.binding.txtSuitcase.setText(model.getSuitcase());
        holder.binding.txtAutomatic.setText(model.getTransmission_name());
        holder.binding.txtDoor.setText(model.getDoor());
        holder.binding.txtFuil.setText(model.getFuel_type_name());

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, CarFleetDetailsActivity.class);
                intent.putExtra(Config.CAR_ID,model.getId());
                context.startActivity(intent);
            }
        });

        holder.binding.imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.imgLike.setVisibility(View.VISIBLE);
                holder.binding.imgDisLike.setVisibility(View.GONE);
            }
        });

        holder.binding.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.imgLike.setVisibility(View.GONE);
                holder.binding.imgDisLike.setVisibility(View.VISIBLE);
            }
        });


        if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            holder.binding.txtSuitcase.setGravity(Gravity.RIGHT);
            holder.binding.txtAutomatic.setGravity(Gravity.RIGHT);
            holder.binding.txtDoor.setGravity(Gravity.RIGHT);
            holder.binding.txtFuil.setGravity(Gravity.RIGHT);
        }

    }

    @Override
    public int getItemCount() {
        return carFleetModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarFleetListBinding binding;
        public viewHolder(@NonNull ActivityCarFleetListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void flipCard(CardView cardViewOne, CardView cardViewTwo){
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardViewOne, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardViewOne, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (cardViewOne.getVisibility()==View.VISIBLE){
                    cardViewOne.setVisibility(View.GONE);
                    cardViewTwo.setVisibility(View.VISIBLE);
                }else if (cardViewTwo.getVisibility()==View.VISIBLE){
                    cardViewTwo.setVisibility(View.GONE);
                    cardViewOne.setVisibility(View.VISIBLE);
                }
                oa2.start();
            }
        });
        oa1.start();
    }
}
