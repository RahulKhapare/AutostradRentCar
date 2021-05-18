package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
import com.example.fastuae.adapter.LocationDetailAdapter;
import com.example.fastuae.databinding.ActivityLocationBinding;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private LocationActivity activity = this;
    private ActivityLocationBinding binding;
    private List<LocationModel> locationModelList;
    private LocationDetailAdapter adapter;
    private LoadingDialog loadingDialog;
    private Session session;
    private boolean callTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.locations));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        callTime = false;
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        locationModelList = new ArrayList<>();
        adapter = new LocationDetailAdapter(activity,locationModelList);
        binding.recyclerLocation.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerLocation.setAdapter(adapter);

        hitLocationData();
        onClick();
    }


    private void onClick(){
        binding.txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.txtViewMore.setVisibility(View.GONE);
                hitLocationData();
            }
        });

    }


    private void hitLocationData() {
        locationModelList.clear();
        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "location")
                .setMethod(Api.GET)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                    checkData();
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        JsonList location_list = json.getJsonList(P.location_list);

                        if (location_list!=null && location_list.size()!=0){

                            for (Json jsonData : location_list){
                                LocationModel model = new LocationModel();
                                model.setId(jsonData.getString(P.id));
                                model.setEmirate_id(jsonData.getString(P.emirate_id));
                                model.setEmirate_name(jsonData.getString(P.emirate_name));
                                model.setLocation_name(jsonData.getString(P.location_name));
                                model.setLocation_timing(jsonData.getString(P.location_timing));
                                model.setAddress(jsonData.getString(P.address));
                                model.setStatus(jsonData.getString(P.status));
                                model.setContact_number(jsonData.getString(P.contact_number));
                                model.setContact_email(jsonData.getString(P.contact_email));
                                model.setLocation_time_data(jsonData.getJsonArray(P.location_time_data));

                                if (!callTime){
                                    if (locationModelList.size()<3){
                                        locationModelList.add(model);
                                    }
                                }else {
                                    locationModelList.add(model);
                                }

                            }

                            callTime = true;
                            adapter.notifyDataSetChanged();

                        }
                        checkData();
                    }else {
                        checkData();
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");

    }

    private void checkData(){
        if (locationModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
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
