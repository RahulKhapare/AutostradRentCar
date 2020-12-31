package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.fastuae.R;
import com.example.fastuae.adapter.FilterSpinnerAdapter;
import com.example.fastuae.databinding.ActivityCarFilterBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class CarFilterActivity extends AppCompatActivity {

    private CarFilterActivity activity = this;
    private ActivityCarFilterBinding binding;
    private List<CarFilterModel> priceList;
    private FilterSpinnerAdapter priceAdapter;

    private List<CarFilterModel> seatsList;
    private FilterSpinnerAdapter seatsAdapter;

    private List<CarFilterModel> passengersList;
    private FilterSpinnerAdapter passengersAdapter;

    private List<CarFilterModel> doorsList;
    private FilterSpinnerAdapter doorsAdapter;

    private List<CarFilterModel> transmissionList;
    private FilterSpinnerAdapter transmissionAdapter;

    private List<CarFilterModel> fuelList;
    private FilterSpinnerAdapter fuelAdapter;

    private List<CarFilterModel> categoryList;
    private FilterSpinnerAdapter categoryAdapter;

    private List<CarFilterModel> groupList;
    private FilterSpinnerAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_filter);

        initView();
    }

    private void initView(){
        binding.toolbar.setTitle(getResources().getString(R.string.filterYourSearch));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        priceList = new ArrayList<>();;
        priceAdapter = new FilterSpinnerAdapter(activity,priceList);
        binding.spinnerPrice.setAdapter(priceAdapter);

        seatsList = new ArrayList<>();;
        seatsAdapter = new FilterSpinnerAdapter(activity,seatsList);
        binding.spinnerSeats.setAdapter(seatsAdapter);

        passengersList = new ArrayList<>();;
        passengersAdapter = new FilterSpinnerAdapter(activity,passengersList);
        binding.spinnerPassengers.setAdapter(passengersAdapter);

        doorsList = new ArrayList<>();;
        doorsAdapter = new FilterSpinnerAdapter(activity,doorsList);
        binding.spinnerDoors.setAdapter(doorsAdapter);

        transmissionList = new ArrayList<>();;
        transmissionAdapter = new FilterSpinnerAdapter(activity,transmissionList);
        binding.spinnerTransmission.setAdapter(transmissionAdapter);

        fuelList = new ArrayList<>();;
        fuelAdapter = new FilterSpinnerAdapter(activity,fuelList);
        binding.spinnerFuel.setAdapter(fuelAdapter);

        categoryList = new ArrayList<>();;
        categoryAdapter = new FilterSpinnerAdapter(activity,categoryList);
        binding.spinnerCategory.setAdapter(categoryAdapter);

        groupList = new ArrayList<>();;
        groupAdapter = new FilterSpinnerAdapter(activity,groupList);
        binding.spinnerGroup.setAdapter(groupAdapter);

        setData();
        onClick();

    }


    private void onClick(){

        binding.txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

    }

    private void setData(){

        priceList.add(new CarFilterModel("Select Price"));
        priceList.add(new CarFilterModel("1000"));
        priceList.add(new CarFilterModel("2000"));
        priceList.add(new CarFilterModel("3000"));
        priceList.add(new CarFilterModel("4000"));
        priceList.add(new CarFilterModel("5000"));
        priceAdapter.notifyDataSetChanged();

        seatsList.add(new CarFilterModel("Select Seat"));
        seatsList.add(new CarFilterModel("1"));
        seatsList.add(new CarFilterModel("2"));
        seatsList.add(new CarFilterModel("3"));
        seatsList.add(new CarFilterModel("4"));
        seatsList.add(new CarFilterModel("5"));
        seatsAdapter.notifyDataSetChanged();

        passengersList.add(new CarFilterModel("Select Passengers"));
        passengersList.add(new CarFilterModel("1"));
        passengersList.add(new CarFilterModel("2"));
        passengersList.add(new CarFilterModel("3"));
        passengersList.add(new CarFilterModel("4"));
        passengersList.add(new CarFilterModel("5"));
        passengersAdapter.notifyDataSetChanged();

        doorsList.add(new CarFilterModel("Select Doors"));
        doorsList.add(new CarFilterModel("1"));
        doorsList.add(new CarFilterModel("2"));
        doorsList.add(new CarFilterModel("3"));
        doorsList.add(new CarFilterModel("4"));
        doorsList.add(new CarFilterModel("5"));
        doorsAdapter.notifyDataSetChanged();

        transmissionList.add(new CarFilterModel("Select Transmission"));
        transmissionList.add(new CarFilterModel("1"));
        transmissionList.add(new CarFilterModel("2"));
        transmissionList.add(new CarFilterModel("3"));
        transmissionList.add(new CarFilterModel("4"));
        transmissionList.add(new CarFilterModel("5"));
        transmissionAdapter.notifyDataSetChanged();

        fuelList.add(new CarFilterModel("Select Fuel"));
        fuelList.add(new CarFilterModel("Petrol"));
        fuelList.add(new CarFilterModel("Deisel"));
        fuelAdapter.notifyDataSetChanged();

        categoryList.add(new CarFilterModel("Select Category"));
        categoryList.add(new CarFilterModel("A"));
        categoryList.add(new CarFilterModel("B"));
        categoryList.add(new CarFilterModel("C"));
        categoryAdapter.notifyDataSetChanged();

        groupList.add(new CarFilterModel("Select Group"));
        groupList.add(new CarFilterModel("A"));
        groupList.add(new CarFilterModel("B"));
        groupList.add(new CarFilterModel("C"));
        groupAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

}