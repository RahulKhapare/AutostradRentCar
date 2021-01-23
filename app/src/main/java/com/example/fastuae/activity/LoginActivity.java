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
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CountryCodeAdapter;
import com.example.fastuae.databinding.ActivityLoginBinding;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.ConnectionUtil;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;
    private LoadingDialog loadingDialog;
    private String countryCode = "";
    private Session session;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView(){
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        binding.txtSkipNow.setPaintFlags(binding.txtSkipNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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

        CountryCodeAdapter adapter = new CountryCodeAdapter(activity, countryCodeModelList,1);
        binding.spinnerCode.setAdapter(adapter);
        binding.spinnerCode.setSelection(position);

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
        }

        return value;
    }


    private void onClick(){

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()){
                    if (ConnectionUtil.isOnline(activity)){
                        hitMobileLogin();
                    }else {
                        H.showMessage(activity,getResources().getString(R.string.noInternet));
                    }
                }
            }
        });

        binding.lnrSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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

    private void hitMobileLogin() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.user_mobile,binding.etxMobileNumber.getText().toString().trim());
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
                        Intent intent = new Intent(activity, OTPVerificationActivity.class);
                        intent.putExtra(Config.VERIFICATION_FOR,Config.LOGIN);
                        intent.putExtra(Config.MOBILE_NUMBER,binding.etxMobileNumber.getText().toString().trim());
                        intent.putExtra(Config.COUNTRY_CODE,countryCode);
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitMobileLogin");
    }
}