package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.activity.AddOnsActivity;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.adapter.CarFleetAdapter;
import com.example.fastuae.adapter.CategorySelectionAdapter;
import com.example.fastuae.databinding.FragmentFleetBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.model.CarFleetModel;
import com.example.fastuae.model.CategoryModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FleetFragment extends Fragment implements CategorySelectionAdapter.onClick{

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

    private List<CategoryModel> categoryModelList;
    private CategorySelectionAdapter categorySelectionAdapter;

    private LoadingDialog loadingDialog;

    private JsonList car_list;

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

        loadingDialog = new LoadingDialog(context);

        Config.FILTER_VALUE = Config.FILTER_TWO;
        groupSelectionList = new ArrayList<>();
        passengerSelectionList = new ArrayList<>();
        doorSelectionList = new ArrayList<>();
        suitcaseSelectionList = new ArrayList<>();
        transmissionSelectionList = new ArrayList<>();
        fuilSelectionList = new ArrayList<>();

        categoryModelList = new ArrayList<>();
        categorySelectionAdapter = new CategorySelectionAdapter(context, categoryModelList,FleetFragment.this,Config.FLEET_TAG,true);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        binding.recyclerCategory.setNestedScrollingEnabled(false);
        binding.recyclerCategory.setAdapter(categorySelectionAdapter);

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

        hitFleetCarData();
    }

    @Override
    public void onCategoryClick(String category) {
        if (car_list!=null && car_list.size()!=0){
            setCarListData(category,car_list);
        }
    }

    private void hitFleetCarData() {

        ProgressView.show(context,loadingDialog);

        Api.newApi(context, P.BaseUrl + "all_cars_listapi")
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Json data = json.getJson(P.data);
//                        int num_rows = json.getInt(P.num_rows);
                        JsonList category_list = data.getJsonList(P.category_list);
                        car_list = data.getJsonList(P.car_list);

                        if (category_list!=null && category_list.size()!=0){
                            setCategoryData(category_list);
                        }

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitFleetCarData");
    }

    private void setCategoryData(JsonList jsonList) {
        for (Json json : jsonList){
            CategoryModel model = new CategoryModel();
            model.setCategoryName(json.getString(P.category_name));
            model.setCategory_name_slug(json.getString(P.category_name_slug));
            categoryModelList.add(model);
        }
        categorySelectionAdapter.notifyDataSetChanged();
    }

    private void setCarListData(String category,JsonList jsonList) {

        boolean availableData = false;
        carFleetModelListOne.clear();
        carFleetModelListTwo.clear();

        for (int i=0; i<jsonList.size(); i++){

            Json json = jsonList.get(i);
            CarFleetModel model = new CarFleetModel();
            model.setId(json.getString(P.id));
            model.setCar_name(json.getString(P.car_name));
            model.setTransmission_name(json.getString(P.transmission_name));
            model.setFuel_type_name(json.getString(P.fuel_type_name));
            model.setGroup_name(json.getString(P.group_name));
            model.setCategory_name(json.getString(P.category_name));
            model.setAir_bags(json.getString(P.air_bags));
            model.setAir_conditioner(json.getString(P.air_conditioner));
            model.setParking_sensors(json.getString(P.parking_sensors));
            model.setRear_parking_camera(json.getString(P.rear_parking_camera));
            model.setBluetooth(json.getString(P.bluetooth));
            model.setCruise_control(json.getString(P.cruise_control));
            model.setSunroof(json.getString(P.sunroof));
            model.setCar_image(json.getString(P.car_image));
            model.setDoor(json.getString(P.door));
            model.setPassenger(json.getString(P.passenger));
            model.setSuitcase(json.getString(P.suitcase));

            Log.e("TAG", "onCategoryClickAASAS: "+model.getCategory_name() + " - " + category  );

            if (model.getCategory_name().equals(category)){
                if ((i % 2) == 0) {
                    availableData = true;
                    carFleetModelListOne.add(model);
                }else {
                    availableData = true;
                    carFleetModelListTwo.add(model);
                }
            }

        }

        carFleetAdapterOne.notifyDataSetChanged();
        carFleetAdapterTwo.notifyDataSetChanged();

        if (availableData){
            binding.txtError.setVisibility(View.GONE);
        }else {
            binding.txtError.setVisibility(View.VISIBLE);
        }

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
}
