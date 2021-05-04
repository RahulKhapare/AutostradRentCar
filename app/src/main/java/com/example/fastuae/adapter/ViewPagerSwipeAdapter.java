package com.example.fastuae.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.adoisstudio.helper.Json;
import com.example.fastuae.R;
import com.example.fastuae.activity.AddOnsActivity;
import com.example.fastuae.activity.CarDetailOneActivity;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.model.CarImageModel;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.LoadImage;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerSwipeAdapter extends PagerAdapter {
    private List<CarModel> carModelList;
    private Context context;

    public ViewPagerSwipeAdapter(Context context,List<CarModel> carModelList){
        this.context = context;
        this.carModelList = carModelList;
    }

    @Override
    public int getCount() {
        return carModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_car_card_list,container,false);
        container.addView(view);

        CarModel  model = carModelList.get(position);

        TextView txtCarName = view.findViewById(R.id.txtCarName);
        TextView txtGroup = view.findViewById(R.id.txtGroup);
        ImageView imgCar = view.findViewById(R.id.imgCar);
        TextView txtSUV = view.findViewById(R.id.txtSUV);
        TextView txtPayLatter = view.findViewById(R.id.txtPayLatter);
        TextView txtPayNow = view.findViewById(R.id.txtPayNow);

        TextView txtFuel = view.findViewById(R.id.txtFuel);
        TextView txtMode = view.findViewById(R.id.txtMode);
        TextView txtSeat = view.findViewById(R.id.txtSeat);
        TextView txtDoor = view.findViewById(R.id.txtDoor);
        TextView txtPassenger = view.findViewById(R.id.txtPassenger);
        TextView txtSuitcase = view.findViewById(R.id.txtSuitcase);
        TextView txtAirBag = view.findViewById(R.id.txtAirBag);
        TextView txtAirConditionar = view.findViewById(R.id.txtAirConditionar);
        TextView txtParkingSensor = view.findViewById(R.id.txtParkingSensor);


        LinearLayout lnrAEDMonth = view.findViewById(R.id.lnrAEDMonth);
        TextView txtMonthAED = view.findViewById(R.id.txtMonthAED);
        TextView txtMonthAEDFor = view.findViewById(R.id.txtMonthAEDFor);
        RecyclerView recyclerCarImage = view.findViewById(R.id.recyclerCarImage);

//        Picasso.get().load(R.drawable.ic_car_four).into(imgCar);
//        Picasso.get().load(R.drawable.ic_view_one).into(img1);
//        Picasso.get().load(R.drawable.ic_view_two).into(img2);
//        Picasso.get().load(R.drawable.ic_view_three).into(img3);

        LoadImage.glideString(context,imgCar,model.getCar_image());
        txtCarName.setText(model.getCar_name());
        txtGroup.setText(context.getResources().getString(R.string.group) + " " + model.getGroup_name());
        txtSUV.setText(model.getCategory_name());

        txtFuel.setText(checkString(model.getFuel_type_name()));
        txtMode.setText(checkString(model.getTransmission_name()));
        txtSeat.setText(checkString("Seats"));
        txtDoor.setText(checkString(model.getDoor()));
        txtPassenger.setText(checkString(model.getPassenger()));
        txtSuitcase.setText(checkString(model.getSuitcase()));

        String airBags = model.getAir_bags();
        if (TextUtils.isEmpty(airBags) || airBags.equals("null") || airBags.equals("0")){
            airBags = "Air Bags";
            txtAirBag.setPaintFlags(txtAirBag.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else if (airBags.equals("1")){
            airBags = "Air Bags";
        }
        txtAirBag.setText(checkString(airBags));

        String airCondition = model.getAir_conditioner();
        if (TextUtils.isEmpty(airCondition) || airCondition.equals("null") || airCondition.equals("0")){
            airCondition = "Air Condition";
            txtAirConditionar.setPaintFlags(txtAirConditionar.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            airCondition = "Air Condition";
        }
        txtAirConditionar.setText(checkString(airCondition));

        String parkingSensor = model.getParking_sensors();
        if (TextUtils.isEmpty(parkingSensor) || parkingSensor.equals("null") || parkingSensor.equals("0")){
            parkingSensor = "Parking Sensor";
            txtParkingSensor.setPaintFlags(txtParkingSensor.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            parkingSensor = "Parking Sensor";
        }
        txtParkingSensor.setText(checkString(parkingSensor));

        if(SelectCarActivity.bookingTYpe.equals(Config.daily)){
            lnrAEDMonth.setVisibility(View.GONE);
            txtPayNow.setText(context.getResources().getString(R.string.payNow) + "\n" + model.getPay_now_rate() + " AED");
            txtPayLatter.setText(context.getResources().getString(R.string.payLater) + "\n" + model.getPay_later_rate() + " AED");
        }else if(SelectCarActivity.bookingTYpe.equals(Config.monthly)){
            lnrAEDMonth.setVisibility(View.VISIBLE);
            txtMonthAED.setText(checkString(model.getPay_now_rate()) + " AED");
            txtMonthAEDFor.setText("("+context.getResources().getString(R.string.fore) +" "+ SelectCarActivity.monthDuration + " " + context.getResources().getString(R.string.month)+")");
            txtPayNow.setText(context.getResources().getString(R.string.payNow));
            txtPayLatter.setText(context.getResources().getString(R.string.payLater));
        }



        recyclerCarImage.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerCarImage.setHasFixedSize(true);
        List<CarImageModel> carImageModelList = new ArrayList<>();
        carImageModelList.add(new CarImageModel(model.getCar_image()));
        try{
            for (int i=0; i<model.getMore_car_image().length(); i++){
                CarImageModel carImageModel = new CarImageModel();
                carImageModel.setImage(model.getMore_car_image().getString(i));
                carImageModelList.add(carImageModel);
            }
        }catch (Exception e){

        }
        CarImageAdapter carImageAdapter = new CarImageAdapter(context,carImageModelList,imgCar,1);
        recyclerCarImage.setAdapter(carImageAdapter);

        txtPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails(model,Config.pay_now,model.getPay_now_rate());
            }
        });

        txtPayLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails(model,Config.pay_latter,model.getPay_later_rate());
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }

    private void jumpToCardDetails(CarModel model,String payType,String aedRate){
        Config.carModel = model;
        Intent intent = new Intent(context, AddOnsActivity.class);
        intent.putExtra(Config.PAY_TYPE,payType);
        intent.putExtra(Config.SELECTED_AED,aedRate);
        context.startActivity(intent);
    }

    private String checkString(String string){
        String value = string;

        if (TextUtils.isEmpty(string) || string.equals("null")){
            value = "";
        }
        return value;
    }

}
