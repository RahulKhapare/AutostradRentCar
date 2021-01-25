package com.example.fastuae.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.PaymentCardAdapter;
import com.example.fastuae.databinding.FragmentManagePaymentBinding;
import com.example.fastuae.model.PaymentCardModel;
import com.example.fastuae.util.CheckString;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class ManagePaymentFragment extends Fragment {

    private Context context;
    private FragmentManagePaymentBinding binding;
    private List<PaymentCardModel> paymentCardModelList;
    private PaymentCardAdapter paymentCardAdapter;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_payment, container, false);
            context = inflater.getContext();
            initView();
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
        paymentCardModelList = new ArrayList<>();
        paymentCardAdapter = new PaymentCardAdapter(context, paymentCardModelList);
        binding.recyclerCard.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerCard.setNestedScrollingEnabled(false);
        binding.recyclerCard.setAdapter(paymentCardAdapter);

        onClick();
        hitUserPaymentDetails();
    }

    private void onClick() {


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
                    hideKeyboard(context);
                    hitAddUserPaymentDetails();
                }
            }
        });
    }

    private boolean checkAddCardDetails() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxCardNumber.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterCardNumber));
        } else if (binding.etxCardNumber.getText().toString().trim().length() < 16 || binding.etxCardNumber.getText().toString().trim().length() > 16) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterValidCarNumber));
        } else if (TextUtils.isEmpty(binding.etxCardName.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterCardName));
        } else if (TextUtils.isEmpty(binding.etxValidMonth.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterMonth));
        } else if (binding.etxValidMonth.getText().toString().trim().length() < 5 || binding.etxValidMonth.getText().toString().trim().length() > 5) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterValidMonth));
        } else if (!binding.etxValidMonth.getText().toString().matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.checkCardFormat));
        } else if (TextUtils.isEmpty(binding.etxCvv.getText().toString().trim())) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterCvv));
        } else if (binding.etxCvv.getText().toString().trim().length() < 3 || binding.etxCvv.getText().toString().trim().length() > 3) {
            value = false;
            H.showMessage(context, getResources().getString(R.string.enterValidCvv));
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

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "user_payments").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
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
                        H.showMessage(context,json.getString(P.error));
                    }

                    paymentCardAdapter.notifyDataSetChanged();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUserPaymentDetails",session.getString(P.token));

    }

    private void hitAddUserPaymentDetails() {

        ProgressView.show(context,loadingDialog);
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

        Api.newApi(context, P.BaseUrl + "add_user_payment_option").addJson(j)
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
                        clearPaymentView();
                    }else {
                        H.showMessage(context,json.getString(P.error));
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

    public static ManagePaymentFragment newInstance() {
        ManagePaymentFragment fragment = new ManagePaymentFragment();
        return fragment;
    }

}
