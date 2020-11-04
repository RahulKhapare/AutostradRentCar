package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityOTPVerificationBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

public class OTPVerificationActivity extends AppCompatActivity {

    private OTPVerificationActivity activity = this;
    private ActivityOTPVerificationBinding binding;
    private LoadingDialog loadingDialog;
    private String number = "";
    private String countryCode = "";
    private String loginOTP = "123456";
    private CountDownTimer timer;
    private String resetOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_t_p_verification);
        initView();
    }

    private void initView(){

        number = getIntent().getStringExtra(Config.MOBILE_NUMBER);
        countryCode = getIntent().getStringExtra(Config.COUNTRY_CODE);

        loadingDialog = new LoadingDialog(activity);
        resetOTP = getResources().getString(R.string.resendOTP);

        binding.txtOTPMessage.setText(getResources().getString(R.string.codeMessage) + " " + number);
        binding.etxOtp.setText(loginOTP);
        binding.etxOtp.setTextColor(getResources().getColor(R.color.colorWhite));

        startTimer();

        binding.txtSeconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.txtSeconds.getText().toString().equals(resetOTP)){
                    ProgressView.show(activity,loadingDialog);
                    new Handler().postDelayed(() -> {
                        ProgressView.dismiss(loadingDialog);
                        H.showMessage(activity,getResources().getString(R.string.optSent));
                        binding.etxOtp.setText(loginOTP);
                        startTimer();
                    }, 1230);
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
                    Intent mainIntent = new Intent(activity,MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
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
}