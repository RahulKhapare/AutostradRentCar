package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivitySplashBinding;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.ConnectionUtil;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

public class SplashActivity extends AppCompatActivity {

    private SplashActivity activity = this;
    private ActivitySplashBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();

    }

    private void initView(){
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        if (ConnectionUtil.isOnline(activity)){
            hitInit();
        }else {
            H.showMessage(activity,getResources().getString(R.string.noInternet));
        }
    }

    private void checkValidation(){
        new Handler().postDelayed(() -> {
            Intent intent = null;
            if (session.getBool(P.userLogin)){
                if(!TextUtils.isEmpty(session.getString(P.languageFlag))){
                    intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }else {
                    intent = new Intent(activity, LanguageSelectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
            }else {
                if(!TextUtils.isEmpty(session.getString(P.languageFlag))){
                    intent = new Intent(activity, LoginDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }else {
                    intent = new Intent(activity, LanguageSelectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
            }

            startActivity(intent);
            finish();
        }, 3000);
    }

    private void hitInit() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "init").addJson(j)
                .setMethod(Api.GET)
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
                        Config.countryJsonList = json.getJsonList(P.country_list);
                        checkValidation();
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitInit");
    }
}