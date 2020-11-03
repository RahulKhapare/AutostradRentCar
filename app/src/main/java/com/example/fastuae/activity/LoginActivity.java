package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLoginBinding;
import com.example.fastuae.util.WindowView;

public class LoginActivity extends AppCompatActivity {

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView(){

    }
}