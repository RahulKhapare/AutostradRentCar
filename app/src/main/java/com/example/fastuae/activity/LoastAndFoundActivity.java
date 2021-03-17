package com.example.fastuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CodeSelectionAdapter;
import com.example.fastuae.adapter.EmirateSelectionAdapter;
import com.example.fastuae.databinding.ActivityLoastAndFoundBinding;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.model.EmirateModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.Validation;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LoastAndFoundActivity extends AppCompatActivity {

    private LoastAndFoundActivity activity = this;
    private ActivityLoastAndFoundBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;
    private List<EmirateModel> listLocation;
    private int positionNumber = 0;
    private String codePrimary = "";
    private String emirateID = "";
    private List<CountryCodeModel> countryCodeModelList;

    String description = "While renting from Fast Rent A Car, it is important to know that you are responsible for your belongings. However, we recognize that there would still be times that you or your family may accidentally lose your belongings in one of our rentals. In such cases, you can be rest assured, that our team will do their best in assisting you with their recovery!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loast_and_found);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.lostFount));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);
        flag = session.getString(P.languageFlag);

        binding.txtDescription.setText(description);

        listLocation = new ArrayList<>();
        countryCodeModelList = new ArrayList<>();

        JsonList jsonList = Config.countryJsonList;
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            CountryCodeModel model = new CountryCodeModel();
            model.setId(json.getString(P.id));
            model.setCountry_name(json.getString(P.country_name));
            model.setPhone_code(json.getString(P.phone_code));
            countryCodeModelList.add(model);

            if (model.getPhone_code().equalsIgnoreCase("971")) {
                positionNumber = i;
            }

        }

        CodeSelectionAdapter adapterOne = new CodeSelectionAdapter(activity, countryCodeModelList);
        binding.spinnerCodeMobile.setAdapter(adapterOne);
        binding.spinnerCodeMobile.setSelection(positionNumber);

        EmirateModel emirateModel = new EmirateModel();
        emirateModel.setEmirate_name(getResources().getString(R.string.selectRental));
        listLocation.add(emirateModel);
        JsonList emirate_list = HomeFragment.emirate_list;
        if (emirate_list != null && emirate_list.size() != 0) {
            for (Json json : emirate_list) {
                String id = json.getString(P.id);
                String emirate_name = json.getString(P.emirate_name);
                String status = json.getString(P.status);
                EmirateModel model = new EmirateModel();
                model.setId(id);
                model.setEmirate_name(emirate_name);
                model.setStatus(status);
                listLocation.add(model);
            }
        }


        EmirateSelectionAdapter adapterLocation = new EmirateSelectionAdapter(activity, listLocation);
        binding.spinnerRentalLocation.setAdapter(adapterLocation);

        updateIcons();
        onSelection();
        onClick();

    }

    private void onSelection() {

        binding.spinnerCodeMobile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                codePrimary = model.getPhone_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerRentalLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmirateModel model = listLocation.get(position);
                if (!TextUtils.isEmpty(model.getId()) && !model.getId().equals("")){
                    emirateID = model.getId();
                }else {
                    emirateID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onClick() {
        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()) {
                    hitLostFoundDetails();
                }
            }
        });
    }

    private boolean checkValidation() {
        boolean value = true;

        if (TextUtils.isEmpty(emirateID)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectRentalLocation));
        } else if (TextUtils.isEmpty(binding.etxComment.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterDescription));
        } else if (binding.etxComment.getText().toString().trim().length() < 3) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterValidDescription));
        } else if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterFirstName));
        } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterLastName));
        } else if (TextUtils.isEmpty(binding.etxNumber.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterMobile));
        } else if (TextUtils.isEmpty(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailId));
        } else if (!Validation.validEmail(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailValid));
        } else if (TextUtils.isEmpty(binding.etxVehicleNo.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterVehicleNo));
        }

        return value;
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgLocationRight.setVisibility(View.GONE);
            binding.imgLocationLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgLocationRight.setVisibility(View.VISIBLE);
            binding.imgLocationLeft.setVisibility(View.GONE);


        }
    }

    private void hitLostFoundDetails() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.emirate_id,emirateID);
        j.addString(P.description,binding.etxComment.getText().toString().trim());
        j.addString(P.user_name,binding.etxFirstName.getText().toString().trim());
        j.addString(P.user_lastname,binding.etxLastName.getText().toString().trim());
        j.addString(P.user_lastname,binding.etxLastName.getText().toString().trim());
        j.addString(P.user_mobile,codePrimary + binding.etxNumber.getText().toString().trim());
        j.addString(P.user_email,binding.etxEmail.getText().toString().trim());
        j.addString(P.user_email,binding.etxEmail.getText().toString().trim());
        j.addString(P.agreement_vehicle_no,binding.etxVehicleNo.getText().toString().trim());


        Api.newApi(activity, P.BaseUrl + "add_lost_and_found").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        H.showMessage(activity,getResources().getString(R.string.lostFoundAdded));
                        new Handler().postDelayed(() -> {
                            finish();
                        }, 500);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLostFoundDetails",session.getString(P.token));
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
        finish();
    }
}