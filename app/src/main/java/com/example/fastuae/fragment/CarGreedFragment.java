package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.adapter.CarGridAdapter;
import com.example.fastuae.databinding.FragmentGreedCardBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class CarGreedFragment extends Fragment {

    private Context context;
    private FragmentGreedCardBinding binding;
    private List<CarModel> carModelList;
    private CarGridAdapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_greed_card, container, false);
            context = inflater.getContext();

            initView();

        }

        return binding.getRoot();
    }

    private void initView(){

        loadingDialog = new LoadingDialog(context);

        carModelList = new ArrayList<>();
        adapter = new CarGridAdapter(context,carModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.recyclerCar.setLayoutManager(linearLayoutManager);
        binding.recyclerCar.setAdapter(adapter);

        hitCarData();
    }

    private void hitCarData() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        String externalParams = "emirate_id=" + SelectCarActivity.pickUpEmirateID + "&pickup_date=" + SelectCarActivity.pickUpDate + "&dropoff_date=" + SelectCarActivity.dropUpDate;

        Api.newApi(context, P.BaseUrl + "cars?" + externalParams).addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);

                        Json min_price = json.getJson(P.min_price);
                        String pay_later_rate = min_price.getString(P.pay_later_rate);
                        String pay_now_rate = min_price.getString(P.pay_now_rate);

                        JsonList car_list = json.getJsonList(P.car_list);
                        for (int i=0; i<car_list.size(); i++){
                            Json jsonData = car_list.get(i);
                            CarModel model = new CarModel();
                            model.setId(jsonData.getString(P.id));
                            model.setCar_name(jsonData.getString(P.car_name));
                            model.setTransmission_name(jsonData.getString(P.transmission_name));
                            model.setFuel_type_name(jsonData.getString(P.fuel_type_name));
                            model.setGroup_name(jsonData.getString(P.group_name));
                            model.setCategory_name(jsonData.getString(P.category_name));
                            model.setAir_bags(jsonData.getString(P.air_bags));
                            model.setAir_conditioner(jsonData.getString(P.air_conditioner));
                            model.setParking_sensors(jsonData.getString(P.parking_sensors));
                            model.setRear_parking_camera(jsonData.getString(P.rear_parking_camera));
                            model.setBluetooth(jsonData.getString(P.bluetooth));
                            model.setCruise_control(jsonData.getString(P.cruise_control));
                            model.setSunroof(jsonData.getString(P.sunroof));
                            model.setCar_image(jsonData.getString(P.car_image));
                            model.setDoor(jsonData.getString(P.door));
                            model.setPassenger(jsonData.getString(P.passenger));
                            model.setSuitcase(jsonData.getString(P.suitcase));
                            model.setPay_later_rate(jsonData.getString(P.pay_later_rate));
                            model.setPay_now_rate(jsonData.getString(P.pay_now_rate));
                            carModelList.add(model);
                        }

                        adapter.notifyDataSetChanged();

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCarData");
    }

    public static CarGreedFragment newInstance() {
        CarGreedFragment fragment = new CarGreedFragment();
        return fragment;
    }

}
