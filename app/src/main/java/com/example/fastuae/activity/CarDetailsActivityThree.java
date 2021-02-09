package com.example.fastuae.activity;

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
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarDetailsThreeBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

public class CarDetailsActivityThree extends AppCompatActivity {

    private CarDetailsActivityThree activity = this;
    private ActivityCarDetailsThreeBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_details_three);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        Config.isEditDetails = false;
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

        binding.txtCarName.setText("Mercedes SUV");
        binding.txtAEDOne.setText("AED 40/Day");
        binding.txtAEDTwo.setText("AED 100 in Total");
        binding.txtPickUpLocation.setText("Mon, 10 Jan, 10:00 AM\n123, Building Name, Street Name, City Name,\nPincode 400000");
        binding.txtDropOffLocation.setText("Mon, 10 Jan, 10:00 AM\n123, Building Name, Street Name, City Name,\nPincode 400000");

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
                Intent intent = new Intent(activity,BookingSucessfullActivity.class);
                startActivity(intent);
            }
        });
    }

    private void hitBookCarData(CarModel model) {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.car_id,model.getId());

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
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitBookCarData");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}
