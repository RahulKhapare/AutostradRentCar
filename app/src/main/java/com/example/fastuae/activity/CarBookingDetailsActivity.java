package com.example.fastuae.activity;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.LocationAdapter;
import com.example.fastuae.databinding.ActivityCarBookingDetailBinding;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.HomeLocationModel;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class CarBookingDetailsActivity extends AppCompatActivity implements LocationAdapter.onClick{

    private CarBookingDetailsActivity activity = this;
    private ActivityCarBookingDetailBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;
    private String payType = "";
    private String aedSelected = "";
    private CarModel model;

    private String pickupType = "deliver";
    private String dropupType = "collect";

    private int pickUpFlag = 1;
    private int dropUpFlag = 2;
    private String pickUpId = "";
    private String pickUpLocation = "";
    private String dropUpId = "";
    private String dropUpLocation = "";
    private String pickUpEmirateID = "";
    private String dropUpEmirateID = "";
    private String pickUpAddress = "";
    private String dropUpAddress = "";
    private String pickUpLandmark = "";
    private String dropUpLandmark = "";

    private List<HomeLocationModel> locationDeliverList;
    private List<HomeLocationModel> locationCollectList;
    private LocationAdapter locationDeliverAdapter;
    private LocationAdapter locationCollectAdapter;

    private String message1 = "'Total' does not include any additional items you may select at the location or any costs arising from the rental (such as damage, fuel or road traffic charges). For renters under the age of 25, additional charges may apply, and are payable at the location.";
    private String message2 = "By clicking on \"Pay & Confirm Booking\" you confirm that you understand and accept our , Rental Terms Qualification and Requirements, Reservation Term & Conditions and you understand the Age Restrictions.";
    private String message3 = "I agree to the terms and conditions and acknowledge that this is a prepaid rate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_booking_detail);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);
        aedSelected = getIntent().getStringExtra(Config.SELECTED_AED);
        initView();
    }

    private void initView() {

        binding.toolbar.setTitle(getResources().getString(R.string.confirmBooking));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        model = Config.carModel;
        binding.txtMessage1.setText(message1);
        binding.txtMessage2.setText(message2);
        binding.txtMessage3.setText(message3);

        locationDeliverList = new ArrayList<>();
        locationDeliverAdapter = new LocationAdapter(activity, locationDeliverList, pickUpFlag);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        binding.recyclerDeliverLocation.setLayoutManager(linearLayoutManager1);
        binding.recyclerDeliverLocation.setHasFixedSize(true);
        binding.recyclerDeliverLocation.setAdapter(locationDeliverAdapter);

        locationCollectList = new ArrayList<>();
        locationCollectAdapter = new LocationAdapter(activity, locationCollectList, dropUpFlag);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(activity);
        binding.recyclerLocationCollect.setLayoutManager(linearLayoutManager2);
        binding.recyclerLocationCollect.setHasFixedSize(true);
        binding.recyclerLocationCollect.setAdapter(locationCollectAdapter);

        setData();
        onClick();
        hitLocationData();
    }


    private void setData(){
        binding.txtTotalAED.setText("AED "+aedSelected);
    }

    @Override
    public void onLocationClick(String location, int flag, HomeLocationModel model) {

        if (flag == pickUpFlag) {
            pickUpId = model.getId();
            pickUpLocation = model.getLocation_name();
            pickUpEmirateID = model.getEmirate_id();
            binding.txtPickUpMessage.setText(location);
            pickUpAddress = model.getAddress();
            pickUpLandmark = model.getLocation_name();
            hideDeliverView();
            hideCollectView();
        } else if (flag == dropUpFlag) {
            dropUpId = model.getId();
            dropUpLocation = model.getLocation_name();
            dropUpEmirateID = model.getEmirate_id();
            binding.txtDropUpMessage.setText(location);
            dropUpAddress = model.getAddress();
            dropUpLandmark = model.getLocation_name();
            hideCollectView();
            hideDeliverView();
        }

    }


    private void onClick(){

        binding.radioDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                pickupType = "deliver";
                binding.radioSelfPickup.setChecked(false);
                blueTin(binding.radioDeliver);
                blackTin(binding.radioSelfPickup);
                binding.cardLocationDeliver.setVisibility(View.GONE);
                hideDeliverView();
                hideCollectView();
            }
        });

        binding.radioSelfPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                pickupType = "self-pickup";
                binding.radioDeliver.setChecked(false);
                blueTin(binding.radioSelfPickup);
                blackTin(binding.radioDeliver);
                binding.cardLocationDeliver.setVisibility(View.GONE);
                hideDeliverView();
                hideCollectView();
            }
        });

        binding.radioCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dropupType = "collect";
                binding.radioSelfReturn.setChecked(false);
                blueTin(binding.radioCollect);
                blackTin(binding.radioSelfReturn);
                binding.cardLocationCollect.setVisibility(View.GONE);
                hideCollectView();
                hideDeliverView();
            }
        });

        binding.radioSelfReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dropupType = "self-return";
                binding.radioCollect.setChecked(false);
                blueTin(binding.radioSelfReturn);
                blackTin(binding.radioCollect);
                binding.cardLocationCollect.setVisibility(View.GONE);
                hideCollectView();
                hideDeliverView();
            }
        });

        binding.cardPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardPickupClick();
                hideCollectView();

            }
        });

        binding.cardDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardDropUpClick();
                hideDeliverView();

            }
        });

        binding.lnrPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPersonalInfoView();
            }
        });

        binding.lnrPaymentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPaymentInfoView();
            }
        });

        binding.lnrBillingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBillingInfoView();
            }
        });


    }

    private void hideDeliverView(){
        binding.cardLocationDeliver.setVisibility(View.GONE);
        binding.imgDeliverArrowUp.setVisibility(View.GONE);
        binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
    }

    private void hideCollectView(){
        binding.cardLocationCollect.setVisibility(View.GONE);
        binding.imgCollectArrowUp.setVisibility(View.GONE);
        binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
    }

    private void cardPickupClick() {
        if (binding.imgDeliverArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgDeliverArrowDown.setVisibility(View.GONE);
            binding.imgDeliverArrowUp.setVisibility(View.VISIBLE);
            binding.cardLocationDeliver.setVisibility(View.VISIBLE);

        } else if (binding.imgDeliverArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgDeliverArrowUp.setVisibility(View.GONE);
            binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
            binding.cardLocationDeliver.setVisibility(View.GONE);
        }
    }

    private void cardDropUpClick() {

        if (binding.imgCollectArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgCollectArrowDown.setVisibility(View.GONE);
            binding.imgCollectArrowUp.setVisibility(View.VISIBLE);
            binding.cardLocationCollect.setVisibility(View.VISIBLE);
        } else if (binding.imgCollectArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgCollectArrowUp.setVisibility(View.GONE);
            binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
            binding.cardLocationCollect.setVisibility(View.GONE);
        }

    }

    private void blackTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.grayDark)}
                    },
                    new int[]{getResources().getColor(R.color.grayDark)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void blueTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }


    private void hitLocationData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "location").addJson(j)
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
                        JsonList location_list = json.getJsonList(P.location_list);
                        for (int i = 0; i < location_list.size(); i++) {
                            Json jsonData = location_list.get(i);
                            HomeLocationModel model = new HomeLocationModel();
                            model.setId(jsonData.getString(P.id));
                            model.setEmirate_id(jsonData.getString(P.emirate_id));
                            model.setEmirate_name(jsonData.getString(P.emirate_name));
                            model.setLocation_name(jsonData.getString(P.location_name));
                            model.setAddress(jsonData.getString(P.address));
                            model.setStatus(jsonData.getString(P.status));
                            model.setContact_number(jsonData.getString(P.contact_number));
                            model.setContact_email(jsonData.getString(P.contact_email));
                            model.setLocation_time_data(jsonData.getJsonList(P.location_time_data));
                            locationCollectList.add(model);
                            locationDeliverList.add(model);
                        }

                        locationCollectAdapter.notifyDataSetChanged();
                        locationDeliverAdapter.notifyDataSetChanged();

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");
    }


    private void checkPersonalInfoView() {
        if (binding.viewPersonalInfo.getVisibility() == View.VISIBLE) {
            binding.viewPersonalInfo.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPersnalInfoLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPersnalInfoRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewPersonalInfo.getVisibility() == View.GONE) {
            binding.viewPersonalInfo.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPersnalInfoLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPersnalInfoRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    private void checkPaymentInfoView() {
        if (binding.viewPaymentInfo.getVisibility() == View.VISIBLE) {
            binding.viewPaymentInfo.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPaymentInfoLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPaymentInfoRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewPaymentInfo.getVisibility() == View.GONE) {
            binding.viewPaymentInfo.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPaymentInfoLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPaymentInfoRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    private void checkBillingInfoView() {
        if (binding.viewBillingInfo.getVisibility() == View.VISIBLE) {
            binding.viewBillingInfo.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgBillingInfoLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgBillingInfoRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewBillingInfo.getVisibility() == View.GONE) {
            binding.viewBillingInfo.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgBillingInfoLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgBillingInfoRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}