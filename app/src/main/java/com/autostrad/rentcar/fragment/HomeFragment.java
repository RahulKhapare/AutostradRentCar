package com.autostrad.rentcar.fragment;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.SelectCarActivity;
import com.autostrad.rentcar.adapter.DurationAdapter;
import com.autostrad.rentcar.adapter.EmirateAdapter;
import com.autostrad.rentcar.adapter.LocationAdapter;
import com.autostrad.rentcar.adapter.SliderImageAdapter;
import com.autostrad.rentcar.databinding.FragmentHomeBinding;
import com.autostrad.rentcar.model.DurationModel;
import com.autostrad.rentcar.model.EmirateModel;
import com.autostrad.rentcar.model.HomeLocationModel;
import com.autostrad.rentcar.model.SliderModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements LocationAdapter.onClick, DurationAdapter.onClick, EmirateAdapter.onClick {

    private Context context;
    private FragmentHomeBinding binding;
    private List<SliderModel> sliderModelList;
    private SliderImageAdapter sliderImageAdapter;
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


    private LoadingDialog loadingDialog;
    public static JsonList carList = null;
    public static JsonList blogList = null;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public static boolean containsLocation(Collection<HomeLocationModel> list, String location) {
        for (HomeLocationModel model : list) {
            if (model != null && model.getAddress()
                    .equals(location)) {
                return true;
            }
        }
        return false;
    }

    private void initView() {

        bookingTYpe = Config.daily;
        pickupType = Config.self_pickup;
        dropupType = Config.self_dropoff;

        loadingDialog = new LoadingDialog(context);
        sliderModelList = new ArrayList<>();
        sliderImageAdapter = new SliderImageAdapter(context, sliderModelList);
        binding.pager.setAdapter(sliderImageAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager, true);

        durationList = new ArrayList<>();
        addDuration();
        durationAdapter = new DurationAdapter(context, durationList, HomeFragment.this);
        LinearLayoutManager linearLayoutManager0 = new LinearLayoutManager(context);
        binding.recyclerDuration.setLayoutManager(linearLayoutManager0);
        binding.recyclerDuration.setHasFixedSize(true);
        binding.recyclerDuration.setNestedScrollingEnabled(false);
        binding.recyclerDuration.setAdapter(durationAdapter);

        locationDailyList = new ArrayList<>();
        locationDailyAdapter = new LocationAdapter(context, locationDailyList, HomeFragment.this, pickUpFlag);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        binding.recyclerDailyLocation.setLayoutManager(linearLayoutManager1);
        binding.recyclerDailyLocation.setHasFixedSize(true);
        binding.recyclerDailyLocation.setAdapter(locationDailyAdapter);

        locationMonthlyList = new ArrayList<>();
        locationMonthlyAdapter = new LocationAdapter(context, locationMonthlyList, HomeFragment.this, dropUpFlag);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        binding.recyclerLocationCollect.setLayoutManager(linearLayoutManager2);
        binding.recyclerLocationCollect.setHasFixedSize(true);
        binding.recyclerLocationCollect.setAdapter(locationMonthlyAdapter);

        deliverEmirateList = new ArrayList<>();
        deliverEmirateAdapter = new EmirateAdapter(getActivity(), deliverEmirateList, HomeFragment.this, deliverEmirateFlag);
        binding.recyclerDeliverEmirate.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerDeliverEmirate.setHasFixedSize(true);
        binding.recyclerDeliverEmirate.setNestedScrollingEnabled(false);
        binding.recyclerDeliverEmirate.setAdapter(deliverEmirateAdapter);

        collectEmirateList = new ArrayList<>();
        collectEmirateAdapter = new EmirateAdapter(getActivity(), collectEmirateList, HomeFragment.this, collectEmirateFlag);
        binding.recyclerCollectEmirate.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerCollectEmirate.setHasFixedSize(true);
        binding.recyclerCollectEmirate.setNestedScrollingEnabled(false);
        binding.recyclerCollectEmirate.setAdapter(collectEmirateAdapter);

        hitHomeData();
        hitLocationData();
        getCurrentDate();
        hitEmirateData();
        onClick();

    }

    private void checkSize(RecyclerView recyclerView) {
        if (locationDailyList.size() > 6) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = 1010;
            recyclerView.setLayoutParams(params);
        }
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
        } else if (flag == dropUpFlag) {
            dropUpId = model.getId();
            dropUpLocation = model.getLocation_name();
            dropUpEmirateID = model.getEmirate_id();
            binding.txtDropUpMessage.setText(location);
            dropUpAddress = model.getAddress();
            dropUpLandmark = model.getLocation_name();
            cardDropUpClick();
        }

    }

    private void getCurrentDate() {
        setCurrentDate();
        setNextDate();
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
        Config.pickupTime = getCurrentTime() + "";

        binding.txtPickDate.setText(dayString);
        binding.txtPickMonth.setText(monthString.toUpperCase());
        binding.txtPickTime.setText(getCurrentTime() + "");

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
        Config.dropOffTime = getCurrentTime() + "";

        binding.txtDropDate.setText(dayString);
        binding.txtDropMonth.setText(monthString.toUpperCase());
        binding.txtDropTime.setText(getCurrentTime());

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
        binding.txtDropTime.setText(getCurrentTime());

        String nextDate = yeare + "-" + monthNumber + "-" + dayString;
        dropUpDate = nextDate;
    }


    private String getCurrentTime() {
//        String delegate = "kk:mm aaa"; //24 HOUR
        String delegate = "hh:mm aaa"; //12 HOUR
        String oldstr = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

        pickUpTime = oldstr;
        dropUpTime = oldstr;

        String str = oldstr.replace("am", "AM").replace("pm", "PM");
        return str;
    }


    private void setUpSliderList(JsonList jsonList) {

        sliderModelList.clear();

        if (jsonList != null) {
            for (Json json : jsonList) {
                SliderModel model = new SliderModel();
                model.setBanner_image(json.getString(P.banner_image));
                model.setImage_alt_text(json.getString(P.image_alt_text));
                model.setUrl(json.getString(P.url));
                sliderModelList.add(model);
            }
        }

        sliderImageAdapter.notifyDataSetChanged();

        if (sliderModelList.isEmpty()) {
            binding.lnrSlider.setVisibility(View.GONE);
        } else {
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
                setTime(binding.txtPickTime, pickUpFlag);
            }
        });

        binding.lnrDropoffTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setTime(binding.txtDropTime, dropUpFlag);
            }
        });

        binding.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.radioMonthlyDeals.isChecked()) {
                    if (TextUtils.isEmpty(monthDuration)) {
                        H.showMessage(context, getResources().getString(R.string.selectDuration));
                        return;
                    }
                }

                if (binding.radioDeliverYes.isChecked()) {
                    if (TextUtils.isEmpty(deleveryEmirateID)) {
                        H.showMessage(context, getResources().getString(R.string.selectDeliverEmirate));
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(pickUpId)) {
                        H.showMessage(context, getResources().getString(R.string.selectSelftPickUpLocation));
                        return;
                    }
                }

                if (binding.radioCollectYes.isChecked()) {
                    if (TextUtils.isEmpty(collectEmirateID)) {
                        H.showMessage(context, getResources().getString(R.string.selectCollectEmirate));
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(dropUpId)) {
                        H.showMessage(context, getResources().getString(R.string.selectDropOffLocation));
                        return;
                    }
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
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

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

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePickerDialog.show();
    }

    private void setDropoffDateTime(TextView txtDay, TextView txtMonth, int flag) {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

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
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
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

    private void hitHomeData() {

        ProgressView.show(context, loadingDialog);
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

                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitHomeData");
    }

    private void hitEmirateData() {

        ProgressView.show(context, loadingDialog);
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
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitEmirateData");
    }

    private void hitLocationData() {

        ProgressView.show(context, loadingDialog);
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
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");
    }

    private void hitVerifyPickUpData(String pickUpID, String pickUpDate, String pickUpTime, String dropUpID, String dropUpDate, String dropUpTime) {

        ProgressView.show(context, loadingDialog);
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

        j.addString(P.pickup_lat, "1");
        j.addString(P.pickup_long, "1");
        j.addString(P.pickup_details, "");
        j.addString(P.pickup_delivery_check, "0");


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
                        hitVerifyDropUpData(pickUpID, pickUpAddress, pickUpDate, pickUpTime, dropUpID, dropUpDate, dropUpTime,pickUpLocation);

                    } else {
                        H.showMessage(context, json.getString(P.error));

                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitVerifyPickUpData");
    }

    private void hitVerifyDropUpData(String pickupId, String pickupAddress, String pickUpDate, String pickUpTime, String id, String date, String time,String pickupLocation) {

        ProgressView.show(context, loadingDialog);
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

        j.addString(P.dropoff_lat, "1");
        j.addString(P.dropoff_long, "1");
        j.addString(P.dropoff_details, "");
        j.addString(P.dropoff_delivery_check, "0");

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

                        Intent intent = new Intent(context, SelectCarActivity.class);
                        intent.putExtra(Config.monthDuration, monthDuration);
                        intent.putExtra(Config.bookingTYpe, bookingTYpe);
                        if (binding.radioDeliverYes.isChecked()) {
                            intent.putExtra(Config.pickUpEmirateID, deleveryEmirateID);
                        } else {
                            intent.putExtra(Config.pickUpEmirateID, pickUpEmirateID);
                        }

                        if (binding.radioCollectYes.isChecked()) {
                            intent.putExtra(Config.dropUpEmirateID, collectEmirateID);
                        } else {
                            intent.putExtra(Config.dropUpEmirateID, dropUpEmirateID);
                        }

                        intent.putExtra(Config.pickUpDate, pickUpDate);
                        intent.putExtra(Config.dropUpDate, dropUpDate);
//                        intent.putExtra(Config.pickUpTime,pickUpTime);
                        intent.putExtra(Config.pickUpTime, binding.txtPickTime.getText().toString().replace(" ", ""));
//                        intent.putExtra(Config.dropUpTime,time);
                        intent.putExtra(Config.dropUpTime, binding.txtDropTime.getText().toString().replace(" ", ""));
                        intent.putExtra(Config.pickUpType, pickupType);
                        intent.putExtra(Config.dropUpType, dropupType);
                        intent.putExtra(Config.pickUpLocationID, pickupId);
                        intent.putExtra(Config.dropUpLocationID, id);
                        intent.putExtra(Config.pickUpAddress, pickupAddress);
                        intent.putExtra(Config.dropUpAddress, dropUpAddress);
                        intent.putExtra(Config.SelectedDropUpAddress, dropUpLocation);
                        intent.putExtra(Config.SelectedPickUpAddress, pickupLocation);
                        startActivity(intent);

                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }

                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitVerifyDropUpData");
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
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


    @Override
    public void onDestroyView() {
        if (binding.getRoot() != null) {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }
}
