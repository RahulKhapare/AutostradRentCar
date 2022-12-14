package com.autostrad.rentcar.activity;

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
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.CountryCodeAdapter;
import com.autostrad.rentcar.databinding.ActivitySignUpBinding;
import com.autostrad.rentcar.model.CountryCodeModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.ConnectionUtil;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.Validation;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private SignUpActivity activity = this;
    private ActivitySignUpBinding binding;
    private LoadingDialog loadingDialog;
    private String countryCode = "";
    private int position = 0;

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

        LoadImage.glide(activity,binding.imgName,getResources().getDrawable(R.drawable.ic_name));
        LoadImage.glide(activity,binding.imgCar,getResources().getDrawable(R.drawable.ic_car_blue_new));

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
                    if (ConnectionUtil.isOnline(activity)){
                        hitSignUp();
                    }else {
                        H.showMessage(activity,getResources().getString(R.string.noInternet));
                    }
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
                        intent.putExtra(Config.USER_EMAIL,binding.etxEmail.getText().toString().trim());
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitSignUp");
    }

}