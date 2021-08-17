package com.autostrad.rentcar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.AEDAdapter;
import com.autostrad.rentcar.adapter.AddOnsAdapter;
import com.autostrad.rentcar.adapter.CarImageAdapter;
import com.autostrad.rentcar.adapter.CarUpgradeAdapter;
import com.autostrad.rentcar.adapter.RentalAdapter;
import com.autostrad.rentcar.databinding.ActivityCarDetailOneBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.AEDModel;
import com.autostrad.rentcar.model.CarImageModel;
import com.autostrad.rentcar.model.CarModel;
import com.autostrad.rentcar.model.CarUpgradeModel;
import com.autostrad.rentcar.model.ChooseExtrasModel;
import com.autostrad.rentcar.model.RentalModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarDetailOneActivity extends AppCompatActivity implements AddOnsAdapter.onClick,AddOnsAdapter.onCalculation{

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
    public static String carID = "";
    private String carNAME = "";
    private String carAED = "";

    String updateImage ;
    String updateId;
    String updateAmount;
    String updateName;

    List<RentalModel> rentalModelList;
    RentalAdapter rentalAdapter;

    Json pickup_location_data;
    Json dropoff_location_data;
    double totalAEDAmount = 0;
    double currentCarAED = 0;
    double showAED = 0;

    String total_car_rate;
    String delivery_charges;
    String collect_charges;
    String inter_emirate_charges;
    String total_amount;

    List<ChooseExtrasModel> chooseExtrasModelList;
    AddOnsAdapter addOnsAdapter;

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
        carImageModelList.add(new CarImageModel(model.getCar_image()));
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


        chooseExtrasModelList = new ArrayList<>();
//        chooseExtrasModelList.addAll(AddOnsActivity.addOnsList);
        binding.recyclerAdOns.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerAdOns.setHasFixedSize(true);
        binding.recyclerAdOns.setNestedScrollingEnabled(false);
        addOnsAdapter = new AddOnsAdapter(activity,chooseExtrasModelList);
        binding.recyclerAdOns.setAdapter(addOnsAdapter);

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

        LoadImage.glideString(activity,binding.imgCar,model.getCar_image());
        binding.txtCarName.setText(model.getCar_name());
        String pickUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedPickUpDate);
        String dropUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedDropUpDate);

        if (!carAED.equals("")){
            currentCarAED = Double.parseDouble(carAED);
        }

        if (AddOnsActivity.addOnsList.isEmpty()){
//            binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +carAED + "");
        }

        if (Config.HOME_DELIVERY_CHECK){
            if (Config.FOR_EDIT_LOCATION){
                binding.txtPickUpLocation.setText(EditPickupDropofftActivity.deliveryEmirateName+",\n"+pickUpDate+", "+Config.SelectedPickUpTime);
            }else {
                binding.txtPickUpLocation.setText(HomeFragment.deliveryEmirateName+",\n"+pickUpDate+", "+Config.SelectedPickUpTime);
            }
        }else {
            binding.txtPickUpLocation.setText(Config.SelectedPickUpAddress+",\n"+pickUpDate+", "+Config.SelectedPickUpTime);
        }

        if (Config.HOME_COLLECT_CHECK){
            if (Config.FOR_EDIT_LOCATION){
                binding.txtDropOffLocation.setText(EditPickupDropofftActivity.collectEmirateName+",\n"+dropUpDate+", "+Config.SelectedDropUpTime);
            }else {
                binding.txtDropOffLocation.setText(HomeFragment.collectEmirateName+",\n"+dropUpDate+", "+Config.SelectedDropUpTime);
            }
        }else {
            if (Config.FOR_EDIT_LOCATION){
                binding.txtDropOffLocation.setText(Config.SelectedDropUpAddress+",\n"+dropUpDate+", "+Config.SelectedDropUpTime);
            }else {
                binding.txtDropOffLocation.setText(Config.SelectedDropUpAddress+",\n"+dropUpDate+", "+Config.SelectedDropUpTime);
            }
        }

        if (payType.equals(Config.pay_now)){
//            binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + model.getPay_now_rate());
            binding.txtPayMethod.setText(getResources().getString(R.string.prePay));
        }else if (payType.equals(Config.pay_latter)){
//            binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + model.getPay_later_rate());
            binding.txtPayMethod.setText(getResources().getString(R.string.postPay));
        }

        if (flag.equals(Config.ARABIC)) {
            binding.txtCarName.setGravity(Gravity.RIGHT);
        }

        if(SelectCarActivity.pickUpType.equals("self_pickup")){
            binding.txtPickupDetails.setVisibility(View.VISIBLE);
        }else {
            binding.txtPickupDetails.setVisibility(View.GONE);
        }

        if(SelectCarActivity.dropUpType.equals("self_dropoff")){
            binding.txtDropupDetails.setVisibility(View.VISIBLE);
        }else {
            binding.txtDropupDetails.setVisibility(View.GONE);
        }

        hitBookingCarData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.FOR_EDIT_LOCATION){
            hitUpdateCarData();
            setData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Config.FOR_EDIT_LOCATION = false;
    }

    @Override
    public void aedCalculation(ChooseExtrasModel model,int position) {

        if (model.getPrice()!=null && !model.getPrice().equals("")){
            double aedAmount = Double.parseDouble(model.getPrice());
            totalAEDAmount = totalAEDAmount + aedAmount;
        }

        if (position==AddOnsActivity.addOnsList.size()-1){

            showAED = totalAEDAmount + currentCarAED;

            DecimalFormat format = new DecimalFormat("#.##");
            double formatedAED = Double.valueOf(format.format(showAED));
            showAED = formatedAED;
//            binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +showAED + "");
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
                onEditClick(getResources().getString(R.string.editAddressMessage));
            }
        });

        binding.txtPickupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                try {
                    if (pickup_location_data==null || pickup_location_data.length()==0 || pickup_location_data.toString().equals("{}")){
                        H.showMessage(activity,getResources().getString(R.string.unableToOpenDetails));
                    }else {
                        updatePickupDropoffDialog(1,pickup_location_data);
                    }
                }catch (Exception e){
                    H.showMessage(activity,getResources().getString(R.string.unableToOpenDetails));
                }
            }
        });

        binding.txtDropupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                onEditClick(getResources().getString(R.string.editAddressMessage));
            }
        });

        binding.txtDropupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                try {
                    if (dropoff_location_data==null || dropoff_location_data.length()==0 || pickup_location_data.toString().equals("{}")){
                        H.showMessage(activity,getResources().getString(R.string.unableToOpenDetails));
                    }else {
                        updatePickupDropoffDialog(2,dropoff_location_data);
                    }
                }catch (Exception e){
                    H.showMessage(activity,getResources().getString(R.string.unableToOpenDetails));
                }
            }
        });

        binding.txtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, CarBookingDetailsActivity.class);
                intent.putExtra(Config.PAY_TYPE,payType);
                intent.putExtra(Config.SELECTED_AED,total_amount + "");
//                if (AddOnsActivity.addOnsList.isEmpty()){
//                    intent.putExtra(Config.SELECTED_AED,carAED + "");
//                }else {
//                    intent.putExtra(Config.SELECTED_AED,showAED + "");
//                }
                startActivity(intent);
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
        txtExtraFee.setText(getResources().getString(R.string.payAED) + " " + getDouble(model.getAmount_difference())+ " " +getResources().getString(R.string.extra));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                currentPosition = position;
                CarUpgradeModel model = carUpgradeModelList.get(position);
                txtCarName.setText(model.getCar_name());
                txtExtraFee.setText(getResources().getString(R.string.payAED) + " " + getDouble(model.getAmount_difference())+ " " +getResources().getString(R.string.extra));

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
                double totalAmount = currentCarAED + Double.parseDouble(updateAmount);

                showAED = totalAEDAmount + totalAmount;

                DecimalFormat format = new DecimalFormat("#.##");
                double formatedAED = Double.valueOf(format.format(showAED));
                showAED = formatedAED;
//                binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +showAED + "");

                binding.txtCarName.setText(updateName);
//                binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + totalAmount + "");
//                binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +showAED + "");

                LoadImage.glideString(activity,binding.imgCar,updateImage);

                CarUpgradeModel carModel = carUpgradeModelList.get(currentPosition);
                List<CarImageModel> carImageModelList = new ArrayList<>();
                CarUpgradeModel modelCurrent = carUpgradeModelList.get(currentPosition);
                carImageModelList.add(new CarImageModel(modelCurrent.getCar_image()));
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

                binding.txtUpgrade.setVisibility(View.GONE);

                hitBookingCarData();

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
        j.addString(P.emirate_id,SelectCarActivity.pickUpEmirateID);
        j.addString(P.coupon_code,"");

        j.addString(P.pickup_type,SelectCarActivity.pickUpType);
        if (Config.HOME_DELIVERY_CHECK){
            j.addString(P.pickup_emirate_id, SelectCarActivity.pickUpEmirateID);
            j.addString(P.pickup_location_id, "0");
            j.addString(P.pickup_address, "");
            j.addString(P.pickup_landmark, "");
            j.addString(P.pickup_location_name,"");
        }else {
            j.addString(P.pickup_emirate_id, SelectCarActivity.pickUpEmirateID);
            j.addString(P.pickup_location_id, SelectCarActivity.pickUpLocationID);
            j.addString(P.pickup_address, SelectCarActivity.pickUpAddress);
            j.addString(P.pickup_landmark, Config.SelectedPickUpLandmark);
            j.addString(P.pickup_location_name,Config.SelectedPickUpAddress);

        }
        j.addString(P.pickup_date,SelectCarActivity.pickUpDate);
        j.addString(P.pickup_time,SelectCarActivity.pickUpTime);


        j.addString(P.dropoff_type,SelectCarActivity.dropUpType);
        if (Config.HOME_COLLECT_CHECK){
            j.addString(P.dropoff_emirate_id, SelectCarActivity.dropUpEmirateID);
            j.addString(P.dropoff_location_id, "0");
            j.addString(P.dropoff_address, "");
            j.addString(P.dropoff_landmark, "");
        }else {
            j.addString(P.dropoff_emirate_id, SelectCarActivity.dropUpEmirateID);
            j.addString(P.dropoff_location_id, SelectCarActivity.dropUpLocationID);
            j.addString(P.dropoff_address, SelectCarActivity.dropUpAddress);
            j.addString(P.dropoff_landmark, Config.SelectedDropUpLandmark);
        }
        j.addString(P.dropoff_date,SelectCarActivity.dropUpDate);
        j.addString(P.dropoff_time,SelectCarActivity.dropUpTime);


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

                        pickup_location_data = data.getJson(P.pickup_location_data);
                        dropoff_location_data = data.getJson(P.dropoff_location_data);

                        total_car_rate = data.getString("total_car_rate");
                        delivery_charges = data.getString("delivery_charges");
                        collect_charges = data.getString("collect_charges");
                        inter_emirate_charges = data.getString("inter_emirate_charges");
                        total_amount = data.getString("total_amount");

                        binding.txtCarRate.setText(getResources().getString(R.string.aed) + " " + total_car_rate + "");
                        binding.txtEnitireECharges.setText(getResources().getString(R.string.aed) + " " + inter_emirate_charges + "");
                        binding.txtCarDeliveryRate.setText(getResources().getString(R.string.aed) + " " + delivery_charges + "");
                        binding.txtCarPickupRate.setText(getResources().getString(R.string.aed) + " " + collect_charges + "");

//                        if(inter_emirate_charges.equals("") || inter_emirate_charges.equals("null") || inter_emirate_charges.equals("0")){
//                            binding.lnrEmirateCostView.setVisibility(View.GONE);
//                            binding.lnrEmirateCostLine.setVisibility(View.GONE);
//                        }else {
//                            binding.lnrEmirateCostView.setVisibility(View.VISIBLE);
//                            binding.lnrEmirateCostLine.setVisibility(View.VISIBLE);
//                        }

                        checkDeliverRate();
                        checkCollectRate();

                        if (binding.lnrCarDeliveryRate.getVisibility()==View.GONE && binding.lnrCarPickupRate.getVisibility()==View.GONE){
                            binding.lnrDeliverAndPickupRateView.setVisibility(View.GONE);
                            binding.lnrDeliverAndPickupRateLine.setVisibility(View.GONE);
                        }else {
                            binding.lnrDeliverAndPickupRateView.setVisibility(View.VISIBLE);
                            binding.lnrDeliverAndPickupRateLine.setVisibility(View.VISIBLE);
                        }

                        binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " +total_amount + "");

                        JsonList car_extra = data.getJsonList("car_extra");
                        chooseExtrasModelList.clear();
                        if (car_extra!=null && car_extra.size()!=0){
                            for (Json jsonValue : car_extra){
                                ChooseExtrasModel model = new ChooseExtrasModel();
                                model.setKey_value(jsonValue.getString("value"));
                                model.setQuantity(jsonValue.getString("quantity"));
                                model.setTitle(jsonValue.getString("title"));
                                model.setPrice(jsonValue.getString("price"));
                                chooseExtrasModelList.add(model);
                            }
                            addOnsAdapter.notifyDataSetChanged();
                        }

                        if (chooseExtrasModelList.isEmpty() || chooseExtrasModelList.size()==0){
                            binding.lnrAddOns.setVisibility(View.GONE);
                            binding.lnrAddONLine.setVisibility(View.GONE);
                        }else {
                            binding.lnrAddOns.setVisibility(View.VISIBLE);
                            binding.lnrAddONLine.setVisibility(View.VISIBLE);
                        }

                    }else {
//                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitBookingCarData");
    }

    private void checkDeliverRate(){
        if (Config.HOME_DELIVERY_CHECK){
            binding.lnrCarDeliveryRate.setVisibility(View.VISIBLE);
//            if(delivery_charges.equals("") || delivery_charges.equals("null") || delivery_charges.equals("0")){
//                binding.lnrCarDeliveryRate.setVisibility(View.GONE);
//            }else {
//                binding.lnrCarDeliveryRate.setVisibility(View.VISIBLE);
//            }
        }else {
            binding.lnrCarDeliveryRate.setVisibility(View.GONE);
        }
    }

    private void checkCollectRate(){
        if (Config.HOME_COLLECT_CHECK){
            binding.lnrCarPickupRate.setVisibility(View.VISIBLE);
//            if(collect_charges.equals("") || collect_charges.equals("null") || collect_charges.equals("0")){
//                binding.lnrCarPickupRate.setVisibility(View.GONE);
//            }else {
//                binding.lnrCarPickupRate.setVisibility(View.VISIBLE);
//            }

        }else {
            binding.lnrCarPickupRate.setVisibility(View.GONE);
        }
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
                        carUpgradeModelList.clear();
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

    private void updatePickupDropoffDialog(int type,Json json) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pickup_dropoff_details);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        TextView txtType = dialog.findViewById(R.id.txtType);
        TextView txtLocation = dialog.findViewById(R.id.txtLocation);
        TextView txtAddress = dialog.findViewById(R.id.txtAddress);
        TextView txtEmail = dialog.findViewById(R.id.txtEmail);
        TextView txtPhoneNo = dialog.findViewById(R.id.txtPhoneNo);
        TextView txtTiming = dialog.findViewById(R.id.txtTiming);

        if (type==1){
            txtType.setText(getResources().getString(R.string.pickUpLocation));
        }else if (type==2){
            txtType.setText(getResources().getString(R.string.dropOfLocation));
        }

        txtLocation.setText(json.getString("location_name"));
        txtAddress.setText(json.getString("address"));
        txtEmail.setText(json.getString("contact_email"));
        txtPhoneNo.setText(json.getString("contact_number"));
        String timingSlot = "";
        try {
            JSONArray timingSlotList = json.getJsonArray("location_time_data");
        for (int i=0; i<timingSlotList.length(); i++){
            if (timingSlot.equals("")){
                timingSlot = timingSlotList.getString(i).toString();
            }else {
                timingSlot = timingSlot + "\n"+ timingSlotList.getString(i).toString();
            }
        }
        }catch (Exception e){

        }
        txtTiming.setText(timingSlot);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private int getCurrentItem(RecyclerView recyclerView) {
        return ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    @Override
    public void aedDetailsClick(ChooseExtrasModel model) {
        hitAEDDetailsData(model.getKey_value());
    }


    private void hitAEDDetailsData(String detail_type) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        String extraParams =
                "emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&pickup_emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&car_id=" + carID+
                        "&pickup_date=" + SelectCarActivity.pickUpDate +
                        "&dropoff_date=" + SelectCarActivity.dropUpDate +
                        "&booking_type=" + SelectCarActivity.bookingTYpe +
                        "&month_time=" + SelectCarActivity.monthDuration +
                        "&detail_type=" + detail_type +
                        "&coupon_code=" + "" ;

        Api.newApi(activity, P.BaseUrl + "car_rate_details?" + extraParams).addJson(j)
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
                        AEDDetailsDialog(json,detail_type);

                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAEDDetailsData");
    }

    private void AEDDetailsDialog(Json json,String detail_type) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_aed_details);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        LinearLayout lnrTotal = dialog.findViewById(R.id.lnrTotal);
        LinearLayout lnrPayLater = dialog.findViewById(R.id.lnrPayLater);
        LinearLayout lnrDiscount = dialog.findViewById(R.id.lnrDiscount);
        LinearLayout lnrPayNow = dialog.findViewById(R.id.lnrPayNow);

        TextView txtTotalAmount = dialog.findViewById(R.id.txtTotalAmount);
        TextView txtPayLaterAmount = dialog.findViewById(R.id.txtPayLaterAmount);
        TextView txtPayDiscountAmount = dialog.findViewById(R.id.txtPayDiscountAmount);
        TextView txtPayNowAmount = dialog.findViewById(R.id.txtPayNowAmount);

        List<AEDModel> aedModelList = new ArrayList<>();

        for (Json jsonData : json.getJsonList("breakup")){
            AEDModel model = new AEDModel();
            model.setDate(jsonData.getString("date"));
            model.setPrice(jsonData.getString("price"));
            model.setSurge(jsonData.getString("surge"));
            model.setDiscount(jsonData.getString("discount"));
//            model.setType(jsonData.getString(detail_type));
            aedModelList.add(model);
        }

        RecyclerView recyclerAED = dialog.findViewById(R.id.recyclerAED);
        recyclerAED.setLayoutManager(new LinearLayoutManager(activity));
        recyclerAED.setNestedScrollingEnabled(false);
        AEDAdapter aedAdapter = new AEDAdapter(activity,aedModelList);
        recyclerAED.setAdapter(aedAdapter);

        String aed  = getResources().getString(R.string.aed);
        txtTotalAmount.setText(aed + " " + checkView(json.getString("breakup_total"),lnrTotal));
        txtPayLaterAmount.setText(aed + " " + checkView(json.getString("pay_later_price"),lnrPayLater));
        txtPayDiscountAmount.setText(aed + " " + checkView(json.getString("pay_now_discount"),lnrDiscount));
        txtPayNowAmount.setText(aed + " " + checkView(json.getString("pay_now_price"),lnrPayNow));

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private String checkView(String string, LinearLayout linearLayout){
        String value = "";
        if (TextUtils.isEmpty(string) || string.equals("null") || string.equals("0")){
            linearLayout.setVisibility(View.GONE);
        }else {
            try {
                double doubleValue = Double.parseDouble(string);
                value = new DecimalFormat("##.##").format(doubleValue);
            }catch (Exception e){
                value = string;
            }
        }
        return value;
    }

    public String getDouble(String string) {
        String value = "";
        try {
            double doubleValue = Double.parseDouble(string);
            value = new DecimalFormat("##.##").format(doubleValue);
        } catch (Exception e) {
            value = string;
        }

        return value;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private void onEditClick(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(activity,EditPickupDropofftActivity.class);
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


        Button positiveButton = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nigativeButton = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.lightBlue));
        nigativeButton.setTextColor(getResources().getColor(R.color.lightBlue));

    }

}