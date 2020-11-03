package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLoginDashboardBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.WindowView;

public class LoginDashboardActivity extends AppCompatActivity {

    private LoginDashboardActivity activity = this;
    private ActivityLoginDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_dashboard);
        initView();
    }

    private void initView(){

        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                Intent mainIntent = new Intent(activity,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                Intent loginIntent = new Intent(activity,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                Intent signUpIntent = new Intent(activity,SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}