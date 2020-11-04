package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CountryCodeAdapter;
import com.example.fastuae.databinding.ActivityLoginBinding;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.CountryCode;
import com.example.fastuae.util.WindowView;

public class LoginActivity extends AppCompatActivity {

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;
    private LoadingDialog loadingDialog;
    private String countryCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView(){
        loadingDialog = new LoadingDialog(activity);

        CountryCodeAdapter adapter = new CountryCodeAdapter(activity, CountryCode.getList());
        binding.spinnerCode.setAdapter(adapter);

        binding.spinnerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = CountryCode.getList().get(position);
                countryCode = model.getCode();
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
                    Intent intent = new Intent(activity, OTPVerificationActivity.class);
                    intent.putExtra(Config.MOBILE_NUMBER,binding.etxMobileNumber.getText().toString().trim());
                    intent.putExtra(Config.COUNTRY_CODE,countryCode);
                    startActivity(intent);
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
}