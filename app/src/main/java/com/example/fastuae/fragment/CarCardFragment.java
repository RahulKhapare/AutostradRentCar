package com.example.fastuae.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.activity.LanguageSelectionActivity;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.adapter.ViewPagerSwipeAdapter;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class CarCardFragment extends Fragment {

    private Context context;
    private ViewPagerSwipeAdapter swipeAdapter;
    private ViewPager viewPagesSwipe;
    private List<CarModel> carModelList;
    private ScrollView scrollView;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_card, container, false);
        context = inflater.getContext();

        loadingDialog = new LoadingDialog(context);
        scrollView = view.findViewById(R.id.scrollview);
        viewPagesSwipe = view.findViewById(R.id.viewPagesSwipe);

        carModelList = new ArrayList<>();
        swipeAdapter = new ViewPagerSwipeAdapter(context, carModelList);
        viewPagesSwipe.setPageTransformer(true, new ViewPagerStack());
        viewPagesSwipe.setOffscreenPageLimit(3);
        viewPagesSwipe.setAdapter(swipeAdapter);

        hitCarData();

        return view;
    }


    private class ViewPagerStack implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position >= 0) {
                page.setScaleX(0.7f - 0.05f * position);
                page.setScaleY(0.7f);
                page.setTranslationX(-page.getWidth() * position);
                page.setTranslationY(-30 * position);
            }
        }
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

                        swipeAdapter.notifyDataSetChanged();

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCarData");
    }


    public static CarCardFragment newInstance() {
        CarCardFragment fragment = new CarCardFragment();
        return fragment;
    }
}
