package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityOTPVerificationBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

public class OTPVerificationActivity extends AppCompatActivity {

    private OTPVerificationActivity activity = this;
    private ActivityOTPVerificationBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;
    private String number = "";
    private String countryCode = "";
    private String loginOTP = "123456";
    private CountDownTimer timer;
    private String resetOTP;
    private String verificationFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_t_p_verification);
        initView();
    }

    private void initView(){
        session = new Session(activity);
        number = getIntent().getStringExtra(Config.MOBILE_NUMBER);
        countryCode = getIntent().getStringExtra(Config.COUNTRY_CODE);
        verificationFor = getIntent().getStringExtra(Config.VERIFICATION_FOR);

        loadingDialog = new LoadingDialog(activity);
        resetOTP = getResources().getString(R.string.resendOTP);

        binding.txtOTPMessage.setText(getResources().getString(R.string.codeMessage) + " " + number);
        binding.etxOtp.setText(loginOTP);

        startTimer();

        binding.txtSeconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.txtSeconds.getText().toString().equals(resetOTP)){
                    hitResendOtp();
                }
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.etxOtp.getText().toString())){
                    H.showMessage(activity,getResources().getString(R.string.enterOTP));
                }else if (binding.etxOtp.getText().toString().length()<6 || binding.etxOtp.getText().toString().length()>6){
                    H.showMessage(activity,getResources().getString(R.string.errorOTP));
                }else {
                    hitVerifyOtp();
                }
            }
        });

    }

    private void startTimer(){
        timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.txtSeconds.setText(getResources().getString(R.string.secondsRemaining) + " " + "00 : " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                binding.txtSeconds.setText(resetOTP);
                binding.etxOtp.setText("");
            }
        }.start();
    }

    private void closeTimer(){
        try {
            if (timer!=null){
                timer.cancel();
            }
        }catch (Exception e){
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeTimer();
    }

    private void hitVerifyOtp() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.user_mobile,number);
        j.addString(P.otp,binding.etxOtp.getText().toString().trim());

        String apiCall = "";
        if (verificationFor.equals(Config.LOGIN)){
            apiCall = "verify_login_otp";
        }else if (verificationFor.equals(Config.SIGN_UP)){
            apiCall = "verify_register_otp";
        }

        Api.newApi(activity, P.BaseUrl + apiCall).addJson(j)
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
                        json = json.getJson(P.data);
                        Json userJson = json.getJson(P.user);

                        session.addBool(P.userLogin,true);
                        session.addString(P.id,userJson.getString(P.id));
                        session.addString(P.user_name,userJson.getString(P.user_name));
                        session.addString(P.user_email,userJson.getString(P.user_email));
                        session.addString(P.user_mobile,userJson.getString(P.user_mobile));
                        session.addString(P.token,userJson.getString(P.token));

                        Intent mainIntent = new Intent(activity,MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitVerifyOtp");
    }

    private void hitResendOtp() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.user_mobile,number);

        String apiCall = "";
        if (verificationFor.equals(Config.LOGIN)){
            apiCall = "resend_login_otp";
        }else if (verificationFor.equals(Config.SIGN_UP)){
            apiCall = "resend_register_otp";
        }

        Api.newApi(activity, P.BaseUrl + apiCall).addJson(j)
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
                        json = json.getJson(P.data);
                        ProgressView.dismiss(loadingDialog);
                        H.showMessage(activity,getResources().getString(R.string.optSent));
                        binding.etxOtp.setText(loginOTP);
                        startTimer();
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitResendOtp");
    }
}