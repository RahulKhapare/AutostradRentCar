package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.AddressSelectionAdapter;
import com.example.fastuae.adapter.CodeSelectionAdapter;
import com.example.fastuae.databinding.ActivityLoastAndFoundBinding;
import com.example.fastuae.model.AddressModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
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
    private List<AddressModel> listLocation;
    private int positionNumber = 0;
    private String codePrimary = "";
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

        AddressModel modelLocation = new AddressModel();
        modelLocation.setCountry_name("Select Rental Location");
        listLocation.add(modelLocation);
        AddressSelectionAdapter adapterLocation = new AddressSelectionAdapter(activity, listLocation);
        binding.spinnerRentalLocation.setAdapter(adapterLocation);

        updateIcons();
        onSelection();
        onClick();

    }

    private void onSelection(){

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onClick(){
        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()){

                }
            }
        });
    }

    private boolean checkValidation(){
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxComment.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterDescription));
        }else if (binding.etxComment.getText().toString().trim().length()<3){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterValidDescription));
        }else if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.pleaseEnterFirstName));
        }else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.pleaseEnterLastName));
        }else if (TextUtils.isEmpty(binding.etxNumber.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterMobile));
        }else if (TextUtils.isEmpty(binding.etxEmail.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterEmailId));
        }else if (!Validation.validEmail(binding.etxEmail.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterEmailValid));
        }else if (TextUtils.isEmpty(binding.etxVehicleNo.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterVehicleNo));
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