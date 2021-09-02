package com.autostrad.rentcar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.AEDAdapter;
import com.autostrad.rentcar.adapter.AddressSelectionAdapter;
import com.autostrad.rentcar.adapter.CodeSelectionAdapter;
import com.autostrad.rentcar.adapter.DocumentAdapter;
import com.autostrad.rentcar.adapter.DocumentListAdapter;
import com.autostrad.rentcar.adapter.EmirateAdapter;
import com.autostrad.rentcar.adapter.PaymentCardAdapter;
import com.autostrad.rentcar.databinding.ActivityCarBookingDetailBinding;
import com.autostrad.rentcar.model.AEDModel;
import com.autostrad.rentcar.model.AddressModel;
import com.autostrad.rentcar.model.CarModel;
import com.autostrad.rentcar.model.CountryCodeModel;
import com.autostrad.rentcar.model.DocumentModel;
import com.autostrad.rentcar.model.DocumentUploadedModel;
import com.autostrad.rentcar.model.EmirateModel;
import com.autostrad.rentcar.model.FieldModel;
import com.autostrad.rentcar.model.ImagePathModel;
import com.autostrad.rentcar.model.PaymentCardModel;
import com.autostrad.rentcar.util.CheckString;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.Validation;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarBookingDetailsActivity extends AppCompatActivity implements EmirateAdapter.onClick, DocumentListAdapter.onClick,
        PaymentCardAdapter.onClick, PaymentCardAdapter.onView, DocumentAdapter.onClick {

    private CarBookingDetailsActivity activity = this;
    private ActivityCarBookingDetailBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;
    private String payType = "";
    private String aedSelected = "";
    private CarModel model;
    private String codePrimary = "";
    private String codeSecondary = "";
    private String countryId = "";

    private DatePickerDialog mDatePickerDialog;

    private List<PaymentCardModel> paymentCardModelList;
    private PaymentCardAdapter paymentCardAdapter;

    private List<CountryCodeModel> countryCodeModelList;
    private List<AddressModel> lisAddressEmirate;
    private List<DocumentUploadedModel> documentUploadedModelList;
    private DocumentListAdapter documentUploadedAdapter;

    private List<DocumentModel> documentModelList;
    private DocumentAdapter documentAdapter;

    private int positionNumber = 0;

    private List<EmirateModel> deliverEmirateList;
    private List<EmirateModel> collectEmirateList;
    private EmirateAdapter deliverEmirateAdapter;
    private EmirateAdapter collectEmirateAdapter;
    private int deliverEmirateFlag = 1;
    private int collectEmirateFlag = 2;
    private String deleveryEmirateName = "";
    private String deleveryEmirateID = "";
    private String collectEmirateName = "";
    private String collectEmirateID = "";
    private boolean forDeliveryLocation = false;
    private boolean forCollectLocation = false;
    private String deliveryLat = "";
    private String deliveryLogn = "";
    private String collectLat = "";
    private String collectLogn = "";
    private String deliveryAddress = "";
    private String collectAddress = "";
    private String deliverType = "";
    private String collectType = "";

    private String paymentId = "";
    private String user_country_id = "";
    String documentName;
    String base64Image;
    TextView textViewDocumnt;
    TextView txtImagePath;
    private static final int READ_WRITE = 20;
    private static final int PDF_DATA = 22;
    private List<ImagePathModel> imagePathModelList;

//    Json jsonMain;
    Json jsonChild;
    boolean updateFlag = true;

    String personalCountryId;
    String personalEmirateId;

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

        getAccess();

        binding.toolbar.setTitle(getResources().getString(R.string.confirmBooking));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        model = Config.carModel;

        imagePathModelList = new ArrayList<>();

        String message2 = getResources().getString(R.string.bookingMsg2);
        if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            message2 = message2.replace("شروط الإيجار والمؤهلات والمتطلبات وشروط وأحكام الحجز", "<font color='#159dd8'>شروط الإيجار والمؤهلات والمتطلبات وشروط وأحكام الحجز</font>");
        }else if (session.getString(P.languageFlag).equals(Config.ENGLISH)){
            message2 = message2.replace("Rental Terms, Qualification and Requirements, Reservation Term & Conditions", "<font color='#159dd8'>Rental Terms, Qualification and Requirements, Reservation Term & Conditions</font>");
        }
        binding.txtMessage2.setText(Html.fromHtml(message2));

        deliverEmirateList = new ArrayList<>();
        deliverEmirateAdapter = new EmirateAdapter(activity, deliverEmirateList, deliverEmirateFlag);
        binding.recyclerDeliverEmirate.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerDeliverEmirate.setHasFixedSize(true);
        binding.recyclerDeliverEmirate.setNestedScrollingEnabled(false);
        binding.recyclerDeliverEmirate.setAdapter(deliverEmirateAdapter);

        collectEmirateList = new ArrayList<>();
        collectEmirateAdapter = new EmirateAdapter(activity, collectEmirateList, collectEmirateFlag);
        binding.recyclerCollectEmirate.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCollectEmirate.setHasFixedSize(true);
        binding.recyclerCollectEmirate.setNestedScrollingEnabled(false);
        binding.recyclerCollectEmirate.setAdapter(collectEmirateAdapter);


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

        CodeSelectionAdapter adapterTwo = new CodeSelectionAdapter(activity, countryCodeModelList);
        binding.spinnerCodeAlternate.setAdapter(adapterTwo);
        binding.spinnerCodeAlternate.setSelection(positionNumber);

        AddressSelectionAdapter adapterAddress = new AddressSelectionAdapter(activity, lisAddressEmirate);
        binding.spinnerAddress.setAdapter(adapterAddress);

        paymentCardModelList = new ArrayList<>();
        paymentCardAdapter = new PaymentCardAdapter(activity, paymentCardModelList, 2);
        binding.recyclerCard.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCard.setNestedScrollingEnabled(false);
        binding.recyclerCard.setAdapter(paymentCardAdapter);

        documentUploadedModelList = new ArrayList<>();
        documentUploadedAdapter = new DocumentListAdapter(activity, documentUploadedModelList);
        binding.recyclerUploadedDocument.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerUploadedDocument.setHasFixedSize(true);
        binding.recyclerUploadedDocument.setNestedScrollingEnabled(true);
        binding.recyclerUploadedDocument.setAdapter(documentUploadedAdapter);

        documentModelList = new ArrayList<>();
        documentAdapter = new DocumentAdapter(activity, documentModelList, 1);
        binding.recyclerDocument.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerDocument.setNestedScrollingEnabled(false);
        binding.recyclerDocument.setAdapter(documentAdapter);

        setDateTimeField(binding.etxBirtDate);
        binding.etxBirtDate.setFocusable(false);
        binding.etxBirtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calender_bg, 0);
        binding.etxBirtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });


        updateIcons();
        setData();
        hitPersonalDetails();
        hitUserPaymentDetails();
        hitAddressDetails();
        hitEmirateData();
        hitUserDocumentDetails();
        onClick();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.FROM_MAP) {
            Config.FROM_MAP = false;
            if (forDeliveryLocation) {
                forDeliveryLocation = false;
                binding.txtDeliverGoogleAddress.setVisibility(View.VISIBLE);
                binding.txtDeliverGoogleAddress.setText(session.getString(P.locationAddress));
                deliveryAddress = session.getString(P.locationAddress);
                deliveryLat = session.getString(P.lat);
                deliveryLogn = session.getString(P.logn);
            } else if (forCollectLocation) {
                forCollectLocation = false;
                binding.txtCollectGoogleAddress.setVisibility(View.VISIBLE);
                binding.txtCollectGoogleAddress.setText(session.getString(P.locationAddress));
                collectAddress = session.getString(P.locationAddress);
                collectLat = session.getString(P.lat);
                collectLogn = session.getString(P.logn);
            }
        }
    }

    private void setData() {

        deliverType = SelectCarActivity.pickUpType;
        collectType = SelectCarActivity.dropUpType;

        if (Config.HOME_DELIVERY_CHECK) {
            binding.radioDeliverYes.setChecked(true);
            binding.radioDeliverNo.setChecked(false);
            blueTin(binding.radioDeliverYes);
            blackTin(binding.radioDeliverNo);
            binding.radioDeliverNo.setEnabled(false);
        } else {
            disablebleDelverView();
        }

        if (Config.HOME_COLLECT_CHECK) {
            binding.radioCollectYes.setChecked(true);
            binding.radioCollectNo.setChecked(false);
            blueTin(binding.radioCollectYes);
            blackTin(binding.radioCollectNo);
            binding.radioCollectNo.setEnabled(false);
        } else {
            disablebleCollectView();
        }

        binding.txtTotalAED.setText(getResources().getString(R.string.aed) + " " + aedSelected);

    }


    @Override
    public void onEmirateClick(int flag, EmirateModel model) {
        if (flag == deliverEmirateFlag) {
            binding.txtDeliverEmirateMessage.setVisibility(View.VISIBLE);
            binding.txtDeliverEmirateMessage.setText(model.getEmirate_name());
            hideDeliverEmirate();
            deleveryEmirateName = model.getEmirate_name();
            deleveryEmirateID = model.getId();
        } else if (flag == collectEmirateFlag) {
            binding.txtCollectEmirateMessage.setVisibility(View.VISIBLE);
            binding.txtCollectEmirateMessage.setText(model.getEmirate_name());
            hideCollectEmirate();
            collectEmirateName = model.getEmirate_name();
            collectEmirateID = model.getId();
        }

    }


    private void onClick() {

        binding.radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioFemale.setChecked(false);
                blueTin(binding.radioMale);
                blackTin(binding.radioFemale);
            }
        });

        binding.radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioMale.setChecked(false);
                blueTin(binding.radioFemale);
                blackTin(binding.radioMale);
            }
        });

        binding.spinnerCodeMobile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                codePrimary = model.getPhone_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerCodeAlternate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                codeSecondary = model.getPhone_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddressModel model = lisAddressEmirate.get(position);
                if (!model.getCountry_name().equals(getResources().getString(R.string.country))) {
                    countryId = model.getId();
                } else {
                    countryId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.radioDeliverYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                deliverType = Config.deliver;
                binding.radioDeliverNo.setChecked(false);
                blueTin(binding.radioDeliverYes);
                blackTin(binding.radioDeliverNo);
                enableDelverView();
            }
        });

        binding.radioDeliverNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                deliverType = Config.self_pickup;
                binding.radioDeliverYes.setChecked(false);
                blueTin(binding.radioDeliverNo);
                blackTin(binding.radioDeliverYes);
                disablebleDelverView();
                binding.txtDeliverGoogleAddress.setText("");
                binding.txtDeliverGoogleAddress.setVisibility(View.GONE);
            }
        });

        binding.radioCollectYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                collectType = Config.collect;
                binding.radioCollectNo.setChecked(false);
                blueTin(binding.radioCollectYes);
                blackTin(binding.radioCollectNo);
                enableCollectView();
            }
        });


        binding.radioCollectNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                collectType = Config.self_dropoff;
                binding.radioCollectYes.setChecked(false);
                blueTin(binding.radioCollectNo);
                blackTin(binding.radioCollectYes);
                disablebleCollectView();
                binding.txtCollectGoogleAddress.setText("");
                binding.txtCollectGoogleAddress.setVisibility(View.GONE);
            }
        });

        binding.cardDeliverEmirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.radioDeliverYes.isChecked()) {
                    cardDeliverEmirateClick();
                    hideCollectEmirate();
                }
            }
        });

        binding.cardCollectEmirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.radioCollectYes.isChecked()) {
                    cardCollectEmirateClick();
                    hideDeliverEmirate();
                }
            }
        });

        binding.txtDeliverSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.radioDeliverYes.isChecked()) {
                    forDeliveryLocation = true;
                    forCollectLocation = false;
                    Intent intent = new Intent(activity, SelectLocationActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.txtCollectSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.radioCollectYes.isChecked()) {
                    forCollectLocation = true;
                    forDeliveryLocation = false;
                    Intent intent = new Intent(activity, SelectLocationActivity.class);
                    startActivity(intent);
                }
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

        binding.txtConfirmBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                if (checkPersonalDetailValidation()) {
                    if (TextUtils.isEmpty(paymentId)) {
                        H.showMessage(activity, getResources().getString(R.string.selectPaymentMethod));
                        checkPaymentErrorView();
                    } else {
                        if (checkAddressDetailValidation()) {
                            if (documentModelList.isEmpty()) {
                                hitUpdatePersonalDetails();
                            } else {
                                checkDocumentValidation();
                            }
                        }
                    }
                }
            }
        });

    }

    private void checkDocumentValidation() {

        updateFlag = true;

//        jsonMain = new Json();
        jsonChild = new Json();

        try {
            for (DocumentModel model : documentModelList) {

                if (model.getCheckValue().equals("1")) {
//                    jsonMain.addString(model.getKey(), "1");
                    for (FieldModel fieldModel : model.getFieldList()) {

                        for (ImagePathModel imageModel : imagePathModelList) {
                            if (imageModel.getTitle().equals(model.getTitle())) {
                                if (fieldModel.getJson().has(P.image)) {
                                    fieldModel.getJson().remove(P.image);
                                    fieldModel.getJson().addString(P.image, imageModel.getPath());
                                } else {
                                    fieldModel.getJson().addString(P.image, imageModel.getPath());
                                }
                            }
                        }

                        jsonChild.addJSON(model.getKey(), fieldModel.getJson());

                        fieldModel.getJson().addString("key","1");

                        if (!fieldModel.getJson().has(fieldModel.getFiled())) {
                            String valueKey = fieldModel.getFiled().replace("_", " ");
                            H.showMessage(activity, getResources().getString(R.string.enter) + " " + capitalize(valueKey));
                            updateFlag = false;
                            checkDocumentErrorView();
                            return;
                        }

                        if (!fieldModel.getJson().has("image")) {
                            H.showMessage(activity, getResources().getString(R.string.selectImage)+ " " + model.getTitle());
                            updateFlag = false;
                            checkDocumentErrorView();
                            return;
                        }

                    }

                } else {
                    H.showMessage(activity, getResources().getString(R.string.select) + " " + model.getTitle());
                    updateFlag = false;
                    checkDocumentErrorView();
                    return;
                }

//                jsonMain.addJSON(P.document, jsonChild);

            }

            if (updateFlag) {
                hitUpdatePersonalDetails();
            }

        } catch (Exception e) {
            H.showMessage(activity, getResources().getString(R.string.somethingWrong));
        }

//        Log.e("TAG", "onClickJSJJS: " + jsonChild.toString());
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
        }
        else if (TextUtils.isEmpty(binding.etxCvv.getText().toString().trim())) {
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
        } catch (Exception e) {

        }
    }

    private void hitUserPaymentDetails() {

        ProgressView.show(activity, loadingDialog);
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
                        for (Json jsonData : jsonList) {

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

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    paymentCardAdapter.notifyDataSetChanged();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUserPaymentDetails", session.getString(P.token));

    }

    private void hitAddUserPaymentDetails() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.id, "");
        j.addString(P.card_number, binding.etxCardNumber.getText().toString().trim());
        j.addString(P.name_on_card, binding.etxCardName.getText().toString().trim());
        String inputMonthYear = binding.etxValidMonth.getText().toString().trim();
        String[] separated = inputMonthYear.split("/");
        String expireMonth = separated[0];
        String expireYear = separated[1];
        j.addString(P.expiry_month, expireMonth);
        j.addString(P.expiry_year, expireYear);
        j.addString(P.cvv, binding.etxCvv.getText().toString().trim());

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
                        H.showMessage(activity, getResources().getString(R.string.dataUpdated));
                        clearPaymentView();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddUserPaymentDetails", session.getString(P.token));
    }

    private void clearPaymentView() {
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

        ProgressView.show(activity, loadingDialog);
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

                        personalCountryId = json.getString(P.country_id);
                        personalEmirateId = json.getString(P.emirate_id);

                        binding.etxFirstName.setText(CheckString.check(json.getString(P.user_name)));
                        binding.etxLastName.setText(CheckString.check(json.getString(P.user_lastname)));
                        binding.etxEmail.setText(CheckString.check(json.getString(P.user_email)));
                        binding.etxBirtDate.setText(CheckString.check(json.getString(P.user_dob)));
                        binding.etxNumber.setText(CheckString.check(json.getString(P.user_mobile)));
                        binding.etxAlternameNumber.setText(CheckString.check(json.getString(P.user_alt_mobile)));

                        String gender = json.getString(P.gender);
                        if (CheckString.check(gender).equalsIgnoreCase("1")) {
                            binding.radioFemale.setChecked(false);
                            binding.radioMale.setChecked(true);
                            blueTin(binding.radioMale);
                            blackTin(binding.radioFemale);
                        } else if (CheckString.check(gender).equalsIgnoreCase("2")) {
                            binding.radioMale.setChecked(false);
                            binding.radioFemale.setChecked(true);
                            blueTin(binding.radioFemale);
                            blackTin(binding.radioMale);
                        }

                        String code1 = CheckString.check(json.getString(P.user_country_code));
                        String code2 = CheckString.check(json.getString(P.user_alt_country_code));

                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.phone_code).equalsIgnoreCase(code1)) {
                                binding.spinnerCodeMobile.setSelection(i);
                                codePrimary = code1;
                            }

                            if (jsonData.getString(P.phone_code).equalsIgnoreCase(code2)) {
                                binding.spinnerCodeAlternate.setSelection(i);
                                codeSecondary = code2;
                            }
                        }

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitPersonalDetails", session.getString(P.token));

    }

    private void hitAddressDetails() {

        ProgressView.show(activity, loadingDialog);
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

                        binding.etxAddress.setText(checkString(bill_address_line_1));
                        binding.etxZipcode.setText(checkString(bill_zipcode));
                        binding.etxCity.setText(checkString(bill_city));
                        binding.etxState.setText(checkString(bill_state));

                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.id).equals(bill_country_id)) {
                                binding.spinnerAddress.setSelection(i + 1);
                                countryId = bill_country_id;
                            }
                        }
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddressDetails", session.getString(P.token));

    }

    private void hitEmirateData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "emirate_list").addJson(j)
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
                        JsonList emirate_list = json.getJsonList(P.emirate_list);

                        if (emirate_list != null && emirate_list.size() != 0) {
                            for (Json jsonE : emirate_list) {
                                String id = jsonE.getString(P.id);
                                String emirate_name = jsonE.getString(P.emirate_name);
                                String status = jsonE.getString(P.status);
                                EmirateModel model = new EmirateModel();
                                model.setId(id);
                                model.setEmirate_name(emirate_name);
                                model.setStatus(status);
                                deliverEmirateList.add(model);
                                collectEmirateList.add(model);

                                if (Config.HOME_DELIVERY_CHECK) {
                                    if (id.equals(SelectCarActivity.pickUpEmirateID)) {
                                        binding.txtDeliverEmirateMessage.setVisibility(View.VISIBLE);
                                        binding.txtDeliverEmirateMessage.setText(model.getEmirate_name());
                                        hideDeliverEmirate();
                                        deleveryEmirateID = model.getId();
                                    }
                                }

                                if (Config.HOME_DELIVERY_CHECK) {
                                    if (id.equals(SelectCarActivity.dropUpEmirateID)) {
                                        binding.txtCollectEmirateMessage.setVisibility(View.VISIBLE);
                                        binding.txtCollectEmirateMessage.setText(model.getEmirate_name());
                                        hideCollectEmirate();
                                        collectEmirateID = model.getId();
                                    }
                                }

                            }
                        }

                        deliverEmirateAdapter.notifyDataSetChanged();
                        collectEmirateAdapter.notifyDataSetChanged();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitEmirateData");
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

    private void hideDeliverEmirate() {
        binding.cardDeliverEmirateView.setVisibility(View.GONE);
        binding.imgDeliverEmirateArrowUp.setVisibility(View.GONE);
        binding.imgDeliverEmirateArrowDown.setVisibility(View.VISIBLE);
    }

    private void hideCollectEmirate() {
        binding.cardCollectEmirateView.setVisibility(View.GONE);
        binding.imgCollectEmirateArrowUp.setVisibility(View.GONE);
        binding.imgCollectEmirateArrowDown.setVisibility(View.VISIBLE);
    }

    private void cardDeliverEmirateClick() {

        if (binding.imgDeliverEmirateArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgDeliverEmirateArrowDown.setVisibility(View.GONE);
            binding.imgDeliverEmirateArrowUp.setVisibility(View.VISIBLE);
            binding.cardDeliverEmirateView.setVisibility(View.VISIBLE);
        } else if (binding.imgDeliverEmirateArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgDeliverEmirateArrowUp.setVisibility(View.GONE);
            binding.imgDeliverEmirateArrowDown.setVisibility(View.VISIBLE);
            binding.cardDeliverEmirateView.setVisibility(View.GONE);
        }

    }

    private void cardCollectEmirateClick() {

        if (binding.imgCollectEmirateArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgCollectEmirateArrowDown.setVisibility(View.GONE);
            binding.imgCollectEmirateArrowUp.setVisibility(View.VISIBLE);
            binding.cardCollectEmirateView.setVisibility(View.VISIBLE);
        } else if (binding.imgCollectEmirateArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgCollectEmirateArrowUp.setVisibility(View.GONE);
            binding.imgCollectEmirateArrowDown.setVisibility(View.VISIBLE);
            binding.cardCollectEmirateView.setVisibility(View.GONE);
        }

    }

    private void enableDelverView() {

        binding.etxDeliverLandmark.setBackground(getResources().getDrawable(R.drawable.corner_gray_bg));
        binding.etxDeliveryDetails.setBackground(getResources().getDrawable(R.drawable.corner_gray_bg));
        trueEdit(binding.etxDeliverLandmark);
        trueEdit(binding.etxDeliveryDetails);

    }

    private void disablebleDelverView() {

        deleveryEmirateName = "";
        deliveryAddress = "";
        deliveryLat = "";
        deliveryLogn = "";
        deleveryEmirateID = "";
        binding.txtDeliverEmirateMessage.setText("");
        binding.etxDeliverLandmark.setText("");
        binding.etxDeliveryDetails.setText("");
        binding.txtDeliverEmirateMessage.setVisibility(View.GONE);
        binding.etxDeliverLandmark.setBackground(getResources().getDrawable(R.drawable.corner_blur_bg));
        binding.etxDeliveryDetails.setBackground(getResources().getDrawable(R.drawable.corner_blur_bg));
        falseEdit(binding.etxDeliverLandmark);
        falseEdit(binding.etxDeliveryDetails);

    }

    private void enableCollectView() {

        binding.etxCollectLandmark.setBackground(getResources().getDrawable(R.drawable.corner_gray_bg));
        binding.etxCollectDetails.setBackground(getResources().getDrawable(R.drawable.corner_gray_bg));
        trueEdit(binding.etxCollectLandmark);
        trueEdit(binding.etxCollectDetails);
    }

    private void disablebleCollectView() {

        collectEmirateName = "";
        collectAddress = "";
        collectLat = "";
        collectLogn = "";
        collectEmirateID = "";
        binding.txtCollectEmirateMessage.setText("");
        binding.etxCollectLandmark.setText("");
        binding.etxCollectDetails.setText("");
        binding.txtCollectEmirateMessage.setVisibility(View.GONE);
        binding.etxCollectLandmark.setBackground(getResources().getDrawable(R.drawable.corner_blur_bg));
        binding.etxCollectDetails.setBackground(getResources().getDrawable(R.drawable.corner_blur_bg));
        falseEdit(binding.etxCollectLandmark);
        falseEdit(binding.etxCollectDetails);
    }

    private void falseEdit(EditText editTextPassword) {
        editTextPassword.setFocusable(false);
        editTextPassword.setFocusableInTouchMode(false);
        editTextPassword.setClickable(false);
    }

    private void trueEdit(EditText editTextPassword) {
        editTextPassword.setFocusable(true);
        editTextPassword.setFocusableInTouchMode(true);
        editTextPassword.setClickable(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private String checkString(String string) {
        String value = "";

        if (!TextUtils.isEmpty(string) && !string.equals("null")) {
            value = string;
        }
        return value;
    }


    @Override
    public void documentView(String imagePath) {
        viewDialog(imagePath);
    }

    private void documentDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_document_view);

        ImageView imgDocument = dialog.findViewById(R.id.imgDocument);
        LoadImage.glideString(activity, imgDocument, imagePath);

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void viewDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_image_view);

        PhotoView imageView = dialog.findViewById(R.id.imageView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        LoadImage.glideString(activity,imageView,imagePath);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }


    @Override
    public void onEditPayment(PaymentCardModel model) {
        editPaymentDialog(model);
    }

    @Override
    public void onDeletePayment(PaymentCardModel model) {
        onDeleteClick(model,getResources().getString(R.string.deletePaymentMSG));
    }

    private void onDeleteClick(PaymentCardModel model, String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        hitDeleteUserPaymentDetails(dialog,model);
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

        Button positiveButton = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nigativeButton = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.lightBlue));
        nigativeButton.setTextColor(getResources().getColor(R.color.lightBlue));

    }

    private void editPaymentDialog(PaymentCardModel model) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_edit_payment_view);

        Log.e("TAG", "editPaymentDialog: " + model.getExpiry_month() + "  --  " + model.getExpiry_year());

        EditText etxCardNumber = dialog.findViewById(R.id.etxCardNumber);
        EditText etxCardName = dialog.findViewById(R.id.etxCardName);
        EditText etxValidMonth = dialog.findViewById(R.id.etxValidMonth);
        EditText etxCvv = dialog.findViewById(R.id.etxCvv);
        TextView txtSave = dialog.findViewById(R.id.txtSave);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        etxCardNumber.setText(model.getCard_number());
        etxCardName.setText(model.getName_on_card());
        etxCvv.setText(model.getCvv());

        String month = "";
        if (model.getExpiry_month().length() == 1) {
            month = "0" + model.getExpiry_month();
        } else {
            month = model.getExpiry_month();
        }

        String year = "";
        if (model.getExpiry_year().length() == 1) {
            year = "0" + model.getExpiry_year();
        } else {
            year = model.getExpiry_year();
        }

        etxValidMonth.addTextChangedListener(new TextWatcher() {
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

        etxValidMonth.setText(month + "/" + year);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                if (TextUtils.isEmpty(etxCardNumber.getText().toString().trim())) {
                    H.showMessage(activity, getResources().getString(R.string.enterCardNumber));
                    return;
                } else if (etxCardNumber.getText().toString().trim().length() < 16 || etxCardNumber.getText().toString().trim().length() > 16) {
                    H.showMessage(activity, getResources().getString(R.string.enterValidCarNumber));
                    return;
                } else if (TextUtils.isEmpty(etxCardName.getText().toString().trim())) {
                    H.showMessage(activity, getResources().getString(R.string.enterCardName));
                    return;
                } else if (TextUtils.isEmpty(etxValidMonth.getText().toString().trim())) {
                    H.showMessage(activity, getResources().getString(R.string.enterMonth));
                    return;
                } else if (etxValidMonth.getText().toString().trim().length() < 5 || etxValidMonth.getText().toString().trim().length() > 5) {
                    H.showMessage(activity, getResources().getString(R.string.enterValidMonth));
                    return;
                } else if (!etxValidMonth.getText().toString().matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) {
                    H.showMessage(activity, getResources().getString(R.string.checkCardFormat));
                    return;
                }
                else if (TextUtils.isEmpty(etxCvv.getText().toString().trim())) {
                    H.showMessage(activity, getResources().getString(R.string.enterCvv));
                    return;
                } else if (etxCvv.getText().toString().trim().length() < 3 || etxCvv.getText().toString().trim().length() > 3) {
                    H.showMessage(activity, getResources().getString(R.string.enterValidCvv));
                    return;
                }

                hideKeyboard(activity);
                hitEditUserPaymentDetails(dialog, model, etxCardNumber, etxCardName, etxValidMonth, etxCvv);

            }
        });


        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void hitEditUserPaymentDetails(Dialog dialog, PaymentCardModel model, EditText etxCardNumber, EditText etxCardName, EditText etxValidMonth, EditText etxCvv) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.id, model.getId());
        j.addString(P.card_number, etxCardNumber.getText().toString().trim());
        j.addString(P.name_on_card, etxCardName.getText().toString().trim());
        String inputMonthYear = etxValidMonth.getText().toString().trim();
        String[] separated = inputMonthYear.split("/");
        String expireMonth = separated[0];
        String expireYear = separated[1];
        j.addString(P.expiry_month, expireMonth);
        j.addString(P.expiry_year, expireYear);
        j.addString(P.cvv, etxCvv.getText().toString().trim());

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
                        dialog.dismiss();
                        json = json.getJson(P.data);
                        H.showMessage(activity, getResources().getString(R.string.dataUpdated));
                        paymentCardModelList.clear();
                        hitUserPaymentDetails();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddUserPaymentDetails", session.getString(P.token));
    }

    private void hitDeleteUserPaymentDetails(DialogInterface dialog,PaymentCardModel model) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.id, model.getId());

        Api.newApi(activity, P.BaseUrl + "delete_user_payment_option").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        dialog.dismiss();
                        json = json.getJson(P.data);
                        H.showMessage(activity, getResources().getString(R.string.dataUpdated));
                        paymentCardModelList.clear();
                        hitUserPaymentDetails();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDeleteUserPaymentDetails", session.getString(P.token));
    }

    private void hitUpdatePersonalDetails() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.user_name, binding.etxFirstName.getText().toString().trim());
        j.addString(P.user_lastname, binding.etxLastName.getText().toString().trim());
        j.addString(P.user_dob, binding.etxBirtDate.getText().toString().trim());
        j.addString(P.user_email, binding.etxEmail.getText().toString().trim());
        if (binding.radioMale.isChecked()) {
            j.addString(P.gender, "1");
        } else if (binding.radioFemale.isChecked()) {
            j.addString(P.gender, "2");
        }
        j.addString(P.user_country_code, codePrimary);
        j.addString(P.user_mobile, binding.etxNumber.getText().toString().trim());
        j.addString(P.user_alt_country_code, codeSecondary);
        j.addString(P.user_alt_mobile, binding.etxAlternameNumber.getText().toString().trim());
        j.addString(P.country_id,personalCountryId);
        j.addString(P.emirate_id,personalEmirateId);

        Api.newApi(activity, P.BaseUrl + "update_user_data").addJson(j)
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
                        hitUpdateAddressDetails();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUpdatePersonalDetails", session.getString(P.token));
    }

    private void hitUpdateAddressDetails() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.bill_address_line_1, binding.etxAddress.getText().toString().trim());
        j.addString(P.bill_address_line_2, binding.etxAddress.getText().toString().trim());
        j.addString(P.bill_city, binding.etxCity.getText().toString().trim());
        j.addString(P.bill_state, binding.etxState.getText().toString().trim());
        j.addString(P.bill_country_id, countryId);
        j.addString(P.bill_zipcode, binding.etxZipcode.getText().toString().trim());

        Api.newApi(activity, P.BaseUrl + "update_user_bill_data").addJson(j)
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

                        if (!binding.checkBoxAccept.isChecked()) {
                            H.showMessage(activity, getResources().getString(R.string.allowTemCondition));
                        }else if (!binding.checkBoxAge.isChecked()) {
                            H.showMessage(activity, getResources().getString(R.string.ageMessage));
                        } else {
                            getVerificationDialog();
                        }

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUpdateAddressDetails", session.getString(P.token));
    }

    private boolean checkPersonalDetailValidation() {

        boolean value = true;

        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterFirstName));
            checkPersonalErrorView();
        } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterLastName));
            checkPersonalErrorView();
        } else if (TextUtils.isEmpty(codePrimary)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectCountryCode));
            checkPersonalErrorView();
        } else if (TextUtils.isEmpty(binding.etxNumber.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterMobile));
            checkPersonalErrorView();
        } else if (TextUtils.isEmpty(binding.etxBirtDate.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterDOB));
            checkPersonalErrorView();
        } else if (TextUtils.isEmpty(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailId));
            checkPersonalErrorView();
        } else if (!Validation.validEmail(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailValid));
            checkPersonalErrorView();
        }

        return value;
    }

    private void checkPersonalErrorView() {
        if (binding.viewPersonalInfo.getVisibility() == View.GONE) {
            binding.viewPersonalInfo.setVisibility(View.VISIBLE);
            binding.nestedCroll.post(new Runnable() {
                @Override
                public void run() {
                    binding.nestedCroll.scrollTo(0, binding.viewPersonalInfo.getBottom());
                }
            });
        }
    }

    private void checkPaymentErrorView() {
        if (binding.viewPaymentInfo.getVisibility() == View.GONE) {
            binding.viewPaymentInfo.setVisibility(View.VISIBLE);
            binding.nestedCroll.post(new Runnable() {
                @Override
                public void run() {
                    binding.nestedCroll.scrollTo(0, binding.viewPaymentInfo.getBottom());
                }
            });
        }
    }

    private void checkBillingErrorView() {
        if (binding.viewBillingInfo.getVisibility() == View.GONE) {
            binding.viewBillingInfo.setVisibility(View.VISIBLE);
            binding.nestedCroll.post(new Runnable() {
                @Override
                public void run() {
                    binding.nestedCroll.scrollTo(0, binding.viewBillingInfo.getBottom());
                }
            });
        }
    }

    private void checkDocumentErrorView() {
        if (binding.viewDocument.getVisibility() == View.GONE) {
            binding.viewDocument.setVisibility(View.VISIBLE);
            binding.nestedCroll.post(new Runnable() {
                @Override
                public void run() {
                    binding.nestedCroll.scrollTo(0, binding.viewDocument.getBottom());
                }
            });
        }
    }

    private boolean checkAddressDetailValidation() {

        boolean value = true;

        if (TextUtils.isEmpty(binding.etxAddress.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterAddress));
            checkBillingErrorView();
        } else if (TextUtils.isEmpty(countryId)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.country));
            checkBillingErrorView();
        } else if (TextUtils.isEmpty(binding.etxState.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterState));
            checkBillingErrorView();
        } else if (TextUtils.isEmpty(binding.etxCity.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectCity1));
            checkBillingErrorView();
        } else if (TextUtils.isEmpty(binding.etxZipcode.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectZip));
            checkBillingErrorView();
        }
        return value;
    }


    private void setDateTimeField(EditText editText) {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(activity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date date = newDate.getTime();
                String fdate = sd.format(date);

                editText.setText(fdate);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String dayString = (String) DateFormat.format("dd", date); // 20
                String monthString = (String) DateFormat.format("MMM", date); // Jun
                String monthNumber = (String) DateFormat.format("MM", date); // 06
                String yeare = (String) DateFormat.format("yyyy", date); // 2013

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    private void hitUserDocumentDetails() {

        ProgressView.show(activity, loadingDialog);
        Api.newApi(activity, P.BaseUrl + "user_data_for_booking")
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Json data = json.getJson(P.data);
//                        Json userJson = data.getJson(P.user);
                        Json userInfo = data.getJson(P.user_info);
                        user_country_id = userInfo.getString(P.country_id);
                        Json userDocument = data.getJson(P.user_profile_document);
                        JsonList uploaded_document = userDocument.getJsonList(P.uploaded_document);
                        JsonList pending_document = userDocument.getJsonList(P.pending_document);

                        if (uploaded_document != null && !uploaded_document.isEmpty() && uploaded_document.size() != 0) {
                            for (Json jsonUploaded : uploaded_document) {

                                DocumentUploadedModel model = new DocumentUploadedModel();

                                model.setId(jsonUploaded.getString(P.id));
                                model.setUser_id(jsonUploaded.getString(P.user_id));
                                model.setDocument_key(jsonUploaded.getString(P.document_key));
                                model.setLicense_number(jsonUploaded.getString(P.license_number));
                                model.setIssue_date(jsonUploaded.getString(P.issue_date));
                                model.setExpiry(jsonUploaded.getString(P.expiry));
                                model.setImage(jsonUploaded.getString(P.image));
                                model.setPassport_number(jsonUploaded.getString(P.passport_number));
                                model.setVisa_issue_date(jsonUploaded.getString(P.visa_issue_date));
                                model.setCredit_card_number(jsonUploaded.getString(P.credit_card_number));
                                model.setName_on_card(jsonUploaded.getString(P.name_on_card));
                                model.setId_number(jsonUploaded.getString(P.id_number));
                                model.setEntry_stamp(jsonUploaded.getString(P.entry_stamp));
                                model.setStatus(jsonUploaded.getString(P.status));
                                model.setIs_approved(jsonUploaded.getString(P.is_approved));
                                model.setAdd_date(jsonUploaded.getString(P.add_date));
                                model.setData_field_status(jsonUploaded.getString(P.data_field_status));
                                model.setUpdate_date(jsonUploaded.getString(P.update_date));
                                model.setImage_url(jsonUploaded.getString(P.image_url));
//                            model.setTitle(jsonUploaded.getString(P.title));

                                documentUploadedModelList.add(model);
                            }
                        }

                        if (pending_document != null && !pending_document.isEmpty() && pending_document.size() != 0) {
                            for (Json jsonData : pending_document) {
                                Log.e("TAG", "hitUserDocumentDetailsSDSD: " + jsonData.toString());
                                DocumentModel documentModel = new DocumentModel();
                                documentModel.setTitle(jsonData.getString(P.title));
                                documentModel.setKey(jsonData.getString(P.key));
                                documentModel.setField(jsonData.getJsonArray(P.field));
                                documentModel.setCheckValue("0");
                                try {
                                    documentModel.setSave_data(jsonData.getJson(P.save_data));
                                } catch (Exception e) {
                                    documentModel.setSave_data(new Json());
                                }
                                documentModelList.add(documentModel);
                            }
                        }
                        documentUploadedAdapter.notifyDataSetChanged();
                        documentAdapter.notifyDataSetChanged();

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUserDocumentDetails", session.getString(P.token));
    }

    @Override
    public void onCheckPayment(PaymentCardModel model) {
        paymentId = model.getId();
    }

    @Override
    public void downloadDocument(String name, TextView textView, TextView txtImage) {
        textViewDocumnt = textView;
        txtImagePath = txtImage;
        documentName = name;
        getPermission();
    }

    private void getAccess() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_WRITE);
    }

    public void jumpToSetting() {
        H.showMessage(activity, getResources().getString(R.string.allowPermission));
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void getDocument() {
        try {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PDF_DATA);
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDocument();
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
            case PDF_DATA:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        Uri selectedPDF = data.getData();

                        String path = getBase64Image(selectedPDF);
                        hitUploadImage(path);

                    } catch (Exception e) {
                        H.showMessage(activity, e.getMessage());
                    }
                }
                break;
        }
    }


    private String getBase64Image(Uri uri) {
        base64Image = "";
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            base64Image = encodeImage(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "setImageDataEE: " + e.getMessage());
            H.showMessage(activity, "Unable to get image, try again.");
        }

        return base64Image;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private void hitUploadImage(String base64Image) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.image, base64Image);
        j.addString(P.extension, "png");
        Api.newApi(activity, P.BaseUrl + "upload_image").addJson(j)
                .setMethod(Api.POST)
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
                        String image = json.getString(P.image);
                        String image_url = json.getString(P.image_url);
                        ImagePathModel model = new ImagePathModel();
                        model.setPath(image);
                        model.setTitle(documentName);
                        imagePathModelList.add(model);
                        textViewDocumnt.setText(getResources().getString(R.string.uploaded) + " " + documentName);
                        txtImagePath.setText(getResources().getString(R.string.imagePath) + " " + image);
                        txtImagePath.setVisibility(View.VISIBLE);
                        txtImagePath.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewDialog(image_url);
                            }
                        });
                        H.showMessage(activity, getResources().getString(R.string.imageUploaded));
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                })
                .run("hitUploadImage", session.getString(P.token));

    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }


    private void hitBookCarData(CarModel model) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.booking_type, SelectCarActivity.bookingTYpe);
        j.addString(P.month_time, SelectCarActivity.monthDuration);
        j.addString(P.car_id, CarDetailOneActivity.carID);
        j.addString(P.pay_type, payType);

        j.addString(P.emirate_id, deleveryEmirateID);
        j.addString(P.coupon_code, "");

        j.addString(P.pickup_type, deliverType);
        if (binding.radioDeliverYes.isChecked()) {

            if (TextUtils.isEmpty(deleveryEmirateID)){
                H.showMessage(activity,getResources().getString(R.string.selectDeliverEmirate));
                return;
            }else {
                j.addString(P.pickup_emirate_id, deleveryEmirateID);
            }

            j.addString(P.pickup_location_id, "0");

            if (TextUtils.isEmpty(binding.etxDeliverLandmark.getText().toString())){
                H.showMessage(activity,getResources().getString(R.string.enterDeliveryLandmark));
                return;
            }else {
                j.addString(P.pickup_landmark, binding.etxDeliverLandmark.getText().toString().trim());
            }

            if (TextUtils.isEmpty(deliveryAddress)){
                H.showMessage(activity,getResources().getString(R.string.selectDeliverAdd));
                return;
            }else {
                j.addString(P.pickup_address, deliveryAddress);
            }

            j.addString(P.pickup_location_name, deleveryEmirateName);
            j.addString(P.pickup_lat, deliveryLat);
            j.addString(P.pickup_long, deliveryLogn);
            j.addString(P.pickup_details, binding.etxDeliveryDetails.getText().toString().trim());

        } else {
            j.addString(P.pickup_emirate_id, SelectCarActivity.pickUpEmirateID);
            j.addString(P.pickup_location_id, SelectCarActivity.pickUpLocationID);
            j.addString(P.pickup_address, SelectCarActivity.pickUpAddress);
            j.addString(P.pickup_landmark, Config.SelectedPickUpLandmark);
            j.addString(P.pickup_location_name, Config.SelectedPickUpAddress);
            j.addString(P.pickup_lat, "");
            j.addString(P.pickup_long, "");
            j.addString(P.pickup_details, "");
        }
        j.addString(P.pickup_date, SelectCarActivity.pickUpDate);
        j.addString(P.pickup_time, SelectCarActivity.pickUpTime);

        j.addString(P.dropoff_type, collectType);
        if (binding.radioCollectYes.isChecked()) {

            if (TextUtils.isEmpty(collectEmirateID)){
                H.showMessage(activity,getResources().getString(R.string.selectCollectEmirate));
                return;
            }else {
                j.addString(P.dropoff_emirate_id, collectEmirateID);
            }

            j.addString(P.dropoff_location_id, "0");

            if (TextUtils.isEmpty(binding.etxCollectLandmark.getText().toString())){
                H.showMessage(activity,getResources().getString(R.string.enterCollectLandmark));
                return;
            }else {
                j.addString(P.dropoff_landmark, binding.etxCollectLandmark.getText().toString().trim());
            }

            if (TextUtils.isEmpty(collectAddress)){
                H.showMessage(activity,getResources().getString(R.string.selectCollectAdd));
                return;
            }else {
                j.addString(P.dropoff_address, collectAddress);
            }

            j.addString(P.dropoff_location_name, collectEmirateName);
            j.addString(P.dropoff_lat, collectLat);
            j.addString(P.dropoff_long, collectLogn);
            j.addString(P.dropoff_details, binding.etxCollectDetails.getText().toString().trim());

        } else {
            j.addString(P.dropoff_emirate_id, SelectCarActivity.dropUpEmirateID);
            j.addString(P.dropoff_location_id, SelectCarActivity.dropUpLocationID);
            j.addString(P.dropoff_address, SelectCarActivity.dropUpAddress);
            j.addString(P.dropoff_landmark, Config.SelectedDropUpLandmark);
            j.addString(P.dropoff_lat, "");
            j.addString(P.dropoff_long, "");
            j.addString(P.dropoff_details, "");
        }
        j.addString(P.dropoff_date, SelectCarActivity.dropUpDate);
        j.addString(P.dropoff_time, SelectCarActivity.dropUpTime);

        j.addString(P.pickup_delivery_check, "1");
        j.addString(P.dropoff_delivery_check, "1");

        JSONArray array = new JSONArray();
        for (Json jsonData : AddOnsActivity.jsonAddOnsList) {
            array.put(jsonData);
        }
        j.addJSONArray(P.car_extra, array);

        j.addString(P.user_name, binding.etxFirstName.getText().toString().trim());
        j.addString(P.user_lastname, binding.etxLastName.getText().toString().trim());
        j.addString(P.user_email, binding.etxEmail.getText().toString().trim());
        j.addString(P.user_country_code, codePrimary);
        j.addString(P.user_mobile, binding.etxNumber.getText().toString().trim());

        j.addString(P.user_payment_option_id, paymentId);

        j.addString(P.address_line_1, binding.etxAddress.getText().toString().trim());
        j.addString(P.country_id, countryId);
        j.addString(P.state, binding.etxState.getText().toString().trim());
        j.addString(P.city, binding.etxCity.getText().toString().trim());
        j.addString(P.zipcode, binding.etxZipcode.getText().toString().trim());

        j.addString(P.booking_remark, binding.etxRemark.getText().toString().trim());
        j.addString(P.failed_url, "");
        j.addString(P.booking_from, Config.MOBILE);
        j.addString(P.success_url, "");
        j.addJSON(P.document, jsonChild);

        Log.e("TAG", "hitBookCarDataJSON: " + j.toString());

        Api.newApi(activity, P.BaseUrl + "book_car").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        Json data = json.getJson(P.data);
                        String paymentLink = "";
                        try {
                            paymentLink = data.getString(P.payment_link);
                        }catch (Exception e){
                        }
                        Intent intent = new Intent(activity,BookingSucessfullActivity.class);
                        intent.putExtra(Config.WEB_URL,paymentLink);
                        intent.putExtra(Config.PAY_TYPE,payType);
                        startActivity(intent);
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitBookCarData",session.getString(P.token));

    }

    private void getVerificationDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_age_check);

        CheckBox checkBoxAge = dialog.findViewById(R.id.checkBoxAge);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtConfirm = dialog.findViewById(R.id.txtConfirm);

        checkBoxAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (!checkBoxAge.isChecked()) {
                    H.showMessage(activity, getResources().getString(R.string.ageMessage));
                }else {
                    dialog.dismiss();
                    hitBookCarData(model);
                }
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

}