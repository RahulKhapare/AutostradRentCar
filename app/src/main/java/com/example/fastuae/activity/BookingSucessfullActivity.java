package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityBookingSucessfullBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.WindowView;

public class BookingSucessfullActivity extends AppCompatActivity {

    private BookingSucessfullActivity activity = this;
    private ActivityBookingSucessfullBinding binding;
    private String webUrl;
    private String payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_sucessfull);
        initView();
    }

    private void initView() {

        webUrl = getIntent().getStringExtra(Config.WEB_URL);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);

        if (payType.equals(Config.pay_now)) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.lnrSuccess.setVisibility(View.GONE);
        } else if (payType.equals(Config.pay_latter)) {
            binding.webView.setVisibility(View.GONE);
            binding.lnrSuccess.setVisibility(View.VISIBLE);
        }

        binding.webView.setWebViewClient(new WebClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(webUrl);

        onClick();
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("TAG", "onPageFinished: " + url);
            if (url.contains("payment_success")) {
                continueShoppingIntent();
            }
        }
    }


    private void continueShoppingIntent() {
        binding.webView.setVisibility(View.GONE);
        binding.lnrSuccess.setVisibility(View.VISIBLE);
    }

    private void onClick() {

        binding.txtBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                goToHome();
            }
        });

        binding.txtViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                goToHome();
            }
        });
    }

    private void goToHome() {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.lnrSuccess.getVisibility()==View.VISIBLE){
            goToHome();
        }else {
            if (binding.webView.canGoBack()){
                binding.webView.goBack();
            }else {
                finish();
            }
        }
    }
}