package com.example.fastuae.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.AddressSelectionAdapter;
import com.example.fastuae.adapter.CodeSelectionAdapter;
import com.example.fastuae.databinding.FragmentMyAccountBinding;
import com.example.fastuae.model.AddressModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.CheckString;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.Validation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAccountFragment extends Fragment {

    private Context context;
    private FragmentMyAccountBinding binding;
    private int positionNumber = 0;
    private DatePickerDialog mDatePickerDialog;
    private List<CountryCodeModel> countryCodeModelList;
    private List<AddressModel> lisAddressEmirate;
    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;
    private String codePrimary = "";
    private String codeSecondary = "";
    private String countryId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
            context = inflater.getContext();
            initView();
            updateIcons();

        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        if (binding.getRoot() != null)
        {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null)
            {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }

    private void initView() {

        loadingDialog = new LoadingDialog(context);
        session = new Session(context);
        flag = session.getString(P.languageFlag);

        countryCodeModelList = new ArrayList<>();

        lisAddressEmirate = new ArrayList<>();
        AddressModel modelAddress1 = new AddressModel();
        modelAddress1.setCountry_name(getResources().getString(R.string.country));
        lisAddressEmirate.add(modelAddress1);



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

        CodeSelectionAdapter adapterOne = new CodeSelectionAdapter(context, countryCodeModelList);
        binding.spinnerCodeMobile.setAdapter(adapterOne);
        binding.spinnerCodeMobile.setSelection(positionNumber);

        CodeSelectionAdapter adapterTwo = new CodeSelectionAdapter(context, countryCodeModelList);
        binding.spinnerCodeAlternate.setAdapter(adapterTwo);
        binding.spinnerCodeAlternate.setSelection(positionNumber);

        AddressSelectionAdapter adapterAddress = new AddressSelectionAdapter(context, lisAddressEmirate);
        binding.spinnerAddress.setAdapter(adapterAddress);

        onClick();

        setDateTimeField(binding.etxBirtDate);
        binding.etxBirtDate.setFocusable(false);
        binding.etxBirtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calender_bg, 0);
        binding.etxBirtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        hitPersonalDetails();
        hitAddressDetails();

    }

    private void onClick() {
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
                if (!model.getCountry_name().equals(getResources().getString(R.string.country))){
                    countryId = model.getId();
                }else {
                    countryId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        binding.personalDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkPersonalDetailsView();
            }
        });

        binding.txtPersonalDetailsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkPersonalDetailValidation()) {
                    hitUpdatePersonalDetails();
                }
            }
        });

        binding.passwordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkPasswordView();
            }
        });

        binding.txtPasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkPasswordValidation()){
                    hitChangePasswordDetails();
                }
            }
        });

        binding.lnrAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkAddressView();
            }
        });

        binding.txtAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkAddressDetailValidation()){
                    hitUpdateAddressDetails();
                }
            }
        });
    }

    private boolean checkAddressDetailValidation() {

        boolean value = true;

        if (TextUtils.isEmpty(binding.etxAddress.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterAddress));
        } else if (TextUtils.isEmpty(binding.etxZipcode.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.selectZip));
        }else if (TextUtils.isEmpty(binding.etxCity.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.selectCity1));
        }else if (TextUtils.isEmpty(binding.etxState.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterState));
        }else if (TextUtils.isEmpty(countryId)) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.country));
        }
        return value;
    }


    private boolean checkPersonalDetailValidation() {

        boolean value = true;

        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.pleaseEnterFirstName));
        } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.pleaseEnterLastName));
        } else if (TextUtils.isEmpty(binding.etxNumber.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterMobile));
        } else if (TextUtils.isEmpty(binding.etxBirtDate.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.pleaseEnterDOB));
        } else if (TextUtils.isEmpty(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterEmailId));
        } else if (!Validation.validEmail(binding.etxEmail.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterEmailValid));
        }

        return value;
    }

    private boolean checkPasswordValidation(){
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterPassword));
        } else if (TextUtils.isEmpty(binding.etxNewPassword.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterNewPassword));
        } else if (binding.etxNewPassword.getText().toString().trim().length()<6){
            value = false;
            H.showMessage(context, getResources().getString(R.string.minPassLength));
        } else if (TextUtils.isEmpty(binding.etxConfirmPassword.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterConfirmPassword));
        } else if (!binding.etxConfirmPassword.getText().toString().trim().equals(binding.etxNewPassword.getText().toString().trim())){
            value = false;
            H.showMessage(context, getResources().getString(R.string.passwordNotMatch));
        }

        return value;
    }


    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgPersonalRight.setVisibility(View.GONE);
            binding.imgPersonalLeft.setVisibility(View.VISIBLE);

            binding.imgPasswordRight.setVisibility(View.GONE);
            binding.imgPasswordLeft.setVisibility(View.VISIBLE);

            binding.imgAddressRight.setVisibility(View.GONE);
            binding.imgAddressLeft.setVisibility(View.VISIBLE);

            binding.imgEmirateRight.setVisibility(View.GONE);
            binding.imgEmirateLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgPersonalRight.setVisibility(View.VISIBLE);
            binding.imgPersonalLeft.setVisibility(View.GONE);

            binding.imgPasswordRight.setVisibility(View.VISIBLE);
            binding.imgPasswordLeft.setVisibility(View.GONE);

            binding.imgAddressRight.setVisibility(View.VISIBLE);
            binding.imgAddressLeft.setVisibility(View.GONE);

            binding.imgEmirateRight.setVisibility(View.VISIBLE);
            binding.imgEmirateLeft.setVisibility(View.GONE);

        }

    }


    private void setDateTimeField(EditText editText) {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(context, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date date = newDate.getTime();
                String fdate = sd.format(date);

                editText.setText(fdate);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String dayString         = (String) DateFormat.format("dd",   date); // 20
                String monthString  = (String) DateFormat.format("MMM",  date); // Jun
                String monthNumber  = (String) DateFormat.format("MM",   date); // 06
                String yeare         = (String) DateFormat.format("yyyy", date); // 2013

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void checkPersonalDetailsView(){
        if (binding.lnrPersonalDetails.getVisibility() == View.GONE) {
            binding.lnrPersonalDetails.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPersonalLeft.setImageResource(R.drawable.ic_minus);
            }else if (flag.equals(Config.ENGLISH)) {
                binding.imgPersonalRight.setImageResource(R.drawable.ic_minus);
            }
        } else if (binding.lnrPersonalDetails.getVisibility() == View.VISIBLE) {
            binding.lnrPersonalDetails.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPersonalLeft.setImageResource(R.drawable.ic_plus);
            }else if (flag.equals(Config.ENGLISH)) {
                binding.imgPersonalRight.setImageResource(R.drawable.ic_plus);
            }
        }
    }

    private void checkPasswordView(){
        if (binding.lnrPassword.getVisibility() == View.GONE) {
            binding.lnrPassword.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPasswordLeft.setImageResource(R.drawable.ic_minus);
            }else if (flag.equals(Config.ENGLISH)) {
                binding.imgPasswordRight.setImageResource(R.drawable.ic_minus);
            }
        } else if (binding.lnrPassword.getVisibility() == View.VISIBLE) {
            binding.lnrPassword.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPasswordLeft.setImageResource(R.drawable.ic_plus);
            }else if (flag.equals(Config.ENGLISH)) {
                binding.imgPasswordRight.setImageResource(R.drawable.ic_plus);
            }
        }
    }

    private void checkAddressView(){
        if (binding.lnrAddress.getVisibility() == View.GONE) {
            binding.lnrAddress.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgAddressLeft.setImageResource(R.drawable.ic_minus);
            }else if (flag.equals(Config.ENGLISH)) {
                binding.imgAddressRight.setImageResource(R.drawable.ic_minus);
            }
        } else if (binding.lnrAddress.getVisibility() == View.VISIBLE) {
            binding.lnrAddress.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgAddressLeft.setImageResource(R.drawable.ic_plus);
            }else if (flag.equals(Config.ENGLISH)) {
                binding.imgAddressRight.setImageResource(R.drawable.ic_plus);
            }
        }
    }


    private void blackTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.textDark)}
                    },
                    new int[]{getResources().getColor(R.color.textDark)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void blueTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }


    private void hitPersonalDetails() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "user_data").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        json = json.getJson(P.user);

                        binding.etxFirstName.setText(CheckString.check(json.getString(P.user_name)));
                        binding.etxLastName.setText(CheckString.check(json.getString(P.user_lastname)));
                        binding.etxEmail.setText(CheckString.check(json.getString(P.user_email)));
                        binding.etxBirtDate.setText(CheckString.check(json.getString(P.user_dob)));
                        binding.etxNumber.setText(CheckString.check(json.getString(P.user_mobile)));
                        binding.etxAlternameNumber.setText(CheckString.check(json.getString(P.user_alt_mobile)));

                        String gender = json.getString(P.gender);
                        if (CheckString.check(gender).equalsIgnoreCase("1")){
                            binding.radioFemale.setChecked(false);
                            binding.radioMale.setChecked(true);
                            blueTin(binding.radioMale);
                            blackTin(binding.radioFemale);
                        }else if (CheckString.check(gender).equalsIgnoreCase("2")){
                            binding.radioMale.setChecked(false);
                            binding.radioFemale.setChecked(true);
                            blueTin(binding.radioFemale);
                            blackTin(binding.radioMale);
                        }

                        String codePrimary = CheckString.check(json.getString(P.user_country_code));
                        String codeSecondary = CheckString.check(json.getString(P.user_alt_country_code));

                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.phone_code).equalsIgnoreCase(codePrimary)) {
                                binding.spinnerCodeMobile.setSelection(i);
                            }

                            if (jsonData.getString(P.phone_code).equalsIgnoreCase(codeSecondary)) {
                                binding.spinnerCodeAlternate.setSelection(i);
                            }
                        }

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitPersonalDetails",session.getString(P.token));

    }

    private void hitUpdatePersonalDetails() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.user_name,binding.etxFirstName.getText().toString().trim());
        j.addString(P.user_lastname,binding.etxLastName.getText().toString().trim());
        j.addString(P.user_dob,binding.etxBirtDate.getText().toString().trim());
        j.addString(P.user_email,binding.etxEmail.getText().toString().trim());
        if (binding.radioMale.isChecked()){
            j.addString(P.gender,"1");
        }else if (binding.radioFemale.isChecked()){
            j.addString(P.gender,"2");
        }
        j.addString(P.user_country_code,codePrimary);
        j.addString(P.user_mobile,binding.etxNumber.getText().toString().trim());
        j.addString(P.user_alt_country_code,codeSecondary);
        j.addString(P.user_alt_mobile,binding.etxAlternameNumber.getText().toString().trim());

        Api.newApi(context, P.BaseUrl + "update_user_data").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        H.showMessage(context,getResources().getString(R.string.dataUpdated));
                        checkPersonalDetailsView();

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUpdatePersonalDetails",session.getString(P.token));
    }

    private void hitChangePasswordDetails() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.old_password,binding.etxPassword.getText().toString().trim());
        j.addString(P.new_password,binding.etxNewPassword.getText().toString().trim());
        j.addString(P.confirm_password,binding.etxConfirmPassword.getText().toString().trim());

        Api.newApi(context, P.BaseUrl + "change_password").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        H.showMessage(context,getResources().getString(R.string.dataUpdated));
                        checkPasswordView();
                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitChangePasswordDetails",session.getString(P.token));
    }


    private void hitAddressDetails() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "user_bill_data").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
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
                        countryId = bill_country_id;

                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.id).equals(bill_country_id)) {
                                binding.spinnerAddress.setSelection(i+1);
                            }
                        }
                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAddressDetails",session.getString(P.token));

    }

    private void hitUpdateAddressDetails() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.bill_address_line_1,binding.etxAddress.getText().toString().trim());
        j.addString(P.bill_address_line_2,binding.etxAddress.getText().toString().trim());
        j.addString(P.bill_city,binding.etxCity.getText().toString().trim());
        j.addString(P.bill_state,binding.etxState.getText().toString().trim());
        j.addString(P.bill_country_id,countryId);
        j.addString(P.bill_zipcode,binding.etxZipcode.getText().toString().trim());

        Api.newApi(context, P.BaseUrl + "update_user_bill_data").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        H.showMessage(context,getResources().getString(R.string.dataUpdated));
                        checkAddressView();

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUpdateAddressDetails",session.getString(P.token));
    }

    public static MyAccountFragment newInstance() {
        MyAccountFragment fragment = new MyAccountFragment();
        return fragment;
    }

}
