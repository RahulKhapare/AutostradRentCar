package com.autostrad.rentcar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityCarDetailsThreeBinding;
import com.autostrad.rentcar.model.CarModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarDetailsActivityThree extends AppCompatActivity {

    private CarDetailsActivityThree activity = this;
    private ActivityCarDetailsThreeBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;
    private CarModel model;

    private String cardNumber;
    private String cardName;
    private String cardValidMonth;
    private String cardValidCVV;
    private String pay_type;
    private String selectedAed;
    private String cardYear;
    private String cardMonth;
    private String paymentID;
    private String cityName;
    private String countryID;
    private String zipCode;
    private String address1;
    private String address2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_details_three);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        Config.isEditDetails = false;

        paymentID = getIntent().getStringExtra(Config.PAY_ID);
        cardNumber = getIntent().getStringExtra(Config.cardNumber);
        cardName = getIntent().getStringExtra(Config.cardName);
        cardValidMonth = getIntent().getStringExtra(Config.cardValidMonth);
        cardValidCVV = getIntent().getStringExtra(Config.cardValidCVV);
        pay_type = getIntent().getStringExtra(Config.PAY_TYPE);
        selectedAed = getIntent().getStringExtra(Config.SELECTED_AED);
        cityName = getIntent().getStringExtra(Config.cityName);
        countryID = getIntent().getStringExtra(Config.countryID);
        zipCode = getIntent().getStringExtra(Config.zipCode);
        address1 = getIntent().getStringExtra(Config.address1);
        address2 = getIntent().getStringExtra(Config.address2);

        String[] separated = cardValidMonth.split("/");
        cardMonth = separated[0];
        cardYear = separated[1];

        initView();
    }

    private void initView(){
        binding.toolbar.setTitle(getResources().getString(R.string.confirmBooking));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setData();
        onClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.isEditDetails){
            setInformation();
        }
    }


    private void setData(){

        model = Config.carModel;

        LoadImage.glideString(activity,binding.imgCar,model.getCar_image());

        binding.txtCarName.setText(model.getCar_name());
        binding.txtAED.setText("AED " + selectedAed + " in Total");

        String pickUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedPickUpDate);
        String dropUpDate = getFormatedDate("yyyy-MM-dd", "dd, MMM yyyy", Config.SelectedDropUpDate);

        binding.txtPickUpLocation.setText(pickUpDate+", "+Config.SelectedPickUpTime+",\n"+Config.SelectedPickUpAddress);
        binding.txtDropOffLocation.setText(dropUpDate+", "+Config.SelectedDropUpTime+",\n"+Config.SelectedDropUpAddress);

        binding.txtMessageOne.setText("Free Booking");
        binding.txtMessageTwo.setText("Free Cancellation");
        binding.txtMessageThree.setText("Pay 100% at the counter or pay with online check-in-24hrs before your pickup time to secure your car and get 5% off");

        setInformation();

        if (flag.equals(Config.ARABIC)){
            binding.txtPhoneNo.setGravity(Gravity.LEFT);
        }

    }

    private void setInformation(){
        binding.txtFirstName.setText(Config.firstName);
        binding.txtLastName.setText(Config.lastName);
        binding.txtEmailName.setText(Config.email);
        binding.txtPhoneNo.setText(Config.code+ "-"+Config.phone);
    }

    private void onClick(){

        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity,EditInformationActivity.class);
                startActivity(intent);
            }
        });

        binding.txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                hitBookCarData(model);
            }
        });
    }

    private void hitBookCarData(CarModel model) {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.booking_type,"daily");
        j.addString(P.month_time,"0");
        j.addString(P.car_id,model.getId());
        j.addString(P.pay_type,pay_type);
        j.addString(P.coupon_code,"");

        j.addString(P.pickup_type,Config.pickUpTypeValue);
        j.addString(P.pickup_emirate_id,Config.SelectedPickUpEmirateID);
        j.addString(P.pickup_location_id,SelectCarActivity.pickUpLocationID);
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

        j.addString(P.user_name,Config.firstName);
        j.addString(P.user_lastname,Config.lastName);
        j.addString(P.user_email,Config.email);
        j.addString(P.user_country_code,Config.code);
        j.addString(P.user_mobile,Config.phone);

        j.addString(P.user_payment_option_id,paymentID);
        j.addString(P.card_number,cardNumber);
        j.addString(P.name_on_card,cardName);
        j.addString(P.expiry_month,cardMonth);
        j.addString(P.expiry_year,cardYear);
        j.addString(P.cvv,cardValidCVV);

        j.addString(P.address_line_1,address1);
        j.addString(P.address_line_2,address2);
        j.addString(P.country_id,countryID);
        j.addString(P.state,"Empty");
        j.addString(P.city,cityName);
        j.addString(P.zipcode,zipCode);

        j.addString(P.booking_remark,"");
        j.addString(P.booking_from,"mobile-android");
        j.addString(P.success_url,"");
        j.addString(P.failed_url,"");

        JSONArray array = new JSONArray();
        j.addJSONArray(P.car_extra,array);

        Api.newApi(activity, P.BaseUrl + "book_car").addJson(j)
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
                        String paymentLink = "";
                        try {
                            paymentLink = data.getString(P.payment_link);
                        }catch (Exception e){
                        }
                        Intent intent = new Intent(activity,BookingSucessfullActivity.class);
                        intent.putExtra(Config.WEB_URL,paymentLink);
                        intent.putExtra(Config.PAY_TYPE,pay_type);
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitBookCarData",session.getString(P.token));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}
