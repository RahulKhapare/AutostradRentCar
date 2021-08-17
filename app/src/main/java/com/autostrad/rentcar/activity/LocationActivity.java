package com.autostrad.rentcar.activity;

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
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.LocationDetailAdapter;
import com.autostrad.rentcar.adapter.LocationFilterAdapter;
import com.autostrad.rentcar.databinding.ActivityLocationBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.LocationModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationFilterAdapter.onClick{

    private LocationActivity activity = this;
    private ActivityLocationBinding binding;
    private List<LocationModel> locationModelList;
    private LocationDetailAdapter adapter;
    private LoadingDialog loadingDialog;
    private Session session;
    private boolean callTime = false;
    private String flag;
    private String emirateID;

    private List<LocationModel> locationEmirateModelList;
    private LocationFilterAdapter locationFilterAdapter;


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

        flag = session.getString(P.languageFlag);

        locationEmirateModelList = new ArrayList<>();
        locationFilterAdapter = new LocationFilterAdapter(activity,locationEmirateModelList,2);
        binding.recyclerEmirateLocation.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerEmirateLocation.setAdapter(locationFilterAdapter);
        binding.recyclerEmirateLocation.setNestedScrollingEnabled(false);

        locationModelList = new ArrayList<>();
        adapter = new LocationDetailAdapter(activity,locationModelList);
        binding.recyclerLocation.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerLocation.setAdapter(adapter);
        binding.recyclerLocation.setNestedScrollingEnabled(false);

        setLocationData();
        onClick();
    }

    private void setLocationData(){
        locationEmirateModelList.clear();
        JsonList emirate_list = HomeFragment.emirate_list;
        if (emirate_list != null && emirate_list.size() != 0) {
            for (Json json : emirate_list) {
                String id = json.getString(P.id);
                String emirate_name = json.getString(P.emirate_name);
                String status = json.getString(P.status);
                LocationModel model = new LocationModel();
                model.setId(id);
                model.setEmirate_name(emirate_name);
                model.setStatus(status);
                locationEmirateModelList.add(model);
            }
            binding.txtArea.setText(locationEmirateModelList.get(0).getEmirate_name());
            emirateID = locationEmirateModelList.get(0).getEmirate_id();
            hitLocationData(emirateID);
        }

        if (locationEmirateModelList.isEmpty()){
            binding.lnrView.setVisibility(View.GONE);
        }else {
            binding.lnrView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFilterClick(LocationModel model) {
        checkLocationViewView();
        binding.txtArea.setText(model.getEmirate_name());
        emirateID = model.getEmirate_id();
        hitLocationData(emirateID);
    }

    private void onClick(){

        binding.cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkLocationViewView();
            }
        });

        binding.txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.txtViewMore.setVisibility(View.GONE);
                hitLocationData(emirateID);
            }
        });

    }

    private void checkLocationViewView() {
        if (binding.lnrLocationListView.getVisibility() == View.VISIBLE) {
            binding.lnrLocationListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgLocationLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgLocationRight.setImageResource(R.drawable.ic_down_arrow);
            }

        } else if (binding.lnrLocationListView.getVisibility() == View.GONE) {
            binding.lnrLocationListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgLocationLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgLocationRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }


    private void hitLocationData(String emirateID) {
        locationModelList.clear();
        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "location?"+"emirate_id=" + emirateID)
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
            binding.txtViewMore.setVisibility(View.GONE);
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
