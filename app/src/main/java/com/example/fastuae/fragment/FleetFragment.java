package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.adapter.CarFleetAdapter;
import com.example.fastuae.adapter.CategorySelectionAdapter;
import com.example.fastuae.databinding.FragmentFleetBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.model.CarFleetModel;
import com.example.fastuae.model.CategoryModel;
import com.example.fastuae.util.Config;

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

    String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum augue turpis, porttitor non porta a, mollis ac orci. Aliquam a risus sed eros mattis maximus. Pellentesque sagittis purus interdum ex pretium faucibus. Pellentesque in mattis magna. Nunc ut massa sed nisi viverra elementum. Morbi porta commodo tellus ac convallis. Nam porta efficitu est, id vehicula est hendrerit in.";

    private List<CategoryModel> categoryModelList;
    private CategorySelectionAdapter adapter;

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

        categoryModelList = new ArrayList<>();
        adapter = new CategorySelectionAdapter(context, categoryModelList,FleetFragment.this,Config.FLEET_TAG);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        binding.recyclerCategory.setNestedScrollingEnabled(false);
        binding.recyclerCategory.setAdapter(adapter);

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

        setCategoryData();
        setData();

    }

    private void setCategoryData() {

        CategoryModel categoryModel1 = new CategoryModel();
        categoryModel1.setCategoryFlag(Config.Compact_Cars);
        categoryModel1.setCategoryName(Config.Compact_Cars);
        categoryModelList.add(categoryModel1);
        CategoryModel categoryModel2 = new CategoryModel();
        categoryModel2.setCategoryFlag(Config.Economy_Cars);
        categoryModel2.setCategoryName(Config.Economy_Cars);
        categoryModelList.add(categoryModel2);
        CategoryModel categoryModel3 = new CategoryModel();
        categoryModel3.setCategoryFlag(Config.Big_Size_Cars);
        categoryModel3.setCategoryName(Config.Big_Size_Cars);
        categoryModelList.add(categoryModel3);
        CategoryModel categoryModel4 = new CategoryModel();
        categoryModel4.setCategoryFlag(Config.Mid_Size_Cars);
        categoryModel4.setCategoryName(Config.Mid_Size_Cars);
        categoryModelList.add(categoryModel4);
        CategoryModel categoryModel5 = new CategoryModel();
        categoryModel5.setCategoryFlag(Config.Small_Size_Cars);
        categoryModel5.setCategoryName(Config.Small_Size_Cars);
        categoryModelList.add(categoryModel5);
        adapter.notifyDataSetChanged();

    }

    private void setData(){

        CarFleetModel modelOne = new CarFleetModel();
        modelOne.setImage(R.drawable.ic_car_grey);
        modelOne.setCarName("Toyota Yeris 1.3");
        modelOne.setGroupName("Group A");
        modelOne.setCarType("Premium Car");
        modelOne.setSeat("5 Seats");
        modelOne.setAutomatic("Automatic");
        modelOne.setPassenger("5 Passengers");
        modelOne.setDoor("3 Doors");
        modelOne.setPetrol("Petrol");
        modelOne.setEngine("Engine");
        modelOne.setSuitcase("2 Suitcase");
        modelOne.setDescription(description);
        modelOne.setImage1(R.drawable.ic_view_one);
        modelOne.setImage2(R.drawable.ic_view_two);
        modelOne.setImage3(R.drawable.ic_view_three);
        carFleetModelListOne.add(modelOne);
        carFleetModelListOne.add(modelOne);
        carFleetModelListOne.add(modelOne);
        carFleetModelListOne.add(modelOne);
        carFleetModelListOne.add(modelOne);

        carFleetAdapterOne.notifyDataSetChanged();

        CarFleetModel modelTwo = new CarFleetModel();
        modelTwo.setImage(R.drawable.ic_car_red);
        modelTwo.setCarName("Hyundai Accent 1.6");
        modelTwo.setGroupName("Group B");
        modelTwo.setCarType("Premium Car");
        modelTwo.setSeat("5 Seats");
        modelTwo.setAutomatic("Automatic");
        modelTwo.setPassenger("5 Passengers");
        modelTwo.setDoor("3 Doors");
        modelTwo.setPetrol("Petrol");
        modelTwo.setEngine("Engine");
        modelTwo.setSuitcase("2 Suitcase");
        modelTwo.setDescription(description);
        modelTwo.setImage1(R.drawable.ic_view_one);
        modelTwo.setImage2(R.drawable.ic_view_two);
        modelTwo.setImage3(R.drawable.ic_view_three);
        carFleetModelListTwo.add(modelTwo);
        carFleetModelListTwo.add(modelTwo);
        carFleetModelListTwo.add(modelTwo);
        carFleetModelListTwo.add(modelTwo);
        carFleetModelListTwo.add(modelTwo);

        carFleetAdapterTwo.notifyDataSetChanged();

    }


    @Override
    public void onCategoryClick(String category) {

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
