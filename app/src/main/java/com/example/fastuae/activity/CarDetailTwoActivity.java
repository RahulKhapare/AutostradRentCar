package com.example.fastuae.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.AddressSelectionAdapter;
import com.example.fastuae.adapter.AddressSelectionLightAdapter;
import com.example.fastuae.databinding.ActivityCarDetailTwoBinding;
import com.example.fastuae.model.AddressModel;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarDetailTwoActivity extends AppCompatActivity {

    private CarDetailTwoActivity activity = this;
    private ActivityCarDetailTwoBinding binding;
    private Session session;
    private LoadingDialog loadingDialog;
    private String flag;
    private String payType = "";
    private String aedSelected = "";
    private String paymentID = "";
    private String countryID = "";
    private CarModel model;
    private List<AddressModel> listCountry;
    private static final int READ_WRIRE = 13;
    private static final int REQUEST_DOCUMENT = 9;

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

        Log.e("TAG", "initViewDDDD: " + session.getString(P.token) );
        binding.toolbar.setTitle(getResources().getString(R.string.booking));
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
            setInformation();
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

        listCountry = new ArrayList<>();
        AddressModel countryModel = new AddressModel();
        countryModel.setCountry_name(getResources().getString(R.string.country));
        listCountry.add(countryModel);
        JsonList jsonList = Config.countryJsonList;
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            AddressModel model = new AddressModel();
            model.setId(json.getString(P.id));
            model.setCountry_name(json.getString(P.country_name));
            model.setPhone_code(json.getString(P.phone_code));
            listCountry.add(model);
        }

        AddressSelectionLightAdapter adapterEmirate = new AddressSelectionLightAdapter(activity, listCountry);
        binding.spinnerCountry.setAdapter(adapterEmirate);

        model = Config.carModel;
        Picasso.get().load(model.getCar_image()).error(R.drawable.ic_image).into(binding.imgCar);

        binding.txtConfirmPayment.setText(getResources().getString(R.string.confirmPayment)+ " : AED " + aedSelected);
        binding.txtCarName.setText(model.getCar_name());
        binding.txtFees.setText("Reservation Fee: AED " + aedSelected);
        setInformation();

        if (flag.equals(Config.ARABIC)){
            binding.txtPhoneNo.setGravity(Gravity.LEFT);
        }

        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddressModel model = listCountry.get(position);
                if (TextUtils.isEmpty(model.getId())){
                    countryID = "";
                }else {
                    countryID =  model.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setInformation(){

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
                getPermission();
            }
        });

        binding.lnrPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPayentView();
            }
        });

        binding.lnrBillingInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBillingView();
            }
        });

        binding.txtConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.viewPaymentDetails.getVisibility()==View.VISIBLE){
                    if (checkAddCardDetails()) {
                        hideKeyboard(activity);
                        hitAddPaymentData();
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

            binding.imgBillingRight.setVisibility(View.GONE);
            binding.imgBillingLeft.setVisibility(View.VISIBLE);

            binding.imgCountryRight.setVisibility(View.GONE);
            binding.imgCountryLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgPaymentRight.setVisibility(View.VISIBLE);
            binding.imgPaymentLeft.setVisibility(View.GONE);

            binding.imgBillingRight.setVisibility(View.VISIBLE);
            binding.imgBillingLeft.setVisibility(View.GONE);

            binding.imgCountryRight.setVisibility(View.VISIBLE);
            binding.imgCountryLeft.setVisibility(View.GONE);

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

    private void checkBillingView() {
        if (binding.viewBillingDetails.getVisibility() == View.VISIBLE) {
            binding.viewBillingDetails.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgBillingLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgBillingRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewBillingDetails.getVisibility() == View.GONE) {
            binding.viewBillingDetails.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgBillingLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgBillingRight.setImageResource(R.drawable.ic_up_arrow);
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
        }else if (binding.viewBillingDetails.getVisibility()==View.GONE){
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterBilling));
        }else if (TextUtils.isEmpty(binding.etxAddressOne.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterAddress1));
        }else if (TextUtils.isEmpty(binding.etxAddressTwo.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterAddress2));
        }else if (TextUtils.isEmpty(countryID)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectCountry));
        }else if (TextUtils.isEmpty(binding.etxCity.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterCity));
        }else if (TextUtils.isEmpty(binding.etxZipCode.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterZipCode));
        }

        return value;
    }

    private void hitAddPaymentData() {

        String[] separated = binding.etxValidMonth.getText().toString().split("/");
        String cardMonth = separated[0];
        String cardYear = separated[1];

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.id,"");
        j.addString(P.card_number,binding.etxCardNumber.getText().toString().trim());
        j.addString(P.name_on_card,binding.etxCardName.getText().toString().trim());
        j.addString(P.expiry_month,cardMonth);
        j.addString(P.expiry_year,cardYear);
        j.addString(P.cvv,binding.etxCvv.getText().toString().trim());

        Api.newApi(activity, P.BaseUrl + "add_user_payment_option").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Json jsonData = json.getJson(P.data);
                        Json detailJson = jsonData.getJson(P.user_payment_option);

                        paymentID = detailJson.getString(P.id);
                        Intent intent = new Intent(activity,CarDetailsActivityThree.class);
                        intent.putExtra(Config.cardNumber,binding.etxCardNumber.getText().toString().trim());
                        intent.putExtra(Config.cardName,binding.etxCardName.getText().toString().trim());
                        intent.putExtra(Config.cardValidMonth,binding.etxValidMonth.getText().toString().trim());
                        intent.putExtra(Config.cardValidCVV,binding.etxCvv.getText().toString().trim());
                        intent.putExtra(Config.PAY_TYPE,payType);
                        intent.putExtra(Config.PAY_ID,paymentID);
                        intent.putExtra(Config.SELECTED_AED,aedSelected);
                        intent.putExtra(Config.cityName,binding.etxCity.getText().toString().trim());
                        intent.putExtra(Config.countryID,countryID);
                        intent.putExtra(Config.zipCode,binding.etxZipCode.getText().toString().trim());
                        intent.putExtra(Config.address1,binding.etxAddressOne.getText().toString().trim());
                        intent.putExtra(Config.address2,binding.etxAddressTwo.getText().toString().trim());
                        startActivity(intent);

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddPaymentData",session.getString(P.token));
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

    private void getPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                READ_WRIRE);
    }

    private void jumpToSetting() {
        H.showMessage(activity, getResources().getString(R.string.permissionAllow));
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRIRE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openDocumentView();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_DOCUMENT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        binding.txtDrivingLicence.setText(getResources().getString(R.string.reUploadDriverLicence));
                        Uri uri = data.getData();
                        openViewPDFDialog(getBase64PDF(uri));
                    } catch (Exception e) {
                    }
                }
                break;
        }
    }

    private String getBase64PDF(Uri filepath){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream =  getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }

    private void storeAndOpenPDF(Context context, String base) {


        String root = Environment.getExternalStorageDirectory().toString();

        Log.d("ResponseEnv",root);

        File myDir = new File(root + "/FastCar");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String fname = "Attachments-" + n + ".pdf";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {

            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = Base64.decode(base, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        File dir = new File(Environment.getExternalStorageDirectory(), "FastCar");
        File imgFile = new File(dir, fname);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);

        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = Uri.parse("file://" + imgFile); // My work-around for new SDKs, causes ActivityNotFoundException in API 10.
        }

        sendIntent.setDataAndType(uri, "application/pdf");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(sendIntent);

    }


    private void openDocumentView() {
        try {
            Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
            intentPDF.setType("application/pdf");
            intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intentPDF , "Select Document"), REQUEST_DOCUMENT);
        } catch (Exception e) {
        }
    }

    private void openViewPDFDialog(String base64Path){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(getResources().getString(R.string.openDocumentMessage));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        storeAndOpenPDF(activity,base64Path);
                    }
                });

        builder1.setNegativeButton(
                getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        Button buttonColor1 = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonColor1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        Button buttonColor2 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonColor2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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