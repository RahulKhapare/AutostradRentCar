package com.example.fastuae.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
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

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.FAQActivity;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.activity.SelectLocationActivity;
import com.example.fastuae.adapter.LocationAdapter;
import com.example.fastuae.adapter.SliderImageAdapter;
import com.example.fastuae.databinding.FragmentHomeBinding;
import com.example.fastuae.model.HomeLocationModel;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.model.SliderModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

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
    private List<HomeLocationModel> locationDeliverModelList;
    private List<HomeLocationModel> locationCollectModelList;
    private LocationAdapter locationDeliverAdapter;
    private LocationAdapter locationCollectAdapter;
    private String from = "";
    private String deliver = "deliver";
    private String collect = "collect";
    private String pickUpId = "";
    private String pickUpLocation = "";
    private String pickUpDate = "";
    private String pickUpTime = "";
    private String dropUpId = "";
    private String dropUpLocation = "";
    private String dropUpDate = "";
    private String dropUpTime = "";
    private String pickUpEmirateID = "";
    private String dropUpEmirateID = "";
    private String pickUpAddress = "";
    private String dropUpAddress = "";
    private String pickUpLandmark = "";
    private String dropUpLandmark = "";
    private String dropupType = "";
    private String pickupType = "";
    private int pickUpFlag = 1;
    private int dropUpFlag = 2;

    private LoadingDialog loadingDialog;
    public static JsonList carList = null;
    public static JsonList blogList = null;
    public static JsonList emirate_list  = null;

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

    @Override
    public void onResume() {
        super.onResume();
//        if (Config.FROM_MAP){
//            Config.FROM_MAP = false;
//            if (from.equals(deliver)){
//                hideCollectView();
//                updateSelfPickupData();
//            }else if (from.equals(collect)){
//                hideDeliverView();
//                updateSelfReturnData();
//            }
//
//        }
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

    private void updateSelfPickupData(){
        String address = new Session(context).getString(P.locationAddress);
        if (locationDeliverModelList!=null && !locationDeliverModelList.isEmpty()){
            if (containsLocation(locationDeliverModelList,address)){
                H.showMessage(context,getResources().getString(R.string.locationAdded));
            }else {
                locationDeliverModelList.add(new HomeLocationModel(address));
            }
        }else {
            locationDeliverModelList.add(new HomeLocationModel(address));
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

    private void updateSelfReturnData(){
        String address = new Session(context).getString(P.locationAddress);
        if (locationCollectModelList!=null && !locationCollectModelList.isEmpty()){
            if (containsLocation(locationCollectModelList,address)){
                H.showMessage(context,getResources().getString(R.string.locationAdded));
            }else {
                locationCollectModelList.add(new HomeLocationModel(address));
            }
        }else {
            locationCollectModelList.add(new HomeLocationModel(address));
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

    public static boolean containsLocation(Collection<HomeLocationModel> list, String location) {
        for(HomeLocationModel model : list) {
            if(model != null && model.getAddress()
                    .equals(location)) {
                return true;
            }
        }
        return false;
    }

    private void initView(){
        loadingDialog = new LoadingDialog(context);
        sliderModelList = new ArrayList<>();
        sliderImageAdapter = new SliderImageAdapter(context, sliderModelList);
        binding.pager.setAdapter(sliderImageAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager, true);

        locationDeliverModelList = new ArrayList<>();
        locationDeliverAdapter = new LocationAdapter(context,locationDeliverModelList,HomeFragment.this,pickUpFlag);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        binding.recyclerLocationDeliver.setLayoutManager(linearLayoutManager1);
        binding.recyclerLocationDeliver.setHasFixedSize(true);
        binding.recyclerLocationDeliver.setAdapter(locationDeliverAdapter);

        locationCollectModelList = new ArrayList<>();
        locationCollectAdapter = new LocationAdapter(context,locationCollectModelList,HomeFragment.this,dropUpFlag);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        binding.recyclerLocationCollect.setLayoutManager(linearLayoutManager2);
        binding.recyclerLocationCollect.setHasFixedSize(true);
        binding.recyclerLocationCollect.setAdapter(locationCollectAdapter);

        hitHomeData();
        hitLocationData();
        getCurrentDate();
        hitEmirateData();
        onClick();

        Log.e("TAG", "initViewSSSS: "+ new Session(getActivity()).getString(P.token) );
    }

    private void checkSize(RecyclerView recyclerView){
        if (locationDeliverModelList.size()>6){
            ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
            params.height=1010;
            recyclerView.setLayoutParams(params);
        }
    }

    @Override
    public void onLocationClick(String location,int flag,HomeLocationModel model) {

        if (flag==pickUpFlag){
            pickUpId = model.getId();
            pickUpLocation = model.getLocation_name();
            pickUpEmirateID = model.getEmirate_id();
            binding.txtPickUpMessage.setText(location);
            pickUpAddress = model.getAddress();
            pickUpLandmark = model.getLocation_name();
            cardPickupClick();
        }else if (flag==dropUpFlag){
            dropUpId = model.getId();
            dropUpLocation = model.getLocation_name();
            dropUpEmirateID = model.getEmirate_id();
            binding.txtDropUpMessage.setText(location);
            dropUpAddress = model.getAddress();
            dropUpLandmark = model.getLocation_name();
            cardDropUpClick();
        }

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

        String currentDate = yeare + "-" + monthNumber + "-" + dayString;
        pickUpDate = currentDate;
        dropUpDate = currentDate;

    }


    private String getCurrentTime() {
        String delegate = "hh:mm aaa";
        String oldstr = (String) DateFormat.format(delegate,Calendar.getInstance().getTime());

        pickUpTime = oldstr;
        dropUpTime = oldstr;

        String str = oldstr.replace("am", "AM").replace("pm","PM");
        return str;
    }


    private void setUpSliderList(JsonList jsonList) {

        sliderModelList.clear();

        if (jsonList!=null){
            for (Json json : jsonList){
                SliderModel model = new SliderModel();
                model.setBanner_image(json.getString(P.banner_image));
                model.setImage_alt_text(json.getString(P.image_alt_text));
                model.setUrl(json.getString(P.url));
                sliderModelList.add(model);
            }
        }

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
                setDateTime(binding.txtPickDate,binding.txtPickMonth,pickUpFlag);
            }
        });

        binding.lnrDropoffDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setDateTime(binding.txtDropDate,binding.txtDropMonth,dropUpFlag);
            }
        });

        binding.lnrPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setTime(binding.txtPickTime,pickUpFlag);
            }
        });

        binding.lnrDropoffTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setTime(binding.txtDropTime,dropUpFlag);
            }
        });

        binding.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                if (TextUtils.isEmpty(pickUpId)){
                    H.showMessage(context,getResources().getString(R.string.selectSelftPickUpLocation));
                }else if (TextUtils.isEmpty(dropUpId)){
                    H.showMessage(context,getResources().getString(R.string.selectSelfReturnUpLocation));
                }else {
                    hitVerifyPickUpData(pickUpId,pickUpDate,pickUpTime,dropUpId,dropUpDate,dropUpTime);

//                    Config.SelectedPickUpEmirateID = pickUpEmirateID;
//                    Config.SelectedPickUpID = pickUpId;
//                    Config.SelectedPickUpAddress = pickUpLocation;
//                    Config.SelectedPickUpDate = pickUpDate;
//                    Config.SelectedPickUpTime = pickUpTime;
//
//                    Config.SelectedDropUpEmirateID = dropUpEmirateID;
//                    Config.SelectedDropUpID = dropUpId;
//                    Config.SelectedDropUpAddress = dropUpLocation;
//                    Config.SelectedDropUpDate = dropUpDate;
//                    Config.SelectedDropUpTime = dropUpTime;
//
//                    Intent intent = new Intent(context,SelectCarActivity.class);
//                    intent.putExtra(Config.pickUpEmirateID,pickUpEmirateID);
//                    intent.putExtra(Config.dropUpEmirateID,dropUpEmirateID);
//                    intent.putExtra(Config.pickUpDate,pickUpDate);
//                    intent.putExtra(Config.dropUpDate,dropUpDate);
//                    startActivity(intent);

                }
            }
        });

        binding.radioDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                pickupType = "deliver";
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
            @Override
            public void onClick(View v) {
                pickupType = "self_pickup";
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
            @Override
            public void onClick(View v) {
                dropupType = "collect";
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
            @Override
            public void onClick(View v) {
                dropupType = "self_return";
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
                cardPickupClick();
            }
        });

        binding.cardDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardDropUpClick();
            }
        });
    }


    private void cardPickupClick(){
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

    private void cardDropUpClick(){

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

    private void setDateTime(TextView txtDay, TextView txtMonth,int flag) {

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

                if (flag==pickUpFlag){
                    String currentDate = yeare + "-" + monthNumber + "-" + dayString;
                    pickUpDate = currentDate;
                }else if (flag==dropUpFlag){
                    String currentDate = yeare + "-" + monthNumber + "-" + dayString;
                    dropUpDate = currentDate;
                }

                txtDay.setText(dayString);
                txtMonth.setText(monthString.toUpperCase());

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDatePickerDialog.show();

    }

    private void setTime(TextView txtTime,int flag){
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

                if(flag==pickUpFlag){
                   pickUpTime = selectedHour + ":" + selectedMinute + " " + AM_PM.toLowerCase();
                }else if (flag==dropUpFlag){
                    dropUpTime = selectedHour + ":" + selectedMinute + " " + AM_PM.toLowerCase();
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    private void blackTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.grayDark)}
                    },
                    new int[]{getResources().getColor(R.color.grayDark)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void blueTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void hitHomeData() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "home").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        JsonList bannerList = json.getJsonList(P.banner_list);
                        carList = json.getJsonList(P.car_list);
                        blogList = json.getJsonList(P.blog_list);
                        setUpSliderList(bannerList);

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitHomeData");
    }

    private void hitEmirateData() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "emirate_list").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        emirate_list = json.getJsonList(P.emirate_list);
                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitEmirateData");
    }

    private void hitLocationData() {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();

        Api.newApi(context, P.BaseUrl + "location").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        JsonList location_list = json.getJsonList(P.location_list);
                        for (int i=0;i<location_list.size();i++){
                            Json jsonData = location_list.get(i);
                            HomeLocationModel model = new HomeLocationModel();
                            model.setId(jsonData.getString(P.id));
                            model.setEmirate_id(jsonData.getString(P.emirate_id));
                            model.setEmirate_name(jsonData.getString(P.emirate_name));
                            model.setLocation_name(jsonData.getString(P.location_name));
                            model.setAddress(jsonData.getString(P.address));
                            model.setStatus(jsonData.getString(P.status));
                            model.setContact_number(jsonData.getString(P.contact_number));
                            model.setContact_email(jsonData.getString(P.contact_email));
                            model.setLocation_time_data(jsonData.getJsonList(P.location_time_data));
                            locationCollectModelList.add(model);
                            locationDeliverModelList.add(model);
                        }

                        locationCollectAdapter.notifyDataSetChanged();
                        locationDeliverAdapter.notifyDataSetChanged();

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");
    }

    private void hitVerifyPickUpData(String pickUpID, String pickUpDate, String pickUpTime,String dropUpID,String dropUpDate,String dropUpTime) {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.pickup_type,pickupType);
        j.addString(P.pickup_emirate_id,pickUpEmirateID);
        j.addString(P.pickup_address,pickUpAddress);
        j.addString(P.pickup_landmark,pickUpLandmark);
        j.addString(P.pickup_location_id,pickUpID);
        j.addString(P.pickup_date,pickUpDate);
        j.addString(P.pickup_time,pickUpTime);
        j.addString(P.booking_type,"daily");

        Api.newApi(context, P.BaseUrl + "verify_pickup_location_date_time").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Config.pickUpTypeValue = pickupType;
                        Config.SelectedPickUpEmirateID = pickUpEmirateID;
                        Config.SelectedPickUpID = pickUpID;
                        Config.SelectedPickUpAddress = pickUpLocation;
                        Config.SelectedPickUpLandmark = pickUpLandmark;
                        Config.SelectedPickUpDate = pickUpDate;
                        Config.SelectedPickUpTime = pickUpTime;
                        hitVerifyDropUpData(pickUpID,pickUpAddress,pickUpDate,pickUpTime,dropUpID,dropUpDate,dropUpTime);

                    }else {
                        H.showMessage(context,json.getString(P.error));

                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitVerifyPickUpData");
    }

    private void hitVerifyDropUpData(String pickupId,String pickupAddress,String pickUpDate,String pickUpTime, String id, String date, String time) {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.pickup_date,pickUpDate);
        j.addString(P.pickup_time,pickUpTime);
        j.addString(P.dropoff_type,dropupType);
        j.addString(P.dropoff_emirate_id,dropUpEmirateID);
        j.addString(P.dropoff_address,dropUpAddress);
        j.addString(P.dropoff_landmark,dropUpLandmark);
        j.addString(P.dropoff_location_id,id);
        j.addString(P.dropoff_date,date);
        j.addString(P.dropoff_time,time);

        Api.newApi(context, P.BaseUrl + "verify_dropoff_location_date_time").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Config.dropUpTypeValue = dropupType;
                        Config.SelectedDropUpEmirateID = dropUpEmirateID;
                        Config.SelectedDropUpID = id;
                        Config.SelectedDropUpAddress = dropUpLocation;
                        Config.SelectedDropUpLandmark = dropUpLandmark;
                        Config.SelectedDropUpDate = dropUpDate;
                        Config.SelectedDropUpTime = dropUpTime;

                        Intent intent = new Intent(context,SelectCarActivity.class);
                        intent.putExtra(Config.pickUpEmirateID,pickUpEmirateID);
                        intent.putExtra(Config.dropUpEmirateID,dropUpEmirateID);
                        intent.putExtra(Config.pickUpDate,pickUpDate);
                        intent.putExtra(Config.dropUpDate,dropUpDate);
//                        intent.putExtra(Config.pickUpTime,pickUpTime);
                        intent.putExtra(Config.pickUpTime,binding.txtPickTime.getText().toString().replace(" ",""));
//                        intent.putExtra(Config.dropUpTime,time);
                        intent.putExtra(Config.dropUpTime,binding.txtDropTime.getText().toString().replace(" ",""));
                        intent.putExtra(Config.pickUpType,pickupType);
                        intent.putExtra(Config.dropUpType,dropupType);
                        intent.putExtra(Config.pickUpLocationID,pickupId);
                        intent.putExtra(Config.dropUpLocationID,id);
                        intent.putExtra(Config.pickUpAddress,pickupAddress);
                        intent.putExtra(Config.dropUpAddress,dropUpAddress);
                        startActivity(intent);

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitVerifyDropUpData");
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

}
