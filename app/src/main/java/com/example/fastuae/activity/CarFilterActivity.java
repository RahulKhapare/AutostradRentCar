package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CarFilterAdapter;
import com.example.fastuae.databinding.ActivityCarFilterBinding;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class CarFilterActivity extends AppCompatActivity implements CarFilterAdapter.onClick {

    private CarFilterActivity activity = this;
    private ActivityCarFilterBinding binding;
    private List<CarFilterModel> priceList;
    private CarFilterAdapter priceAdapter;

    private List<CarFilterModel> seatsList;
    private CarFilterAdapter seatsAdapter;

    private List<CarFilterModel> passengersList;
    private CarFilterAdapter passengersAdapter;

    private List<CarFilterModel> doorsList;
    private CarFilterAdapter doorsAdapter;

    private List<CarFilterModel> transmissionList;
    private CarFilterAdapter transmissionAdapter;

    private List<CarFilterModel> fuelList;
    private CarFilterAdapter fuelAdapter;

    private List<CarFilterModel> categoryList;
    private CarFilterAdapter categoryAdapter;

    private List<CarFilterModel> groupList;
    private CarFilterAdapter groupAdapter;

    private Session session;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_filter);
        session = new Session(activity);
        flag = session.getString(P.languageFlag);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.filterYourSearch));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        updateIcons();

        priceList = new ArrayList<>();
        priceAdapter = new CarFilterAdapter(activity, priceList);
        binding.recycelrPrice.setLayoutManager(new LinearLayoutManager(activity));
        binding.recycelrPrice.setAdapter(priceAdapter);

        seatsList = new ArrayList<>();
        seatsAdapter = new CarFilterAdapter(activity, seatsList);
        binding.recyclerSeats.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerSeats.setAdapter(seatsAdapter);

        passengersList = new ArrayList<>();
        passengersAdapter = new CarFilterAdapter(activity, passengersList);
        binding.recyclerPassengers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerPassengers.setAdapter(passengersAdapter);

        doorsList = new ArrayList<>();
        doorsAdapter = new CarFilterAdapter(activity, doorsList);
        binding.recyclerDoors.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerDoors.setAdapter(doorsAdapter);

        transmissionList = new ArrayList<>();
        transmissionAdapter = new CarFilterAdapter(activity, transmissionList);
        binding.recyclerTransmission.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerTransmission.setAdapter(transmissionAdapter);

        fuelList = new ArrayList<>();
        fuelAdapter = new CarFilterAdapter(activity, fuelList);
        binding.recyclerFuel.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerFuel.setAdapter(fuelAdapter);

        categoryList = new ArrayList<>();
        categoryAdapter = new CarFilterAdapter(activity, categoryList);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCategory.setAdapter(categoryAdapter);

        groupList = new ArrayList<>();
        groupAdapter = new CarFilterAdapter(activity, groupList);
        binding.recyclerGroup.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerGroup.setAdapter(groupAdapter);

        setData();
        onClick();

    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgPriceRight.setVisibility(View.GONE);
            binding.imgPriceLeft.setVisibility(View.VISIBLE);

            binding.imgSeatRight.setVisibility(View.GONE);
            binding.imgSeatLeft.setVisibility(View.VISIBLE);

            binding.imgPassengerRight.setVisibility(View.GONE);
            binding.imgPassengerLeft.setVisibility(View.VISIBLE);

            binding.imgDoorRight.setVisibility(View.GONE);
            binding.imgDoorLeft.setVisibility(View.VISIBLE);

            binding.imgTransRight.setVisibility(View.GONE);
            binding.imgTransLeft.setVisibility(View.VISIBLE);

            binding.imgFuelRight.setVisibility(View.GONE);
            binding.imgFuelLeft.setVisibility(View.VISIBLE);

            binding.imgCategoryRight.setVisibility(View.GONE);
            binding.imgCategoryLeft.setVisibility(View.VISIBLE);

            binding.imgGroupRight.setVisibility(View.GONE);
            binding.imgGroupLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgPriceRight.setVisibility(View.VISIBLE);
            binding.imgPriceLeft.setVisibility(View.GONE);

            binding.imgSeatRight.setVisibility(View.VISIBLE);
            binding.imgSeatLeft.setVisibility(View.GONE);

            binding.imgPassengerRight.setVisibility(View.VISIBLE);
            binding.imgPassengerLeft.setVisibility(View.GONE);

            binding.imgDoorRight.setVisibility(View.VISIBLE);
            binding.imgDoorLeft.setVisibility(View.GONE);

            binding.imgTransRight.setVisibility(View.VISIBLE);
            binding.imgTransLeft.setVisibility(View.GONE);

            binding.imgFuelRight.setVisibility(View.VISIBLE);
            binding.imgFuelLeft.setVisibility(View.GONE);

            binding.imgCategoryRight.setVisibility(View.VISIBLE);
            binding.imgCategoryLeft.setVisibility(View.GONE);

            binding.imgGroupRight.setVisibility(View.VISIBLE);
            binding.imgGroupLeft.setVisibility(View.GONE);

        }
    }

    private void onClick() {

        binding.txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                finish();
            }
        });

        binding.cardPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkPriceView();
                hideSeatView();
                hidePassengerView();
                hideDoorView();
                hideTrasnView();
                hideFuelView();
                hideCategoryView();
                hideGroupView();
            }
        });

        binding.cardSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkSeatView();
                hidePriceView();
                hidePassengerView();
                hideDoorView();
                hideTrasnView();
                hideFuelView();
                hideCategoryView();
                hideGroupView();
            }
        });

        binding.cardPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkPassengerView();
                hidePriceView();
                hideSeatView();
                hideDoorView();
                hideTrasnView();
                hideFuelView();
                hideCategoryView();
                hideGroupView();
            }
        });

        binding.cardDoors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkDoorView();
                hidePriceView();
                hideSeatView();
                hidePassengerView();
                hideTrasnView();
                hideFuelView();
                hideCategoryView();
                hideGroupView();
            }
        });

        binding.cardTransmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkTrasnView();
                hidePriceView();
                hideSeatView();
                hidePassengerView();
                hideDoorView();
                hideFuelView();
                hideCategoryView();
                hideGroupView();
            }
        });

        binding.cardFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkFuelView();
                hidePriceView();
                hideSeatView();
                hidePassengerView();
                hideDoorView();
                hideTrasnView();
                hideCategoryView();
                hideGroupView();
            }
        });

        binding.cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkCategoryView();
                hidePriceView();
                hideSeatView();
                hidePassengerView();
                hideDoorView();
                hideTrasnView();
                hideFuelView();
                hideGroupView();
            }
        });

        binding.cardGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkGroupView();
                hidePriceView();
                hideSeatView();
                hidePassengerView();
                hideDoorView();
                hideTrasnView();
                hideFuelView();
                hideCategoryView();
            }
        });

    }

    private void setData() {

        priceList.add(new CarFilterModel("1000"));
        priceList.add(new CarFilterModel("2000"));
        priceList.add(new CarFilterModel("3000"));
        priceList.add(new CarFilterModel("4000"));
        priceList.add(new CarFilterModel("5000"));
        checkSize(priceList, binding.recycelrPrice);
        priceAdapter.notifyDataSetChanged();

        seatsList.add(new CarFilterModel("1"));
        seatsList.add(new CarFilterModel("2"));
        seatsList.add(new CarFilterModel("3"));
        seatsList.add(new CarFilterModel("4"));
        seatsList.add(new CarFilterModel("5"));
        checkSize(seatsList, binding.recyclerSeats);
        seatsAdapter.notifyDataSetChanged();

        passengersList.add(new CarFilterModel("1"));
        passengersList.add(new CarFilterModel("2"));
        passengersList.add(new CarFilterModel("3"));
        passengersList.add(new CarFilterModel("4"));
        passengersList.add(new CarFilterModel("5"));
        checkSize(passengersList, binding.recyclerPassengers);
        passengersAdapter.notifyDataSetChanged();

        doorsList.add(new CarFilterModel("1"));
        doorsList.add(new CarFilterModel("2"));
        doorsList.add(new CarFilterModel("3"));
        doorsList.add(new CarFilterModel("4"));
        doorsList.add(new CarFilterModel("5"));
        checkSize(doorsList, binding.recyclerDoors);
        doorsAdapter.notifyDataSetChanged();

        transmissionList.add(new CarFilterModel("1"));
        transmissionList.add(new CarFilterModel("2"));
        transmissionList.add(new CarFilterModel("3"));
        transmissionList.add(new CarFilterModel("4"));
        transmissionList.add(new CarFilterModel("5"));
        checkSize(transmissionList, binding.recyclerTransmission);
        transmissionAdapter.notifyDataSetChanged();

        fuelList.add(new CarFilterModel("Petrol"));
        fuelList.add(new CarFilterModel("Deisel"));
        fuelList.add(new CarFilterModel("Oil"));
        fuelList.add(new CarFilterModel("Cerocine"));
        fuelList.add(new CarFilterModel("CNG"));
        checkSize(fuelList, binding.recyclerFuel);
        fuelAdapter.notifyDataSetChanged();

        categoryList.add(new CarFilterModel("A"));
        categoryList.add(new CarFilterModel("B"));
        categoryList.add(new CarFilterModel("C"));
        categoryList.add(new CarFilterModel("D"));
        categoryList.add(new CarFilterModel("E"));
        checkSize(categoryList, binding.recyclerCategory);
        categoryAdapter.notifyDataSetChanged();

        groupList.add(new CarFilterModel("A"));
        groupList.add(new CarFilterModel("B"));
        groupList.add(new CarFilterModel("C"));
        groupList.add(new CarFilterModel("D"));
        groupList.add(new CarFilterModel("E"));
        checkSize(groupList, binding.recyclerGroup);
        groupAdapter.notifyDataSetChanged();


    }

    private void checkSize(List<CarFilterModel> list, RecyclerView recyclerView) {
        if (list.size() > 6) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = 900;
            recyclerView.setLayoutParams(params);
        }
    }

    @Override
    public void onFilterClick(String location) {

    }

    private void checkPriceView() {
        if (binding.viewPrice.getVisibility() == View.VISIBLE) {
            binding.viewPrice.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPriceLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPriceRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewPrice.getVisibility() == View.GONE) {
            binding.viewPrice.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPriceLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPriceRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hidePriceView() {
        binding.viewPrice.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgPriceLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgPriceRight.setImageResource(R.drawable.ic_down_arrow);
        }
    }

    private void checkSeatView() {
        if (binding.viewSeats.getVisibility() == View.VISIBLE) {
            binding.viewSeats.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgSeatLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgSeatRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewSeats.getVisibility() == View.GONE) {
            binding.viewSeats.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgSeatLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgSeatRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideSeatView() {
        binding.viewSeats.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgSeatLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgSeatRight.setImageResource(R.drawable.ic_down_arrow);
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

    private void checkCategoryView() {
        if (binding.viewCategory.getVisibility() == View.VISIBLE) {
            binding.viewCategory.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgCategoryLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgCategoryRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewCategory.getVisibility() == View.GONE) {
            binding.viewCategory.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgCategoryLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgCategoryRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void hideCategoryView() {
        binding.viewCategory.setVisibility(View.GONE);
        if (flag.equals(Config.ARABIC)) {
            binding.imgCategoryLeft.setImageResource(R.drawable.ic_down_arrow);
        } else if (flag.equals(Config.ENGLISH)) {
            binding.imgCategoryRight.setImageResource(R.drawable.ic_down_arrow);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}