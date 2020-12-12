package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CountryCodeAdapter;
import com.example.fastuae.databinding.ActivitySignUpBinding;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.Validation;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private SignUpActivity activity = this;
    private ActivitySignUpBinding binding;
    private LoadingDialog loadingDialog;
    private String countryCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView(){
        loadingDialog = new LoadingDialog(activity);
        binding.txtSkipNow.setPaintFlags(binding.txtSkipNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        List<CountryCodeModel> countryCodeModelList = new ArrayList<>();

        for (Json json : Config.countryJsonList){
            CountryCodeModel model = new CountryCodeModel();
            model.setId(json.getString(P.id));
            model.setCountry_name(json.getString(P.country_name));
            model.setPhone_code(json.getString(P.phone_code));
            countryCodeModelList.add(model);
        }

        CountryCodeAdapter adapter = new CountryCodeAdapter(activity, countryCodeModelList);
        binding.spinnerCode.setAdapter(adapter);

        binding.spinnerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                countryCode = model.getPhone_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onClick();
    }

    private boolean checkValidation(){
        boolean value = true;

        if(TextUtils.isEmpty(binding.etxMobileNumber.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterMobile));
            value = false;
        }else if(TextUtils.isEmpty(binding.etxEmail.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterEmailId));
            value = false;
        }else if(!Validation.validEmail(binding.etxEmail.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterEmailValid));
            value = false;
        }

        return value;
    }

    private void onClick(){

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()){
//                    hitSignUp();
                    Intent intent = new Intent(activity, OTPVerificationActivity.class);
                    intent.putExtra(Config.VERIFICATION_FOR,Config.SIGN_UP);
                    intent.putExtra(Config.MOBILE_NUMBER,binding.etxMobileNumber.getText().toString().trim());
                    intent.putExtra(Config.COUNTRY_CODE,countryCode);
                    startActivity(intent);
                }
            }
        });

        binding.lnrLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
//                overridePendingTransition(0, 0);
            }
        });

        binding.txtSkipNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    private void hitSignUp() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.user_mobile,binding.etxMobileNumber.getText().toString().trim());
        j.addString(P.user_email,binding.etxEmail.getText().toString().trim());
        j.addString(P.user_country_code,countryCode);

        Api.newApi(activity, P.BaseUrl + "register").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        Intent intent = new Intent(activity, OTPVerificationActivity.class);
                        intent.putExtra(Config.VERIFICATION_FOR,Config.SIGN_UP);
                        intent.putExtra(Config.MOBILE_NUMBER,binding.etxMobileNumber.getText().toString().trim());
                        intent.putExtra(Config.COUNTRY_CODE,countryCode);
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitSignUp");
    }

}