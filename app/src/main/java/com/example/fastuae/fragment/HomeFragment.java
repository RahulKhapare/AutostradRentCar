package com.example.fastuae.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.example.fastuae.R;
import com.example.fastuae.activity.SelectCarActivity;
import com.example.fastuae.adapter.SliderImageAdapter;
import com.example.fastuae.databinding.FragmentHomeBinding;
import com.example.fastuae.model.SliderModel;
import com.example.fastuae.util.Click;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private Context context;
    private FragmentHomeBinding binding;
    private List<SliderModel> sliderModelList;
    private SliderImageAdapter sliderImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            context = inflater.getContext();

            initView();
        }

        return binding.getRoot();
    }

    private void initView(){
        sliderModelList = new ArrayList<>();
        sliderImageAdapter = new SliderImageAdapter(context, sliderModelList);
        binding.pager.setAdapter(sliderImageAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager, true);

        getCurrentDate();
        setUpSliderList();
        onClick();
    }

    private void getCurrentDate(){
        Calendar c= Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthInt = c.get(Calendar.MONTH);
        String monthString = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int day = c.get(Calendar.DAY_OF_MONTH);

        binding.txtPickDate.setText(day+"");
        binding.txtPickMonth.setText(monthString.toUpperCase());
        binding.txtPickTime.setText(getCurrentTime()+"");

        binding.txtDropDate.setText(day+"");
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

        binding.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent selectCarIntent = new Intent(context, SelectCarActivity.class);
                startActivity(selectCarIntent);
            }
        });
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

}
