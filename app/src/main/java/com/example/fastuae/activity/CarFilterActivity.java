package com.example.fastuae.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CarFilterAdapter;
import com.example.fastuae.databinding.ActivityCarFilterBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.model.HomeLocationModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarFilterActivity extends AppCompatActivity implements CarFilterAdapter.onClick {

    private CarFilterActivity activity = this;
    private ActivityCarFilterBinding binding;

    private List<CarFilterModel> suitcaseList;
    private CarFilterAdapter suitcaseAdapter;

    private List<CarFilterModel> passengersList;
    private CarFilterAdapter passengersAdapter;

    private List<CarFilterModel> doorsList;
    private CarFilterAdapter doorsAdapter;

    private List<CarFilterModel> transmissionList;
    private CarFilterAdapter transmissionAdapter;

    private List<CarFilterModel> fuelList;
    private CarFilterAdapter fuelAdapter;

    private List<CarFilterModel> groupList;
    private CarFilterAdapter groupAdapter;

    private Session session;
    private String flag;
    private LoadingDialog loadingDialog;

    private int groupFlag = 1;
    private int passengerFlag = 2;
    private int doorFlag = 3;
    private int suitcaseFlag = 4;
    private int transmissionFlag = 5;
    private int fuelFlag = 6;
    String selectGroup;
    String selectPassengers;
    String selectDoors;
    String selectSuitcase;
    String selectTransmission;
    String selectFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_filter);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.filterYourSearch));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        selectGroup = getResources().getString(R.string.selectGroup);
        selectPassengers = getResources().getString(R.string.selectPassengers);
        selectDoors = getResources().getString(R.string.selectDoors);
        selectSuitcase = getResources().getString(R.string.selectSuitcase);
        selectTransmission = getResources().getString(R.string.selectTransmission);
        selectFuel = getResources().getString(R.string.selectFuel);

        updateIcons();

        suitcaseList = new ArrayList<>();
        suitcaseAdapter = new CarFilterAdapter(activity, suitcaseList,suitcaseFlag);
        binding.recyclerSuitcase.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerSuitcase.setAdapter(suitcaseAdapter);

        passengersList = new ArrayList<>();
        passengersAdapter = new CarFilterAdapter(activity, passengersList,passengerFlag);
        binding.recyclerPassengers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerPassengers.setAdapter(passengersAdapter);

        doorsList = new ArrayList<>();
        doorsAdapter = new CarFilterAdapter(activity, doorsList,doorFlag);
        binding.recyclerDoors.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerDoors.setAdapter(doorsAdapter);

        transmissionList = new ArrayList<>();
        transmissionAdapter = new CarFilterAdapter(activity, transmissionList,transmissionFlag);
        binding.recyclerTransmission.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerTransmission.setAdapter(transmissionAdapter);

        fuelList = new ArrayList<>();
        fuelAdapter = new CarFilterAdapter(activity, fuelList,fuelFlag);
        binding.recyclerFuel.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerFuel.setAdapter(fuelAdapter);

        groupList = new ArrayList<>();
        groupAdapter = new CarFilterAdapter(activity, groupList,groupFlag);
        binding.recyclerGroup.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerGroup.setAdapter(groupAdapter);

        onClick();

        hitCarFilterData();

    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {


            binding.imgPassengerRight.setVisibility(View.GONE);
            binding.imgPassengerLeft.setVisibility(View.VISIBLE);

            binding.imgDoorRight.setVisibility(View.GONE);
            binding.imgDoorLeft.setVisibility(View.VISIBLE);

            binding.imgTransRight.setVisibility(View.GONE);
            binding.imgTransLeft.setVisibility(View.VISIBLE);

            binding.imgFuelRight.setVisibility(View.GONE);
            binding.imgFuelLeft.setVisibility(View.VISIBLE);

            binding.imgSuitcaseRight.setVisibility(View.GONE);
            binding.imgSuitcaseLeft.setVisibility(View.VISIBLE);

            binding.imgGroupRight.setVisibility(View.GONE);
            binding.imgGroupLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgPassengerRight.setVisibility(View.VISIBLE);
            binding.imgPassengerLeft.setVisibility(View.GONE);

            binding.imgDoorRight.setVisibility(View.VISIBLE);
            binding.imgDoorLeft.setVisibility(View.GONE);

            binding.imgTransRight.setVisibility(View.VISIBLE);
            binding.imgTransLeft.setVisibility(View.GONE);

            binding.imgFuelRight.setVisibility(View.VISIBLE);
            binding.imgFuelLeft.setVisibility(View.GONE);

            binding.imgSuitcaseRight.setVisibility(View.VISIBLE);
            binding.imgSuitcaseLeft.setVisibility(View.GONE);

            binding.imgGroupRight.setVisibility(View.VISIBLE);
            binding.imgGroupLeft.setVisibility(View.GONE);

        }
    }


    private void suitcaseClick(){
        checkSuitcaseView();
        hidePassengerView();
        hideDoorView();
        hideTrasnView();
        hideFuelView();
        hideGroupView();
    }

    private void passengerClick(){
        checkPassengerView();
        hideDoorView();
        hideTrasnView();
        hideFuelView();
        hideGroupView();
        hideSuitcaseView();
    }

    private void doorClick(){
        checkDoorView();
        hidePassengerView();
        hideTrasnView();
        hideFuelView();
        hideGroupView();
        hideSuitcaseView();
    }

    private void transmissionClick(){
        checkTrasnView();
        hidePassengerView();
        hideDoorView();
        hideFuelView();
        hideGroupView();
        hideSuitcaseView();
    }

    private void groupClick(){
        checkGroupView();
        hidePassengerView();
        hideDoorView();
        hideTrasnView();
        hideFuelView();
        hideSuitcaseView();
    }

    private void fuelClick(){
        checkFuelView();
        hidePassengerView();
        hideDoorView();
        hideTrasnView();
        hideGroupView();
        hideSuitcaseView();
    }

    private void onClick() {

        binding.txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                SelectCarActivity.isApplyFilter = true;
                finish();
            }
        });

        binding.cardSuitcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                suitcaseClick();
            }
        });

        binding.cardPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                passengerClick();
            }
        });

        binding.cardDoors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                doorClick();
            }
        });

        binding.cardTransmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                transmissionClick();
            }
        });

        binding.cardFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
               fuelClick();
            }
        });

        binding.cardGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                groupClick();
            }
        });

    }

    private void checkSize(List<CarFilterModel> list, RecyclerView recyclerView) {
        if (list.size() > 6) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = 900;
            recyclerView.setLayoutParams(params);
        }
    }

    private void addGroupPosition(){
        groupList.add(0,new CarFilterModel("",selectGroup,""));
        groupAdapter.notifyDataSetChanged();
    }

    private void addPassengerPosition(){
        passengersList.add(0,new CarFilterModel("",selectPassengers,""));
        passengersAdapter.notifyDataSetChanged();
    }

    private void addDoorPosition(){
        doorsList.add(0,new CarFilterModel("",selectDoors,""));
        doorsAdapter.notifyDataSetChanged();
    }

    private void addSuitcasePosition(){
        suitcaseList.add(0,new CarFilterModel("",selectSuitcase,""));
        suitcaseAdapter.notifyDataSetChanged();
    }

    private void addTransmissionPosition(){
        transmissionList.add(0,new CarFilterModel("",selectTransmission,""));
        transmissionAdapter.notifyDataSetChanged();
    }

    private void addFuelPosition(){
        fuelList.add(0,new CarFilterModel("",selectFuel,""));
        fuelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterClick(CarFilterModel model,int flag) {

        if (flag==groupFlag){
            if (!TextUtils.isEmpty(model.getId())){
                SelectCarActivity.groupValue = model.getId();
                if (!containsValue(groupList,selectGroup)){
                 addGroupPosition();
                }
            }else {
                SelectCarActivity.groupValue = "";
                if (containsValue(groupList,selectGroup)){
                    groupList.remove(0);
                    groupAdapter.notifyDataSetChanged();
                }
            }
            binding.txtGroupTitle.setText(model.getName());
            groupClick();
        }else if (flag==passengerFlag){
            if (!TextUtils.isEmpty(model.getId())){
                SelectCarActivity.passengerValue = model.getId();
                if (!containsValue(passengersList,selectPassengers)){
                    addPassengerPosition();
                }
            }else {
                SelectCarActivity.passengerValue = "";
                if (containsValue(passengersList,selectPassengers)){
                    passengersList.remove(0);
                    passengersAdapter.notifyDataSetChanged();
                }
            }
            binding.txtPassengetTitle.setText(model.getName());
            passengerClick();
        }else if (flag==doorFlag){
            if (!TextUtils.isEmpty(model.getId())){
                SelectCarActivity.doorValue = model.getId();
                if (!containsValue(doorsList,selectDoors)){
                   addDoorPosition();
                }
            }else {
                SelectCarActivity.doorValue = "";
                if (containsValue(doorsList,selectDoors)){
                    doorsList.remove(0);
                    doorsAdapter.notifyDataSetChanged();
                }
            }
            binding.txtDoorTitle.setText(model.getName());
            doorClick();
        }else if (flag==suitcaseFlag){
            if (!TextUtils.isEmpty(model.getId())){
                SelectCarActivity.suitcaseValue = model.getId();
                if (!containsValue(suitcaseList,selectSuitcase)){
                   addSuitcasePosition();
                }
            }else {
                SelectCarActivity.suitcaseValue = "";
                if (containsValue(suitcaseList,selectSuitcase)){
                    suitcaseList.remove(0);
                    suitcaseAdapter.notifyDataSetChanged();
                }
            }
            binding.txtSuitcaseTitle.setText(model.getName());
            suitcaseClick();
        }else if (flag==transmissionFlag){
            if (!TextUtils.isEmpty(model.getId())){
                SelectCarActivity.transmissionValue = model.getId();
                if (!containsValue(transmissionList,selectTransmission)){
                   addTransmissionPosition();
                }
            }else {
                SelectCarActivity.transmissionValue = "";
                if (containsValue(transmissionList,selectTransmission)){
                    transmissionList.remove(0);
                    transmissionAdapter.notifyDataSetChanged();
                }
            }
            binding.txtTransmissionTitle.setText(model.getName());
            transmissionClick();
        }else if (flag==fuelFlag){
            if (!TextUtils.isEmpty(model.getId())){
                SelectCarActivity.fuilValue = model.getId();
                if (!containsValue(fuelList,selectFuel)){
                    addFuelPosition();
                }
            }else {
                SelectCarActivity.fuilValue = "";
                if (containsValue(fuelList,selectFuel)){
                    fuelList.remove(0);
                    fuelAdapter.notifyDataSetChanged();
                }
            }
            binding.txtFuilTitle.setText(model.getName());
            fuelClick();
        }

    }

    private void checkSuitcaseView() {
        if (binding.viewSuitcase.getVisibility() == View.VISIBLE) {
            binding.viewSuitcase.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgSuitcaseLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgSuitcaseRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewSuitcase.getVisibility() == View.GONE) {
            binding.viewSuitcase.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgSuitcaseLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgSuitcaseRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideSuitcaseView() {
        binding.viewSuitcase.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgSuitcaseLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgSuitcaseRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }

    private void checkPassengerView() {
        if (binding.viewPassengers.getVisibility() == View.VISIBLE) {
            binding.viewPassengers.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPassengerLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPassengerRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewPassengers.getVisibility() == View.GONE) {
            binding.viewPassengers.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPassengerLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPassengerRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hidePassengerView() {
        binding.viewPassengers.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgPassengerLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgPassengerRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }

    private void checkDoorView() {
        if (binding.viewDoors.getVisibility() == View.VISIBLE) {
            binding.viewDoors.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDoorLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDoorRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewDoors.getVisibility() == View.GONE) {
            binding.viewDoors.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgDoorLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgDoorRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideDoorView() {
        binding.viewDoors.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgDoorLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgDoorRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }

    private void checkTrasnView() {
        if (binding.viewTransmission.getVisibility() == View.VISIBLE) {
            binding.viewTransmission.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgTransLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgTransRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewTransmission.getVisibility() == View.GONE) {
            binding.viewTransmission.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgTransLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgTransRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideTrasnView() {
        binding.viewTransmission.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgTransLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgTransRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }

    private void checkFuelView() {
        if (binding.viewFuel.getVisibility() == View.VISIBLE) {
            binding.viewFuel.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgFuelLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgFuelRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewFuel.getVisibility() == View.GONE) {
            binding.viewFuel.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgFuelLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgFuelRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideFuelView() {
        binding.viewFuel.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgFuelLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgFuelRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }


    private void checkGroupView() {
        if (binding.viewGroup.getVisibility() == View.VISIBLE) {
            binding.viewGroup.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgGroupLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgGroupRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewGroup.getVisibility() == View.GONE) {
            binding.viewGroup.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgGroupLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgGroupRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideGroupView() {
        binding.viewGroup.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgGroupLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgGroupRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }

    private void hitCarFilterData() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "car_masters").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);

                        JsonList group = json.getJsonList(P.group);
                        JsonList passengers = json.getJsonList(P.passengers);
                        JsonList doors = json.getJsonList(P.doors);
                        JsonList suitcase = json.getJsonList(P.suitcase);
                        JsonList transmission = json.getJsonList(P.transmission);
                        JsonList fuel_type = json.getJsonList(P.fuel_type);

                        setGroupData(group);
                        setPassengerData(passengers);
                        setDoorData(doors);
                        setSuitcaseData(suitcase);
                        setTransmissionData(transmission);
                        setFuelData(fuel_type);

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCarFilterData");
    }

    private void setGroupData(JsonList jsonList){
        if (jsonList.size()!=0){
            String selectionValue = "";
            for (int i=0;i<jsonList.size();i++){
                Json json = jsonList.get(i);
                CarFilterModel model = new CarFilterModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.group_name));
                model.setStatus(json.getString(P.status));
                groupList.add(model);
                if (model.getId().equals(SelectCarActivity.groupValue)){
                    selectionValue = model.getName();
                }
            }
            checkSize(groupList, binding.recyclerGroup);
            groupAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(selectionValue)){
                binding.txtGroupTitle.setText(selectionValue);
                addGroupPosition();
            }
        }
    }

    private void setPassengerData(JsonList jsonList){
        if (jsonList.size()!=0){
            String selectionValue = "";
            for (int i=0;i<jsonList.size();i++){
                Json json = jsonList.get(i);
                CarFilterModel model = new CarFilterModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.passengers_name));
                model.setStatus("");
                passengersList.add(model);
                if (model.getId().equals(SelectCarActivity.passengerValue)){
                    selectionValue = model.getName();

                }
            }
            checkSize(passengersList, binding.recyclerPassengers);
            passengersAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(selectionValue)){
                binding.txtPassengetTitle.setText(selectionValue);
                addPassengerPosition();
            }
        }
    }

    private void setDoorData(JsonList jsonList){
        if (jsonList.size()!=0){
            String selectionValue = "";
            for (int i=0;i<jsonList.size();i++){
                Json json = jsonList.get(i);
                CarFilterModel model = new CarFilterModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.doors_name));
                model.setStatus("");
                doorsList.add(model);
                if (model.getId().equals(SelectCarActivity.doorValue)){
                    selectionValue = model.getName();
                }
            }
            checkSize(doorsList, binding.recyclerDoors);
            doorsAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(selectionValue)){
                binding.txtDoorTitle.setText(selectionValue);
                addDoorPosition();
            }
        }
    }

    private void setSuitcaseData(JsonList jsonList){
        if (jsonList.size()!=0){
            String selectionValue = "";
            for (int i=0;i<jsonList.size();i++){
                Json json = jsonList.get(i);
                CarFilterModel model = new CarFilterModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.suitcase_name));
                model.setStatus("");
                suitcaseList.add(model);
                if (model.getId().equals(SelectCarActivity.suitcaseValue)){
                    selectionValue = model.getName();
                }
            }
            checkSize(suitcaseList, binding.recyclerSuitcase);
            suitcaseAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(selectionValue)){
                binding.txtSuitcaseTitle.setText(selectionValue);
                addSuitcasePosition();
            }
        }
    }

    private void setTransmissionData(JsonList jsonList){
        if (jsonList.size()!=0){
            String selectionValue = "";
            for (int i=0;i<jsonList.size();i++){
                Json json = jsonList.get(i);
                CarFilterModel model = new CarFilterModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.transmission_name));
                model.setStatus(json.getString(P.status));
                transmissionList.add(model);
                if (model.getId().equals(SelectCarActivity.transmissionValue)){
                    selectionValue = model.getName();
                }
            }
            checkSize(transmissionList, binding.recyclerTransmission);
            transmissionAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(selectionValue)){
                binding.txtTransmissionTitle.setText(selectionValue);
                addTransmissionPosition();
            }
        }
    }

    private void setFuelData(JsonList jsonList){
        if (jsonList.size()!=0){
            String selectionValue = "";
            for (int i=0;i<jsonList.size();i++){
                Json json = jsonList.get(i);
                CarFilterModel model = new CarFilterModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.fuel_type_name));
                model.setStatus(json.getString(P.status));
                fuelList.add(model);
                if (model.getId().equals(SelectCarActivity.fuilValue)){
                    selectionValue = model.getName();
                }
            }
            checkSize(fuelList, binding.recyclerFuel);
            fuelAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(selectionValue)){
                binding.txtFuilTitle.setText(selectionValue);
                addFuelPosition();
            }
        }
    }


    public static boolean containsValue(Collection<CarFilterModel> list, String value) {
        for(CarFilterModel model : list) {
            if(model != null && model.getName()
                    .equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}