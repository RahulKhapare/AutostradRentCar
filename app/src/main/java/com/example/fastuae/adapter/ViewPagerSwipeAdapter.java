package com.example.fastuae.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.fastuae.R;
import com.example.fastuae.activity.CarDetailOneActivity;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.Click;
import com.squareup.picasso.Picasso;

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
        ImageView img1 = view.findViewById(R.id.img1);
        ImageView img2 = view.findViewById(R.id.img2);
        ImageView img3 = view.findViewById(R.id.img3);
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

//        Picasso.get().load(R.drawable.ic_car_four).into(imgCar);
//        Picasso.get().load(R.drawable.ic_view_one).into(img1);
//        Picasso.get().load(R.drawable.ic_view_two).into(img2);
//        Picasso.get().load(R.drawable.ic_view_three).into(img3);

        Picasso.get().load(model.getCar_image()).error(R.drawable.ic_image).into(imgCar);
        txtCarName.setText(model.getCar_name());
        txtGroup.setText(context.getResources().getString(R.string.group) + " " + model.getGroup_name());
        txtSUV.setText(model.getCategory_name());

        txtFuel.setText(checkString(model.getFuel_type_name()));
        txtMode.setText(checkString(model.getTransmission_name()));
        txtSeat.setText(checkString("Seats"));
        txtDoor.setText(checkString(model.getDoor()));
        txtPassenger.setText(checkString(model.getPassenger()));
        txtSuitcase.setText(checkString(model.getSuitcase()));
        txtAirBag.setText(checkString(model.getAir_bags()));
        txtAirConditionar.setText(checkString(model.getAir_conditioner()));
        txtParkingSensor.setText(checkString(model.getParking_sensors()));

        txtPayNow.setText(context.getResources().getString(R.string.payNow) + "\n" + model.getPay_now_rate() + " AED");
        txtPayLatter.setText(context.getResources().getString(R.string.payLater) + "\n" + model.getPay_later_rate() + " AED");

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) img1.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                imgCar.setImageBitmap(bitmap);

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) img2.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                imgCar.setImageBitmap(bitmap);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) img3.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                imgCar.setImageBitmap(bitmap);
            }
        });

        txtPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails();
            }
        });

        txtPayLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                jumpToCardDetails();
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }

    private void jumpToCardDetails(){
        Intent intent = new Intent(context, CarDetailOneActivity.class);
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
