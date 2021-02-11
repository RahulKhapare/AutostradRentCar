package com.example.fastuae.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CarUpgradeAdapter;
import com.example.fastuae.databinding.ActivityCarDetailOneBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.CarUpgradeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;
import com.example.fastuae.util.ZoomFadeTransformer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarDetailOneActivity extends AppCompatActivity {

    private CarDetailOneActivity activity = this;
    private ActivityCarDetailOneBinding binding;
    private Session session;
    private String flag;
    private int currentPosition = 0;
    private LoadingDialog loadingDialog;
    private String payType = "";
    private String aedSelected = "";
    private CarModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_detail_one);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);
        aedSelected = getIntent().getStringExtra(Config.SELECTED_AED);
        initView();
        updateIcons();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.carDetails));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        model = Config.carModel;
        hitCarExtrasData();
        setData();
        onClick();
    }

    private void setData() {



        hitBookingCarData(model);

        Picasso.get().load(model.getCar_image()).error(R.drawable.ic_image).into(binding.imgCar);
        binding.txtCarName.setText(model.getCar_name());
        String pickUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedPickUpDate);
        String dropUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedDropUpDate);

        binding.txtPickUpLocation.setText(pickUpDate+", "+Config.SelectedPickUpTime+",\n"+Config.SelectedPickUpAddress);
        binding.txtDropOffLocation.setText(dropUpDate+", "+Config.SelectedDropUpTime+",\n"+Config.SelectedDropUpAddress);

        binding.txtPetrolInst.setText(checkString(model.getFuel_type_name()));
        binding.txtSeatInst.setText("Seat");
        binding.txtAutomaticInst.setText(checkString(model.getTransmission_name()));
        binding.txtDoorInst.setText(checkString(model.getDoor()));
        binding.txtPassengerInst.setText(checkString(model.getPassenger()));
        binding.txtSuitcaseInst.setText(checkString(model.getSuitcase()));

        String airBags = model.getAir_bags();
        if (TextUtils.isEmpty(airBags) || airBags.equals("null") || airBags.equals("0")){
            airBags = "Air Bags";
            binding.txtAirBags.setPaintFlags(binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else if (airBags.equals("1")){
            airBags = "Air Bags";
        }
        binding.txtAirBags.setText(checkString(airBags));

        String airCondition = model.getAir_conditioner();
        if (TextUtils.isEmpty(airCondition) || airCondition.equals("null") || airCondition.equals("0")){
            airCondition = "Air Condition";
            binding.txtAirConditionar.setPaintFlags(binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            airCondition = "Air Condition";
        }
        binding.txtAirConditionar.setText(checkString(airCondition));

        String parkingSensor = model.getParking_sensors();
        if (TextUtils.isEmpty(parkingSensor) || parkingSensor.equals("null") || parkingSensor.equals("0")){
            parkingSensor = "Parking Sensor";
            binding.txtParkinSensor.setPaintFlags(binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            parkingSensor = "Parking Sensor";
        }
        binding.txtParkinSensor.setText(checkString(parkingSensor));

        String camera = model.getRear_parking_camera();
        if (TextUtils.isEmpty(camera) || camera.equals("null") || camera.equals("0")){
            camera = "Parking Camera";
            binding.txtRearParkingCamera.setPaintFlags(binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            camera = "Parking Camera";
        }
        binding.txtRearParkingCamera.setText(checkString(camera));

        String bluetooth = model.getBluetooth();
        if (TextUtils.isEmpty(bluetooth) || bluetooth.equals("null") || bluetooth.equals("0")){
            bluetooth = "Bluetooth";
            binding.txtBluetooth.setPaintFlags(binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            bluetooth = "Bluetooth";
        }

        binding.txtBluetooth.setText(checkString(bluetooth));

        String control = model.getCruise_control();
        if (TextUtils.isEmpty(control) || control.equals("null") || control.equals("0")){
            control = "Cruise Control";
            binding.txtCruiseControl.setPaintFlags(binding.txtAirBags.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            control = "Cruise Control";
        }
        binding.txtCruiseControl.setText(checkString(control));



        binding.txtMessageOne.setText("Free Booking");
        binding.txtMessageTwo.setText("Free Cancellation");
        binding.txtMessageThree.setText("Pay 100% at the counter or pay with online check-in-24hrs before your pickup time to secure your car and get 5% off");

        if (flag.equals(Config.ARABIC)) {
            binding.txtCarName.setGravity(Gravity.RIGHT);
        }
    }


    private void onClick() {


        binding.txtUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                updateCarDialog();
            }
        });

        binding.txtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, CarDetailTwoActivity.class);
                intent.putExtra(Config.PAY_TYPE,payType);
                intent.putExtra(Config.SELECTED_AED,aedSelected);
                startActivity(intent);
            }
        });

        binding.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) binding.img1.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                binding.imgCar.setImageBitmap(bitmap);

            }
        });
        binding.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) binding.img2.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                binding.imgCar.setImageBitmap(bitmap);
            }
        });
        binding.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) binding.img3.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                binding.imgCar.setImageBitmap(bitmap);
            }
        });

        binding.lnrPromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkPromotionView();
            }
        });

        binding.lnrEstimated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkEstimationView();
            }
        });

        binding.lnrInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkInsuranceView();
            }
        });

        binding.lnrTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkTermView();
            }
        });
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgInsuranceRight.setVisibility(View.GONE);
            binding.imgInsuranceLeft.setVisibility(View.VISIBLE);

            binding.imgPromotionRight.setVisibility(View.GONE);
            binding.imgPromotionLeft.setVisibility(View.VISIBLE);

            binding.imgEstimatedRight.setVisibility(View.GONE);
            binding.imgEstimatedLeft.setVisibility(View.VISIBLE);

            binding.imgTermRight.setVisibility(View.GONE);
            binding.imgTemLeft.setVisibility(View.VISIBLE);


        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgInsuranceRight.setVisibility(View.VISIBLE);
            binding.imgInsuranceLeft.setVisibility(View.GONE);

            binding.imgPromotionRight.setVisibility(View.VISIBLE);
            binding.imgPromotionLeft.setVisibility(View.GONE);

            binding.imgEstimatedRight.setVisibility(View.VISIBLE);
            binding.imgEstimatedLeft.setVisibility(View.GONE);

            binding.imgTermRight.setVisibility(View.VISIBLE);
            binding.imgTemLeft.setVisibility(View.GONE);

        }
    }

    private void checkPromotionView() {
        if (binding.viewPromotion.getVisibility() == View.VISIBLE) {
            binding.viewPromotion.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPromotionLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPromotionRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewPromotion.getVisibility() == View.GONE) {
            binding.viewPromotion.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPromotionLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPromotionRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkInsuranceView() {
        if (binding.viewInsurance.getVisibility() == View.VISIBLE) {
            binding.viewInsurance.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgInsuranceLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgInsuranceRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewInsurance.getVisibility() == View.GONE) {
            binding.viewInsurance.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgInsuranceLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgInsuranceRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkEstimationView() {
        if (binding.viewEstimated.getVisibility() == View.VISIBLE) {
            binding.viewEstimated.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgEstimatedLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgEstimatedRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewEstimated.getVisibility() == View.GONE) {
            binding.viewEstimated.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgEstimatedLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgEstimatedRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkTermView() {
        if (binding.viewTerm.getVisibility() == View.VISIBLE) {
            binding.viewTerm.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgTemLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgTermRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewTerm.getVisibility() == View.GONE) {
            binding.viewTerm.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgTemLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgTermRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }


    private void updateCarDialog() {

        List<CarUpgradeModel> carUpgradeModelList = new ArrayList<>();
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car, "Hyundai Creta", "Reservation Fee: AED 100", "Estimated Total: AED 200"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_black, "Maruti Suzuki", "Reservation Fee: AED 300", "Estimated Total: AED 400"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_blue_new, "Maruti Alto", "Reservation Fee: AED 500", "Estimated Total: AED 600"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_new, "Hyundai Creta", "Reservation Fee: AED 700", "Estimated Total: AED 800"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_red_new, "Maruti Zen", "Reservation Fee: AED 900", "Estimated Total: AED 1000"));

        CarUpgradeAdapter adapter = new CarUpgradeAdapter(activity, carUpgradeModelList);

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_update_car);

        ViewPager viewPager = dialog.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

//        viewPager.setPageMargin(40);
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(false, new ZoomFadeTransformer(viewPager.getPaddingLeft(), 0.85f, 0));


//        viewPager.setPageMargin(100);
//        viewPager.setPageTransformer(false, new ViewPager.PageTransformer()
//        {
//            @Override
//            public void transformPage(View page, float position)
//            {
//                int pageWidth = viewPager.getMeasuredWidth() -
//                        viewPager.getPaddingLeft() - viewPager.getPaddingRight();
//                int pageHeight = viewPager.getHeight();
//                int paddingLeft = viewPager.getPaddingLeft();
//                float transformPos = (float) (page.getLeft() -
//                        (viewPager.getScrollX() + paddingLeft)) / pageWidth;
//                int max = pageHeight / 10;
//                if (transformPos < -1)
//                {
//                    // [-Infinity,-1)
//                    // This page is way off-screen to the left.
//                    page.setAlpha(0.5f);// to make left transparent
//                    page.setScaleY(0.7f);
//                }
//                else if (transformPos <= 1)
//                {
//                    // [-1,1]
//                    page.setScaleY(1f);
//                }
//                else
//                {
//                    // (1,+Infinity]
//                    // This page is way off-screen to the right.
//                    page.setAlpha(0.5f);// to make right transparent
//                    page.setScaleY(0.7f);
//                }
//            }
//        });

//        viewPager.setPageMargin(40);
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//                    @Override
//                    public void transformPage(View page, float position) {
//                        float r = 1 - Math.abs(position);
//                        page.setScaleY(0.85f + r * 0.15f);
//
////                        int pageWidth = viewPager.getMeasuredWidth() -
////                                viewPager.getPaddingLeft() - viewPager.getPaddingRight();
////                        int paddingLeft = viewPager.getPaddingLeft();
////                        float transformPos = (float) (page.getLeft() -
////                                (viewPager.getScrollX() + paddingLeft)) / pageWidth;
////
////                        if (transformPos < -1){
////                            page.setScaleY(0.8f);
////                        } else if (transformPos <= 1) {
////                            page.setScaleY(1f);
////                        } else {
////                            page.setScaleY(0.8f);
////                        }
//
//                    }
//                }
//        );

//        viewPager.setClipToPadding(false);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//        int paddingToSet = width/4; //set this ratio according to how much of the next and previos screen you want to show.
//        viewPager.setPadding(paddingToSet,0,paddingToSet,0);
//
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                page.setScaleY(0.85f + r * 0.15f);
//            }
//        });
//        viewPager.setPageTransformer(compositePageTransformer);

        TextView txtCarName = dialog.findViewById(R.id.txtCarName);
        TextView txtReservationFee = dialog.findViewById(R.id.txtReservationFee);
        TextView txtEstimateTotal = dialog.findViewById(R.id.txtEstimateTotal);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtUpgrade = dialog.findViewById(R.id.txtUpgrade);
        ImageView imgLeft = dialog.findViewById(R.id.imgLeft);
        ImageView imgRight = dialog.findViewById(R.id.imgRight);

        CarUpgradeModel model = carUpgradeModelList.get(0);
        txtCarName.setText(model.getCarName());
        txtReservationFee.setText(model.getReservationFee());
        txtEstimateTotal.setText(model.getEstimateTotal());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                currentPosition = position;
                CarUpgradeModel model = carUpgradeModelList.get(position);
                txtCarName.setText(model.getCarName());
                txtReservationFee.setText(model.getReservationFee());
                txtEstimateTotal.setText(model.getEstimateTotal());
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (currentPosition > 0 && currentPosition <= carUpgradeModelList.size()) {
                    currentPosition--;
                    viewPager.setCurrentItem(currentPosition);
                }
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (currentPosition < carUpgradeModelList.size()) {
                    currentPosition++;
                    viewPager.setCurrentItem(currentPosition);
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        txtUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
//                binding.imgCar.setImageResource(carUpgradeModelList.get(currentPosition).getImage());
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void hitBookingCarData(CarModel model) {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.car_id,model.getId());
        j.addString(P.pay_type,payType);
        j.addString(P.pickup_emirate_id,Config.SelectedPickUpEmirateID);
        j.addString(P.pickup_location_id,Config.SelectedPickUpID);
        j.addString(P.pickup_date,Config.SelectedPickUpDate);
        j.addString(P.pickup_time,Config.SelectedPickUpTime);
        j.addString(P.pickup_type,"self_pickup");

        j.addString(P.dropoff_emirate_id,Config.SelectedDropUpEmirateID);
        j.addString(P.dropoff_location_id,Config.SelectedDropUpID);
        j.addString(P.dropoff_date,Config.SelectedDropUpDate);
        j.addString(P.dropoff_time,Config.SelectedDropUpTime);
        j.addString(P.dropoff_type,"self_return");

        Api.newApi(activity, P.BaseUrl + "car_booking_data").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Json data = json.getJson(P.data);

                        data.getString(P.total_booking_days);
                        data.getString(P.payment_method);
                        data.getString(P.pay_now_discount_per);
                        data.getString(P.total_advance_booking_days);
                        data.getString(P.advance_discount_per);
                        data.getString(P.total_car_rate);
                        data.getString(P.total_amount);

                        Json pickUpData = data.getJson(P.pickup_location_data);
                        pickUpData.getString(P.location_name);
                        pickUpData.getString(P.contact_email);
                        pickUpData.getString(P.contact_number);
                        pickUpData.getJsonList(P.location_time_data);
                        pickUpData.getJsonList(P.location_time_data);

                        Json dropUpData = data.getJson(P.dropoff_location_data);
                        dropUpData.getString(P.location_name);
                        dropUpData.getString(P.contact_email);
                        dropUpData.getString(P.contact_number);
                        dropUpData.getJsonList(P.location_time_data);
                        dropUpData.getJsonList(P.location_time_data);

                        Json carData = data.getJson(P.car_data);
                        carData.getString(P.car_name);
                        carData.getString(P.transmission_name);
                        carData.getString(P.fuel_type_name);
                        carData.getString(P.group_name);
                        carData.getString(P.category_name);
                        carData.getString(P.air_bags);
                        carData.getString(P.air_conditioner);
                        carData.getString(P.parking_sensors);
                        carData.getString(P.rear_parking_camera);
                        carData.getString(P.bluetooth);
                        carData.getString(P.cruise_control);
                        carData.getString(P.sunroof);
                        carData.getString(P.car_image);
                        carData.getString(P.door);
                        carData.getString(P.passenger);
                        carData.getString(P.suitcase);

                    }else {
//                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitBookingCarData");
    }

    private void hitCarExtrasData() {

        String url = "emirate_id=" + SelectCarActivity.pickUpEmirateID + "&car_id=" + model.getId() + "&pickup_date=" + SelectCarActivity.pickUpDate + "&dropoff_date=" + SelectCarActivity.dropUpDate;

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "cars_choose_extras?" + url).addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        JsonList choose_extras = json.getJsonList(P.choose_extras);

                        for (Json data : choose_extras){
                            data.getString(P.title);
                            data.getString(P.key_value);
                            data.getString(P.description);
                            data.getString(P.max_quantity);
                            data.getString(P.price);
                        }

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCarExtrasData");
    }

    private String checkString(String string){
        String value = string;

        if (TextUtils.isEmpty(string) || string.equals("null")){
            value = "";
        }
        return value;
    }

    private String getFormatedDate(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    private int getCurrentItem(RecyclerView recyclerView) {
        return ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}