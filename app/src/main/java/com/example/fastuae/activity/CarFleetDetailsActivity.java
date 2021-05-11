package com.example.fastuae.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CarImageAdapter;
import com.example.fastuae.databinding.ActivityCarFleetDetailsBinding;
import com.example.fastuae.model.CarImageModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.LoadImage;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.RemoveHtml;
import com.example.fastuae.util.WindowView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CarFleetDetailsActivity extends AppCompatActivity {

    private CarFleetDetailsActivity activity = this;
    private ActivityCarFleetDetailsBinding binding;

    private Session session;
    private LoadingDialog loadingDialog;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_fleet_details);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String car_id = getIntent().getStringExtra(Config.CAR_ID);

        binding.recyclerCarRelated.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recyclerCarRelated.setHasFixedSize(true);
        binding.recyclerCarRelated.setNestedScrollingEnabled(false);

        updateIcons();

        hitCarDetailsData(car_id);

    }


    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

        } else if (flag.equals(Config.ENGLISH)) {

        }
    }

    private void hitCarDetailsData(String car_id) {

        ProgressView.show(activity, loadingDialog);

        Api.newApi(activity, P.BaseUrl + "car_detail/" + car_id)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Json data = json.getJson(P.data);
                        Json detail = data.getJson(P.detail);

                        String id = detail.getString(P.id);
                        String car_name = detail.getString(P.car_name);
                        String detail_description = detail.getString(P.detail_description);
                        String transmission_name = detail.getString(P.transmission_name);
                        String fuel_type_name = detail.getString(P.fuel_type_name);
                        String group_name = detail.getString(P.group_name);
                        String category_name = detail.getString(P.category_name);
                        String air_bags = detail.getString(P.air_bags);
                        String air_conditioner = detail.getString(P.air_conditioner);
                        String parking_sensors = detail.getString(P.parking_sensors);
                        String rear_parking_camera = detail.getString(P.rear_parking_camera);
                        String bluetooth = detail.getString(P.bluetooth);
                        String cruise_control = detail.getString(P.cruise_control);
                        String sunroof = detail.getString(P.sunroof);
                        String door = detail.getString(P.door);
                        String passenger = detail.getString(P.passenger);
                        String suitcase = detail.getString(P.suitcase);
                        String car_detail_banner_file_image = detail.getString(P.car_detail_banner_file_image);
                        String car_image = detail.getString(P.car_image);
                        JSONArray more_car_image = detail.getJsonArray(P.more_car_image);


                        String upToNCharacters = detail_description.substring(0, Math.min(detail_description.length(), 500));
                        if (detail_description.length()>500){
                            binding.txtDescription.setText(RemoveHtml.html2text(upToNCharacters));
                        }else {
                            binding.txtReadMore.setVisibility(View.GONE);
                            binding.txtDescription.setText(RemoveHtml.html2text(detail_description));
                        }

                        binding.txtReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                binding.txtReadMore.setVisibility(View.GONE);
                                binding.txtDescription.setText(RemoveHtml.html2text(detail_description));
                            }
                        });

                        LoadImage.glideString(activity, binding.imgCar, car_detail_banner_file_image);

                        binding.txtCarName.setText(car_name);
                        binding.txtCarGroup.setText("Group " + group_name);
                        binding.txtCarType.setText(category_name);

                        binding.txtSuitcase.setText(suitcase);
                        binding.txtSuitcase.setText(suitcase);
                        binding.txtDoor.setText(door);
                        binding.txtPassengers.setText(passenger);
                        binding.txtMode.setText(transmission_name);
                        binding.txtPetrol.setText(fuel_type_name);

                        binding.txtAirCondition.setText("Air Condition");
                        if (TextUtils.isEmpty(air_conditioner) || air_conditioner.equals("null")) {
                            binding.txtAirCondition.setPaintFlags(binding.txtAirCondition.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                        binding.txtAirBag.setText("Air Bag");
                        if (TextUtils.isEmpty(air_bags) || air_bags.equals("null")) {
                            binding.txtAirBag.setPaintFlags(binding.txtAirBag.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                        binding.txtParkingCamera.setText("Parking Camera");
                        if (TextUtils.isEmpty(rear_parking_camera) || rear_parking_camera.equals("null")) {
                            binding.txtParkingCamera.setPaintFlags(binding.txtParkingCamera.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                        binding.txtBluetooth.setText("Bluetooth");
                        if (TextUtils.isEmpty(bluetooth) || bluetooth.equals("null")) {
                            binding.txtBluetooth.setPaintFlags(binding.txtBluetooth.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                        List<CarImageModel> carImageModelList = new ArrayList<>();
                        try {
                            for (int i = 0; i < more_car_image.length(); i++) {
                                CarImageModel carImageModel = new CarImageModel();
                                carImageModel.setImage(more_car_image.getString(i));
                                carImageModelList.add(carImageModel);
                            }
                        } catch (Exception e) {
                        }
                        CarImageAdapter carImageAdapter = new CarImageAdapter(activity, carImageModelList, binding.imgCar, 3);
                        binding.recyclerCarRelated.setAdapter(carImageAdapter);

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCarDetailsData");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}