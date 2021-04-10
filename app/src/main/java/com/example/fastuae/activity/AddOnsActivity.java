package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;

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
import com.example.fastuae.adapter.ChooseExtrasAdapter;
import com.example.fastuae.databinding.ActivityAddOnesBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class AddOnsActivity extends AppCompatActivity{

    private AddOnsActivity activity = this;
    private ActivityAddOnesBinding binding;

    private LoadingDialog loadingDialog;

    private List<ChooseExtrasModel> chooseExtrasModelLis;
    ChooseExtrasAdapter adapter;
    private CarModel model;
    private Session session;
    private String flag;
    private String payType = "";
    private String aedSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ones);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView(){

        binding.toolbar.setTitle(getResources().getString(R.string.addOes));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        model = Config.carModel;
        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);
        flag = session.getString(P.languageFlag);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);
        aedSelected = getIntent().getStringExtra(Config.SELECTED_AED);

        chooseExtrasModelLis = new ArrayList<>();
        adapter = new ChooseExtrasAdapter(activity,chooseExtrasModelLis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.recyclerExtras.setLayoutManager(linearLayoutManager);
        binding.recyclerExtras.setNestedScrollingEnabled(false);
        binding.recyclerExtras.setHasFixedSize(true);
        binding.recyclerExtras.setAdapter(adapter);

        hitChooseExtraData();

    }

    private void hitChooseExtraData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        String extraParams =
                        "emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&car_id=" + model.getId()+
                        "&pickup_date=" + SelectCarActivity.pickUpDate +
                        "&dropoff_date=" + SelectCarActivity.dropUpDate +
                        "&booking_type=" + Config.pickUpTypeValue +
                        "&month_time=" + "2" ;

        Api.newApi(activity, P.BaseUrl + "cars_choose_extras?" + extraParams).addJson(j)
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
                        JsonList location_list = json.getJsonList(P.choose_extras);
                        for (int i = 0; i < location_list.size(); i++) {
                            Json jsonData = location_list.get(i);
                            String title = jsonData.getString(P.title);
                            String key_value = jsonData.getString(P.key_value);
                            String description = jsonData.getString(P.description);
                            String max_quantity = jsonData.getString(P.max_quantity);
                            String price = jsonData.getString(P.price);

                            ChooseExtrasModel model = new ChooseExtrasModel();
                            model.setTitle(title);
                            model.setKey_value(key_value);
                            model.setDescription(description);
                            model.setMax_quantity(max_quantity);
                            model.setPrice(price);

                            chooseExtrasModelLis.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitChooseExtraData");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

}