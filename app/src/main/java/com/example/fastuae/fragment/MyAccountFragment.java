package com.example.fastuae.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.AddressSelectionAdapter;
import com.example.fastuae.adapter.CodeSelectionAdapter;
import com.example.fastuae.databinding.FragmentMyAccountBinding;
import com.example.fastuae.model.AddressModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
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

        session = new Session(context);
        flag = session.getString(P.languageFlag);

        countryCodeModelList = new ArrayList<>();

        lisAddressEmirate = new ArrayList<>();
        AddressModel modelAddress1 = new AddressModel();
        modelAddress1.setEmirate(getResources().getString(R.string.selectEmirate));
        lisAddressEmirate.add(modelAddress1);

        AddressModel modelAddress2 = new AddressModel();
        modelAddress2.setEmirate("1");
        lisAddressEmirate.add(modelAddress2);

        AddressModel modelAddress3 = new AddressModel();
        modelAddress3.setEmirate("2");
        lisAddressEmirate.add(modelAddress3);


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

    }

    private void onClick() {
        binding.spinnerCodeMobile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerCodeAlternate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddressModel model = lisAddressEmirate.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.radioMale.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioFemale.setChecked(false);
                blueTin(binding.radioMale);
                blackTin(binding.radioFemale);
            }
        });

        binding.radioFemale.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        });

        binding.txtPersonalDetailsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkPersonalDetailValidation()) {

                }
            }
        });

        binding.passwordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
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
        });

        binding.txtPasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkPasswordValidation()){

                }
            }
        });

        binding.lnrAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
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
        });

        binding.txtAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

            }
        });
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
        } else if (binding.etxPassword.getText().toString().trim().length()<6){
            value = false;
            H.showMessage(context, getResources().getString(R.string.minPassLength));
        } else if (TextUtils.isEmpty(binding.etxConfirmPassword.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterConfirmPassword));
        } else if (!binding.etxConfirmPassword.getText().toString().trim().equals(binding.etxPassword.getText().toString().trim())){
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void blackTin(RadioButton radioButton) {
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{getResources().getColor(R.color.textDark)}
                },
                new int[]{getResources().getColor(R.color.textDark)}
        );

        radioButton.setButtonTintList(myColorStateList);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void blueTin(RadioButton radioButton) {
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{getResources().getColor(R.color.lightBlue)}
                },
                new int[]{getResources().getColor(R.color.lightBlue)}
        );

        radioButton.setButtonTintList(myColorStateList);
    }


    public static MyAccountFragment newInstance() {
        MyAccountFragment fragment = new MyAccountFragment();
        return fragment;
    }

}
