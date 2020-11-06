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
import com.example.fastuae.adapter.CarGridAdapter;
import com.example.fastuae.databinding.FragmentGreedCardBinding;
import com.example.fastuae.model.CarModel;

import java.util.ArrayList;
import java.util.List;

public class CarGreedFragment extends Fragment {

    private Context context;
    private FragmentGreedCardBinding binding;
    private List<CarModel> carModelList;
    private CarGridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_greed_card, container, false);
            context = inflater.getContext();

            initView();

        }

        return binding.getRoot();
    }

    private void initView(){

        carModelList = new ArrayList<>();

        CarModel model = new CarModel();
        model.setName("Murcedes suv");
        model.setGroup("Group A");
        model.setType("Automatic");
        model.setModel("SUV");
        model.setSeat("5 Seat");
        model.setEngine("Engine");
        model.setDore("3 Door");
        model.setAedNow("1160 AED");
        model.setAedLater("1200 AED");

        carModelList.add(model);
        carModelList.add(model);
        carModelList.add(model);
        carModelList.add(model);
        carModelList.add(model);

        adapter = new CarGridAdapter(context,carModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.recyclerCar.setLayoutManager(linearLayoutManager);
        binding.recyclerCar.setAdapter(adapter);

    }

    public static CarGreedFragment newInstance() {
        CarGreedFragment fragment = new CarGreedFragment();
        return fragment;
    }

}
