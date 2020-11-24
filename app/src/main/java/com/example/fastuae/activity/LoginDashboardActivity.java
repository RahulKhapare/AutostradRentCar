package com.example.fastuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLoginDashboardBinding;
import com.example.fastuae.fragment.InstructionFragmentOne;
import com.example.fastuae.fragment.InstructionFragmentTwo;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Localization;
import com.example.fastuae.util.P;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LoginDashboardActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private LoginDashboardActivity activity = this;
    private ActivityLoginDashboardBinding binding;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        session = new Session(activity);
        Localization.loadLocal(activity,session.getString(P.languageFlag));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_dashboard);
        initView();
    }

    private void initView(){
        setUpViewPager();
        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                Intent mainIntent = new Intent(activity,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                Intent loginIntent = new Intent(activity,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                Intent signUpIntent = new Intent(activity,SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void setUpViewPager() {
        binding.tabLayoutTop.setupWithViewPager(binding.viewPagerTop, true);
//        binding.tabLayoutTop.setNestedScrollingEnabled(true);
        addPagerAdapter(binding.viewPagerTop);
        binding.tabLayoutTop.setupWithViewPager(binding.viewPagerTop);
        binding.viewPagerTop.addOnPageChangeListener(activity);
    }

    private void addPagerAdapter(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InstructionFragmentOne(),"");
        adapter.addFragment(new InstructionFragmentTwo(),"");
        viewPager.setAdapter(adapter);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
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
}