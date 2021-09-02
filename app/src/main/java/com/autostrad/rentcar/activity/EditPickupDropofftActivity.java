package com.autostrad.rentcar.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.DurationAdapter;
import com.autostrad.rentcar.adapter.EmirateAdapter;
import com.autostrad.rentcar.adapter.LocationAdapter;
import com.autostrad.rentcar.adapter.TimeSlotAdapter;
import com.autostrad.rentcar.databinding.ActivityEditPickupDropoffBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.DurationModel;
import com.autostrad.rentcar.model.EmirateModel;
import com.autostrad.rentcar.model.HomeLocationModel;
import com.autostrad.rentcar.model.TimeSlotModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditPickupDropofftActivity extends AppCompatActivity implements LocationAdapter.onClick, DurationAdapter.onClick, EmirateAdapter.onClick, TimeSlotAdapter.onClick {

    private EditPickupDropofftActivity activity = this;
    private ActivityEditPickupDropoffBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    private List<HomeLocationModel> locationDailyList;
    private List<HomeLocationModel> locationMonthlyList;
    private List<DurationModel> durationList;
    private LocationAdapter locationDailyAdapter;
    private LocationAdapter locationMonthlyAdapter;
    private DurationAdapter durationAdapter;
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
    private String bookingTYpe = "";
    private String monthDuration = "";
    private String deleveryEmirateID = "";
    private String collectEmirateID = "";
    private int pickUpFlag = 1;
    private int dropUpFlag = 2;

    public static JsonList emirate_list = null;

    private List<EmirateModel> deliverEmirateList;
    private List<EmirateModel> collectEmirateList;
    private EmirateAdapter deliverEmirateAdapter;
    private EmirateAdapter collectEmirateAdapter;
    private int deliverEmirateFlag = 1;
    private int collectEmirateFlag = 2;
    public static String deliveryEmirateName = "";
    public static String collectEmirateName = "";

    private int pickupYEAR = 0;
    private int pickupMONTH = 0;
    private int pickupDAY = 0;

    private List<TimeSlotModel> puckupTimeSlotList;
    private List<TimeSlotModel> dropoffTimeSlotList;

    private int callTimeFor = 0;
    private int callTimeForPickup = 1;
    private int callTimeForDropoff = 2;
    private int pickup_time_position;
    private int dropoff_time_position;

    private boolean callPickupFirst = false;
    private boolean callDropoffFirst = false;

    private String lastConfigPickupTime = "";
    private String lastActivityPickupTime = "";

    private String lastConfigDropoffTime = "";
    private String lastActivityDropoffTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_pickup_dropoff);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.editLocationTime));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        callPickupFirst = false;
        callDropoffFirst = false;

        puckupTimeSlotList = new ArrayList<>();
        dropoffTimeSlotList = new ArrayList<>();

        bookingTYpe = Config.daily;
        pickupType = Config.self_pickup;
        dropupType = Config.self_dropoff;

        durationList = new ArrayList<>();
        addDuration();
        durationAdapter = new DurationAdapter(activity, durationList, true);
        LinearLayoutManager linearLayoutManager0 = new LinearLayoutManager(activity);
        binding.recyclerDuration.setLayoutManager(linearLayoutManager0);
        binding.recyclerDuration.setHasFixedSize(true);
        binding.recyclerDuration.setNestedScrollingEnabled(false);
        binding.recyclerDuration.setAdapter(durationAdapter);

        locationDailyList = new ArrayList<>();
        locationDailyAdapter = new LocationAdapter(activity, locationDailyList,pickUpFlag,true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        binding.recyclerDailyLocation.setLayoutManager(linearLayoutManager1);
        binding.recyclerDailyLocation.setHasFixedSize(true);
        binding.recyclerDailyLocation.setAdapter(locationDailyAdapter);

        locationMonthlyList = new ArrayList<>();
        locationMonthlyAdapter = new LocationAdapter(activity, locationMonthlyList, dropUpFlag,true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(activity);
        binding.recyclerLocationCollect.setLayoutManager(linearLayoutManager2);
        binding.recyclerLocationCollect.setHasFixedSize(true);
        binding.recyclerLocationCollect.setAdapter(locationMonthlyAdapter);

        deliverEmirateList = new ArrayList<>();
        deliverEmirateAdapter = new EmirateAdapter(activity, deliverEmirateList, deliverEmirateFlag,true);
        binding.recyclerDeliverEmirate.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerDeliverEmirate.setHasFixedSize(true);
        binding.recyclerDeliverEmirate.setNestedScrollingEnabled(false);
        binding.recyclerDeliverEmirate.setAdapter(deliverEmirateAdapter);

        collectEmirateList = new ArrayList<>();
        collectEmirateAdapter = new EmirateAdapter(activity, collectEmirateList, collectEmirateFlag,true);
        binding.recyclerCollectEmirate.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCollectEmirate.setHasFixedSize(true);
        binding.recyclerCollectEmirate.setNestedScrollingEnabled(false);
        binding.recyclerCollectEmirate.setAdapter(collectEmirateAdapter);

        hitLocationData();
        getCurrentDate();
        hitEmirateData();
        onClick();

        setDat();
    }

    private void setDat(){

        if (SelectCarActivity.bookingTYpe.equals(Config.daily)){
            binding.radioDailyRental.performClick();
        }else if (SelectCarActivity.bookingTYpe.equals(Config.monthly)){
            binding.radioMonthlyDeals.performClick();
        }

        if (SelectCarActivity.pickUpType.equals(Config.deliver)){
            binding.radioDeliverYes.performClick();
            binding.txtDeliverEmirateMessage.setVisibility(View.VISIBLE);
            binding.txtDeliverEmirateMessage.setText(Config.deliveryEmirateName);
        }else if (SelectCarActivity.pickUpType.equals(Config.self_pickup)){
            binding.radioDeliverNo.performClick();
            binding.txtPickUpMessage.setText(Config.SelectedPickUpLandmark);
        }

        if (SelectCarActivity.dropUpType.equals(Config.collect)){
            binding.radioCollectYes.performClick();
            binding.txtCollectEmirateMessage.setVisibility(View.VISIBLE);
            binding.txtCollectEmirateMessage.setText(Config.collectEmirateName);
        }else if (SelectCarActivity.dropUpType.equals(Config.self_dropoff)){
            binding.radioCollectNo.performClick();
            binding.txtDropUpMessage.setText(Config.SelectedDropUpLandmark);
        }

        binding.txtPickDate.setText(Config.pickupDay);
        binding.txtPickMonth.setText(Config.pickupMonth);
        binding.txtPickTime.setText(Config.pickupTime);
        lastConfigPickupTime = Config.pickupTime;

        binding.txtDropDate.setText(Config.dropOffDay);
        binding.txtDropMonth.setText(Config.dropOffMonth);
        binding.txtDropTime.setText(Config.dropOffTime);
        lastConfigDropoffTime = Config.dropOffTime;

        monthDuration = SelectCarActivity.monthDuration;
        bookingTYpe = SelectCarActivity.bookingTYpe;

        deleveryEmirateID = SelectCarActivity.pickUpEmirateID;
        collectEmirateID = SelectCarActivity.dropUpEmirateID;

        pickupType = SelectCarActivity.pickUpType;
        pickUpId = SelectCarActivity.pickUpLocationID;
        pickUpDate = SelectCarActivity.pickUpDate;
        pickUpTime = SelectCarActivity.pickUpTime;
        pickUpEmirateID = SelectCarActivity.pickUpEmirateID;
        pickUpAddress = SelectCarActivity.pickUpAddress;
        pickUpLandmark = Config.SelectedPickUpLandmark;
        pickUpLocation = SelectCarActivity.pickupLocation;
        lastActivityPickupTime = SelectCarActivity.pickUpTime;

        dropupType = SelectCarActivity.dropUpType;
        dropUpId = SelectCarActivity.dropUpLocationID;
        dropUpDate = SelectCarActivity.dropUpDate;
        dropUpTime = SelectCarActivity.dropUpTime;
        dropUpEmirateID = SelectCarActivity.dropUpEmirateID;
        dropUpAddress = SelectCarActivity.dropUpAddress;
        dropUpLandmark = Config.SelectedDropUpLandmark;
        dropUpLocation = SelectCarActivity.dropUpLocation;
        lastActivityDropoffTime = SelectCarActivity.dropUpTime;

        hitPickUpTimeData(pickupType,pickUpId,pickUpDate);
        hitDropOffTimeData(dropupType,dropUpId,dropUpDate);
    }

    @Override
    public void onDurationClick(DurationModel model) {
        monthDuration = model.getDuration().replace("month", "").trim();
        binding.txtDurationMessage.setVisibility(View.VISIBLE);
        binding.txtDurationMessage.setText(monthDuration);
        hideDuration();
        setDateMonthTime(binding.txtDropDate, binding.txtDropMonth, monthDuration);
    }


    @Override
    public void onEmirateClick(int flag, EmirateModel model) {

        if (flag == deliverEmirateFlag) {
            binding.txtDeliverEmirateMessage.setVisibility(View.VISIBLE);
            binding.txtDeliverEmirateMessage.setText(model.getEmirate_name());
            deleveryEmirateID = model.getId();
            deliveryEmirateName = model.getEmirate_name();
            Config.deliveryEmirateName = model.getEmirate_name();
            hideDeliverEmirate();
        } else if (flag == collectEmirateFlag) {
            binding.txtCollectEmirateMessage.setVisibility(View.VISIBLE);
            binding.txtCollectEmirateMessage.setText(model.getEmirate_name());
            collectEmirateID = model.getId();
            collectEmirateName = model.getEmirate_name();
            Config.collectEmirateName = model.getEmirate_name();
            hideCollectEmirate();
        }

    }

    @Override
    public void onLocationClick(String location, int flag, HomeLocationModel model) {

        if (flag == pickUpFlag) {
            pickUpId = model.getId();
            pickUpLocation = model.getLocation_name();
            pickUpEmirateID = model.getEmirate_id();
            binding.txtPickUpMessage.setText(location);
            pickUpAddress = model.getAddress();
            pickUpLandmark = model.getLocation_name();
            cardPickupClick();
            hitPickUpTimeData(pickupType,pickUpId,pickUpDate);
        } else if (flag == dropUpFlag) {
            dropUpId = model.getId();
            dropUpLocation = model.getLocation_name();
            dropUpEmirateID = model.getEmirate_id();
            binding.txtDropUpMessage.setText(location);
            dropUpAddress = model.getAddress();
            dropUpLandmark = model.getLocation_name();
            cardDropUpClick();
            hitDropOffTimeData(dropupType,dropUpId,dropUpDate);
        }

    }

    @Override
    public void onTimeClick(TimeSlotModel model,int position) {
        if (callTimeFor==callTimeForPickup){
            Config.pickupTime = model.getValue();
            pickUpTime = model.getValue();
            binding.txtPickTime.setText(model.getValue());
            pickup_time_position = position;
        }else if (callTimeFor==callTimeForDropoff){
            Config.dropOffTime = model.getValue();
            dropUpTime = model.getValue();
            binding.txtDropTime.setText(model.getValue());
            dropoff_time_position = position;
        }
    }

    private void getCurrentDate() {
//        setCurrentDate();
//        setNextDate();
    }

    private void setCurrentDate() {
        Calendar c = Calendar.getInstance();
        final Date date = c.getTime();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String dayString = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String yeare = (String) DateFormat.format("yyyy", date); // 2013

        Config.pickupDay = dayString;
        Config.pickupMonth = monthString;
        Config.pickupYear = yeare;

        binding.txtPickDate.setText(dayString);
        binding.txtPickMonth.setText(monthString.toUpperCase());
//        binding.txtPickTime.setText(getCurrentTime() + "");


        String currentDate = yeare + "-" + monthNumber + "-" + dayString;
        pickUpDate = currentDate;

        pickupYEAR = Integer.parseInt(yeare);
        if (monthNumber.equals("1")) {
            pickupMONTH = Integer.parseInt(monthNumber);
        } else {
            pickupMONTH = Integer.parseInt(monthNumber) - 1;
        }
        pickupDAY = Integer.parseInt(dayString) + 1;

    }

    private void setNextDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        final Date date = c.getTime();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String dayString = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String yeare = (String) DateFormat.format("yyyy", date); // 2013

        Config.dropOffDay = dayString;
        Config.dropOffMonth = monthString;
        Config.dropOffYear = yeare;

        binding.txtDropDate.setText(dayString);
        binding.txtDropMonth.setText(monthString.toUpperCase());
//        binding.txtDropTime.setText(getCurrentTime());

        String nextDate = yeare + "-" + monthNumber + "-" + dayString;
        dropUpDate = nextDate;
    }

    private void setNextSelectionDate() {
        Calendar c = Calendar.getInstance();
        c.set(pickupYEAR, pickupMONTH, pickupDAY);
        final Date date = c.getTime();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String dayString = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String yeare = (String) DateFormat.format("yyyy", date); // 2013

        binding.txtDropDate.setText(dayString);
        binding.txtDropMonth.setText(monthString.toUpperCase());
//        binding.txtDropTime.setText(getCurrentTime());

        String nextDate = yeare + "-" + monthNumber + "-" + dayString;
        dropUpDate = nextDate;
    }


    private String getCurrentTime() {
        String delegate = "hh:mm aaa";
        String oldstr = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

        pickUpTime = oldstr;
        dropUpTime = oldstr;

        String str = oldstr.replace("am", "AM").replace("pm", "PM");
        return str;
    }


    private void onClick() {

        binding.lnrPickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setPickupDateTime(binding.txtPickDate, binding.txtPickMonth, pickUpFlag);
            }
        });

        binding.lnrDropoffDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (!bookingTYpe.equals(Config.monthly)) {
                    setDropoffDateTime(binding.txtDropDate, binding.txtDropMonth, dropUpFlag);
                }
            }
        });

        binding.lnrPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (TextUtils.isEmpty(pickUpId)) {
                    H.showMessage(activity, getResources().getString(R.string.selectSelftPickUpLocation));
                }else {
                    callTimeFor = callTimeForPickup;
                    setTimeDialog(puckupTimeSlotList,pickup_time_position);
//                setTime(binding.txtPickTime, pickUpFlag);
                }
            }
        });

        binding.lnrDropoffTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (TextUtils.isEmpty(dropUpId)) {
                    H.showMessage(activity, getResources().getString(R.string.selectDropOffLocation));
                }else {
                    callTimeFor = callTimeForDropoff;
                    setTimeDialog(dropoffTimeSlotList,dropoff_time_position);
//                setTime(binding.txtDropTime, dropUpFlag);
                }
            }
        });

        binding.txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.radioMonthlyDeals.isChecked()) {
                    if (TextUtils.isEmpty(monthDuration)) {
                        H.showMessage(activity, getResources().getString(R.string.selectDuration));
                        return;
                    }
                }

                if (binding.radioDeliverYes.isChecked()) {
                    if (TextUtils.isEmpty(deleveryEmirateID)) {
                        H.showMessage(activity, getResources().getString(R.string.selectDeliverEmirate));
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(pickUpId)) {
                        H.showMessage(activity, getResources().getString(R.string.selectSelftPickUpLocation));
                        return;
                    }
                }

                if (binding.radioCollectYes.isChecked()) {
                    if (TextUtils.isEmpty(collectEmirateID)) {
                        H.showMessage(activity, getResources().getString(R.string.selectCollectEmirate));
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(dropUpId)) {
                        H.showMessage(activity, getResources().getString(R.string.selectDropOffLocation));
                        return;
                    }
                }

                if (TextUtils.isEmpty(pickUpTime)){
                    H.showMessage(activity, getResources().getString(R.string.selectPickupTime));
                    return;
                }

                if (TextUtils.isEmpty(dropUpTime)){
                    H.showMessage(activity, getResources().getString(R.string.selectDropoffTime));
                    return;
                }

                hitVerifyPickUpData(pickUpId, pickUpDate, pickUpTime, dropUpId, dropUpDate, dropUpTime);

            }
        });

        binding.radioDailyRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
//                monthDuration = "";
//                binding.txtDurationMessage.setText("");
//                binding.txtDurationMessage.setVisibility(View.GONE);
                bookingTYpe = Config.daily;
                binding.radioMonthlyDeals.setChecked(false);
                blueTin(binding.radioDailyRental);
                blackTin(binding.radioMonthlyDeals);
                binding.cardDuration.setVisibility(View.GONE);
                hideDuration();
                hideAllCardView();
                getCurrentDate();
                updateDropDateBGDark();
            }
        });

        binding.radioMonthlyDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                monthDuration = "";
//                binding.txtDurationMessage.setText("");
//                binding.txtDurationMessage.setVisibility(View.GONE);
                bookingTYpe = Config.monthly;
                Click.preventTwoClick(v);
                binding.radioDailyRental.setChecked(false);
                blueTin(binding.radioMonthlyDeals);
                blackTin(binding.radioDailyRental);
                binding.cardDuration.setVisibility(View.VISIBLE);
                hideAllCardView();
                getCurrentDate();
                updateDropDateBGGray();
            }
        });

        binding.radioDeliverYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.HOME_DELIVERY_CHECK = true;
                binding.radioDeliverNo.setChecked(false);
                blueTin(binding.radioDeliverYes);
                blackTin(binding.radioDeliverNo);
                pickupType = Config.deliver;
                binding.cardPickLocation.setVisibility(View.GONE);
                binding.cardDeliverEmirate.setVisibility(View.VISIBLE);
                hideOnYesNo();

            }
        });


        binding.radioDeliverNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.HOME_DELIVERY_CHECK = false;
                binding.radioDeliverYes.setChecked(false);
                blueTin(binding.radioDeliverNo);
                blackTin(binding.radioDeliverYes);
                pickupType = Config.self_pickup;
                binding.cardPickLocation.setVisibility(View.VISIBLE);
                binding.cardDeliverEmirate.setVisibility(View.GONE);
                hideOnYesNo();

            }
        });

        binding.radioCollectYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.HOME_COLLECT_CHECK = true;
                binding.radioCollectNo.setChecked(false);
                blueTin(binding.radioCollectYes);
                blackTin(binding.radioCollectNo);
                dropupType = Config.collect;
                binding.cardDropLocation.setVisibility(View.GONE);
                binding.cardCollectEmirate.setVisibility(View.VISIBLE);
                hideOnYesNo();
            }
        });


        binding.radioCollectNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Config.HOME_COLLECT_CHECK = false;
                binding.radioCollectYes.setChecked(false);
                blueTin(binding.radioCollectNo);
                blackTin(binding.radioCollectYes);
                dropupType = Config.self_dropoff;
                binding.cardDropLocation.setVisibility(View.VISIBLE);
                binding.cardCollectEmirate.setVisibility(View.GONE);
                hideOnYesNo();
            }
        });


        binding.cardPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                cardPickupClick();
                hideDropUpLocation();
                hideDuration();


            }
        });

        binding.cardDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                cardDropUpClick();
                hidePickupLocation();
                hideDuration();

            }
        });

        binding.cardDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                cardDurationClick();
                hidePickupLocation();
                hideDropUpLocation();
            }
        });

        binding.cardDeliverEmirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardDeliverEmirateClick();
                hideCollectEmirate();
                hideDuration();
                hidePickupLocation();
                hideDropUpLocation();
            }
        });

        binding.cardCollectEmirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                cardCollectEmirateClick();
                hideDeliverEmirate();
                hideDuration();
                hidePickupLocation();
                hideDropUpLocation();
            }
        });
    }

    private void updateDropDateBGDark() {
        binding.lnrDropoffDate.setBackground(getResources().getDrawable(R.drawable.corner_blue_bg));
        binding.txtDropOffText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        binding.txtDropDate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        binding.txtDropMonth.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void updateDropDateBGGray() {
        binding.lnrDropoffDate.setBackground(getResources().getDrawable(R.drawable.corner_light_gray_bg));
        binding.txtDropOffText.setTextColor(getResources().getColor(R.color.semi_transparent));
        binding.txtDropDate.setTextColor(getResources().getColor(R.color.semi_transparent));
        binding.txtDropMonth.setTextColor(getResources().getColor(R.color.semi_transparent));
    }

    private void hideOnYesNo() {
        hideDuration();
        hideDeliverEmirate();
        hidePickupLocation();
        hideCollectEmirate();
        hideDropUpLocation();
    }

    private void hidePickupLocation() {
        binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
        binding.imgDeliverArrowUp.setVisibility(View.GONE);
        binding.cardLocationDeliver.setVisibility(View.GONE);
    }

    private void hideDropUpLocation() {
        binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
        binding.imgCollectArrowUp.setVisibility(View.GONE);
        binding.cardLocationCollect.setVisibility(View.GONE);
    }

    private void hideDuration() {
        binding.cardDurationView.setVisibility(View.GONE);
        binding.imgDurationArrowUp.setVisibility(View.GONE);
        binding.imgDurationArrowDown.setVisibility(View.VISIBLE);
    }

    private void hideDeliverEmirate() {
        binding.cardDeliverEmirateView.setVisibility(View.GONE);
        binding.imgDeliverEmirateArrowUp.setVisibility(View.GONE);
        binding.imgDeliverEmirateArrowDown.setVisibility(View.VISIBLE);
    }

    private void hideCollectEmirate() {
        binding.cardCollectEmirateView.setVisibility(View.GONE);
        binding.imgCollectEmirateArrowUp.setVisibility(View.GONE);
        binding.imgCollectEmirateArrowDown.setVisibility(View.VISIBLE);
    }


    private void hideAllCardView() {

        binding.cardLocationDeliver.setVisibility(View.GONE);
        binding.imgDeliverArrowUp.setVisibility(View.GONE);
        binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);

        binding.cardLocationCollect.setVisibility(View.GONE);
        binding.imgCollectArrowUp.setVisibility(View.GONE);
        binding.imgCollectArrowDown.setVisibility(View.VISIBLE);

    }

    private void cardPickupClick() {
        if (binding.imgDeliverArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgDeliverArrowDown.setVisibility(View.GONE);
            binding.imgDeliverArrowUp.setVisibility(View.VISIBLE);
            binding.cardLocationDeliver.setVisibility(View.VISIBLE);

        } else if (binding.imgDeliverArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgDeliverArrowUp.setVisibility(View.GONE);
            binding.imgDeliverArrowDown.setVisibility(View.VISIBLE);
            binding.cardLocationDeliver.setVisibility(View.GONE);
        }
    }

    private void cardDropUpClick() {

        if (binding.imgCollectArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgCollectArrowDown.setVisibility(View.GONE);
            binding.imgCollectArrowUp.setVisibility(View.VISIBLE);
            binding.cardLocationCollect.setVisibility(View.VISIBLE);
        } else if (binding.imgCollectArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgCollectArrowUp.setVisibility(View.GONE);
            binding.imgCollectArrowDown.setVisibility(View.VISIBLE);
            binding.cardLocationCollect.setVisibility(View.GONE);
        }

    }

    private void cardDurationClick() {

        if (binding.imgDurationArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgDurationArrowDown.setVisibility(View.GONE);
            binding.imgDurationArrowUp.setVisibility(View.VISIBLE);
            binding.cardDurationView.setVisibility(View.VISIBLE);
        } else if (binding.imgDurationArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgDurationArrowUp.setVisibility(View.GONE);
            binding.imgDurationArrowDown.setVisibility(View.VISIBLE);
            binding.cardDurationView.setVisibility(View.GONE);
        }

    }

    private void cardDeliverEmirateClick() {

        if (binding.imgDeliverEmirateArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgDeliverEmirateArrowDown.setVisibility(View.GONE);
            binding.imgDeliverEmirateArrowUp.setVisibility(View.VISIBLE);
            binding.cardDeliverEmirateView.setVisibility(View.VISIBLE);
        } else if (binding.imgDeliverEmirateArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgDeliverEmirateArrowUp.setVisibility(View.GONE);
            binding.imgDeliverEmirateArrowDown.setVisibility(View.VISIBLE);
            binding.cardDeliverEmirateView.setVisibility(View.GONE);
        }

    }

    private void cardCollectEmirateClick() {

        if (binding.imgCollectEmirateArrowDown.getVisibility() == View.VISIBLE) {
            binding.imgCollectEmirateArrowDown.setVisibility(View.GONE);
            binding.imgCollectEmirateArrowUp.setVisibility(View.VISIBLE);
            binding.cardCollectEmirateView.setVisibility(View.VISIBLE);
        } else if (binding.imgCollectEmirateArrowUp.getVisibility() == View.VISIBLE) {
            binding.imgCollectEmirateArrowUp.setVisibility(View.GONE);
            binding.imgCollectEmirateArrowDown.setVisibility(View.VISIBLE);
            binding.cardCollectEmirateView.setVisibility(View.GONE);
        }

    }

    private void setPickupDateTime(TextView txtDay, TextView txtMonth, int flag) {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(activity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date date = newDate.getTime();
                String fdate = sd.format(date);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String dayString = (String) DateFormat.format("dd", date); // 20
                String monthString = (String) DateFormat.format("MMM", date); // Jun
                String monthNumber = (String) DateFormat.format("MM", date); // 06
                String yeare = (String) DateFormat.format("yyyy", date); // 2013

                if (flag == pickUpFlag) {
                    String currentDate = yeare + "-" + monthNumber + "-" + dayString;
                    pickUpDate = currentDate;

                    Config.pickupDay = dayString;
                    Config.pickupMonth = monthString;
                    Config.pickupYear = yeare;

                } else if (flag == dropUpFlag) {
                    String currentDate = yeare + "-" + monthNumber + "-" + dayString;
                    dropUpDate = currentDate;
                }

                txtDay.setText(dayString);
                txtMonth.setText(monthString.toUpperCase());

                pickupYEAR = Integer.parseInt(yeare);
                if (monthNumber.equals("1")) {
                    pickupMONTH = Integer.parseInt(monthNumber);
                } else {
                    pickupMONTH = Integer.parseInt(monthNumber) - 1;
                }
                pickupDAY = Integer.parseInt(dayString) + 1;

                setNextSelectionDate();
                if (!TextUtils.isEmpty(pickUpId)){
                    hitPickUpTimeData(pickupType,pickUpId,pickUpDate);
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePickerDialog.show();
    }

    private void setDropoffDateTime(TextView txtDay, TextView txtMonth, int flag) {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(activity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date date = newDate.getTime();
                String fdate = sd.format(date);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String dayString = (String) DateFormat.format("dd", date); // 20
                String monthString = (String) DateFormat.format("MMM", date); // Jun
                String monthNumber = (String) DateFormat.format("MM", date); // 06
                String yeare = (String) DateFormat.format("yyyy", date); // 2013

                if (flag == pickUpFlag) {
                    String currentDate = yeare + "-" + monthNumber + "-" + dayString;
                    pickUpDate = currentDate;

                } else if (flag == dropUpFlag) {
                    String currentDate = yeare + "-" + monthNumber + "-" + dayString;
                    dropUpDate = currentDate;

                    Config.dropOffDay = dayString;
                    Config.dropOffMonth = monthString;
                    Config.dropOffYear = yeare;
                }

                txtDay.setText(dayString);
                txtMonth.setText(monthString.toUpperCase());

                if (!TextUtils.isEmpty(dropUpId)){
                    hitDropOffTimeData(dropupType,dropUpId,dropUpDate);
                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar c = Calendar.getInstance();
        c.set(pickupYEAR, pickupMONTH, pickupDAY);

        mDatePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
//        mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePickerDialog.show();
    }

    private void setDateMonthTime(TextView txtDay, TextView txtMonth, String month) {

        Calendar newDate = Calendar.getInstance();
        newDate.add(Calendar.MONTH, Integer.parseInt(month));
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = newDate.getTime();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String dayString = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String yeare = (String) DateFormat.format("yyyy", date); // 2013

        String currentDate = yeare + "-" + monthNumber + "-" + dayString;
        dropUpDate = currentDate;

        txtDay.setText(dayString);
        txtMonth.setText(monthString.toUpperCase());

        Config.dropOffDay = dayString;
        Config.dropOffMonth = monthString;
        Config.dropOffYear = yeare;

    }

    private void setTime(TextView txtTime, int flag) {

        //AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
        //AlertDialog.THEME_DEVICE_DEFAULT_DARK
        //AlertDialog.THEME_HOLO_DARK
        //AlertDialog.THEME_HOLO_LIGHT
        //AlertDialog.THEME_TRADITIONAL

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
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

                txtTime.setText(selectedHour + ":" + selectedMinute + " " + AM_PM);

                if (flag == pickUpFlag) {
                    pickUpTime = selectedHour + ":" + selectedMinute + " " + AM_PM.toLowerCase();
                    Config.pickupTime = pickUpTime;
                } else if (flag == dropUpFlag) {
                    dropUpTime = selectedHour + ":" + selectedMinute + " " + AM_PM.toLowerCase();
                    Config.dropOffTime = dropUpTime;
                }

            }
        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void setTimeDialog(List<TimeSlotModel> timeSlotModelList,int position){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_time_slot);

        LinearLayout lnrError = dialog.findViewById(R.id.lnrError);
        LinearLayout lnrView = dialog.findViewById(R.id.lnrView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        TimeSlotAdapter adapter = new TimeSlotAdapter(activity,timeSlotModelList, 2,position,dialog);
        RecyclerView recyclerTimeSlot = dialog.findViewById(R.id.recyclerTimeSlot);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerTimeSlot.setLayoutManager(linearLayoutManager);
        recyclerTimeSlot.setHasFixedSize(true);
        recyclerTimeSlot.setAdapter(adapter);
        recyclerTimeSlot.setItemViewCacheSize(timeSlotModelList.size());
        recyclerTimeSlot.smoothScrollToPosition(position);

        if (timeSlotModelList.isEmpty()){
            lnrView.setVisibility(View.GONE);
            lnrError.setVisibility(View.VISIBLE);
        }else {
            lnrView.setVisibility(View.VISIBLE);
            lnrError.setVisibility(View.GONE);
        }

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void hitEmirateData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "emirate_list").addJson(j)
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
                        emirate_list = json.getJsonList(P.emirate_list);

                        if (emirate_list != null && emirate_list.size() != 0) {
                            for (Json jsonE : emirate_list) {
                                String id = jsonE.getString(P.id);
                                String emirate_name = jsonE.getString(P.emirate_name);
                                String status = jsonE.getString(P.status);
                                EmirateModel model = new EmirateModel();
                                model.setId(id);
                                model.setEmirate_name(emirate_name);
                                model.setStatus(status);
                                deliverEmirateList.add(model);
                                collectEmirateList.add(model);
                            }
                        }

                        deliverEmirateAdapter.notifyDataSetChanged();
                        collectEmirateAdapter.notifyDataSetChanged();
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitEmirateData");
    }

    private void hitLocationData() {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "location").addJson(j)
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
                        JsonList location_list = json.getJsonList(P.location_list);
                        for (int i = 0; i < location_list.size(); i++) {
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
                            locationDailyList.add(model);
                            locationMonthlyList.add(model);
                        }

                        locationDailyAdapter.notifyDataSetChanged();
                        locationMonthlyAdapter.notifyDataSetChanged();

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");
    }

    private void hitVerifyPickUpData(String pickUpID, String pickUpDate, String pickUpTime, String dropUpID, String dropUpDate, String dropUpTime) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        j.addString(P.booking_type, bookingTYpe);

        if (binding.radioMonthlyDeals.isChecked()) {
            j.addString(P.month_time, monthDuration);
        } else {
            j.addString(P.month_time, "");
        }

        j.addString(P.pickup_type, pickupType);

        if (binding.radioDeliverYes.isChecked()) {
            j.addString(P.pickup_emirate_id, deleveryEmirateID);
            j.addString(P.pickup_location_id, "0");
            j.addString(P.pickup_address, "");
            j.addString(P.pickup_landmark, "");
        } else {
            j.addString(P.pickup_emirate_id, pickUpEmirateID);
            j.addString(P.pickup_location_id, pickUpID);
            j.addString(P.pickup_address, pickUpAddress);
            j.addString(P.pickup_landmark, pickUpLandmark);
        }

        j.addString(P.pickup_date, pickUpDate);
        j.addString(P.pickup_time, pickUpTime);

        Api.newApi(activity, P.BaseUrl + "verify_pickup_location_date_time").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
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
                        hitVerifyDropUpData(pickUpID, pickUpAddress, pickUpDate, pickUpTime, dropUpID, dropUpDate, dropUpTime);

                    } else {
                        H.showMessage(activity, json.getString(P.error));

                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitVerifyPickUpData");
    }

    private void hitVerifyDropUpData(String pickupId, String pickupAddress, String pickUpDate, String pickUpTime, String id, String date, String time) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        j.addString(P.action, "");

        j.addString(P.pickup_date, pickUpDate);
        j.addString(P.pickup_time, pickUpTime);
        j.addString(P.dropoff_type, dropupType);

        if (binding.radioCollectYes.isChecked()) {
            j.addString(P.dropoff_emirate_id, collectEmirateID);
            j.addString(P.dropoff_location_id, "0");
            j.addString(P.dropoff_address, "");
            j.addString(P.dropoff_landmark, "");
        } else {
            j.addString(P.dropoff_emirate_id, dropUpEmirateID);
            j.addString(P.dropoff_location_id, id);
            j.addString(P.dropoff_address, dropUpAddress);
            j.addString(P.dropoff_landmark, dropUpLandmark);
        }

        j.addString(P.dropoff_date, date);
        j.addString(P.dropoff_time, time);

        Api.newApi(activity, P.BaseUrl + "verify_dropoff_location_date_time").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        Config.FOR_EDIT_LOCATION = true;
                        Config.dropUpTypeValue = dropupType;
                        Config.SelectedDropUpEmirateID = dropUpEmirateID;
                        Config.SelectedDropUpID = id;
                        Config.SelectedDropUpAddress = dropUpLocation;
                        Config.SelectedDropUpLandmark = dropUpLandmark;
                        Config.SelectedDropUpDate = dropUpDate;
                        Config.SelectedDropUpTime = dropUpTime;

                        SelectCarActivity.monthDuration = monthDuration;
                        SelectCarActivity.bookingTYpe = bookingTYpe;

                        if (binding.radioDeliverYes.isChecked()) {
                            SelectCarActivity.pickUpEmirateID = deleveryEmirateID;
                        } else {
                            SelectCarActivity.pickUpEmirateID = pickUpEmirateID;
                        }
                        if (binding.radioDeliverYes.isChecked()) {
                            SelectCarActivity.dropUpEmirateID = collectEmirateID;
                        } else {
                            SelectCarActivity.dropUpEmirateID = dropUpEmirateID;
                        }

                        SelectCarActivity.pickUpDate = pickUpDate;
                        SelectCarActivity.dropUpDate = dropUpDate;
                        SelectCarActivity.pickUpTime = binding.txtPickTime.getText().toString().replace(" ", "");
                        SelectCarActivity.dropUpTime = binding.txtDropTime.getText().toString().replace(" ", "");
                        SelectCarActivity.pickUpType = pickupType;
                        SelectCarActivity.dropUpType = dropupType;
                        SelectCarActivity.pickUpLocationID = pickupId;
                        SelectCarActivity.dropUpLocationID = id;
                        SelectCarActivity.pickUpAddress = pickupAddress;
                        SelectCarActivity.dropUpAddress = dropUpAddress;

                        finish();

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitVerifyDropUpData");
    }


    public void addDuration() {
        durationList.add(new DurationModel("1 month"));
        durationList.add(new DurationModel("2 month"));
        durationList.add(new DurationModel("3 month"));
        durationList.add(new DurationModel("4 month"));
        durationList.add(new DurationModel("5 month"));
        durationList.add(new DurationModel("6 month"));
        durationList.add(new DurationModel("7 month"));
        durationList.add(new DurationModel("8 month"));
        durationList.add(new DurationModel("9 month"));
        durationList.add(new DurationModel("10 month"));
        durationList.add(new DurationModel("11 month"));
        durationList.add(new DurationModel("12 month"));
        durationList.add(new DurationModel("13 month"));
        durationList.add(new DurationModel("14 month"));
        durationList.add(new DurationModel("15 month"));
        durationList.add(new DurationModel("16 month"));
        durationList.add(new DurationModel("17 month"));
        durationList.add(new DurationModel("18 month"));
        durationList.add(new DurationModel("19 month"));
        durationList.add(new DurationModel("20 month"));
        durationList.add(new DurationModel("21 month"));
        durationList.add(new DurationModel("22 month"));
        durationList.add(new DurationModel("23 month"));
        durationList.add(new DurationModel("24 month"));
        durationList.add(new DurationModel("25 month"));
        durationList.add(new DurationModel("26 month"));
        durationList.add(new DurationModel("27 month"));
        durationList.add(new DurationModel("28 month"));
        durationList.add(new DurationModel("29 month"));
        durationList.add(new DurationModel("30 month"));
        durationList.add(new DurationModel("31 month"));
        durationList.add(new DurationModel("32 month"));
        durationList.add(new DurationModel("33 month"));
        durationList.add(new DurationModel("34 month"));
        durationList.add(new DurationModel("35 month"));
        durationList.add(new DurationModel("36 month"));
        durationList.add(new DurationModel("37 month"));
        durationList.add(new DurationModel("38 month"));
        durationList.add(new DurationModel("39 month"));
    }

    private void hitPickUpTimeData(String pickup_type, String pickup_location_id, String pickup_date) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        j.addString(P.pickup_type, pickup_type);
        j.addString(P.pickup_location_id, pickup_location_id);
        j.addString(P.pickup_date, pickup_date);

        Api.newApi(activity, P.BaseUrl + "pickup_location_time").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        puckupTimeSlotList.clear();
                        if (!callPickupFirst){
                            callPickupFirst = true;
                        }else {
                            binding.txtPickTime.setText(getResources().getString(R.string.selectTime));
                            Config.pickupTime = "";
                            pickUpTime = "";
                            pickup_time_position = 0;
                        }

                        Json data = json.getJson(P.data);
                        JsonList time_list = data.getJsonList(P.time_list);
                        if (time_list!=null && time_list.size()!=0){
                            for (Json datValue : time_list){
                                String key = datValue.getString("key");
                                String value = datValue.getString("value");
                                TimeSlotModel model = new TimeSlotModel();
                                model.setKey(key);
                                model.setValue(value);
                                puckupTimeSlotList.add(model);
                            }
                        }
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitPickUpTimeData");
    }

    private void hitDropOffTimeData(String dropoff_type, String dropoff_location_id, String dropoff_date) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        j.addString(P.dropoff_type, dropoff_type);
        j.addString(P.dropoff_location_id, dropoff_location_id);
        j.addString(P.dropoff_date, dropoff_date);

        Api.newApi(activity, P.BaseUrl + "dropoff_location_time").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        dropoffTimeSlotList.clear();
                        if (!callDropoffFirst){
                            callDropoffFirst = true;
                        }else {
                            binding.txtDropTime.setText(getResources().getString(R.string.selectTime));
                            Config.dropOffTime = "";
                            dropUpTime = "";
                            dropoff_time_position = 0;
                        }
                        Json data = json.getJson(P.data);
                        JsonList time_list = data.getJsonList(P.time_list);
                        if (time_list!=null && time_list.size()!=0){
                            for (Json datValue : time_list){
                                String key = datValue.getString("key");
                                String value = datValue.getString("value");
                                TimeSlotModel model = new TimeSlotModel();
                                model.setKey(key);
                                model.setValue(value);
                                dropoffTimeSlotList.add(model);
                            }
                        }
                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitDropOffTimeData");
    }

    @Override
    public void onBackPressed() {
        Config.pickupTime = lastConfigPickupTime;
        Config.dropOffTime = lastConfigDropoffTime;
        SelectCarActivity.pickUpTime = lastActivityPickupTime;
        SelectCarActivity.dropUpTime = lastActivityDropoffTime;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}