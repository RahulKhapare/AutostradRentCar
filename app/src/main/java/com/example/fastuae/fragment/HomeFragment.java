package com.example.fastuae.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.activity.SelectLocationActivity;
import com.example.fastuae.adapter.LocationAdapter;
import com.example.fastuae.adapter.SliderImageAdapter;
import com.example.fastuae.databinding.FragmentHomeBinding;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.model.SliderModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements LocationAdapter.onClick{

    private Context context;
    private FragmentHomeBinding binding;
    private List<SliderModel> sliderModelList;
    private SliderImageAdapter sliderImageAdapter;
    private List<LocationModel> locationDeliverModelList;
    private List<LocationModel> locationCollectModelList;
    private LocationAdapter locationDeliverAdapter;
    private LocationAdapter locationCollectAdapter;
    private String from = "";
    private String deliver = "deliver";
    private String collect = "collect";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            context = inflater.getContext();
            Config.FROM_MAP = false;
            initView();
        }

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        if (Config.FROM_MAP){
            Config.FROM_MAP = false;
            if (from.equals(deliver)){
                hideCollectView();
                updateSelfPickupData();
            }else if (from.equals(collect)){
                hideDeliverView();
                updateSelfReturnData();
            }

        }
    }

    private void hideDeliverView(){
        binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
        binding.imgDeliverArrowUp.setVisibility(View.GONE);
        binding.cardLocationDeliver.setVisibility(View.GONE);
    }

    private void hideCollectView(){
        binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
        binding.imgCollectArrowUp.setVisibility(View.GONE);
        binding.cardLocationCollect.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateSelfPickupData(){
        String address = new Session(context).getString(P.locationAddress);
        if (locationDeliverModelList!=null && !locationDeliverModelList.isEmpty()){
            if (containsLocation(locationDeliverModelList,address)){
                H.showMessage(context,getResources().getString(R.string.locationAdded));
            }else {
                locationDeliverModelList.add(new LocationModel(address));
            }
        }else {
            locationDeliverModelList.add(new LocationModel(address));
        }
        Collections.reverse(locationDeliverModelList);
        checkSize(binding.recyclerLocationDeliver);
        locationDeliverAdapter.notifyDataSetChanged();

        binding.imgDeliverArrowDown.setVisibility(View.GONE);
        binding.imgDeliverArrowUp.setVisibility(View.VISIBLE);
        binding.cardLocationDeliver.setVisibility(View.VISIBLE);
        binding.txtGetLocationDeliver.setVisibility(View.GONE);
        binding.recyclerLocationDeliver.setVisibility(View.VISIBLE);

        binding.radioDeliver.setChecked(false);
        binding.txtPickUpMessage.setVisibility(View.VISIBLE);
        binding.txtPickUpTitle.setText(getResources().getString(R.string.pickUpLocation));
        binding.radioSelfPickup.setChecked(true);
        blueTin(binding.radioSelfPickup);
        blackTin(binding.radioDeliver);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateSelfReturnData(){
        String address = new Session(context).getString(P.locationAddress);
        if (locationCollectModelList!=null && !locationCollectModelList.isEmpty()){
            if (containsLocation(locationCollectModelList,address)){
                H.showMessage(context,getResources().getString(R.string.locationAdded));
            }else {
                locationCollectModelList.add(new LocationModel(address));
            }
        }else {
            locationCollectModelList.add(new LocationModel(address));
        }
        Collections.reverse(locationCollectModelList);
        checkSize(binding.recyclerLocationCollect);
        locationCollectAdapter.notifyDataSetChanged();

        binding.imgCollectArrowDown.setVisibility(View.GONE);
        binding.imgCollectArrowUp.setVisibility(View.VISIBLE);
        binding.cardLocationCollect.setVisibility(View.VISIBLE);
        binding.txtGetLocationCollect.setVisibility(View.GONE);
        binding.recyclerLocationCollect.setVisibility(View.VISIBLE);

        binding.radioCollect.setChecked(false);
        binding.txtDropUpMessage.setVisibility(View.VISIBLE);
        binding.txtDropUpTitle.setText(getResources().getString(R.string.dropOfLocation));
        binding.radioSelfReturn.setChecked(true);
        blueTin(binding.radioSelfReturn);
        blackTin(binding.radioCollect);

    }

    public static boolean containsLocation(Collection<LocationModel> list, String location) {
        for(LocationModel model : list) {
            if(model != null && model.getLocation()
                    .equals(location)) {
                return true;
            }
        }
        return false;
    }

    private void initView(){
        sliderModelList = new ArrayList<>();
        sliderImageAdapter = new SliderImageAdapter(context, sliderModelList);
        binding.pager.setAdapter(sliderImageAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager, true);

        locationDeliverModelList = new ArrayList<>();
        locationDeliverModelList.add(new LocationModel("Al Quoz - Service/sales branch, Dubai"));
        locationDeliverModelList.add(new LocationModel("Sarjah, Dubai"));
        locationDeliverModelList.add(new LocationModel("Ruwais Mall, Al Ruwais"));
        locationDeliverAdapter = new LocationAdapter(context,locationDeliverModelList,HomeFragment.this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        binding.recyclerLocationDeliver.setLayoutManager(linearLayoutManager1);
        binding.recyclerLocationDeliver.setHasFixedSize(true);
        binding.recyclerLocationDeliver.setAdapter(locationDeliverAdapter);

        locationCollectModelList = new ArrayList<>();
        locationCollectModelList.add(new LocationModel("Al Quoz - Service/sales branch, Dubai"));
        locationCollectModelList.add(new LocationModel("Sarjah, Dubai"));
        locationCollectModelList.add(new LocationModel("Ruwais Mall, Al Ruwais"));
        locationCollectAdapter = new LocationAdapter(context,locationCollectModelList,HomeFragment.this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        binding.recyclerLocationCollect.setLayoutManager(linearLayoutManager2);
        binding.recyclerLocationCollect.setHasFixedSize(true);
        binding.recyclerLocationCollect.setAdapter(locationCollectAdapter);

        getCurrentDate();
        setUpSliderList();
        onClick();
    }

    private void checkSize(RecyclerView recyclerView){
        if (locationDeliverModelList.size()>6){
            ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
            params.height=1010;
            recyclerView.setLayoutParams(params);
        }
    }

    @Override
    public void onLocationClick(String location) {

    }

    private void getCurrentDate(){
        Calendar c= Calendar.getInstance();
        final Date date = c.getTime();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String dayString         = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        String yeare         = (String) DateFormat.format("yyyy", date); // 2013

        binding.txtPickDate.setText(dayString);
        binding.txtPickMonth.setText(monthString.toUpperCase());
        binding.txtPickTime.setText(getCurrentTime()+"");

        binding.txtDropDate.setText(dayString);
        binding.txtDropMonth.setText(monthString.toUpperCase());
        binding.txtDropTime.setText(getCurrentTime());

    }


    private String getCurrentTime() {
        String delegate = "hh:mm aaa";
        String oldstr = (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
        String str = oldstr.replace("am", "AM").replace("pm","PM");
        return str;
    }


    private void setUpSliderList() {
        sliderModelList.clear();

        SliderModel model = new SliderModel();
        sliderModelList.add(model);
        sliderModelList.add(model);
        sliderModelList.add(model);

        sliderImageAdapter.notifyDataSetChanged();

        if (sliderModelList.isEmpty()){
            binding.lnrSlider.setVisibility(View.GONE);
        }else {
            binding.lnrSlider.setVisibility(View.VISIBLE);
        }

        Handler handler = new Handler();
        Runnable runnable = null;

        if (runnable != null)
            handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 3000);
                if (binding.pager.getCurrentItem() == sliderImageAdapter.getCount() - 1)
                    binding.pager.setCurrentItem(0);
                else
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
            }
        };
        runnable.run();
    }


    private void onClick(){

        binding.txtGetLocationDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.txtPickUpMessage.getVisibility()==View.VISIBLE){
                    binding.imgDeliverArrowUp.setVisibility(View.GONE);
                    binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
                    binding.cardLocationDeliver.setVisibility(View.GONE);
                }
                from = deliver;
                Intent intent = new Intent(context, SelectLocationActivity.class);
                startActivity(intent);
            }
        });

        binding.txtGetLocationCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.txtDropUpMessage.getVisibility()==View.VISIBLE){
                    binding.imgCollectArrowUp.setVisibility(View.GONE);
                    binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
                    binding.cardLocationCollect.setVisibility(View.GONE);
                }
                from = collect;
                Intent intent = new Intent(context, SelectLocationActivity.class);
                startActivity(intent);
            }
        });

        binding.lnrPickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setDateTime(binding.txtPickDate,binding.txtPickMonth);
            }
        });

        binding.lnrDropoffDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setDateTime(binding.txtDropDate,binding.txtDropMonth);
            }
        });

        binding.lnrPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setTime(binding.txtPickTime);
            }
        });

        binding.lnrDropoffTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setTime(binding.txtDropTime);
            }
        });

        binding.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent selectCarIntent = new Intent(context, SelectCarActivity.class);
                startActivity(selectCarIntent);
            }
        });

        binding.radioDeliver.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioSelfPickup.setChecked(false);
                binding.txtPickUpMessage.setVisibility(View.GONE);
                binding.txtPickUpTitle.setText(getResources().getString(R.string.enterLocationDeliver));
                blueTin(binding.radioDeliver);
                blackTin(binding.radioSelfPickup);

                binding.cardLocationDeliver.setVisibility(View.GONE);
                binding.imgDeliverArrowUp.setVisibility(View.GONE);
                binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);

            }
        });

        binding.radioSelfPickup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioDeliver.setChecked(false);
                binding.txtPickUpMessage.setVisibility(View.VISIBLE);
                binding.txtPickUpTitle.setText(getResources().getString(R.string.pickUpLocation));
                blueTin(binding.radioSelfPickup);
                blackTin(binding.radioDeliver);

                binding.cardLocationDeliver.setVisibility(View.GONE);
                binding.imgDeliverArrowUp.setVisibility(View.GONE);
                binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
            }
        });

        binding.radioCollect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioSelfReturn.setChecked(false);
                binding.txtDropUpMessage.setVisibility(View.GONE);
                binding.txtDropUpTitle.setText(getResources().getString(R.string.enterLocationDeliver));
                blueTin(binding.radioCollect);
                blackTin(binding.radioSelfReturn);

                binding.cardLocationCollect.setVisibility(View.GONE);
                binding.imgCollectArrowUp.setVisibility(View.GONE);
                binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
            }
        });

        binding.radioSelfReturn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.radioCollect.setChecked(false);
                binding.txtDropUpMessage.setVisibility(View.VISIBLE);
                binding.txtDropUpTitle.setText(getResources().getString(R.string.dropOfLocation));
                blueTin(binding.radioSelfReturn);
                blackTin(binding.radioCollect);

                binding.cardLocationCollect.setVisibility(View.GONE);
                binding.imgCollectArrowUp.setVisibility(View.GONE);
                binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
            }
        });

        binding.cardPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                hideCollectView();
                if (binding.imgDeliverArrowDown.getVisibility()==View.VISIBLE){
                    binding.imgDeliverArrowDown.setVisibility(View.GONE);
                    binding.imgDeliverArrowUp.setVisibility(View.VISIBLE);
                    binding.cardLocationDeliver.setVisibility(View.VISIBLE);
                    if (binding.radioDeliver.isChecked()){
                        binding.txtGetLocationDeliver.setVisibility(View.VISIBLE);
                        binding.recyclerLocationDeliver.setVisibility(View.GONE);
                    }else {
                        binding.txtGetLocationDeliver.setVisibility(View.GONE);
                        binding.recyclerLocationDeliver.setVisibility(View.VISIBLE);
                    }
                }else if (binding.imgDeliverArrowUp.getVisibility()==View.VISIBLE){
                    binding.imgDeliverArrowUp.setVisibility(View.GONE);
                    binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
                    binding.cardLocationDeliver.setVisibility(View.GONE);
                }

            }
        });

        binding.cardDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                hideDeliverView();
                if (binding.imgCollectArrowDown.getVisibility()==View.VISIBLE){
                    binding.imgCollectArrowDown.setVisibility(View.GONE);
                    binding.imgCollectArrowUp.setVisibility(View.VISIBLE);
                    binding.cardLocationCollect.setVisibility(View.VISIBLE);
                    if (binding.radioCollect.isChecked()){
                        binding.txtGetLocationCollect.setVisibility(View.VISIBLE);
                        binding.recyclerLocationCollect.setVisibility(View.GONE);
                    }else {
                        binding.txtGetLocationCollect.setVisibility(View.GONE);
                        binding.recyclerLocationCollect.setVisibility(View.VISIBLE);
                    }
                }else if (binding.imgCollectArrowUp.getVisibility()==View.VISIBLE){
                    binding.imgCollectArrowUp.setVisibility(View.GONE);
                    binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
                    binding.cardLocationCollect.setVisibility(View.GONE);
                }

            }
        });
    }

    private void setDateTime(TextView txtDay, TextView txtMonth) {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date date = newDate.getTime();
                String fdate = sd.format(date);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String dayString         = (String) DateFormat.format("dd",   date); // 20
                String monthString  = (String) DateFormat.format("MMM",  date); // Jun
                String monthNumber  = (String) DateFormat.format("MM",   date); // 06
                String yeare         = (String) DateFormat.format("yyyy", date); // 2013

                txtDay.setText(dayString);
                txtMonth.setText(monthString.toUpperCase());

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDatePickerDialog.show();

    }

    private void setTime(TextView txtTime){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(),R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AM_PM = "";
                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    AM_PM = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    AM_PM = "PM";

                txtTime.setText( selectedHour + ":" + selectedMinute + " " + AM_PM);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void blackTin(RadioButton radioButton){
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{getResources().getColor(R.color.grayDark)}
                },
                new int[]{getResources().getColor(R.color.grayDark)}
        );

        radioButton.setButtonTintList(myColorStateList);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void blueTin(RadioButton radioButton){
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{getResources().getColor(R.color.lightBlue)}
                },
                new int[]{getResources().getColor(R.color.lightBlue)}
        );

        radioButton.setButtonTintList(myColorStateList);
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

}
