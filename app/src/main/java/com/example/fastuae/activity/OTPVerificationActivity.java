package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityOTPVerificationBinding;
import com.example.fastuae.util.WindowView;

public class OTPVerificationActivity extends AppCompatActivity {

    private OTPVerificationActivity activity = this;
    private ActivityOTPVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_t_p_verification);
        initView();
    }

    private void initView(){

    }
}