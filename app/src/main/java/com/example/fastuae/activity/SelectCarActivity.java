package com.example.fastuae.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivitySelectCarBinding;
import com.example.fastuae.fragment.CarCardFragment;
import com.example.fastuae.fragment.CarGreedFragment;
import com.example.fastuae.model.CarFilterModel;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.model.HomeLocationModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectCarActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private SelectCarActivity activity = this;
    private ActivitySelectCarBinding binding;
    private CarCardFragment carCardFragment = CarCardFragment.newInstance();
    private CarGreedFragment carGreedFragment = CarGreedFragment.newInstance();
    private int[] tabIcons = {
            R.drawable.ic_card_dark,
            R.drawable.ic_greed_dark
    };
    private LoadingDialog loadingDialog;

    public static String monthDuration = "";//
    public static String bookingTYpe = "";//
    public static String pickUpEmirateID = "";//
    public static String dropUpEmirateID = "";//
    public static String pickUpDate = "";//
    public static String dropUpDate = "";//
    public static String pickUpTime = "";
    public static String dropUpTime = "";
    public static String pickUpType = "";
    public static String dropUpType = "";
    public static String pickUpLocationID = "";
    public static String dropUpLocationID = "";
    public static String pickUpAddress = "";
    public static String dropUpAddress = "";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_car);

        groupSelectionList = new ArrayList<>();
        passengerSelectionList = new ArrayList<>();
        doorSelectionList = new ArrayList<>();
        suitcaseSelectionList = new ArrayList<>();
        transmissionSelectionList = new ArrayList<>();
        fuilSelectionList = new ArrayList<>();

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView(){
        Config.FILTER_VALUE = Config.FILTER_ONE;
        binding.toolbar.setTitle(getResources().getString(R.string.selectCar));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        isApplyFilter = false;
        monthDuration = getIntent().getStringExtra(Config.monthDuration);
        bookingTYpe = getIntent().getStringExtra(Config.bookingTYpe);
        pickUpEmirateID = getIntent().getStringExtra(Config.pickUpEmirateID);
        dropUpEmirateID = getIntent().getStringExtra(Config.dropUpEmirateID);
        pickUpDate = getIntent().getStringExtra(Config.pickUpDate);
        dropUpDate = getIntent().getStringExtra(Config.dropUpDate);
        pickUpTime = getIntent().getStringExtra(Config.pickUpTime);
        dropUpTime = getIntent().getStringExtra(Config.dropUpTime);
        pickUpType = getIntent().getStringExtra(Config.pickUpType);
        dropUpType = getIntent().getStringExtra(Config.dropUpType);
        pickUpLocationID = getIntent().getStringExtra(Config.pickUpLocationID);
        dropUpLocationID = getIntent().getStringExtra(Config.dropUpLocationID);
        pickUpAddress = getIntent().getStringExtra(Config.pickUpAddress);
        dropUpAddress = getIntent().getStringExtra(Config.dropUpAddress);

        loadingDialog = new LoadingDialog(activity);
        setUpViewPager();
        setupTabIcons();

    }

    private void setupTabIcons() {

//        binding.tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        binding.tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_customt_ab, null);
        ((ImageView) view1.findViewById(R.id.icon)).setImageResource(R.drawable.ic_card_pink);
        ((TextView) view1.findViewById(R.id.text)).setText(getResources().getString(R.string.cardview));
        ((TextView) view1.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.lightBlue));

        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_customt_ab, null);
        ((ImageView) view2.findViewById(R.id.icon)).setImageResource(R.drawable.ic_greed_dark);
        ((TextView) view2.findViewById(R.id.text)).setText(getResources().getString(R.string.gridview));

        binding.tabLayout.getTabAt(0).setCustomView(view1);
        binding.tabLayout.getTabAt(1).setCustomView(view2);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ((TextView) view.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.lightBlue));
                if (tab.getPosition()==0){
                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_card_pink);
                }else if (tab.getPosition()==1){
                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_greed_pink);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ((TextView) view.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.textDark));
                if (tab.getPosition()==0){
                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_card_dark);
                }else if (tab.getPosition()==1){
                    ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_greed_dark);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager() {
//        binding.tabLayout.setNestedScrollingEnabled(true);
        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(activity);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(carCardFragment,getResources().getString(R.string.cardview));
//        adapter.addFragment(carGreedFragment, getResources().getString(R.string.gridview));
        adapter.addFragment(carCardFragment,"");
        adapter.addFragment(carGreedFragment, "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        else if (item.getItemId() == R.id.ic_filter) {
            Intent intent = new Intent(activity,CarFilterActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
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