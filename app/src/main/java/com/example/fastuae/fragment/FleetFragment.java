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
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.adapter.CarFleetAdapter;
import com.example.fastuae.databinding.FragmentFleetBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.model.CarFleetModel;
import com.example.fastuae.util.Config;

import java.util.ArrayList;
import java.util.List;

public class FleetFragment extends Fragment {

    private Context context;
    private FragmentFleetBinding binding;
    private List<CarFleetModel> carFleetModelListOne;
    private List<CarFleetModel> carFleetModelListTwo;
    private CarFleetAdapter carFleetAdapterOne;
    private CarFleetAdapter carFleetAdapterTwo;

    public static String groupValue = "";
    public static String passengerValue = "";
    public static String doorValue = "";
    public static String suitcaseValue = "";
    public static String transmissionValue = "";
    public static String fuilValue = "";
    public static boolean isApplyFilter = false;

    public static List<CarFilterModel> groupSelectionList;
    public static List<CarFilterModel> passengerSelectionList;
    public static List<CarFilterModel> doorSelectionList;
    public static List<CarFilterModel> suitcaseSelectionList;
    public static List<CarFilterModel> transmissionSelectionList;
    public static List<CarFilterModel> fuilSelectionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fleet, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
    }

    private void initView(){

        Config.FILTER_VALUE = Config.FILTER_TWO;
        groupSelectionList = new ArrayList<>();
        passengerSelectionList = new ArrayList<>();
        doorSelectionList = new ArrayList<>();
        suitcaseSelectionList = new ArrayList<>();
        transmissionSelectionList = new ArrayList<>();
        fuilSelectionList = new ArrayList<>();

        carFleetModelListOne = new ArrayList<>();
        carFleetAdapterOne = new CarFleetAdapter(context,carFleetModelListOne);
        binding.recyclerCarFleetOne.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerCarFleetOne.setNestedScrollingEnabled(false);
        binding.recyclerCarFleetOne.setAdapter(carFleetAdapterOne);

        carFleetModelListTwo = new ArrayList<>();
        carFleetAdapterTwo = new CarFleetAdapter(context,carFleetModelListTwo);
        binding.recyclerCarFleetTwo.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerCarFleetTwo.setNestedScrollingEnabled(false);
        binding.recyclerCarFleetTwo.setAdapter(carFleetAdapterTwo);

        setData();

    }


    private void setData(){

        CarFleetModel modelOne = new CarFleetModel();
        modelOne.setImage(R.drawable.ic_car_grey);
        modelOne.setCarName("Toyota Yeris 1.3");
        modelOne.setGroupName("Group A");
        carFleetModelListOne.add(modelOne);
        carFleetModelListOne.add(modelOne);
        carFleetModelListOne.add(modelOne);

        carFleetAdapterOne.notifyDataSetChanged();

        CarFleetModel modelTwo = new CarFleetModel();
        modelTwo.setImage(R.drawable.ic_car_red);
        modelTwo.setCarName("Hyundai Accent 1.6");
        modelTwo.setGroupName("Group B");
        carFleetModelListTwo.add(modelTwo);
        carFleetModelListTwo.add(modelTwo);
        carFleetModelListTwo.add(modelTwo);

        carFleetAdapterTwo.notifyDataSetChanged();

    }


    public static FleetFragment newInstance() {
        FleetFragment fragment = new FleetFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (FleetFragment.isApplyFilter) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isApplyFilter = false;
        groupValue = "";
        passengerValue = "";
        doorValue = "";
        suitcaseValue = "";
        transmissionValue = "";
        fuilValue = "";
    }
}
