package com.autostrad.rentcar.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.TextUtils;
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
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.AddOnsActivity;
import com.autostrad.rentcar.databinding.ActivityCarGreedListBinding;
import com.autostrad.rentcar.model.CarModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;

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

        LoadImage.glideString(context,holder.binding.imgCar,model.getCar_image());
        holder.binding.txtCarName.setText(model.getCar_name());
        holder.binding.txtModel.setText(model.getCategory_name());
        holder.binding.txtCarGroup.setText(context.getResources().getString(R.string.group) + " " +model.getGroup_name());
        holder.binding.txtType.setText(model.getTransmission_name());
        holder.binding.txtPassenger.setText(model.getPassenger());
        holder.binding.txtSuitcase.setText(model.getSuitcase());
        holder.binding.txtDoor.setText(model.getDoor());
        holder.binding.txtPayNow.setText(context.getResources().getString(R.string.payNow) +  " - " + model.getPay_now_rate() + " AED");
        holder.binding.txtPayLatter.setText(context.getResources().getString(R.string.payLater) +  " - " + model.getPay_later_rate() + " AED");

        holder.binding.txtPetrolInst.setText(checkString(model.getFuel_type_name()));
        holder.binding.txtSeatInst.setText("Seat");
        holder.binding.txtAutomaticInst.setText(checkString(model.getTransmission_name()));
        holder.binding.txtDoorInst.setText(checkString(model.getDoor()));
        holder.binding.txtPassengerInst.setText(checkString(model.getPassenger()));
        holder.binding.txtSuitcaseInst.setText(checkString(model.getSuitcase()));

        String airBags = model.getAir_bags();
        if (TextUtils.isEmpty(airBags) || airBags.equals("null") || airBags.equals("0")){
            airBags = "Air Bags";
            holder.binding.txtAirBags.setPaintFlags(holder.binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else if (airBags.equals("1")){
            airBags = "Air Bags";
        }
        holder.binding.txtAirBags.setText(checkString(airBags));

        String airCondition = model.getAir_conditioner();
        if (TextUtils.isEmpty(airCondition) || airCondition.equals("null") || airCondition.equals("0")){
            airCondition = "Air Condition";
            holder.binding.txtAirConditionar.setPaintFlags(holder.binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            airCondition = "Air Condition";
        }
        holder.binding.txtAirConditionar.setText(checkString(airCondition));

        String parkingSensor = model.getParking_sensors();
        if (TextUtils.isEmpty(parkingSensor) || parkingSensor.equals("null") || parkingSensor.equals("0")){
            parkingSensor = "Parking Sensor";
            holder.binding.txtParkinSensor.setPaintFlags(holder.binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            parkingSensor = "Parking Sensor";
        }
        holder.binding.txtParkinSensor.setText(checkString(parkingSensor));

        String camera = model.getRear_parking_camera();
        if (TextUtils.isEmpty(camera) || camera.equals("null") || camera.equals("0")){
            camera = "Parking Camera";
            holder.binding.txtRearParkingCamera.setPaintFlags(holder.binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            camera = "Parking Camera";
        }
        holder.binding.txtRearParkingCamera.setText(checkString(camera));

        String bluetooth = model.getBluetooth();
        if (TextUtils.isEmpty(bluetooth) || bluetooth.equals("null") || bluetooth.equals("0")){
            bluetooth = "Bluetooth";
            holder.binding.txtBluetooth.setPaintFlags(holder.binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            bluetooth = "Bluetooth";
        }

        holder.binding.txtBluetooth.setText(checkString(bluetooth));

        String control = model.getCruise_control();
        if (TextUtils.isEmpty(control) || control.equals("null") || control.equals("0")){
            control = "Cruise Control";
            holder.binding.txtCruiseControl.setPaintFlags(holder.binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            control = "Cruise Control";
        }
        holder.binding.txtCruiseControl.setText(checkString(control));

        String more = context.getResources().getString(R.string.more);
        SpannableString content = new SpannableString(more);
        content.setSpan(new UnderlineSpan(), 0, more.length(), 0);
        holder.binding.txtMore.setText(content);

        holder.binding.txtPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails(model,Config.pay_now,model.getPay_now_rate());
            }
        });

        holder.binding.txtPayLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails(model,Config.pay_latter,model.getPay_later_rate());
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
            holder.binding.txtDoorInst.setGravity(Gravity.RIGHT);
            holder.binding.txtPassengerInst.setGravity(Gravity.RIGHT);
            holder.binding.txtSuitcaseInst.setGravity(Gravity.RIGHT);
            holder.binding.txtAirBags.setGravity(Gravity.RIGHT);
            holder.binding.txtAirConditionar.setGravity(Gravity.RIGHT);
            holder.binding.txtParkinSensor.setGravity(Gravity.RIGHT);
            holder.binding.txtRearParkingCamera.setGravity(Gravity.RIGHT);
            holder.binding.txtBluetooth.setGravity(Gravity.RIGHT);
            holder.binding.txtCruiseControl.setGravity(Gravity.RIGHT);

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

    private void jumpToCardDetails(CarModel model,String payType,String aedRate){
        Config.carModel = model;
        Intent intent = new Intent(context, AddOnsActivity.class);
        intent.putExtra(Config.PAY_TYPE,payType);
        intent.putExtra(Config.SELECTED_AED,aedRate);
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

    private String checkString(String string){
        String value = string;

        if (TextUtils.isEmpty(string) || string.equals("null")){
            value = "";
        }
        return value;
    }
}
