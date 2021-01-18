package com.example.fastuae.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.example.fastuae.activity.CarDetailOneActivity;
import com.example.fastuae.databinding.ActivityCarGreedListBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

import java.util.List;

public class CarGridAdapter extends RecyclerView.Adapter<CarGridAdapter.viewHolder> {

    private Context context;
    private List<CarModel> carModelList;
    private Session session;

    public CarGridAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = carModelList;
        session = new Session(context);
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

        holder.binding.txtPetrolInst.setText("Petrol");
        holder.binding.txtSeatInst.setText("5 Seat");
        holder.binding.txtAutomaticInst.setText("Automatic");
        holder.binding.txtEnginInst.setText("Engine");
        holder.binding.txtDoorInst.setText("3 Door");
        holder.binding.txtPassengerInst.setText("5 Passengers");
        holder.binding.txtSuitcaseInst.setText("2 Suitcases");
        holder.binding.txtPassengerTwoInst.setText("5 Passengers");
        holder.binding.txtSuitcaseTwoInst.setText("2 Suitcases");
        holder.binding.txtPassengerThreeInst.setText("2 Suitcases");

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
                jumpToCardDetails();
            }
        });

        holder.binding.txtPayLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails();
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

            holder.binding.txtPetrolInst.setGravity(Gravity.RIGHT);
            holder.binding.txtSeatInst.setGravity(Gravity.RIGHT);
            holder.binding.txtAutomaticInst.setGravity(Gravity.RIGHT);
            holder.binding.txtEnginInst.setGravity(Gravity.RIGHT);
            holder.binding.txtDoorInst.setGravity(Gravity.RIGHT);
            holder.binding.txtPassengerInst.setGravity(Gravity.RIGHT);
            holder.binding.txtSuitcaseInst.setGravity(Gravity.RIGHT);
            holder.binding.txtPassengerTwoInst.setGravity(Gravity.RIGHT);
            holder.binding.txtSuitcaseTwoInst.setGravity(Gravity.RIGHT);
            holder.binding.txtPassengerThreeInst.setGravity(Gravity.RIGHT);

        }

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

    private void jumpToCardDetails(){
        Intent intent = new Intent(context, CarDetailOneActivity.class);
        context.startActivity(intent);
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
