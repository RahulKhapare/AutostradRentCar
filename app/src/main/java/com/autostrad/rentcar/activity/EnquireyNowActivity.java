package com.autostrad.rentcar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.CodeSelectionAdapter;
import com.autostrad.rentcar.adapter.EmirateSelectionAdapter;
import com.autostrad.rentcar.databinding.ActivityEnquireyNowBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.CountryCodeModel;
import com.autostrad.rentcar.model.EmirateModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.Validation;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class EnquireyNowActivity extends AppCompatActivity {

    private EnquireyNowActivity activity = this;
    private ActivityEnquireyNowBinding binding;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;

    private int positionNumber = 0;
    private String codePrimary = "";
    private String emirateID = "";
    private String enquiryType = "";
    private String duration = "";
    private List<CountryCodeModel> countryCodeModelList;
    private List<EmirateModel> listEmirate;
    private List<EmirateModel> listEnquery;
    private List<EmirateModel> listDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enquirey_now);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.enquireyNow));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);
        flag = session.getString(P.languageFlag);

        listEmirate = new ArrayList<>();
        listEnquery = new ArrayList<>();
        listDuration = new ArrayList<>();
        countryCodeModelList = new ArrayList<>();

        JsonList jsonList = Config.countryJsonList;
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            CountryCodeModel model = new CountryCodeModel();
            model.setId(json.getString(P.id));
            model.setCountry_name(json.getString(P.country_name));
            model.setPhone_code(json.getString(P.phone_code));
            countryCodeModelList.add(model);

            if (model.getPhone_code().equalsIgnoreCase("971")) {
                positionNumber = i;
            }

        }

        CodeSelectionAdapter adapterOne = new CodeSelectionAdapter(activity, countryCodeModelList);
        binding.spinnerCodeMobile.setAdapter(adapterOne);
        binding.spinnerCodeMobile.setSelection(positionNumber);

        EmirateModel emirateModel = new EmirateModel();
        emirateModel.setEmirate_name(getResources().getString(R.string.selectEmirate));
        listEmirate.add(emirateModel);
        JsonList emirate_list = HomeFragment.emirate_list;
        if (emirate_list != null && emirate_list.size() != 0) {
            for (Json json : emirate_list) {
                String id = json.getString(P.id);
                String emirate_name = json.getString(P.emirate_name);
                String status = json.getString(P.status);
                EmirateModel model = new EmirateModel();
                model.setId(id);
                model.setEmirate_name(emirate_name);
                model.setStatus(status);
                listEmirate.add(model);
            }
        }


        EmirateSelectionAdapter adapterEmirate = new EmirateSelectionAdapter(activity, listEmirate);
        binding.spinnerEmirate.setAdapter(adapterEmirate);


        listEnquery.add(new EmirateModel(getResources().getString(R.string.selectEnquiryType),""));
        listEnquery.add(new EmirateModel("Corporate","0"));
        listEnquery.add(new EmirateModel("Individual","0"));
        EmirateSelectionAdapter adapterEnquirey = new EmirateSelectionAdapter(activity, listEnquery);
        binding.spinnerEnquirey.setAdapter(adapterEnquirey);

        listDuration.add(new EmirateModel(getResources().getString(R.string.selectDuration),""));
        listDuration.add(new EmirateModel("Daily","0"));
        listDuration.add(new EmirateModel("Weekly","0"));
        listDuration.add(new EmirateModel("Monthly","0"));
        listDuration.add(new EmirateModel("Yearly","0"));
        EmirateSelectionAdapter adapterDuration = new EmirateSelectionAdapter(activity, listDuration);
        binding.spinnerDuration.setAdapter(adapterDuration);

        updateIcons();
        onClick();
        onSelection();
    }

    private void onSelection() {

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

        binding.spinnerEmirate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmirateModel model = listEmirate.get(position);
                if (!TextUtils.isEmpty(model.getId()) && !model.getId().equals("")) {
                    emirateID = model.getId();
                } else {
                    emirateID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerEnquirey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmirateModel model = listEnquery.get(position);
                if (!TextUtils.isEmpty(model.getId()) && !model.getId().equals("")) {
                    enquiryType = model.getEmirate_name().toLowerCase();
                } else {
                    enquiryType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmirateModel model = listDuration.get(position);
                if (!TextUtils.isEmpty(model.getId()) && !model.getId().equals("")) {
                    duration = model.getEmirate_name().toLowerCase();
                } else {
                    duration = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onClick() {

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()) {
                    hitEnquiryDetails();
                }
            }
        });
    }

    private boolean checkValidation() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterFirstName));
        } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterLastName));
        } else if (TextUtils.isEmpty(binding.etxNumber.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterMobile));
        } else if (TextUtils.isEmpty(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailId));
        } else if (!Validation.validEmail(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailValid));
        } else if (TextUtils.isEmpty(emirateID)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectEmirateId));
        } else if (TextUtils.isEmpty(enquiryType)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectEnquiry));
        } else if (TextUtils.isEmpty(duration)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.selectDuration));
        } else if (TextUtils.isEmpty(binding.etxComment.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterComment));
        } else if (binding.etxComment.getText().toString().trim().length() < 3) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterValidComment));
        }

        return value;
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgEmirateRight.setVisibility(View.GONE);
            binding.imgEmirateLeft.setVisibility(View.VISIBLE);

            binding.imgEnquireyRight.setVisibility(View.GONE);
            binding.imgEnqureyLeft.setVisibility(View.VISIBLE);

            binding.imgDurationRight.setVisibility(View.GONE);
            binding.imgDurationLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgEmirateRight.setVisibility(View.VISIBLE);
            binding.imgEmirateLeft.setVisibility(View.GONE);

            binding.imgEnquireyRight.setVisibility(View.VISIBLE);
            binding.imgEnqureyLeft.setVisibility(View.GONE);

            binding.imgDurationRight.setVisibility(View.VISIBLE);
            binding.imgDurationLeft.setVisibility(View.GONE);

        }

    }

    private void hitEnquiryDetails() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString(P.user_name, binding.etxFirstName.getText().toString().trim());
        j.addString(P.user_lastname, binding.etxLastName.getText().toString().trim());
        j.addString(P.user_country_code, codePrimary);
        j.addString(P.emirate_id, emirateID);
        j.addString(P.enquiry_type, enquiryType);
        j.addString(P.duration, duration);
        j.addString(P.user_mobile, binding.etxNumber.getText().toString().trim());
        j.addString(P.user_email, binding.etxEmail.getText().toString().trim());
        j.addString(P.description, binding.etxComment.getText().toString().trim());

        Api.newApi(activity, P.BaseUrl + "enquire_now").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        H.showMessage(activity, getResources().getString(R.string.enquiryAdded));
                        new Handler().postDelayed(() -> {
                            finish();
                        }, 500);
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitEnquiryDetails", session.getString(P.token));

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
        finish();
    }

}