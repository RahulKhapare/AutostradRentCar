package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivitySplashBinding;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

public class SplashActivity extends AppCompatActivity {

    private SplashActivity activity = this;
    private ActivitySplashBinding binding;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();

    }

    private void initView(){
        loadingDialog = new LoadingDialog(activity);
        ProgressView.show(activity,loadingDialog);
        new Handler().postDelayed(() -> {
            ProgressView.dismiss(loadingDialog);
            Intent intent = new Intent(activity, LanguageSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }, 3000);
    }
}