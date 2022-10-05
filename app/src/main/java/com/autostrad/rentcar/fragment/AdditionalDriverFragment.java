package com.autostrad.rentcar.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.ProfileViewActivity;
import com.autostrad.rentcar.adapter.AdditionalDriverListAdapter;
import com.autostrad.rentcar.adapter.AddressSelectionAdapter;
import com.autostrad.rentcar.adapter.CodeSelectionAdapter;
import com.autostrad.rentcar.adapter.EmirateSelectionAdapter;
import com.autostrad.rentcar.databinding.FragmentAdditionalDriveBinding;
import com.autostrad.rentcar.model.AdditionalDriverModel;
import com.autostrad.rentcar.model.AddressModel;
import com.autostrad.rentcar.model.CountryCodeModel;
import com.autostrad.rentcar.model.EmirateModel;
import com.autostrad.rentcar.util.CheckString;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.Validation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdditionalDriverFragment extends Fragment implements AdditionalDriverListAdapter.onClick {

    //9594022128
    private Context context;
    private FragmentAdditionalDriveBinding binding;
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
    private String emirateID = "";
    private String countryName = "";

    private List<EmirateModel> listLocation;

    public static boolean forEditData = false;
    public static boolean forAddData = false;
    private String edit_driver_id = "";

    private List<AdditionalDriverModel> additionalDriverModelList;
    private AdditionalDriverListAdapter additionalDriverListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_additional_drive, container, false);
            context = inflater.getContext();
            initView();
            updateIcons();

        }

        return binding.getRoot();
    }

    public void callBackEdit() {
        if (forEditData) {
            forEditData = false;
            clearView();
            binding.additionalDetailView.setVisibility(View.GONE);
            binding.lnrAdditionalDetails.setVisibility(View.GONE);
            binding.lnrDriverList.setVisibility(View.VISIBLE);
            return;
        }
    }

    public void callBackAdd() {
        if (forAddData) {
            forAddData = false;
            clearView();
            if (additionalDriverModelList.isEmpty()) {
                binding.additionalDetailView.setVisibility(View.VISIBLE);
                binding.lnrAdditionalDetails.setVisibility(View.GONE);
                binding.lnrDriverList.setVisibility(View.GONE);
            } else {
                binding.additionalDetailView.setVisibility(View.GONE);
                binding.lnrAdditionalDetails.setVisibility(View.GONE);
                binding.lnrDriverList.setVisibility(View.VISIBLE);
            }
            return;
        }
    }

    @Override
    public void onDestroyView() {
        if (binding.getRoot() != null) {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }

    private void initView() {

        forEditData = false;
        forAddData = false;

        loadingDialog = new LoadingDialog(context);
        session = new Session(context);
        flag = session.getString(P.languageFlag);

        countryCodeModelList = new ArrayList<>();

        lisAddressEmirate = new ArrayList<>();
        AddressModel modelAddress1 = new AddressModel();
        modelAddress1.setCountry_name(getResources().getString(R.string.nationality));
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
        binding.spinnerNationality.setAdapter(adapterAddress);

        listLocation = new ArrayList<>();
        EmirateModel emirateModel = new EmirateModel();
        emirateModel.setEmirate_name(getResources().getString(R.string.selectEmirate));
        listLocation.add(emirateModel);
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
                listLocation.add(model);
            }
        }


        EmirateSelectionAdapter adapterLocation = new EmirateSelectionAdapter(context, listLocation);
        binding.spinnerEmirate.setAdapter(adapterLocation);

        setDateTimeField(binding.etxBirtDate);
        binding.etxBirtDate.setFocusable(false);
        binding.etxBirtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calender_bg, 0);
        binding.etxBirtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        additionalDriverModelList = new ArrayList<>();
        additionalDriverListAdapter = new AdditionalDriverListAdapter(context, additionalDriverModelList, AdditionalDriverFragment.this);
        binding.recyclerDriverData.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerDriverData.setHasFixedSize(true);
        binding.recyclerDriverData.setNestedScrollingEnabled(false);
        binding.recyclerDriverData.setAdapter(additionalDriverListAdapter);

        onClick();

        getUserDriverList();
    }

    @Override
    public void editClick(AdditionalDriverModel model) {
        forEditData = true;
        forAddData = false;
        edit_driver_id = model.getId();
        getUserDriverDetails(model.getId());
    }

    @Override
    public void deleteClick(AdditionalDriverModel model) {
        onDeleteClick(model, getResources().getString(R.string.deleteDriver));
    }

    @Override
    public void uploadClick(AdditionalDriverModel model) {
        Config.currentProfileFlag = Config.Additional_Driver_Document;
        Config.driverIDFORDOC = model.getId();
        Intent intent = new Intent(context, ProfileViewActivity.class);
        startActivity(intent);
    }

    private void onDeleteClick(AdditionalDriverModel model, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        hitDeleteDriveDetailsDetails(dialog, model.getId());
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

    private void onClick() {

        binding.txtAddNewDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                forEditData = false;
                forAddData = true;
                binding.lnrDriverList.setVisibility(View.GONE);
                binding.additionalDetailView.setVisibility(View.VISIBLE);
                binding.lnrAdditionalDetails.setVisibility(View.VISIBLE);
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

        binding.spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddressModel model = lisAddressEmirate.get(position);
                if (!model.getCountry_name().equals(getResources().getString(R.string.nationality))) {
                    countryName = model.getCountry_name();
                    countryId = model.getId();

                    if (countryName.equals(Config.UAE)) {
                        binding.lnrEmirate.setVisibility(View.VISIBLE);
                    } else {
                        emirateID = "";
                        binding.lnrEmirate.setVisibility(View.GONE);
                    }

                } else {
                    emirateID = "";
                    countryName = "";
                    countryId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerEmirate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmirateModel model = listLocation.get(position);
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

        binding.additionalDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDetailVisibility();
            }
        });

        binding.txtPersonalDetailsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkAdditionalDetailValidation()) {
                    hitSaveDriverDetails();
                }
            }
        });
    }

    private void checkListVisibility() {
        if (binding.lnrDriverList.getVisibility() == View.GONE) {
            binding.lnrDriverList.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDriveLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDriveRight.setImageResource(R.drawable.ic_minus);
            }
        } else if (binding.lnrDriverList.getVisibility() == View.VISIBLE) {
            binding.lnrDriverList.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDriveLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDriveRight.setImageResource(R.drawable.ic_plus);
            }
        }
    }

    private void checkDetailVisibility() {
        if (binding.lnrAdditionalDetails.getVisibility() == View.GONE) {
            binding.lnrAdditionalDetails.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDriveLeft.setImageResource(R.drawable.ic_minus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDriveRight.setImageResource(R.drawable.ic_minus);
            }
        } else if (binding.lnrAdditionalDetails.getVisibility() == View.VISIBLE) {
            binding.lnrAdditionalDetails.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDriveLeft.setImageResource(R.drawable.ic_plus);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDriveRight.setImageResource(R.drawable.ic_plus);
            }
        }
    }

    private boolean checkAdditionalDetailValidation() {

        boolean value = true;

        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.pleaseEnterFirstName));
        } else if (TextUtils.isEmpty(binding.etxMiddleName.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterMiddleName));
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
        } else if (TextUtils.isEmpty(countryId)) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.selectNationality));
        } else if (binding.lnrEmirate.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(emirateID)) {
                value = false;
                H.showMessage(context, getResources().getString(R.string.selectEmirate));
            }
        }

        return value;
    }


    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgDriveRight.setVisibility(View.GONE);
            binding.imgDriveLeft.setVisibility(View.VISIBLE);

            binding.imgNationalityRight.setVisibility(View.GONE);
            binding.imgNationalityLeft.setVisibility(View.VISIBLE);

            binding.imgEmirateRight.setVisibility(View.GONE);
            binding.imgEmirateLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgDriveRight.setVisibility(View.VISIBLE);
            binding.imgDriveLeft.setVisibility(View.GONE);

            binding.imgNationalityRight.setVisibility(View.VISIBLE);
            binding.imgNationalityLeft.setVisibility(View.GONE);

            binding.imgEmirateRight.setVisibility(View.VISIBLE);
            binding.imgEmirateLeft.setVisibility(View.GONE);

        }

    }

    private void setDateTimeField(EditText editText) {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

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

    private void getUserDriverList() {

        ProgressView.show(context, loadingDialog);

        Api.newApi(context, P.BaseUrl + "user_driver_list")
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        additionalDriverModelList.clear();
                        additionalDriverListAdapter.notifyDataSetChanged();

                        json = json.getJson(P.data);
                        JsonList list = json.getJsonList(P.list);

                        if (list != null && list.size() != 0) {

                            for (Json jsonValue : list) {
                                AdditionalDriverModel model = new AdditionalDriverModel();
                                model.setId(jsonValue.getString("id"));
                                model.setDriver_name(jsonValue.getString("driver_name"));
                                model.setDriver_middlename(jsonValue.getString("driver_middlename"));
                                model.setDriver_lastname(jsonValue.getString("driver_lastname"));
                                model.setDriver_dob(jsonValue.getString("driver_dob"));
                                model.setDriver_country_name(jsonValue.getString("driver_country_name"));
                                model.setDriver_country_id(jsonValue.getString("driver_country_id"));
                                model.setDriver_emirate_id(jsonValue.getString("driver_emirate_id"));
                                model.setDriver_emirate_name(jsonValue.getString("driver_emirate_name"));
                                model.setDriver_email(jsonValue.getString("driver_email"));
                                model.setDriver_user_country_code(jsonValue.getString("driver_user_country_code"));
                                model.setDriver_mobile(jsonValue.getString("driver_mobile"));
                                model.setDriver_alt_country_code(jsonValue.getString("driver_alt_country_code"));
                                model.setDriver_alt_mobile(jsonValue.getString("driver_alt_mobile"));
                                model.setDriver_gender(jsonValue.getString("driver_gender"));
                                model.setDriver_gender_name(jsonValue.getString("driver_gender_name"));
                                additionalDriverModelList.add(model);
                            }

                            additionalDriverListAdapter.notifyDataSetChanged();
                        }

                        if (additionalDriverModelList.isEmpty()) {
                            binding.additionalDetailView.setVisibility(View.VISIBLE);
                            binding.lnrAdditionalDetails.setVisibility(View.GONE);
                            binding.lnrDriverList.setVisibility(View.GONE);
                        } else {
                            binding.additionalDetailView.setVisibility(View.GONE);
                            binding.lnrAdditionalDetails.setVisibility(View.GONE);
                            binding.lnrDriverList.setVisibility(View.VISIBLE);
                        }

                        binding.nestedScroll.fullScroll(View.FOCUS_UP);
                    } else {
                        binding.nestedScroll.fullScroll(View.FOCUS_UP);
                        H.showMessage(context, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("getUserDriverList", session.getString(P.token));

    }

    private void getUserDriverDetails(String driver_id) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        j.addString(P.driver_id, driver_id);

        Api.newApi(context, P.BaseUrl + "user_driver_data").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        binding.lnrDriverList.setVisibility(View.GONE);
                        binding.additionalDetailView.setVisibility(View.VISIBLE);
                        binding.lnrAdditionalDetails.setVisibility(View.VISIBLE);

                        json = json.getJson(P.data);
                        json = json.getJson(P.detail);

                        binding.etxFirstName.setText(CheckString.check(json.getString(P.driver_name)));
                        binding.etxMiddleName.setText(CheckString.check(json.getString(P.driver_middlename)));
                        binding.etxLastName.setText(CheckString.check(json.getString(P.driver_lastname)));
                        binding.etxBirtDate.setText(CheckString.check(json.getString(P.driver_dob)));
                        binding.etxEmail.setText(CheckString.check(json.getString(P.driver_email)));

                        binding.etxNumber.setText(CheckString.check(json.getString(P.driver_mobile)));
                        binding.etxAlternameNumber.setText(CheckString.check(json.getString(P.driver_alt_mobile)));

                        String gender = json.getString(P.driver_gender);
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

                        String codePrimary = CheckString.check(json.getString(P.driver_user_country_code));
                        String codeSecondary = CheckString.check(json.getString(P.driver_alt_country_code));
                        String countryID = CheckString.check(json.getString(P.driver_country_id));
                        String emirateID = CheckString.check(json.getString(P.driver_emirate_id));


                        JsonList jsonList = Config.countryJsonList;
                        for (int i = 0; i < jsonList.size(); i++) {
                            Json jsonData = jsonList.get(i);
                            if (jsonData.getString(P.phone_code).equalsIgnoreCase(codePrimary)) {
                                binding.spinnerCodeMobile.setSelection(i);
                            }

                            if (jsonData.getString(P.id).equalsIgnoreCase(codeSecondary)) {
                                binding.spinnerCodeAlternate.setSelection(i);
                            }

                            if (jsonData.getString(P.id).equalsIgnoreCase(countryID)) {
                                if (i < jsonList.size()) {
                                    binding.spinnerNationality.setSelection(i + 1);
                                }
                            }
                        }

                        JsonList emirate_list = HomeFragment.emirate_list;
                        for (int i = 0; i < emirate_list.size(); i++) {
                            Json jsonData = emirate_list.get(i);
                            if (jsonData.getString(P.id).equalsIgnoreCase(emirateID)) {
                                binding.spinnerEmirate.setSelection(i + 1);
                            }
                        }

                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("getUserDriverDetails", session.getString(P.token));

    }

    private void clearView() {
        binding.etxFirstName.setText("");
        binding.etxMiddleName.setText("");
        binding.etxLastName.setText("");
        binding.etxBirtDate.setText("");
        binding.etxEmail.setText("");
        binding.etxNumber.setText("");
        binding.etxAlternameNumber.setText("");
        binding.spinnerNationality.setSelection(0);
        binding.spinnerEmirate.setSelection(0);
    }

    private void hitSaveDriverDetails() {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        if (forEditData) {
            j.addString(P.driver_id, edit_driver_id);
        }
        j.addString(P.driver_name, binding.etxFirstName.getText().toString().trim());
        j.addString(P.driver_middlename, binding.etxMiddleName.getText().toString().trim());
        j.addString(P.driver_lastname, binding.etxLastName.getText().toString().trim());
        j.addString(P.driver_dob, binding.etxBirtDate.getText().toString().trim());
        j.addString(P.driver_email, binding.etxEmail.getText().toString().trim());
        if (binding.radioMale.isChecked()) {
            j.addString(P.driver_gender, "1");
        } else if (binding.radioFemale.isChecked()) {
            j.addString(P.driver_gender, "2");
        }
        j.addString(P.driver_user_country_code, codePrimary);
        j.addString(P.driver_mobile, binding.etxNumber.getText().toString().trim());
        j.addString(P.driver_alt_country_code, codeSecondary);
        j.addString(P.driver_alt_mobile, binding.etxAlternameNumber.getText().toString().trim());
        j.addString(P.driver_country_id, countryId);
        j.addString(P.driver_emirate_id, emirateID);

        Api.newApi(context, P.BaseUrl + "save_driver_data").addJson(j)
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
                        H.showMessage(context, getResources().getString(R.string.dataUpdated));
                        forEditData = false;
                        forAddData = false;
                        clearView();
                        getUserDriverList();
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitSaveDriverDetails", session.getString(P.token));
    }

    private void hitDeleteDriveDetailsDetails(DialogInterface dialog, String driver_id) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        j.addString(P.driver_id, driver_id);

        Api.newApi(context, P.BaseUrl + "delete_driver_data").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        dialog.dismiss();
                        getUserDriverList();
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDeleteDriveDetailsDetails", session.getString(P.token));
    }

    public static AdditionalDriverFragment newInstance() {
        AdditionalDriverFragment fragment = new AdditionalDriverFragment();
        return fragment;
    }

}
