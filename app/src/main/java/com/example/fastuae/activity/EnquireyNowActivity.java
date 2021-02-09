package com.example.fastuae.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.AddressSelectionAdapter;
import com.example.fastuae.adapter.CodeSelectionAdapter;
import com.example.fastuae.databinding.ActivityEnquireyNowBinding;
import com.example.fastuae.model.AddressModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.Validation;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class EnquireyNowActivity extends AppCompatActivity {

    private EnquireyNowActivity activity = this;
    private ActivityEnquireyNowBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;

    private int positionNumber = 0;
    private String codePrimary = "";
    private List<CountryCodeModel> countryCodeModelList;
    private List<AddressModel> listEmirate;
    private List<AddressModel> listEnquery;
    private List<AddressModel> listDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enquirey_now);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.enquireyNow));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);
        flag = session.getString(P.languageFlag);

        listEmirate = new ArrayList<>();
        listEnquery = new ArrayList<>();
        listDuration = new ArrayList<>();
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

        AddressModel modelEmirate = new AddressModel();
        modelEmirate.setCountry_name("Select Emirate");
        listEmirate.add(modelEmirate);
        AddressSelectionAdapter adapterEmirate = new AddressSelectionAdapter(activity, listEmirate);
        binding.spinnerEmirate.setAdapter(adapterEmirate);


        AddressModel modelEnqirey = new AddressModel();
        modelEnqirey.setCountry_name("Select Enquiry Type");
        listEnquery.add(modelEnqirey);
        AddressSelectionAdapter adapterEnquirey = new AddressSelectionAdapter(activity, listEnquery);
        binding.spinnerEnquirey.setAdapter(adapterEnquirey);

        AddressModel modelDuration = new AddressModel();
        modelDuration.setCountry_name("Select Duration");
        listDuration.add(modelDuration);
        AddressSelectionAdapter adapterDuration = new AddressSelectionAdapter(activity, listDuration);
        binding.spinnerDuration.setAdapter(adapterDuration);

        updateIcons();
        onClick();
        onSelection();
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

        binding.spinnerEmirate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerEmirate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())){
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
        }else if (TextUtils.isEmpty(binding.etxComment.getText().toString().trim())){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterComment));
        }else if (binding.etxComment.getText().toString().trim().length()<3){
            value = false;
            H.showMessage(activity,getResources().getString(R.string.enterValidComment));
        }

        return value;
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgEmirateRight.setVisibility(View.GONE);
            binding.imgEmirateLeft.setVisibility(View.VISIBLE);

            binding.imgEnquireyRight.setVisibility(View.GONE);
            binding.imgEnqureyLeft.setVisibility(View.VISIBLE);

            binding.imgDurationRight.setVisibility(View.GONE);
            binding.imgDurationLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgEmirateRight.setVisibility(View.VISIBLE);
            binding.imgEmirateLeft.setVisibility(View.GONE);

            binding.imgEnquireyRight.setVisibility(View.VISIBLE);
            binding.imgEnqureyLeft.setVisibility(View.GONE);

            binding.imgDurationRight.setVisibility(View.VISIBLE);
            binding.imgDurationLeft.setVisibility(View.GONE);

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