package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.activity.FAQActivity;
import com.example.fastuae.adapter.FAQAdapter;
import com.example.fastuae.databinding.FragmentGeneralBinding;
import com.example.fastuae.model.FAQModel;
import com.example.fastuae.util.ConnectionUtil;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class GeneralFragment extends Fragment {

    private Context context;
    private FragmentGeneralBinding binding;
    private List<FAQModel> faqModelList;
    private FAQAdapter adapter;
    private LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
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
        loadingDialog = new LoadingDialog(context);
        faqModelList = new ArrayList<>();
        adapter = new FAQAdapter(context,faqModelList);
        binding.recycelrFAQ.setLayoutManager(new LinearLayoutManager(context));
        binding.recycelrFAQ.setNestedScrollingEnabled(false);
        binding.recycelrFAQ.setAdapter(adapter);

        if (ConnectionUtil.isOnline(context)){
            hitFAQData();
        }else {
            H.showMessage(context,getResources().getString(R.string.noInternet));
        }

    }

    private void setData(){

        if (FAQActivity.generalJsonList!=null){
            faqModelList.clear();
            for (Json json : FAQActivity.generalJsonList){
                FAQModel model = new FAQModel();
                model.setQuestion(json.getString(P.question));
                model.setAnswer(json.getString(P.answer));
                model.setFaq_category_name(json.getString(P.faq_category_name));
                model.setClickFlag(false);
                faqModelList.add(model);
            }

            adapter.notifyDataSetChanged();
        }

    }

    private void hitFAQData() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "faq").addJson(j)
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
                        Json faqJson = json.getJson(P.faq_list);
                        FAQActivity.generalJsonList = faqJson.getJsonList("GENERAL");
                        FAQActivity.paymentJsonList = faqJson.getJsonList("PAYMENT RELATED");
                        FAQActivity.vehicleJsonList = faqJson.getJsonList("VEHICLE RELATED");
                        FAQActivity.insuranceJsonList = faqJson.getJsonList("INSURANCE RELATED");
                        setData();

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitFAQData");
    }

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        return fragment;
    }

}
