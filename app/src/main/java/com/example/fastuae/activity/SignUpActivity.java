package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivitySignUpBinding;
import com.example.fastuae.util.WindowView;

public class SignUpActivity extends AppCompatActivity {

    private SignUpActivity activity = this;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView(){

    }
}