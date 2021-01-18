package com.example.fastuae.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
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
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarFleetListBinding;
import com.example.fastuae.model.CarFleetModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

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

        holder.binding.imgCar.setImageResource(model.getImage());
        holder.binding.txtCarName.setText(model.getCarName());
        holder.binding.txtCarGroup.setText(model.getGroupName());
        holder.binding.txtCarGroup.setText(model.getGroupName());

        holder.binding.txtSeat.setText("5 Seat");
        holder.binding.txtAutomatic.setText("Automatic");
        holder.binding.txtDoor.setText("3 Door");
        holder.binding.txtPassengerOne.setText("Passenger");
        holder.binding.txtFuel.setText("Petrol");
        holder.binding.txtSuitcaseOne.setText("2 Suitcase");
        holder.binding.txtEngine.setText("Engine");
        holder.binding.txtSuitcaseTwo.setText("2 Suitcase");
        holder.binding.txtSuitcaseThree.setText("2 Suitcase");
        holder.binding.txtPassengerTwo.setText("5 Passenger");

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        holder.binding.txtLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
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

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                flipCard(holder.binding.cardView,holder.binding.cardInstruction);
            }
        });

        holder.binding.txtLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                flipCard(holder.binding.cardInstruction,holder.binding.cardView);
            }
        });

        if (session.getString(P.languageFlag).equals(Config.ARABIC)){

            holder.binding.txtSeat.setGravity(Gravity.RIGHT);
            holder.binding.txtAutomatic.setGravity(Gravity.RIGHT);
            holder.binding.txtDoor.setGravity(Gravity.RIGHT);
            holder.binding.txtPassengerOne.setGravity(Gravity.RIGHT);
            holder.binding.txtFuel.setGravity(Gravity.RIGHT);
            holder.binding.txtSuitcaseOne.setGravity(Gravity.RIGHT);
            holder.binding.txtEngine.setGravity(Gravity.RIGHT);
            holder.binding.txtSuitcaseTwo.setGravity(Gravity.RIGHT);
            holder.binding.txtSuitcaseThree.setGravity(Gravity.RIGHT);
            holder.binding.txtPassengerTwo.setGravity(Gravity.RIGHT);
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
