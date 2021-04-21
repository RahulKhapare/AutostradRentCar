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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.fastuae.adapter.AddOnsAdapter;
import com.example.fastuae.adapter.CarImageAdapter;
import com.example.fastuae.adapter.CarUpgradeAdapter;
import com.example.fastuae.adapter.RentalAdapter;
import com.example.fastuae.databinding.ActivityCarDetailOneBinding;
import com.example.fastuae.model.CarImageModel;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.CarUpgradeModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.model.RentalModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;
import com.example.fastuae.util.ZoomFadeTransformer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

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
    private List<CarUpgradeModel> carUpgradeModelList;
    private String carID = "";
    private String carNAME = "";
    private String carAED = "";

    String updateImage ;
    String updateId;
    String updateAmount;
    String updateName;

    List<RentalModel> rentalModelList;
    RentalAdapter rentalAdapter;

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

        model = Config.carModel;

        carID = model.getId();
        carNAME = model.getCar_name();
        carAED = aedSelected;

        binding.toolbar.setTitle(getResources().getString(R.string.booking));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        binding.recyclerCarRelated.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
        binding.recyclerCarRelated.setHasFixedSize(true);
        binding.recyclerCarRelated.setNestedScrollingEnabled(false);

        List<CarImageModel> carImageModelList = new ArrayList<>();
        try{
            for (int i=0; i<model.getMore_car_image().length(); i++){
                CarImageModel carImageModel = new CarImageModel();
                carImageModel.setImage(model.getMore_car_image().getString(i));
                carImageModelList.add(carImageModel);
            }
        }catch (Exception e){
        }
        CarImageAdapter carImageAdapter = new CarImageAdapter(activity,carImageModelList,binding.imgCar,2);
        binding.recyclerCarRelated.setAdapter(carImageAdapter);


        List<ChooseExtrasModel> chooseExtrasModelList = new ArrayList<>();
        chooseExtrasModelList.addAll(AddOnsActivity.addOnsList);
        binding.recyclerAdOns.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerAdOns.setHasFixedSize(true);
        binding.recyclerAdOns.setNestedScrollingEnabled(false);
        AddOnsAdapter addOnsAdapter = new AddOnsAdapter(activity,chooseExtrasModelList);
        binding.recyclerAdOns.setAdapter(addOnsAdapter);

        if (chooseExtrasModelList.isEmpty()){
            binding.lnrAddOns.setVisibility(View.GONE);
        }

        rentalModelList = new ArrayList<>();
        binding.recyclerRentalView.setLayoutManager(new GridLayoutManager(activity,2));
        binding.recyclerRentalView.setHasFixedSize(true);
        binding.recyclerRentalView.setNestedScrollingEnabled(false);
        rentalAdapter = new RentalAdapter(activity,rentalModelList);
        binding.recyclerRentalView.setAdapter(rentalAdapter);

        setData();
        onClick();

        carUpgradeModelList = new ArrayList<>();
        hitUpdateCarData();
        hitRentalTermsData();
    }

    private void setData() {

        hitBookingCarData();

        Picasso.get().load(model.getCar_image()).error(R.drawable.ic_image).into(binding.imgCar);
        binding.txtCarName.setText(model.getCar_name());
        String pickUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedPickUpDate);
        String dropUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedDropUpDate);

        binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +carAED);

        binding.txtPickUpLocation.setText(Config.SelectedPickUpAddress+",\n"+pickUpDate+", "+Config.SelectedPickUpTime);
        binding.txtDropOffLocation.setText(Config.SelectedDropUpAddress+",\n"+dropUpDate+", "+Config.SelectedDropUpTime);

        if (payType.equals(Config.pay_now)){
            binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + model.getPay_now_rate());
            binding.txtPayMethod.setText(getResources().getString(R.string.prePay));
        }else if (payType.equals(Config.pay_latter)){
            binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + model.getPay_later_rate());
            binding.txtPayMethod.setText(getResources().getString(R.string.postPay));
        }

        if (flag.equals(Config.ARABIC)) {
            binding.txtCarName.setGravity(Gravity.RIGHT);
        }
    }


    private void onClick() {


        binding.txtUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (!carUpgradeModelList.isEmpty()){
                    updateCarDialog();
                }else {
                    H.showMessage(activity,getResources().getString(R.string.unableFondUpdateData));
                }
            }
        });

        binding.txtPickupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.txtPickupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.txtDropupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.txtDropupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.txtAEDDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.txtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
//                Intent intent = new Intent(activity, CarBookingDetailsActivity.class);
//                intent.putExtra(Config.PAY_TYPE,payType);
//                intent.putExtra(Config.SELECTED_AED,carAED);
//                startActivity(intent);
            }
        });

        binding.lnrInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInsuranceView();
            }
        });
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgInsuranceRight.setVisibility(View.GONE);
            binding.imgInsuranceLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgInsuranceRight.setVisibility(View.VISIBLE);
            binding.imgInsuranceLeft.setVisibility(View.GONE);


        }
    }

    private void checkInsuranceView() {
        if (binding.viewInsurance.getVisibility() == View.VISIBLE) {
            binding.viewInsurance.setVisibility(View.GONE);
//            binding.nestedSroll.fullScroll(View.FOCUS_UP);
            if (flag.equals(Config.ARABIC)) {
                binding.imgInsuranceLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgInsuranceRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewInsurance.getVisibility() == View.GONE) {
            binding.viewInsurance.setVisibility(View.VISIBLE);
//            binding.nestedSroll.fullScroll(View.FOCUS_DOWN);
            if (flag.equals(Config.ARABIC)) {
                binding.imgInsuranceLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgInsuranceRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void updateCarDialog() {
        currentPosition = 0;
        CarUpgradeAdapter adapter = new CarUpgradeAdapter(activity, carUpgradeModelList);

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_update_car);

        ViewPager viewPager = dialog.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TextView txtCarName = dialog.findViewById(R.id.txtCarName);
        TextView txtExtraFee = dialog.findViewById(R.id.txtExtraFee);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtUpgrade = dialog.findViewById(R.id.txtUpgrade);
        ImageView imgLeft = dialog.findViewById(R.id.imgLeft);
        ImageView imgRight = dialog.findViewById(R.id.imgRight);

        CarUpgradeModel model = carUpgradeModelList.get(currentPosition);
        txtCarName.setText(model.getCar_name());
        txtExtraFee.setText(getResources().getString(R.string.payAED) + " " + model.getAmount_difference()+ " " +getResources().getString(R.string.extra));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                currentPosition = position;
                CarUpgradeModel model = carUpgradeModelList.get(position);
                txtCarName.setText(model.getCar_name());
                txtExtraFee.setText(getResources().getString(R.string.payAED) + " " + model.getAmount_difference()+ " " +getResources().getString(R.string.extra));

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

                updateImage = carUpgradeModelList.get(currentPosition).getCar_image();
                updateId = carUpgradeModelList.get(currentPosition).getCar_id();
                updateAmount = carUpgradeModelList.get(currentPosition).getAmount_difference();
                updateName = carUpgradeModelList.get(currentPosition).getCar_name();

                carID = carUpgradeModelList.get(currentPosition).getCar_id();
                double totalAmount = Double.parseDouble(aedSelected) + Double.parseDouble(updateAmount);
                carAED = totalAmount + "";

                binding.txtCarName.setText(updateName);
                binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +carAED);
                binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + carAED);

                if (!TextUtils.isEmpty(updateImage)){
                    Picasso.get().load(model.getCar_image()).error(R.drawable.ic_no_car).into(binding.imgCar);
                }else {
                    Picasso.get().load(R.drawable.ic_no_car).into(binding.imgCar);
                }

                CarUpgradeModel carModel = carUpgradeModelList.get(currentPosition);
                List<CarImageModel> carImageModelList = new ArrayList<>();
                try{
                    for (int i=0; i<carModel.getMore_car_image().length(); i++){
                        CarImageModel carImageModel = new CarImageModel();
                        carImageModel.setImage(carModel.getMore_car_image().getString(i));
                        carImageModelList.add(carImageModel);
                    }
                }catch (Exception e){
                }
                CarImageAdapter carImageAdapter = new CarImageAdapter(activity,carImageModelList,binding.imgCar,2);
                binding.recyclerCarRelated.setAdapter(carImageAdapter);

            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void hitBookingCarData() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.booking_type,SelectCarActivity.bookingTYpe);
        j.addString(P.month_time,SelectCarActivity.monthDuration);
        j.addString(P.car_id,carID);
        j.addString(P.pay_type,payType);
        j.addString(P.emirate_id,Config.SelectedPickUpEmirateID);
        j.addString(P.coupon_code,"");

        j.addString(P.pickup_type,Config.pickUpTypeValue);
        j.addString(P.pickup_emirate_id,Config.SelectedPickUpEmirateID);
        j.addString(P.pickup_location_id,SelectCarActivity.pickUpLocationID);
        j.addString(P.pickup_location_name,Config.SelectedPickUpAddress);
        j.addString(P.pickup_address,SelectCarActivity.pickUpAddress);
        j.addString(P.pickup_landmark,Config.SelectedPickUpLandmark);
        j.addString(P.pickup_date,Config.SelectedPickUpDate);
        j.addString(P.pickup_time,Config.SelectedPickUpTime);

        j.addString(P.dropoff_type,Config.dropUpTypeValue);
        j.addString(P.dropoff_emirate_id,Config.SelectedDropUpEmirateID);
        j.addString(P.dropoff_location_id,SelectCarActivity.dropUpLocationID);
        j.addString(P.dropoff_location_name,Config.SelectedDropUpAddress);
        j.addString(P.dropoff_address,SelectCarActivity.dropUpAddress);
        j.addString(P.dropoff_landmark,Config.SelectedDropUpLandmark);
        j.addString(P.dropoff_date,Config.SelectedDropUpDate);
        j.addString(P.dropoff_time,Config.SelectedDropUpTime);

        JSONArray array = new JSONArray();
        for (Json jsonData : AddOnsActivity.jsonAddOnsList){
            array.put(jsonData);
        }
        j.addJSONArray(P.car_extra,array);

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
                        data.getString(P.delivery_charges);
                        data.getString(P.collect_charges);

                        if (data.has(P.car_extra)){
                            JsonList carExtraData = data.getJsonList(P.car_extra);
                            for (Json jsonCar :  carExtraData){
                                jsonCar.getString(P.value);
                                jsonCar.getString(P.quantity);
                                jsonCar.getString(P.title);
                                jsonCar.getString(P.price);
                            }
                        }

                        Json couponData = data.getJson(P.coupon);
                        couponData.getString(P.coupon_code);
                        couponData.getString(P.msg);
                        couponData.getString(P.err);

                        Json pickUpData = data.getJson(P.pickup_location_data);
                        pickUpData.getString(P.location_name);
                        pickUpData.getString(P.contact_email);
                        pickUpData.getString(P.contact_number);
                        pickUpData.getJsonList(P.location_time_data);

                        Json dropUpData = data.getJson(P.dropoff_location_data);
                        dropUpData.getString(P.location_name);
                        dropUpData.getString(P.contact_email);
                        dropUpData.getString(P.contact_number);
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


    private void hitUpdateCarData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        String extraParams =
                "emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&car_id=" + model.getId()+
                        "&pickup_date=" + SelectCarActivity.pickUpDate +
                        "&dropoff_date=" + SelectCarActivity.dropUpDate +
                        "&booking_type=" + SelectCarActivity.bookingTYpe +
                        "&month_time=" + SelectCarActivity.monthDuration +
                        "&pay_type=" + payType ;

        Api.newApi(activity, P.BaseUrl + "upgrade_car?" + extraParams).addJson(j)
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
                        JsonList car_list = json.getJsonList(P.car_list);
                        for (int i = 0; i < car_list.size(); i++) {
                            Json jsonData = car_list.get(i);
                            String car_id = jsonData.getString(P.car_id);
                            String car_name = jsonData.getString(P.car_name);
                            String car_image = jsonData.getString(P.car_image);
                            String amount_difference = jsonData.getString(P.amount_difference);
                            JSONArray more_car_image = jsonData.getJsonArray(P.more_car_image);
                            CarUpgradeModel model = new CarUpgradeModel();
                            model.setCar_id(car_id);
                            model.setCar_name(car_name);
                            model.setCar_image(car_image);
                            model.setAmount_difference(amount_difference);
                            model.setMore_car_image(more_car_image);

                            carUpgradeModelList.add(model);

                        }
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUpdateCarData");
    }

    private void hitRentalTermsData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "car_rental_terms")
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
                        JsonList list = json.getJsonList(P.list);
                        for (int i = 0; i < list.size(); i++) {
                            Json jsonData = list.get(i);
                            String title = jsonData.getString(P.title);
                            String description = jsonData.getString(P.description);
                            RentalModel rentalModel = new RentalModel();
                            rentalModel.setTitle(title);
                            rentalModel.setDescription(description);
                            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)){
                                rentalModelList.add(rentalModel);
                            }
                        }
                        rentalAdapter.notifyDataSetChanged();

                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitRentalTermsData");
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