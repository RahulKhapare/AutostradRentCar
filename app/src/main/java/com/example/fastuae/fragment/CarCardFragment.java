package com.example.fastuae.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
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
    private TextView txtError;
    private List<CarModel> carModelList;
    private ScrollView scrollView;
    private LoadingDialog loadingDialog;

    int count;
    int pageCount = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_card, container, false);
        context = inflater.getContext();

        loadingDialog = new LoadingDialog(context);
        scrollView = view.findViewById(R.id.scrollview);
        viewPagesSwipe = view.findViewById(R.id.viewPagesSwipe);
        txtError = view.findViewById(R.id.txtError);

        carModelList = new ArrayList<>();
        setAdapter(0);
        hitCarData(getPaginationUrl(false), false, 0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SelectCarActivity.isApplyFilter) {
            hitCarData(getPaginationUrl(true), true, 0);
        }
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


    public void hitCarData(String url, boolean isFilter, int lastPosition) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "cars?" + url).addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        if (isFilter) {
                            carModelList.clear();
                        }

                        json = json.getJson(P.data);

                        String num_rows = json.getString(P.num_rows);
                        if (!TextUtils.isEmpty(num_rows) && !num_rows.equals("null")) {
                            count = Integer.parseInt(num_rows);
                        }

//                        Json min_price = json.getJson(P.min_price);
//                        String pay_later_rate = min_price.getString(P.pay_later_rate);
//                        String pay_now_rate = min_price.getString(P.pay_now_rate);

                        JsonList car_list = json.getJsonList(P.car_list);

                        for (int i = 0; i < car_list.size(); i++) {
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

                        setAdapter(lastPosition);

                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCarData");
    }

    private void setAdapter(int lastPosition) {
        swipeAdapter = new ViewPagerSwipeAdapter(context, carModelList);
        viewPagesSwipe.setPageTransformer(true, new ViewPagerStack());
        viewPagesSwipe.setOffscreenPageLimit(3);
        viewPagesSwipe.setAdapter(swipeAdapter);
        viewPagesSwipe.setCurrentItem(lastPosition);

        viewPagesSwipe.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int page = position + 1;
                if (carModelList != null && !carModelList.isEmpty()) {
                    if (page == carModelList.size()) {
                        if (carModelList.size() < count) {
                            pageCount++;
                            hitCarData(getPaginationUrl(false), false, page - 1);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (carModelList.isEmpty()) {
            txtError.setVisibility(View.VISIBLE);
        } else {
            txtError.setVisibility(View.GONE);
        }
    }

    private String getPaginationUrl(boolean isFilter) {
        String url =

                        "booking_type=" + "daily" +

                        "&emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&pickup_type=" + SelectCarActivity.pickUpType+
                        "&pickup_emirate_id=" + SelectCarActivity.pickUpEmirateID +
                        "&pickup_location_id=" + SelectCarActivity.pickUpLocationID +
                        "&pickup_address=" + SelectCarActivity.pickUpAddress +
                        "&pickup_date=" + SelectCarActivity.pickUpDate +
                        "&pickup_time=" + SelectCarActivity.pickUpTime +

                        "&dropoff_type=" + SelectCarActivity.dropUpType +
                        "&dropoff_emirate_id=" + SelectCarActivity.dropUpEmirateID +
                        "&dropoff_location_id=" + SelectCarActivity.dropUpLocationID +
                        "&dropoff_address=" + SelectCarActivity.dropUpAddress +
                        "&dropoff_date=" + SelectCarActivity.dropUpDate +
                        "&dropoff_time=" + SelectCarActivity.dropUpTime +

                        "&coupon_code=" + "" ;

        if (!TextUtils.isEmpty(SelectCarActivity.groupValue)) {
            url = url + "&group=" + SelectCarActivity.groupValue;
        }

        if (!TextUtils.isEmpty(SelectCarActivity.passengerValue)) {
            url = url + "&passengers=" + SelectCarActivity.passengerValue;
        }

        if (!TextUtils.isEmpty(SelectCarActivity.doorValue)) {
            url = url + "&doors=" + SelectCarActivity.doorValue;
        }

        if (!TextUtils.isEmpty(SelectCarActivity.suitcaseValue)) {
            url = url + "&suitcase=" + SelectCarActivity.suitcaseValue;
        }

        if (!TextUtils.isEmpty(SelectCarActivity.transmissionValue)) {
            url = url + "&transmission=" + SelectCarActivity.transmissionValue;
        }

        if (!TextUtils.isEmpty(SelectCarActivity.fuilValue)) {
            url = url + "&fuel=" + SelectCarActivity.fuilValue;
        }

        if (isFilter) {
            url = url + "&page=" + "1";
            pageCount = 1;
        } else {
            url = url + "&page=" + pageCount + "";
        }

        url = url + "&per_page=" + "10";

        return url;
    }


    public static CarCardFragment newInstance() {
        CarCardFragment fragment = new CarCardFragment();
        return fragment;
    }
}
