package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fastuae.R;
import com.example.fastuae.activity.MainActivity;
import com.example.fastuae.activity.ProfileViewActivity;
import com.example.fastuae.adapter.InvoiceAdapter;
import com.example.fastuae.databinding.FragmentTrafficLinesBinding;
import com.example.fastuae.model.InvoiceModel;

import java.util.ArrayList;
import java.util.List;

public class TrafficLinesFragment extends Fragment implements InvoiceAdapter.onClick {

    private Context context;
    private FragmentTrafficLinesBinding binding;
    private List<InvoiceModel> invoiceModelList;
    private InvoiceAdapter invoiceAdapter;
    private String path = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_traffic_lines, container, false);
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

    private void initView(){

        invoiceModelList = new ArrayList<>();
        invoiceAdapter = new InvoiceAdapter(context,invoiceModelList,TrafficLinesFragment.this,3);
        binding.recyclerTraffic.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerTraffic.setNestedScrollingEnabled(false);
        binding.recyclerTraffic.setAdapter(invoiceAdapter);

        setData();

    }

    private void setData(){

        InvoiceModel model = new InvoiceModel();
        model.setDate("5/12/2020");
        model.setType("RCT");
        model.setNo("1545248545485");
        model.setCreditAmount("95998.00");
        model.setDebitAmount("15212.00");
        model.setPaymentType("Visa Credit Car Payment");
        model.setPath(path);

        invoiceModelList.add(model);
        invoiceModelList.add(model);
        invoiceModelList.add(model);
        invoiceAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDownload(String path) {
        ((ProfileViewActivity)getActivity()).checkPDF(path);
    }

    public static TrafficLinesFragment newInstance() {
        TrafficLinesFragment fragment = new TrafficLinesFragment();
        return fragment;
    }

}
