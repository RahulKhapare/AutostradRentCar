package com.example.fastuae.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.AddressSelectionAdapter;
import com.example.fastuae.adapter.CodeSelectionAdapter;
import com.example.fastuae.adapter.DocumentAdapter;
import com.example.fastuae.adapter.DocumentListAdapter;
import com.example.fastuae.adapter.LocationAdapter;
import com.example.fastuae.adapter.PaymentCardAdapter;
import com.example.fastuae.databinding.ActivityCarBookingDetailBinding;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.model.AddressModel;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.model.HomeLocationModel;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.model.PaymentCardModel;
import com.example.fastuae.util.CheckString;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class CarBookingDetailsActivity extends AppCompatActivity implements LocationAdapter.onClick{

    private CarBookingDetailsActivity activity = this;
    private ActivityCarBookingDetailBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;
    private String payType = "";
    private String aedSelected = "";
    private CarModel model;

    private String pickupType = "deliver";
    private String dropupType = "collect";

    private int pickUpFlag = 1;
    private int dropUpFlag = 2;
    private String pickUpId = "";
    private String pickUpLocation = "";
    private String dropUpId = "";
    private String dropUpLocation = "";
    private String pickUpEmirateID = "";
    private String dropUpEmirateID = "";
    private String pickUpAddress = "";
    private String dropUpAddress = "";
    private String pickUpLandmark = "";
    private String dropUpLandmark = "";

    private List<HomeLocationModel> locationDeliverList;
    private List<HomeLocationModel> locationCollectList;
    private LocationAdapter locationDeliverAdapter;
    private LocationAdapter locationCollectAdapter;

    private List<PaymentCardModel> paymentCardModelList;
    private PaymentCardAdapter paymentCardAdapter;

    private String message1 = "'Total' does not include any additional items you may select at the location or any costs arising from the rental (such as damage, fuel or road traffic charges). For renters under the age of 25, additional charges may apply, and are payable at the location.";
    private String message2 = "By clicking on \"Pay & Confirm Booking\" you confirm that you understand and accept our , Rental Terms, Qualification and Requirements, Reservation Term & Conditions and you understand the Age Restrictions.";
    private String message3 = "I agree to the terms and conditions and acknowledge that this is a prepaid rate";

    private List<CountryCodeModel> countryCodeModelList;
    private List<AddressModel> lisAddressEmirate;
    private List<DocumentModel> documentModelList;

    private int positionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_booking_detail);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        payType = getIntent().getStringExtra(Config.PAY_TYPE);
        aedSelected = getIntent().getStringExtra(Config.SELECTED_AED);
        initView();
    }

    private void initView() {

        binding.toolbar.setTitle(getResources().getString(R.string.confirmBooking));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        message2 = message2.replace("Rental Terms, Qualification and Requirements, Reservation Term & Conditions", "<font color='#159dd8'>Rental Terms, Qualification and Requirements, Reservation Term & Conditions</font>");
        model = Config.carModel;
        binding.txtMessage1.setText(message1);
        binding.txtMessage2.setText(Html.fromHtml(message2));
        binding.txtMessage3.setText(message3);

        locationDeliverList = new ArrayList<>();
        locationDeliverAdapter = new LocationAdapter(activity, locationDeliverList, pickUpFlag);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        binding.recyclerDeliverLocation.setLayoutManager(linearLayoutManager1);
        binding.recyclerDeliverLocation.setHasFixedSize(true);
        binding.recyclerDeliverLocation.setAdapter(locationDeliverAdapter);

        locationCollectList = new ArrayList<>();
        locationCollectAdapter = new LocationAdapter(activity, locationCollectList, dropUpFlag);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(activity);
        binding.recyclerLocationCollect.setLayoutManager(linearLayoutManager2);
        binding.recyclerLocationCollect.setHasFixedSize(true);
        binding.recyclerLocationCollect.setAdapter(locationCollectAdapter);

        lisAddressEmirate = new ArrayList<>();
        AddressModel modelAddress1 = new AddressModel();
        modelAddress1.setCountry_name(getResources().getString(R.string.country2));
        lisAddressEmirate.add(modelAddress1);

        countryCodeModelList = new ArrayList<>();
        JsonList jsonList = Config.countryJsonList;
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            CountryCodeModel model = new CountryCodeModel();
            model.setId(json.getString(P.id));
            model.setCountry_name(json.getString(P.country_name));
            model.setPhone_code(json.getString(P.phone_code));
            countryCodeModelList.add(model);

            AddressModel modelAddress = new AddressModel();
            modelAddress.setId(json.getString(P.id));
            modelAddress.setCountry_name(json.getString(P.country_name));
            modelAddress.setPhone_code(json.getString(P.phone_code));
            lisAddressEmirate.add(modelAddress);

            if (model.getPhone_code().equalsIgnoreCase("971")) {
                positionNumber = i;
            }

        }

        CodeSelectionAdapter adapterOne = new CodeSelectionAdapter(activity, countryCodeModelList);
        binding.spinnerCodeMobile.setAdapter(adapterOne);
        binding.spinnerCodeMobile.setSelection(positionNumber);

        AddressSelectionAdapter adapterAddress = new AddressSelectionAdapter(activity, lisAddressEmirate);
        binding.spinnerAddress.setAdapter(adapterAddress);

        paymentCardModelList = new ArrayList<>();
        paymentCardAdapter = new PaymentCardAdapter(activity, paymentCardModelList);
        binding.recyclerCard.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCard.setNestedScrollingEnabled(false);
        binding.recyclerCard.setAdapter(paymentCardAdapter);

        documentModelList = new ArrayList<>();
        DocumentModel documentModel = new DocumentModel();
        documentModel.setDocumentName("GCC ID");
        documentModel.setDocumentDetails("Number - GCCID0000\nExpiry Date - 01/ 21");
        documentModelList.add(documentModel);
        documentModelList.add(documentModel);
        documentModelList.add(documentModel);
        documentModelList.add(documentModel);
        documentModelList.add(documentModel);
        DocumentListAdapter documentAdapter = new DocumentListAdapter(activity,documentModelList);
        binding.recyclerDocument.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerDocument.setHasFixedSize(true);
        binding.recyclerDocument.setNestedScrollingEnabled(true);
        binding.recyclerDocument.setAdapter(documentAdapter);

        updateIcons();
        setData();
        onClick();
        hitLocationData();
        hitUserPaymentDetails();
        hitPersonalDetails();
        hitAddressDetails();

    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgPersnalInfoRight.setVisibility(View.GONE);
            binding.imgPersnalInfoLeft.setVisibility(View.VISIBLE);

            binding.imgPaymentInfoRight.setVisibility(View.GONE);
            binding.imgPaymentInfoLeft.setVisibility(View.VISIBLE);

            binding.imgBillingInfoRight.setVisibility(View.GONE);
            binding.imgBillingInfoLeft.setVisibility(View.VISIBLE);

            binding.imgDocumentRight.setVisibility(View.GONE);
            binding.imgDocumentLeft.setVisibility(View.VISIBLE);

            binding.imgEmirateRight.setVisibility(View.GONE);
            binding.imgEmirateLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgPersnalInfoRight.setVisibility(View.VISIBLE);
            binding.imgPersnalInfoLeft.setVisibility(View.GONE);

            binding.imgPaymentInfoRight.setVisibility(View.VISIBLE);
            binding.imgPaymentInfoLeft.setVisibility(View.GONE);

            binding.imgBillingInfoRight.setVisibility(View.VISIBLE);
            binding.imgBillingInfoLeft.setVisibility(View.GONE);

            binding.imgDocumentRight.setVisibility(View.VISIBLE);
            binding.imgDocumentLeft.setVisibility(View.GONE);

            binding.imgEmirateRight.setVisibility(View.VISIBLE);
            binding.imgEmirateLeft.setVisibility(View.GONE);

        }

    }


    private void setData(){
        binding.txtTotalAED.setText("AED "+aedSelected);
    }

    @Override
    public void onLocationClick(String location, int flag, HomeLocationModel model) {

        if (flag == pickUpFlag) {
            pickUpId = model.getId();
            pickUpLocation = model.getLocation_name();
            pickUpEmirateID = model.getEmirate_id();
            binding.txtPickUpMessage.setText(location);
            pickUpAddress = model.getAddress();
            pickUpLandmark = model.getLocation_name();
            hideDeliverView();
            hideCollectView();
        } else if (flag == dropUpFlag) {
            dropUpId = model.getId();
            dropUpLocation = model.getLocation_name();
            dropUpEmirateID = model.getEmirate_id();
            binding.txtDropUpMessage.setText(location);
            dropUpAddress = model.getAddress();
            dropUpLandmark = model.getLocation_name();
            hideCollectView();
            hideDeliverView();
        }

    }


    private void onClick(){

        binding.radioDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                pickupType = "deliver";
                binding.radioSelfPickup.setChecked(false);
                blueTin(binding.radioDeliver);
                blackTin(binding.radioSelfPickup);
                binding.cardLocationDeliver.setVisibility(View.GONE);
                hideDeliverView();
                hideCollectView();
            }
        });

        binding.radioSelfPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                pickupType = "self-pickup";
                binding.radioDeliver.setChecked(false);
                blueTin(binding.radioSelfPickup);
                blackTin(binding.radioDeliver);
                binding.cardLocationDeliver.setVisibility(View.GONE);
                hideDeliverView();
                hideCollectView();
            }
        });

        binding.radioCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dropupType = "collect";
                binding.radioSelfReturn.setChecked(false);
                blueTin(binding.radioCollect);
                blackTin(binding.radioSelfReturn);
                binding.cardLocationCollect.setVisibility(View.GONE);
                hideCollectView();
                hideDeliverView();
            }
        });

        binding.radioSelfReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dropupType = "self-return";
                binding.radioCollect.setChecked(false);
                blueTin(binding.radioSelfReturn);
                blackTin(binding.radioCollect);
                binding.cardLocationCollect.setVisibility(View.GONE);
                hideCollectView();
                hideDeliverView();
            }
        });

        binding.cardPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardPickupClick();
                hideCollectView();

            }
        });

        binding.cardDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardDropUpClick();
                hideDeliverView();

            }
        });

        binding.lnrPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPersonalInfoView();
            }
        });

        binding.lnrPaymentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPaymentInfoView();
            }
        });

        binding.lnrBillingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBillingInfoView();
            }
        });

        binding.lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDocumentView();
            }
        });

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


        binding.lnrAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.lnrAddNewView.setVisibility(View.VISIBLE);
                binding.lnrDisplayView.setVisibility(View.GONE);
            }
        });

        binding.imgCancelAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.lnrAddNewView.setVisibility(View.GONE);
                binding.lnrDisplayView.setVisibility(View.VISIBLE);
            }
        });

        binding.checkSecure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        binding.txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkAddCardDetails()) {
                    hideKeyboard(activity);
                    hitAddUserPaymentDetails();
                }
            }
        });


    }

    private void hideDeliverView(){
        binding.cardLocationDeliver.setVisibility(View.GONE);
        binding.imgDeliverArrowUp.setVisibility(View.GONE);
        binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
    }

    private void hideCollectView(){
        binding.cardLocationCollect.setVisibility(View.GONE);
        binding.imgCollectArrowUp.setVisibility(View.GONE);
        binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
    }

    private void cardPickupClick() {
        if (binding.imgDeliverArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgDeliverArrowDown.setVisibility(View.GONE);
            binding.imgDeliverArrowUp.setVisibility(View.VISIBLE);
            binding.cardLocationDeliver.setVisibility(View.VISIBLE);

        } else if (binding.imgDeliverArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgDeliverArrowUp.setVisibility(View.GONE);
            binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
            binding.cardLocationDeliver.setVisibility(View.GONE);
        }
    }

    private void cardDropUpClick() {

        if (binding.imgCollectArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgCollectArrowDown.setVisibility(View.GONE);
            binding.imgCollectArrowUp.setVisibility(View.VISIBLE);
            binding.cardLocationCollect.setVisibility(View.VISIBLE);
        } else if (binding.imgCollectArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgCollectArrowUp.setVisibility(View.GONE);
            binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
            binding.cardLocationCollect.setVisibility(View.GONE);
        }

    }

    private void blackTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.grayDark)}
                    },
                    new int[]{getResources().getColor(R.color.grayDark)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void blueTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }


    private void hitLocationData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "location").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        JsonList location_list = json.getJsonList(P.location_list);
                        for (int i = 0; i < location_list.size(); i++) {
                            Json jsonData = location_list.get(i);
                            HomeLocationModel model = new HomeLocationModel();
                            model.setId(jsonData.getString(P.id));
                            model.setEmirate_id(jsonData.getString(P.emirate_id));
                            model.setEmirate_name(jsonData.getString(P.emirate_name));
                            model.setLocation_name(jsonData.getString(P.location_name));
                            model.setAddress(jsonData.getString(P.address));
                            model.setStatus(jsonData.getString(P.status));
                            model.setContact_number(jsonData.getString(P.contact_number));
                            model.setContact_email(jsonData.getString(P.contact_email));
                            model.setLocation_time_data(jsonData.getJsonList(P.location_time_data));
                            locationCollectList.add(model);
                            locationDeliverList.add(model);
                        }

                        locationCollectAdapter.notifyDataSetChanged();
                        locationDeliverAdapter.notifyDataSetChanged();

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");
    }

    private boolean checkAddCardDetails() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxCardNumber.getText().toString().trim())) {
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
    private void hitUserPaymentDetails() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "user_payments").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        paymentCardModelList.clear();
                        json = json.getJson(P.data);
                        JsonList jsonList = json.getJsonList(P.user_payment_option_list);
                        for (Json jsonData : jsonList){

                            PaymentCardModel model = new PaymentCardModel();

                            model.setId(jsonData.getString(P.id));
                            model.setUser_id(jsonData.getString(P.user_id));
                            model.setCard_number(jsonData.getString(P.card_number));
                            model.setName_on_card(jsonData.getString(P.name_on_card));
                            model.setExpiry_month(jsonData.getString(P.expiry_month));
                            model.setExpiry_year(jsonData.getString(P.expiry_year));
                            model.setCvv(jsonData.getString(P.cvv));
                            model.setAdd_date(jsonData.getString(P.add_date));
                            model.setUpdate_date(jsonData.getString(P.update_date));

                            paymentCardModelList.add(model);

                        }

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }

                    paymentCardAdapter.notifyDataSetChanged();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUserPaymentDetails",session.getString(P.token));

    }

    private void hitAddUserPaymentDetails() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.id,"");
        j.addString(P.card_number,binding.etxCardNumber.getText().toString().trim());
        j.addString(P.name_on_card,binding.etxCardName.getText().toString().trim());
        String inputMonthYear = binding.etxValidMonth.getText().toString().trim();
        String[] separated = inputMonthYear.split("/");
        String expireMonth = separated[0];
        String expireYear = separated[1];
        j.addString(P.expiry_month,expireMonth);
        j.addString(P.expiry_year,expireYear);
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

                        json = json.getJson(P.data);
                        H.showMessage(activity,getResources().getString(R.string.dataUpdated));
                        clearPaymentView();
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddUserPaymentDetails",session.getString(P.token));
    }

    private void clearPaymentView(){
        binding.etxCardNumber.setText("");
        binding.etxCardName.setText("");
        binding.etxValidMonth.setText("");
        binding.etxCvv.setText("");
        paymentCardAdapter.notifyDataSetChanged();
        binding.lnrAddNewView.setVisibility(View.GONE);
        binding.lnrDisplayView.setVisibility(View.VISIBLE);
        hitUserPaymentDetails();
    }

    private void hitPersonalDetails() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "user_data").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        json = json.getJson(P.user);

                        binding.etxFirstName.setText(CheckString.check(json.getString(P.user_name)));
                        binding.etxLastName.setText(CheckString.check(json.getString(P.user_lastname)));
                        binding.etxEmail.setText(CheckString.check(json.getString(P.user_email)));
                        binding.etxNumber.setText(CheckString.check(json.getString(P.user_mobile)));


                        String codePrimary = CheckString.check(json.getString(P.user_country_code));
                        String codeSecondary = CheckString.check(json.getString(P.user_alt_country_code));

                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.phone_code).equalsIgnoreCase(codePrimary)) {
                                binding.spinnerCodeMobile.setSelection(i);
                            }
                        }

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitPersonalDetails",session.getString(P.token));

    }

    private void hitAddressDetails() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "user_bill_data").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        json = json.getJson(P.bill_info);
                        String bill_address_line_1 = json.getString(P.bill_address_line_1);
                        String bill_address_line_2 = json.getString(P.bill_address_line_2);
                        String bill_city = json.getString(P.bill_city);
                        String bill_state = json.getString(P.bill_state);
                        String bill_country_id = json.getString(P.bill_country_id);
                        String bill_zipcode = json.getString(P.bill_zipcode);

                        binding.etxAddress.setText(bill_address_line_1);
                        binding.etxZipcode.setText(bill_zipcode);
                        binding.etxCity.setText(bill_city);
                        binding.etxState.setText(bill_state);

                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.id).equals(bill_country_id)) {
                                binding.spinnerAddress.setSelection(i+1);
                            }
                        }
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddressDetails",session.getString(P.token));

    }

    private void checkPersonalInfoView() {
        if (binding.viewPersonalInfo.getVisibility() == View.VISIBLE) {
            binding.viewPersonalInfo.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPersnalInfoLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPersnalInfoRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewPersonalInfo.getVisibility() == View.GONE) {
            binding.viewPersonalInfo.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPersnalInfoLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPersnalInfoRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    private void checkPaymentInfoView() {
        if (binding.viewPaymentInfo.getVisibility() == View.VISIBLE) {
            binding.viewPaymentInfo.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPaymentInfoLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPaymentInfoRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewPaymentInfo.getVisibility() == View.GONE) {
            binding.viewPaymentInfo.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPaymentInfoLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPaymentInfoRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    private void checkBillingInfoView() {
        if (binding.viewBillingInfo.getVisibility() == View.VISIBLE) {
            binding.viewBillingInfo.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgBillingInfoLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgBillingInfoRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewBillingInfo.getVisibility() == View.GONE) {
            binding.viewBillingInfo.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgBillingInfoLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgBillingInfoRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    private void checkDocumentView() {
        if (binding.viewDocument.getVisibility() == View.VISIBLE) {
            binding.viewDocument.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDocumentLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDocumentRight.setImageResource(R.drawable.ic_plus);
            }
        } else if (binding.viewDocument.getVisibility() == View.GONE) {
            binding.viewDocument.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDocumentLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDocumentRight.setImageResource(R.drawable.ic_minus);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}