package com.autostrad.rentcar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.autostrad.rentcar.adapter.PaymentCardAdapter;
import com.autostrad.rentcar.databinding.FragmentManagePaymentBinding;
import com.autostrad.rentcar.model.PaymentCardModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class ManagePaymentFragment extends Fragment implements PaymentCardAdapter.onClick {

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
        paymentCardAdapter = new PaymentCardAdapter(context, paymentCardModelList,1,ManagePaymentFragment.this);
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
        }
        else if (TextUtils.isEmpty(binding.etxCvv.getText().toString().trim())) {
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

    @Override
    public void onEditPayment(PaymentCardModel model) {
        editPaymentDialog(model);
    }

    @Override
    public void onDeletePayment(PaymentCardModel model) {
        onDeleteClick(model,getResources().getString(R.string.deletePaymentMSG));
    }

    private void onDeleteClick(PaymentCardModel model, String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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

        final Dialog dialog = new Dialog(context);
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
                    H.showMessage(context, getResources().getString(R.string.enterCardNumber));
                    return;
                } else if (etxCardNumber.getText().toString().trim().length() < 16 || etxCardNumber.getText().toString().trim().length() > 16) {
                    H.showMessage(context, getResources().getString(R.string.enterValidCarNumber));
                    return;
                } else if (TextUtils.isEmpty(etxCardName.getText().toString().trim())) {
                    H.showMessage(context, getResources().getString(R.string.enterCardName));
                    return;
                } else if (TextUtils.isEmpty(etxValidMonth.getText().toString().trim())) {
                    H.showMessage(context, getResources().getString(R.string.enterMonth));
                    return;
                } else if (etxValidMonth.getText().toString().trim().length() < 5 || etxValidMonth.getText().toString().trim().length() > 5) {
                    H.showMessage(context, getResources().getString(R.string.enterValidMonth));
                    return;
                } else if (!etxValidMonth.getText().toString().matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) {
                    H.showMessage(context, getResources().getString(R.string.checkCardFormat));
                    return;
                }
                else if (TextUtils.isEmpty(etxCvv.getText().toString().trim())) {
                    H.showMessage(context, getResources().getString(R.string.enterCvv));
                    return;
                } else if (etxCvv.getText().toString().trim().length() < 3 || etxCvv.getText().toString().trim().length() > 3) {
                    H.showMessage(context, getResources().getString(R.string.enterValidCvv));
                    return;
                }

                hideKeyboard(context);
                hitEditUserPaymentDetails(dialog, model, etxCardNumber, etxCardName, etxValidMonth, etxCvv);

            }
        });


        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private void hitEditUserPaymentDetails(Dialog dialog, PaymentCardModel model, EditText etxCardNumber, EditText etxCardName, EditText etxValidMonth, EditText etxCvv) {

        ProgressView.show(context, loadingDialog);
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
                        dialog.dismiss();
                        json = json.getJson(P.data);
                        H.showMessage(context, getResources().getString(R.string.dataUpdated));
                        paymentCardModelList.clear();
                        hitUserPaymentDetails();
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitEditUserPaymentDetails", session.getString(P.token));
    }

    private void hitDeleteUserPaymentDetails(DialogInterface dialog,PaymentCardModel model) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        j.addString(P.id, model.getId());

        Api.newApi(context, P.BaseUrl + "delete_user_payment_option").addJson(j)
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
                        json = json.getJson(P.data);
                        H.showMessage(context, getResources().getString(R.string.dataUpdated));
                        paymentCardModelList.clear();
                        hitUserPaymentDetails();
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDeleteUserPaymentDetails", session.getString(P.token));
    }
}
