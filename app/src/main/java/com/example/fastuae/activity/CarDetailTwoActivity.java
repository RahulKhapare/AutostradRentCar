package com.example.fastuae.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarDetailTwoBinding;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;
import com.squareup.picasso.Picasso;

public class CarDetailTwoActivity extends AppCompatActivity {

    private CarDetailTwoActivity activity = this;
    private ActivityCarDetailTwoBinding binding;
    private Session session;
    private LoadingDialog loadingDialog;
    private String flag;
    private String payType = "";
    private String aedSelected = "";
    private CarModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_detail_two);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);
        aedSelected = getIntent().getStringExtra(Config.SELECTED_AED);
        Config.isEditDetails = false;
        initView();
    }

    private void initView(){
        binding.toolbar.setTitle(getResources().getString(R.string.carDetails));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        updateIcons();
        setData();
        onClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.isEditDetails){
            setInformation(true);
        }
    }

    private void clearData(){
        Config.firstName  = "";
        Config.lastName  = "";
        Config.email  = "";
        Config.code  = "";
        Config.phone  = "";
        Config.positionCode  = 0;
    }

    private void setData(){

        model = Config.carModel;
        Picasso.get().load(model.getCar_image()).error(R.drawable.ic_image).into(binding.imgCar);

        binding.txtConfirmPayment.setText(getResources().getString(R.string.confirmPayment)+ " : AED " + aedSelected);
        binding.txtCarName.setText(model.getCar_name());
        binding.txtFees.setText("Reservation Fee: AED " + aedSelected);
        setInformation(false);

        if (flag.equals(Config.ARABIC)){
            binding.txtPhoneNo.setGravity(Gravity.LEFT);
        }
    }

    private void setInformation(boolean setData){

        String name = session.getString(P.user_name);
        String email = session.getString(P.user_email);
        String number = session.getString(P.user_mobile);

        if (TextUtils.isEmpty(binding.txtFirstName.getText().toString())){
            Config.firstName = name;
        }

        if (TextUtils.isEmpty(binding.txtEmailName.getText().toString())){
            Config.email = email;
        }

        if (TextUtils.isEmpty(binding.txtPhoneNo.getText().toString())){
            Config.phone = number;
        }

        binding.txtFirstName.setText(Config.firstName);
        binding.txtLastName.setText(Config.lastName);
        binding.txtEmailName.setText(Config.email);
        if (!TextUtils.isEmpty(Config.code) && !TextUtils.isEmpty(Config.phone)){
            binding.txtPhoneNo.setText(Config.code+ "-"+Config.phone);
        }else if (TextUtils.isEmpty(Config.code) && !TextUtils.isEmpty(Config.phone)){
            binding.txtPhoneNo.setText(Config.phone);
        }

    }

    private void onClick(){

        binding.etxValidMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if ('/' == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf("/")).length <= 2) {
                        editable.insert(editable.length() - 1, String.valueOf("/"));
                    }
                }
            }
        });

        binding.checkSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity,EditInformationActivity.class);
                startActivity(intent);
            }
        });

        binding.lnrUploadLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.lnrPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPayentView();
            }
        });

        binding.txtConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.viewPaymentDetails.getVisibility()==View.VISIBLE){
                    if (checkAddCardDetails()) {
                        hideKeyboard(activity);
                        Intent intent = new Intent(activity,CarDetailsActivityThree.class);
                        intent.putExtra(Config.cardNumber,binding.etxCardNumber.getText().toString().trim());
                        intent.putExtra(Config.cardName,binding.etxCardName.getText().toString().trim());
                        intent.putExtra(Config.cardValidMonth,binding.etxValidMonth.getText().toString().trim());
                        intent.putExtra(Config.cardValidCVV,binding.etxCvv.getText().toString().trim());
                        intent.putExtra(Config.PAY_TYPE,payType);
                        intent.putExtra(Config.SELECTED_AED,aedSelected);
                        startActivity(intent);
                    }
                }else {
                    H.showMessage(activity,getResources().getString(R.string.checkPaymentDetails));
                }
            }
        });
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgPaymentRight.setVisibility(View.GONE);
            binding.imgPaymentLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgPaymentRight.setVisibility(View.VISIBLE);
            binding.imgPaymentLeft.setVisibility(View.GONE);

        }
    }

    private void checkPayentView() {
        if (binding.viewPaymentDetails.getVisibility() == View.VISIBLE) {
            binding.viewPaymentDetails.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPaymentLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPaymentRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewPaymentDetails.getVisibility() == View.GONE) {
            binding.viewPaymentDetails.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPaymentLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPaymentRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private boolean checkAddCardDetails() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.txtFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterFirstName));
        } else if (TextUtils.isEmpty(binding.txtLastName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterLastName));
        }else if (TextUtils.isEmpty(binding.txtEmailName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailId));
        }else if (TextUtils.isEmpty(binding.txtPhoneNo.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailId));
        }else if (TextUtils.isEmpty(binding.etxCardNumber.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterCardNumber));
        } else if (binding.etxCardNumber.getText().toString().trim().length() < 16 || binding.etxCardNumber.getText().toString().trim().length() > 16) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterValidCarNumber));
        } else if (TextUtils.isEmpty(binding.etxCardName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterCardName));
        } else if (TextUtils.isEmpty(binding.etxValidMonth.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterMonth));
        } else if (binding.etxValidMonth.getText().toString().trim().length() < 5 || binding.etxValidMonth.getText().toString().trim().length() > 5) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterValidMonth));
        } else if (!binding.etxValidMonth.getText().toString().matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.checkCardFormat));
        } else if (TextUtils.isEmpty(binding.etxCvv.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterCvv));
        } else if (binding.etxCvv.getText().toString().trim().length() < 3 || binding.etxCvv.getText().toString().trim().length() > 3) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterValidCvv));
        }

        return value;
    }

    public void hideKeyboard(Context mContext) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                    .getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        clearData();
        finish();
    }
}