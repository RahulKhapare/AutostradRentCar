package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Json;
import com.example.fastuae.R;
import com.example.fastuae.activity.FAQActivity;
import com.example.fastuae.adapter.FAQAdapter;
import com.example.fastuae.databinding.FragmentVehicleRelatedBinding;
import com.example.fastuae.model.FAQModel;
import com.example.fastuae.util.P;

import java.util.ArrayList;
import java.util.List;

public class VehicleRelatedFragment extends Fragment {

    private Context context;
    private FragmentVehicleRelatedBinding binding;
    private List<FAQModel> faqModelList;
    private FAQAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_related, container, false);
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

        faqModelList = new ArrayList<>();
        adapter = new FAQAdapter(context,faqModelList);
        binding.recycelrFAQ.setLayoutManager(new LinearLayoutManager(context));
        binding.recycelrFAQ.setNestedScrollingEnabled(false);
        binding.recycelrFAQ.setAdapter(adapter);
        setData();
    }

    private void setData(){

        if (FAQActivity.vehicleJsonList!=null){
            faqModelList.clear();
            for (Json json : FAQActivity.vehicleJsonList){
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


    public static VehicleRelatedFragment newInstance() {
        VehicleRelatedFragment fragment = new VehicleRelatedFragment();
        return fragment;
    }

}
