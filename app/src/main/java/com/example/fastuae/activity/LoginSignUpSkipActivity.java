package com.example.fastuae.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CountryCodeAdapter;
import com.example.fastuae.databinding.ActivityLoginSignUpSkipBinding;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.ConnectionUtil;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.Validation;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LoginSignUpSkipActivity extends AppCompatActivity {

    private LoginSignUpSkipActivity activity = this;
    private ActivityLoginSignUpSkipBinding binding;
    private LoadingDialog loadingDialog;
    private String countryCode = "";
    private Session session;
    private int position = 0;
    private String payType = "";
    private String aedSelected = "";
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_sign_up_skip);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.login));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        flag = session.getString(P.languageFlag);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);
        aedSelected = getIntent().getStringExtra(Config.SELECTED_AED);

        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);

        List<CountryCodeModel> countryCodeModelList = new ArrayList<>();

        JsonList jsonList = Config.countryJsonList;
        for (int i=0;i<jsonList.size(); i++){
            Json json = jsonList.get(i);
            CountryCodeModel model = new CountryCodeModel();
            model.setId(json.getString(P.id));
            model.setCountry_name(json.getString(P.country_name));
            model.setPhone_code(json.getString(P.phone_code));
            countryCodeModelList.add(model);

            if (model.getPhone_code().equalsIgnoreCase("971")){
                position = i;
            }

        }

        CountryCodeAdapter adapterLogin = new CountryCodeAdapter(activity, countryCodeModelList,2);
        binding.spinnerCodeLogin.setAdapter(adapterLogin);
        binding.spinnerCodeLogin.setSelection(position);

        binding.spinnerCodeLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                countryCode = model.getPhone_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        CountryCodeAdapter adapterSignUp = new CountryCodeAdapter(activity, countryCodeModelList,2);
        binding.spinnerCodeSignup.setAdapter(adapterSignUp);
        binding.spinnerCodeSignup.setSelection(position);

        binding.spinnerCodeSignup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private boolean checkLoginValidation(){
        boolean value = true;

        if(TextUtils.isEmpty(binding.etxMobileNumberLogin.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterMobile));
            value = false;
        }

        return value;
    }

    private boolean checkSignUpValidation(){
        boolean value = true;

        if(TextUtils.isEmpty(binding.etxMobileNumberSignup.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterMobile));
            value = false;
        }else if(TextUtils.isEmpty(binding.etxEmailSignup.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterEmailId));
            value = false;
        }else if(!Validation.validEmail(binding.etxEmailSignup.getText().toString().trim())){
            H.showMessage(activity,getResources().getString(R.string.enterEmailValid));
            value = false;
        }

        return value;
    }

    private void onClick(){

        binding.lnrClickCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                visibleSignUp();
            }
        });

        binding.radioCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                visibleSignUp();
            }
        });

        binding.lnrClickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                visibleLogin();
            }
        });

        binding.radioLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                visibleLogin();
            }
        });

        binding.txtProceedSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkSignUpValidation()){
                    if (ConnectionUtil.isOnline(activity)){
                        hitSignUp();
                    }else {
                        H.showMessage(activity,getResources().getString(R.string.noInternet));
                    }
                }
            }
        });

        binding.txtProceedLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkLoginValidation()){
                    if (ConnectionUtil.isOnline(activity)){
                        hitMobileLogin();
                    }else {
                        H.showMessage(activity,getResources().getString(R.string.noInternet));
                    }
                }
            }
        });

    }


    private void hitMobileLogin() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.user_mobile,binding.etxMobileNumberLogin.getText().toString().trim());
        j.addString(P.user_country_code,countryCode);

        Api.newApi(activity, P.BaseUrl + "login").addJson(j)
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
                        Intent intent = new Intent(activity, OTPVerificationSkipActivity.class);
                        intent.putExtra(Config.VERIFICATION_FOR,Config.LOGIN);
                        intent.putExtra(Config.MOBILE_NUMBER,binding.etxMobileNumberLogin.getText().toString().trim());
                        intent.putExtra(Config.COUNTRY_CODE,countryCode);
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitMobileLogin");
    }


    private void hitSignUp() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.user_mobile,binding.etxMobileNumberSignup.getText().toString().trim());
        j.addString(P.user_email,binding.etxEmailSignup.getText().toString().trim());
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
                        Intent intent = new Intent(activity, OTPVerificationSkipActivity.class);
                        intent.putExtra(Config.VERIFICATION_FOR,Config.SIGN_UP);
                        intent.putExtra(Config.MOBILE_NUMBER,binding.etxMobileNumberSignup.getText().toString().trim());
                        intent.putExtra(Config.COUNTRY_CODE,countryCode);
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitSignUp");
    }

    private void visibleSignUp(){
        binding.etxMobileNumberLogin.setText("");
        binding.etxMobileNumberSignup.setText("");
        binding.etxEmailSignup.setText("");
        binding.toolbar.setTitle(getResources().getString(R.string.signUp));
        binding.radioCreateAccount.setChecked(true);
        binding.radioLogin.setChecked(false);
        blueTin(binding.radioCreateAccount);
        blackTin(binding.radioLogin);
        binding.lnrLogin.setVisibility(View.GONE);
        binding.lnrSignUp.setVisibility(View.VISIBLE);
    }

    private void visibleLogin(){
        binding.etxMobileNumberLogin.setText("");
        binding.etxMobileNumberSignup.setText("");
        binding.etxEmailSignup.setText("");
        binding.toolbar.setTitle(getResources().getString(R.string.login));
        binding.radioLogin.setChecked(true);
        binding.radioCreateAccount.setChecked(false);
        blueTin(binding.radioLogin);
        blackTin(binding.radioCreateAccount);
        binding.lnrSignUp.setVisibility(View.GONE);
        binding.lnrLogin.setVisibility(View.VISIBLE);
    }

    private void blackTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.grayDark)}
                    },
                    new int[]{getResources().getColor(R.color.grayDark)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void blueTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
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